package ru.javahero.top.bot

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.GetFile
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Message
import ru.javahero.top.model.Post
import ru.javahero.top.model.PostRepository
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels
import java.text.SimpleDateFormat
import java.util.*


@Component
class SaveToDatabaseProcessor(val botOperations: BotOperations,
                              val postRepository: PostRepository,
                              val botConfig: BotConfig) {

    private val log = LoggerFactory.getLogger(SaveToDatabaseProcessor::class.java)

    fun send(requestMessage: Message) {

        val date = Date(requestMessage.date * 1000L)
        val formatter = SimpleDateFormat("dd.MM.yy")
        val dateFormatted = formatter.format(date)

        var text = requestMessage.text
                .replace("\n\n", "<br />")
                .replace("\n", "<br />")

        var indexOfLink = text.indexOf("http")
        while (indexOfLink != -1) {
            var endIndexOfLink = text.indexOf(" ", indexOfLink)
            if (endIndexOfLink == -1) endIndexOfLink = text.length
            text = text.substring(0, indexOfLink) + "<noindex><a href=\"" + text.substring(indexOfLink, endIndexOfLink) + "\">" +
                    text.substring(indexOfLink, endIndexOfLink) + "</a></noindex>" + text.substring(endIndexOfLink, text.length)
            indexOfLink = text.indexOf("http", text.indexOf("</a>", endIndexOfLink))
        }
        val post = Post(
                requestMessage.forwardFromChat.userName.orEmpty(),
                dateFormatted,
                text,
                requestMessage.forwardFromChat.title.orEmpty(),
                null,
                null,
                requestMessage.forwardFromChat.title.orEmpty() + " writes..."
        )
        val save = postRepository.save(post)

        try {
            if (save.id != -1) {
                val sendMessageQuery = SendMessage()
                sendMessageQuery.enableMarkdown(true)
                sendMessageQuery.chatId = requestMessage.chatId.toString()
                sendMessageQuery.text = save.id.toString()
                botOperations.sendMessage(sendMessageQuery)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            log.error("send message error", e)
        }
    }

    fun saveName(requestMessage: Message) {
        val postNum = requestMessage.text.split(" ")[0].toIntOrNull()
        val post = postRepository.findOne(postNum)
        post.title = requestMessage.text.substring(requestMessage.text.indexOf(" ") + 1)
        val save = postRepository.save(post)
        try {
            if (save.id != -1) {
                val sendMessageQuery = SendMessage()
                sendMessageQuery.enableMarkdown(true)
                sendMessageQuery.chatId = requestMessage.chatId.toString()
                sendMessageQuery.text = "OK title: " + save.id.toString()
                botOperations.sendMessage(sendMessageQuery)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            log.error("send message error", e)
        }
    }

    fun saveImg(requestMessage: Message) {
        val postNum = requestMessage.caption.toIntOrNull()
        val post = postRepository.findOne(postNum)
        val photo = requestMessage.photo.maxBy({ photo -> photo.width })
        val hasFilePath = photo?.hasFilePath()
//        if (hasFilePath == null || hasFilePath == false) {
//            try {
//                val sendMessageQuery = SendMessage()
//                sendMessageQuery.enableMarkdown(true)
//                sendMessageQuery.chatId = requestMessage.chatId.toString()
//                sendMessageQuery.text = "Bad image"
//                botOperations.sendMessage(sendMessageQuery)
//            } catch (e: Exception) {
//                e.printStackTrace()
//                log.error("send message error", e)
//                return
//            }
//        }
        val getFile = GetFile()
        getFile.fileId = photo!!.fileId
        val file = botOperations.getFile(getFile)
        val url = file.getFileUrl(botConfig.BOT_TOKEN)
        val website = URL(url)
        val rbc = Channels.newChannel(website.openStream())

        val date = Date(requestMessage.date * 1000L)
        val formatter = SimpleDateFormat("yy/MM/dd")
        val dateFormatted = formatter.format(date)

        val outFile = File("/opt/topstatic/" + dateFormatted)
        outFile.mkdirs()

        val fos = FileOutputStream("/opt/topstatic/" + dateFormatted + "/" + getFile.fileId + ".png")
        fos.channel.transferFrom(rbc, 0, java.lang.Long.MAX_VALUE)

        post.image = "/" + dateFormatted + "/" + getFile.fileId + ".png"
        val save = postRepository.save(post)
        try {
            if (save.id != -1) {
                val sendMessageQuery = SendMessage()
                sendMessageQuery.enableMarkdown(true)
                sendMessageQuery.chatId = requestMessage.chatId.toString()
                sendMessageQuery.text = "OK image: " + save.id.toString()
                botOperations.sendMessage(sendMessageQuery)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            log.error("send message error", e)
        }
    }
}
