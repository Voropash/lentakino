package ru.javahero.kinolenta.bot

import org.springframework.stereotype.Controller
import org.telegram.telegrambots.api.methods.ForwardMessage
import org.telegram.telegrambots.api.methods.send.SendDocument
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.methods.send.SendPhoto
import org.telegram.telegrambots.api.methods.send.SendSticker
import org.telegram.telegrambots.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.api.objects.Message
import ru.javahero.kinolenta.bot.util.admin.DbInfoSender
import kotlin.reflect.KFunction1

@Controller
class IncomingMessageHandler(val dbInfoSender: DbInfoSender,
                             val reviewSendProcessor: ReviewSendProcessor,
                             val botConfig: BotConfig,
                             val messageWithUrlSendProcessor: MessageWithUrlSendProcessor,
                             val messageToAdminSendProcessor: MessageToAdminSendProcessor,
                             val messageToUserSendProcessor: MessageFromAdminSendProcessor,
                             val startMessageSendProcessor: StartMessageSendProcessor) {

    fun handleIncomingMessage(requestMessage: Message,
                              sendPhoto: KFunction1<@ParameterName(name = "sendPhoto") SendPhoto, Message>,
                              sendMessage: KFunction1<@ParameterName(name = "sendMessage") SendMessage, Message>,
                              deleteMessage: KFunction1<@ParameterName(name = "deleteMessage") DeleteMessage, Boolean>,
                              forwardMessage: KFunction1<@ParameterName(name = "forwardMessage") ForwardMessage, Message>,
                              sendSticker: KFunction1<@ParameterName(name = "sendSticker") SendSticker, Message>,
                              sendDocument: KFunction1<@ParameterName(name = "sendDocument") SendDocument, Message>) {
        when {
            requestMessage.text == "/start" -> {
                startMessageSendProcessor.send(requestMessage, sendMessage)
                messageToAdminSendProcessor.forwardMessage(requestMessage, forwardMessage)
            }
            requestMessage.chatId != botConfig.ADMIN_CHAT_ID -> messageToAdminSendProcessor.forwardMessage(requestMessage, forwardMessage)
            requestMessage.text == "/new" -> reviewSendProcessor.sendNewReviewToChannel(sendPhoto, sendMessage, deleteMessage)
            requestMessage.text == "/dbsize" -> dbInfoSender.sendDbInfo(requestMessage, sendMessage)
            requestMessage.text.orEmpty().startsWith("/sendWithUrl") -> messageWithUrlSendProcessor.send(requestMessage, sendMessage)
            requestMessage.replyToMessage != null -> messageToUserSendProcessor.send(requestMessage, sendMessage, sendSticker, sendDocument, sendPhoto)
        }
    }

}
