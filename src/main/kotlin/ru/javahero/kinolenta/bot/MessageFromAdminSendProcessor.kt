package ru.javahero.kinolenta.bot

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.send.SendDocument
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.methods.send.SendPhoto
import org.telegram.telegrambots.api.methods.send.SendSticker
import org.telegram.telegrambots.api.objects.Message
import kotlin.reflect.KFunction1
import java.io.PrintWriter
import java.io.StringWriter



@Component
class MessageFromAdminSendProcessor(val messageToAdminSendProcessor: MessageToAdminSendProcessor,
                                    val botOperations: BotOperations) {

    private val log = LoggerFactory.getLogger(MessageFromAdminSendProcessor::class.java)

    fun send(requestMessage: Message,
             sendSticker: KFunction1<@ParameterName(name = "sendSticker") SendSticker, Message>,
             sendDocument: KFunction1<@ParameterName(name = "sendDocument") SendDocument, Message>) {

        val replyToMessage = requestMessage.replyToMessage
        val forwardFrom = replyToMessage.forwardFrom
        val userId = forwardFrom.id
        val stringUserId = userId.toString()

        try {
            if (requestMessage.hasDocument()) {
                val sendDocumentQuery = SendDocument()
                val document = requestMessage.document
                sendDocumentQuery.document = document.fileId
                sendDocumentQuery.chatId = stringUserId
                sendDocument(sendDocumentQuery)
            }
            if (requestMessage.hasPhoto()) {
                val sendPhotoQuery = SendPhoto()
                val photo = requestMessage.photo.maxBy({ photo -> photo.width })
                sendPhotoQuery.photo = photo?.fileId
                sendPhotoQuery.caption = requestMessage.caption
                sendPhotoQuery.chatId = stringUserId
                botOperations.sendPhoto(sendPhotoQuery)
            }
            val sticker = requestMessage.sticker
            if (sticker != null) {
                val sendStickerQuery = SendSticker()
                sendStickerQuery.chatId = stringUserId
                sendStickerQuery.sticker = sticker.fileId
                sendSticker(sendStickerQuery)
            }
            if (requestMessage.text != null) {
                val sendMessageQuery = SendMessage()
                sendMessageQuery.enableMarkdown(true)
                sendMessageQuery.chatId = stringUserId
                sendMessageQuery.text = requestMessage.text
                botOperations.sendMessage(sendMessageQuery)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            log.error("send message error", e)
            sendSendMessageError(e)
        }
    }

    private fun sendSendMessageError(e: Exception) {
        try {
            messageToAdminSendProcessor.sendMessage("send message error: " + e)
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            e.printStackTrace(pw)
            val sStackTrace = sw.toString()
            messageToAdminSendProcessor.sendMessage("```$sStackTrace```")
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("send error message error", e)
        }
    }
}
