package ru.javahero.top.bot

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.telegram.telegrambots.api.methods.GetFile
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import javax.annotation.PostConstruct


@Controller
class TopBot(val botConfig: BotConfig,
             val incomingMessageHandler: IncomingMessageHandler,
             val botOperations: BotOperations,
             options: DefaultBotOptions) : TelegramLongPollingBot(options) {

    private val log = LoggerFactory.getLogger(BotController::class.java)

    @PostConstruct
    fun botOperationsInit() {
        botOperations.sendMessage = this::sendMessage
        botOperations.getFile = this::getFile
    }

    override fun getBotUsername(): String {
        return botConfig.BOT_USER
    }

    override fun getBotToken(): String {
        return botConfig.BOT_TOKEN
    }

    override fun onUpdateReceived(update: Update) {
        try {
            val message = update.message
            if (message != null) {
                incomingMessageHandler.handleIncomingMessage(message)
            }
        } catch (e: Exception) {
            log.error("Internal error", e)
        }
    }

}
