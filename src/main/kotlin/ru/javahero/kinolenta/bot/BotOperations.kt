package ru.javahero.kinolenta.bot

import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.api.methods.ForwardMessage
import org.telegram.telegrambots.api.methods.send.SendDocument
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.methods.send.SendPhoto
import org.telegram.telegrambots.api.methods.send.SendSticker
import org.telegram.telegrambots.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.api.objects.Message

@Component
class BotOperations {
    lateinit var sendPhoto: (SendPhoto) -> Message
    lateinit var sendMessage: (SendMessage) -> Message
    lateinit var deleteMessage: (DeleteMessage) -> Boolean
    lateinit var forwardMessage: (ForwardMessage) -> Message
    lateinit var sendSticker: (SendSticker) -> Message
    lateinit var sendDocument: (SendDocument) -> Message
    lateinit var answerCallbackQuery: (AnswerCallbackQuery) -> Boolean
}
