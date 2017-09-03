package ru.javahero.kinolenta.bot

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.codec.binary.Base64
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.methods.send.SendPhoto
import org.telegram.telegrambots.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.api.objects.Message
import org.telegram.telegrambots.exceptions.TelegramApiException
import ru.javahero.kinolenta.bot.util.keyboard.LikeKeyboard
import ru.javahero.kinolenta.model.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.reflect.KFunction1

@Component
class ReviewSendProcessor(val reviewRepository: ReviewRepository,
                          val reviewTextRepository: ReviewTextRepository,
                          val reviewRatingRepository: ReviewRatingRepository,
                          val botConfig: BotConfig,
                          val objectMapper: ObjectMapper,
                          val likeKeyboard: LikeKeyboard) {

    private val log = LoggerFactory.getLogger(ReviewSendProcessor::class.java)

    fun sendNewReviewToChannel(sendPhoto: KFunction1<@ParameterName(name = "sendPhoto") SendPhoto, Message>,
                               sendMessage: KFunction1<@ParameterName(name = "sendMessage") SendMessage, Message>,
                               deleteMessage: KFunction1<@ParameterName(name = "deleteMessage") DeleteMessage, Boolean>): Message? {
        val page = reviewRepository.findAll(PageRequest(1, 1))
        val review = page.elementAtOrNull(0)

        val sendPhotoQuery = SendPhoto()
        sendPhotoQuery.chatId = botConfig.CHANNEL_NAME
        sendPhotoQuery.disableNotification()
        val dearr = Base64.decodeBase64(review?.imageBase64)
        val destinationFile = Paths.get("imageDir", "cover.png")
        Files.write(destinationFile, dearr)
        val photo = File("imageDir/cover.png")
        sendPhotoQuery.setNewPhoto(photo)
        var sendPhotoMessage: Message? = null
        try {
            sendPhotoMessage = sendPhoto(sendPhotoQuery)
        } catch (e: TelegramApiException) {
            log.error("Error sending image", e)
        }


        val sendMessageQuery = SendMessage()
        sendMessageQuery.enableHtml(true)
        sendMessageQuery.chatId = botConfig.CHANNEL_NAME
        var alternativeHeadline = review?.alternativeHeadline.orEmpty()
        if (alternativeHeadline.isNotEmpty()) alternativeHeadline = " - " + alternativeHeadline
        val infos = objectMapper.readTree(review?.infos)

        var year = getPropertyStringFromItems(infos, "год", false, false)
        if (year.isNotEmpty()) year = "($year)"

        val country = getPropertyStringFromItems(infos, "страна", true, true)
        val rej = getPropertyStringFromItems(infos, "режиссер", true, true)
        val slogan = getPropertyStringFromItems(infos, "слоган", true, true)
        val money = getPropertyStringFromItems(infos, "бюджет", true, true)
        val money_usa = getPropertyStringFromItems(infos, "сборы в США", true, true)
        var money_world = getPropertyStringFromItems(infos, "сборы в мире", true, true)
        val money_russia = getPropertyStringFromItems(infos, "сборы в России", true, true)
        var tags = getPropertyStringFromItems(infos, "жанр", true, true)
        val time = getPropertyStringFromItems(infos, "время", true, true)
        tags = tags.replace(", ...", "")
                .replace("...", "")
                .replace("\n", "")
                .replace("слова", "") + "\n"
        money_world = money_world.replace("\nсборы", "")
                .replace("сборы", "")
        val imdbRating = review?.imdbRating
                .orEmpty()
                .replace("IMDb:", "<b>IMDb:</b>")
        val actorList = review?.actorList.orEmpty()
                .replace("...", "")
        val reviewText = review?.reviewText.orEmpty()
                .replace("&nbsp;", "")
                .replace("\n\n\n", "\n")
        val hashTags = "#кl_" + getPropertyStringFromItems(infos, "жанр", false, false)
                .replace(", ...", "")
                .replace("...", "")
                .replace("\n", "")
                .replace("слова", "")
                .replace("<i>", "")
                .replace("</i>", "")
                .replace(", ", " #кl_")
        val messageText = java.lang.String.format(
                "<b>%s</b> %s %s \n\n" +
                        "<b>Кинопоиск:</b> %s (%s)\n" +
                        "%s\n\n" +

                        "%s" +
                        "%s" +
                        "%s" +
                        "%s" +
                        "%s" +
                        "%s" +
                        "%s" +
                        "%s" +
                        "%s\n" +
                        "<b>Актеры:</b>\n" +
                        "%s\n\n" +
                        "%s\n\n" +
                        "%s",
                review?.filmName, alternativeHeadline, year,

                review?.kpRating, review?.kpRatingCount,
                imdbRating,

                country,
                rej,
                slogan,
                money,
                money_usa,
                money_world,
                money_russia,
                tags,
                time,

                actorList,
                reviewText,
                hashTags)
        sendMessageQuery.text = messageText

        val keyboardMarkup = likeKeyboard.buildLikeKeyboard(1, 1)
        sendMessageQuery.replyMarkup = keyboardMarkup
        try {
            val result = sendMessage(sendMessageQuery)
            val messageId = result.messageId

            reviewTextRepository.save(ReviewText(messageId, messageText))
            reviewRatingRepository.save(ReviewRating(messageId, 0, 1))
            reviewRatingRepository.save(ReviewRating(messageId, -1, 2))
            reviewRepository.delete(review)
            return result
        } catch (e: TelegramApiException) {
            log.error("BadRequest: " + objectMapper.writeValueAsString(sendMessageQuery))
            e.printStackTrace()
            log.error("Internal error", e)
            reviewRepository.delete(review)
            try {
                val photoMessageId = sendPhotoMessage?.messageId
                val deleteMessageQuery = DeleteMessage()
                deleteMessageQuery.chatId = botConfig.CHANNEL_NAME
                deleteMessageQuery.messageId = photoMessageId
                deleteMessage(deleteMessageQuery)
            } catch (e: Exception) {
                e.printStackTrace()
                log.error("Error when delete photo message", e)
            }
        }
        return null
    }

    private fun getPropertyStringFromItems(infos: JsonNode, s: String, addEOL: Boolean, addHeader: Boolean): String {
        for (item in infos) {
            if (item.get(0).asText() == s) {
                var itemValueString = item.get(1).asText()
                itemValueString = "<i>$itemValueString</i>"
                if (addEOL) itemValueString += "\n"
                if (addHeader) itemValueString = "<b>$s</b>: $itemValueString"
                return itemValueString
            }
        }
        return ""
    }
}
