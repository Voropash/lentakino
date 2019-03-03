package ru.javahero.top.bot

import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.BotApiMethod
import org.telegram.telegrambots.api.methods.GetFile
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.File
import org.telegram.telegrambots.api.objects.Message

@Component
class BotOperations {
    lateinit var sendMessage: (SendMessage) -> Message
    lateinit var getFile: (GetFile) -> File
}

