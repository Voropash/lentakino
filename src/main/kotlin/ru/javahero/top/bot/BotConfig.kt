package ru.javahero.top.bot

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.bots.DefaultBotOptions
import javax.annotation.PostConstruct

@Configuration
class BotConfig {

    private val botOptions: DefaultBotOptions = DefaultBotOptions()

    val BOT_TOKEN = "469515436:AAF5bQb01NNpH-guIw1w21Nez0iiHcWaB7w"
    val BOT_USER = "parsersericovbot"

//    val BOT_TOKEN = "452377130:AAFljtbLAqKeHdaA1pRaM7T29EQZraw6PmY"
//    val BOT_USER = "topjournalbot"

    @PostConstruct
    fun init() {
    }

    @Bean
    fun getBotOptions(): DefaultBotOptions {
        return botOptions
    }
}
