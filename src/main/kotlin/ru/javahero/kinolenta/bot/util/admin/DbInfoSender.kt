package ru.javahero.kinolenta.bot.util.admin

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Message
import org.telegram.telegrambots.exceptions.TelegramApiException
import ru.javahero.kinolenta.bot.BotController
import ru.javahero.kinolenta.model.ReviewRepository
import kotlin.reflect.KFunction1

@Component
class DbInfoSender(val reviewRepository: ReviewRepository) {

    private val log = LoggerFactory.getLogger(BotController::class.java)

    fun sendDbInfo(requestMessage: Message,
                   sendMessage: KFunction1<@ParameterName(name = "sendMessage") SendMessage, Message>) {
        val message = SendMessage()
        message.enableMarkdown(true)
        message.chatId = requestMessage.chatId.toString()
        val count = reviewRepository.count()
        message.text = "В базе находится $count рецензий"
        try {
            sendMessage(message)
        } catch (e: TelegramApiException) {
            log.error("Internal error", e)
        }

    }
}
