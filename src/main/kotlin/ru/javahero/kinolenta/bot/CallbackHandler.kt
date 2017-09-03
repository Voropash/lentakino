package ru.javahero.kinolenta.bot

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.api.objects.CallbackQuery
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup
import ru.javahero.kinolenta.bot.util.keyboard.LikeKeyboard
import ru.javahero.kinolenta.model.ReviewRating
import ru.javahero.kinolenta.model.ReviewRatingRepository
import ru.javahero.kinolenta.model.ReviewTextRepository
import java.io.Serializable
import kotlin.reflect.KFunction1

@Controller
class CallbackHandler(val botConfig: BotConfig,
                      val reviewTextRepository: ReviewTextRepository,
                      val reviewRatingRepository: ReviewRatingRepository,
                      val likeKeyboard: LikeKeyboard) {

    private val log = LoggerFactory.getLogger(CallbackHandler::class.java)

    fun handleLikeCallback(callbackQuery: CallbackQuery,
                           answerCallbackQuery: KFunction1<@ParameterName(name = "answerCallbackQuery") AnswerCallbackQuery, Boolean>,
                           editMessageText: KFunction1<@ParameterName(name = "editMessageText") EditMessageText, Serializable>) {
        val message = callbackQuery.message
        val chatId = message.chatId
        val messageId = message.messageId
        val text = message.text
        val from = callbackQuery.from
        val userId = from.id
        val callbackQuerryId = callbackQuery.id
        val callbackValue = callbackQuery.data.split(";")

        fastLikeProcess(callbackValue, chatId, messageId, text, callbackQuerryId, answerCallbackQuery, editMessageText)
        smartLikeProcess(messageId, userId, callbackValue, chatId, text, editMessageText)
    }

    private fun fastLikeProcess(callbackValue: List<String>,
                                chatId: Long?,
                                messageId: Int?,
                                text: String?,
                                callbackQuerryId: String?,
                                answerCallbackQuery: KFunction1<@ParameterName(name = "answerCallbackQuery") AnswerCallbackQuery, Boolean>,
                                editMessageText: KFunction1<@ParameterName(name = "editMessageText") EditMessageText, Serializable>) {
        var keyboardMarkup = InlineKeyboardMarkup()
        if (callbackValue[0] == botConfig.LIKE_VALUE) {
            keyboardMarkup = likeKeyboard.buildLikeKeyboard(Integer.valueOf(callbackValue[1]), Integer.valueOf(callbackValue[2]))
        } else if (callbackValue[0] == botConfig.DISLIKE_VALUE) {
            keyboardMarkup = likeKeyboard.buildLikeKeyboard(Integer.valueOf(callbackValue[1]), Integer.valueOf(callbackValue[2]))
        }

        val answerCallbackQueryQuery = AnswerCallbackQuery()
                .setCallbackQueryId(callbackQuerryId)
                .setText("Ваш голос учтен")
                .setShowAlert(false)
        answerCallbackQuery(answerCallbackQueryQuery)

        val editMessageTextQuery = EditMessageText()
                .setChatId(chatId)
                .setMessageId(messageId)
                .setText(text)
                .setReplyMarkup(keyboardMarkup)
        try {
            editMessageText(editMessageTextQuery)
        } catch (e: Exception) {
            log.error("Edit Message Text Error", e)
        }
    }

    private fun smartLikeProcess(messageId: Int,
                                 userId: Int,
                                 callbackValue: List<String>,
                                 chatId: Long?,
                                 text: String?,
                                 editMessageText: KFunction1<@ParameterName(name = "editMessageText") EditMessageText, Serializable>) {
        var newText = text
        val findOneByMessageIdAndUserId = reviewRatingRepository.findOneByMessageIdAndUserId(messageId, userId)
        if (findOneByMessageIdAndUserId != null) {
            val choice = findOneByMessageIdAndUserId.choise
            val isVoteChanges = callbackValue[0] == botConfig.LIKE_VALUE && choice == 2 || callbackValue[0] == botConfig.DISLIKE_VALUE && choice == 1
            if (isVoteChanges) {
                val id = findOneByMessageIdAndUserId.id
                reviewRatingRepository.delete(id)
                saveLikesToDb(callbackValue, messageId, userId)
            }
        } else {
            saveLikesToDb(callbackValue, messageId, userId)
        }
        val likes = reviewRatingRepository.countByMessageIdAndChoise(messageId, 1)
        val dislikes = reviewRatingRepository.countByMessageIdAndChoise(messageId, 2)
        val keyboardMarkup = likeKeyboard.buildLikeKeyboard(likes, dislikes)
        val savedMessage = reviewTextRepository.findOneByMessageId(messageId)
        if (savedMessage != null) {
            val savedText = savedMessage.text
            if (savedText.isNotEmpty()) newText = savedText
        }
        val editMessageTextQuery = EditMessageText()
                .setChatId(chatId)
                .setMessageId(messageId)
                .setText(newText)
                .enableHtml(true)
                .setReplyMarkup(keyboardMarkup)
        try {
            editMessageText(editMessageTextQuery)
        } catch (e: Exception) {
            log.warn("Recalculate rating edit message text error")
        }
        log.debug("End of smartLikeProcess()")
    }

    private fun saveLikesToDb(callbackValue: List<String>, messageId: Int, userId: Int) {
        if (callbackValue[0] == botConfig.LIKE_VALUE) {
            reviewRatingRepository.save(ReviewRating(messageId, userId, 1))
        } else if (callbackValue[0] == botConfig.DISLIKE_VALUE) {
            reviewRatingRepository.save(ReviewRating(messageId, userId, 2))
        }
    }
}
