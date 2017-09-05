package ru.javahero.kinolenta.bot

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.ForwardMessage
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Message
import kotlin.reflect.KFunction1

@Component
class MessageToAdminSendProcessor(val botConfig: BotConfig,
                                  val botOperations: BotOperations) {

    private val log = LoggerFactory.getLogger(MessageToAdminSendProcessor::class.java)

    fun forwardMessage(requestMessage: Message,
                       forwardMessage: KFunction1<@ParameterName(name = "forwardMessage") ForwardMessage, Message>) {

        val forwardMessageQuery = ForwardMessage()
        val fromChatId = requestMessage.chatId
        forwardMessageQuery.fromChatId = fromChatId.toString()
        forwardMessageQuery.messageId = requestMessage.messageId
        forwardMessageQuery.chatId = botConfig.ADMIN_CHAT_ID.toString()
        try {
            forwardMessage(forwardMessageQuery)
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("forward message error", e)
        }
    }

    fun sendMessage(text: String) {

        val sendMessageQuery = SendMessage()
        sendMessageQuery.enableMarkdown(true)
        sendMessageQuery.text = text
        sendMessageQuery.chatId = botConfig.ADMIN_CHAT_ID.toString()
        try {
            botOperations.sendMessage(sendMessageQuery)
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("send message error", e)
        }
    }
}
