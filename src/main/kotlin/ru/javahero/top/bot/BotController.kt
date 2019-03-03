package ru.javahero.top.bot

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.TelegramBotsApi
import org.telegram.telegrambots.exceptions.TelegramApiException
import javax.annotation.PostConstruct

@Controller
class BotController(var topBot: TopBot) {

    private val log = LoggerFactory.getLogger(BotController::class.java)

    @PostConstruct
    fun init() {
        ApiContextInitializer.init()
        val telegramBotsApi = TelegramBotsApi()
        try {
            telegramBotsApi.registerBot(topBot)
        } catch (e: TelegramApiException) {
            log.error("Runtime Error", e)
        }
    }
}
