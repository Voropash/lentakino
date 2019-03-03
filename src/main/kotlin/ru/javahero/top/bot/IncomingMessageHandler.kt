package ru.javahero.top.bot

import org.springframework.stereotype.Controller
import org.telegram.telegrambots.api.objects.Message

@Controller
class IncomingMessageHandler(val saveToDatabaseProcessor: SaveToDatabaseProcessor) {

    fun handleIncomingMessage(requestMessage: Message) {
        when {
            requestMessage.text != null &&
                    requestMessage.text.split(" ")[0].toIntOrNull() != null &&
                    requestMessage.text.length < 255
            ->
                saveToDatabaseProcessor.saveName(requestMessage)
            requestMessage.caption != null &&
                    requestMessage.caption.toIntOrNull() != null &&
                    requestMessage.hasPhoto()
            ->
                saveToDatabaseProcessor.saveImg(requestMessage)
            requestMessage.text != null ->
                saveToDatabaseProcessor.send(requestMessage)
        }
    }

}
