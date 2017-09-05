package ru.javahero.kinolenta.bot.util.admin

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Message
import org.telegram.telegrambots.exceptions.TelegramApiException
import ru.javahero.kinolenta.bot.BotController
import ru.javahero.kinolenta.bot.BotOperations
import ru.javahero.kinolenta.model.ReviewRepository
import kotlin.reflect.KFunction1

@Component
class DbInfoSender(val reviewRepository: ReviewRepository,
                   val botOperations: BotOperations) {

    private val log = LoggerFactory.getLogger(BotController::class.java)

    fun sendDbInfo(requestMessage: Message) {
        val message = SendMessage()
        message.enableMarkdown(true)
        message.chatId = requestMessage.chatId.toString()
        val count = reviewRepository.count()
        message.text = "В базе находится $count рецензий"
        try {
            botOperations.sendMessage(message)
        } catch (e: TelegramApiException) {
            log.error("Internal error", e)
        }

    }
}
