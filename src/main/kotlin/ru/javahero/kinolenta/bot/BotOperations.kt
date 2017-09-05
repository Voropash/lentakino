package ru.javahero.kinolenta.bot

import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.methods.send.SendPhoto
import org.telegram.telegrambots.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.api.objects.Message

@Component
class BotOperations {
    lateinit var sendPhoto: (SendPhoto) -> Message
    lateinit var sendMessage: (SendMessage) -> Message
    lateinit var deleteMessage: (DeleteMessage) -> Boolean
}