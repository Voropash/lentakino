package ru.javahero.kinolenta.bot

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Message
import ru.javahero.kinolenta.bot.util.keyboard.UrlKeyboard
import kotlin.reflect.KFunction1

@Component
class StartMessageSendProcessor(val botConfig: BotConfig,
                                val urlKeyboard: UrlKeyboard,
                                val botOperations: BotOperations) {

    private val log = LoggerFactory.getLogger(StartMessageSendProcessor::class.java)

    fun send(requestMessage: Message) {
        val chatId = requestMessage.chatId
        val sendMessageQuery = SendMessage()
        sendMessageQuery.enableHtml(true)
        sendMessageQuery.chatId = chatId?.toString()
        sendMessageQuery.text = botConfig.START_MESSAGE_TEXT
        try {
            botOperations.sendMessage(sendMessageQuery)
            Thread({
                Thread.sleep(botConfig.SLEEP_BEFORE_LINK_MS)
                sendLink(sendMessageQuery)
            }).start()
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("send message error", e)
        }
    }

    fun sendLink(sendMessageQuery: SendMessage) {
        sendMessageQuery.text = botConfig.START_MESSAGE_LINK_TEXT
        sendMessageQuery.replyMarkup = urlKeyboard.buildUrlKeyboard(botConfig.CHANNEL_LINK, botConfig.PLACEHOLDER)
        try {
            botOperations.sendMessage(sendMessageQuery)
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("send message error", e)
        }
    }
}
