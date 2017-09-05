package ru.javahero.kinolenta.bot

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Message
import ru.javahero.kinolenta.bot.util.keyboard.UrlKeyboard

@Component
class MessageWithUrlSendProcessor(val botConfig: BotConfig,
                                  val urlKeyboard: UrlKeyboard,
                                  val botOperations: BotOperations) {

    private val log = LoggerFactory.getLogger(MessageWithUrlSendProcessor::class.java)

    fun send(requestMessage: Message) {
        var linkString = requestMessage.text
        linkString = linkString.replace("/sendWithUrl" + " ", "")
        val indexOfSeparator = linkString.indexOf(" ")
        val url = linkString.substring(0, indexOfSeparator)
        val placeholder = linkString.substring(indexOfSeparator + 1, linkString.length)
        val keyboardMarkup = urlKeyboard.buildUrlKeyboard(url, placeholder)

        val replyToMessage = requestMessage.replyToMessage
        if (replyToMessage == null) {
            log.error("Сообщение должно содержать replied текст")
        }
        val text = replyToMessage.text
        val sendMessageQuery = SendMessage()
        sendMessageQuery.text = text
        sendMessageQuery.chatId = botConfig.CHANNEL_NAME
        sendMessageQuery.enableHtml(true)
        sendMessageQuery.replyMarkup = keyboardMarkup
        try {
            botOperations.sendMessage(sendMessageQuery)
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("send message error", e)
        }
    }
}
