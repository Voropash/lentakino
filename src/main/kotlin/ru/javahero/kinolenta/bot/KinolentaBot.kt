package ru.javahero.kinolenta.bot

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.telegram.telegrambots.api.objects.Message
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import javax.annotation.PostConstruct


@Controller
class KinolentaBot(val botConfig: BotConfig,
                   val callbackHandler: CallbackHandler,
                   val incomingMessageHandler: IncomingMessageHandler,
                   val reviewSendProcessor: ReviewSendProcessor,
                   val botOperations: BotOperations,
                   options: DefaultBotOptions) : TelegramLongPollingBot(options) {

    private val log = LoggerFactory.getLogger(BotController::class.java)

    @PostConstruct
    fun botOperationsInit() {
        botOperations.sendMessage = this::sendMessage
        botOperations.sendPhoto = this::sendPhoto
        botOperations.deleteMessage = this::deleteMessage
        botOperations.forwardMessage = this::forwardMessage
        botOperations.sendSticker = this::sendSticker
        botOperations.sendDocument = this::sendDocument
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
            val callbackQuery = update.callbackQuery
            if (message != null) {
                incomingMessageHandler.handleIncomingMessage(message)
            } else if (callbackQuery != null && callbackQuery.data != null) {
                callbackHandler.handleLikeCallback(callbackQuery, this::answerCallbackQuery, this::editMessageText)
            }
        } catch (e: Exception) {
            log.error("Internal error", e)
        }
    }

    fun sendNewReviewToChannel(): Message? {
        return reviewSendProcessor.sendNewReviewToChannel()
    }

}
