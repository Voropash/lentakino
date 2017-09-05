package ru.javahero.kinolenta.bot

import org.springframework.stereotype.Controller
import org.telegram.telegrambots.api.methods.ForwardMessage
import org.telegram.telegrambots.api.methods.send.SendDocument
import org.telegram.telegrambots.api.methods.send.SendSticker
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
                              forwardMessage: KFunction1<@ParameterName(name = "forwardMessage") ForwardMessage, Message>,
                              sendSticker: KFunction1<@ParameterName(name = "sendSticker") SendSticker, Message>,
                              sendDocument: KFunction1<@ParameterName(name = "sendDocument") SendDocument, Message>) {
        when {
            requestMessage.text == "/start" -> {
                startMessageSendProcessor.send(requestMessage)
                messageToAdminSendProcessor.forwardMessage(requestMessage, forwardMessage)
            }
            requestMessage.chatId != botConfig.ADMIN_CHAT_ID -> messageToAdminSendProcessor.forwardMessage(requestMessage, forwardMessage)
            requestMessage.text == "/new" -> reviewSendProcessor.sendNewReviewToChannel()
            requestMessage.text == "/dbsize" -> dbInfoSender.sendDbInfo(requestMessage)
            requestMessage.text.orEmpty().startsWith("/sendWithUrl") -> messageWithUrlSendProcessor.send(requestMessage)
            requestMessage.replyToMessage != null -> messageToUserSendProcessor.send(requestMessage, sendSticker, sendDocument)
        }
    }

}
