package ru.javahero.kinolenta.bot

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.api.objects.Message
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot


@Controller
class KinolentaBot(val botConfig: BotConfig,
                   val callbackHandler: CallbackHandler,
                   val incomingMessageHandler: IncomingMessageHandler,
                   val reviewSendProcessor: ReviewSendProcessor,
                   options: DefaultBotOptions) : TelegramLongPollingBot(options) {

    private val log = LoggerFactory.getLogger(BotController::class.java)

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
//                incomingMessageHandler.handleIncomingMessage(message,
//                        this::sendPhoto,
//                        this::sendMessage,
//                        this::deleteMessage,
//                        this::forwardMessage,
//                        this::sendSticker,
//                        this::sendDocument)
                when {
                    message.text.orEmpty() == "_" -> {
                        try {
                            val sendMessage = SendMessage()
                            sendMessage.text = "4321"
                            sendMessage.chatId = message.chatId.toString()
                            val message1 = sendMessage(sendMessage)
                            sendMessage.text = message1.messageId.toString()
                            sendMessage(sendMessage)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            log.error("send message error", e)
                        }
                    }
                    message.text.orEmpty() != "_"  -> {
                        try {
                            val editMessage = EditMessageText()
                            editMessage.text = "1234"
                            editMessage.chatId = message.chatId.toString()
                            editMessage.messageId = message.text.orEmpty().toInt()
                            editMessageText(editMessage)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            log.error("send message error", e)
                        }
                    }
                }
            } else if (callbackQuery != null && callbackQuery.data != null) {
                callbackHandler.handleLikeCallback(callbackQuery, this::answerCallbackQuery, this::editMessageText)
            }
        } catch (e: Exception) {
            log.error("Internal error", e)
        }
    }

    fun sendNewReviewToChannel(): Message? {
        return reviewSendProcessor.sendNewReviewToChannel(this::sendPhoto, this::sendMessage, this::deleteMessage)
    }

}
