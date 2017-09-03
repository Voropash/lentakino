package ru.javahero.kinolenta.bot

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Message
import ru.javahero.kinolenta.bot.util.keyboard.UrlKeyboard
import kotlin.reflect.KFunction1

@Component
class StartMessageSendProcessor(val botConfig: BotConfig,
                                val urlKeyboard: UrlKeyboard) {

    private val log = LoggerFactory.getLogger(StartMessageSendProcessor::class.java)

    fun send(requestMessage: Message,
             sendMessage: KFunction1<@ParameterName(name = "sendMessage") SendMessage, Message>) {
        val chatId = requestMessage.chatId
        val sendMessageQuery = SendMessage()
        sendMessageQuery.enableHtml(true)
        sendMessageQuery.chatId = chatId?.toString()
        sendMessageQuery.text = botConfig.START_MESSAGE_TEXT
        try {
            sendMessage(sendMessageQuery)
            Thread({
                Thread.sleep(botConfig.SLEEP_BEFORE_LINK_MS)
                sendLink(sendMessageQuery, sendMessage)
            }).start()
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("send message error", e)
        }
    }

    fun sendLink(sendMessageQuery: SendMessage,
                 sendMessage: KFunction1<@ParameterName(name = "sendMessage") SendMessage, Message>) {
        sendMessageQuery.text = botConfig.START_MESSAGE_LINK_TEXT
        sendMessageQuery.replyMarkup = urlKeyboard.buildUrlKeyboard(botConfig.CHANNEL_LINK, botConfig.PLACEHOLDER)
        try {
            sendMessage(sendMessageQuery)
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("send message error", e)
        }
    }
}
