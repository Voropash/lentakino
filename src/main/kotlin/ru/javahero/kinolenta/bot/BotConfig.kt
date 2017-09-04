package ru.javahero.kinolenta.bot

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.bots.DefaultBotOptions
import javax.annotation.PostConstruct

@Configuration
class BotConfig {

    private val botOptions: DefaultBotOptions = DefaultBotOptions()

    val BOT_TOKEN = "BOT_TOKEN"
    val BOT_USER = "BOT_USER"
    val CHANNEL_NAME = "@channel"
    val LIKE_VALUE = "LIKE"
    val DISLIKE_VALUE = "DISLIKE"
    val ADMIN_CHAT_ID = 59718526L
    val START_MESSAGE_TEXT = """<b>Я Йёга.</b> Так меня зовут друзья.
        |
        |<b>Возраст:</b> <i>37 лет</i>
        |<b>Пол:</b> <i>мужской</i>
        |<b>Дети:</b> <i>нет</i>
        |<b>Авто:</b> <i>форд</i>
        |<b>Город:</b> <i>Москва</i>
        |<b>Знак зодиак:</b> <i>овен</i>
        |<b>Образование:</b> <i>высшее (юриспруденция) МГУ</i>
        |<b>Отношение к курению:</b> <i>нет</i>
        |<b>Отношение к алкоголю:</b> <i>нет</i>
        |<b>Рост:</b> <i>192</i>
        |
        |<b>Обо мне:</b> Родился в Австрии, Вена.
        |В 7 лет вместе с родителями переехал в Зеленоград. Окончил с отличием школу с физическим уклоном.
        |
        |С треском провалил экзамены во ВГИК. Поступил в Королёвский коледж полиции. Проучился там 1 год. Поступил на Юрфак МГУ им. Ломоносова. Закончив МГУ долгое время работал по специальности.
        |
        |Переехал в Москву, женился. Свадьбу играли в Абхазии, весело было. Развелся через неделю после свадьбы. 3 года ходил в походы по Уралу.
        |
        |Чуть не женился во второй раз на бывшей жене. Сейчас работаю менеджером.
        |
        |Решил попробовать себя в онлайн-бизнесе. Открыл свой телеграм-канал.
        |
        |Мечта - заработать миллион долларов в интернете.
        |
        |Беру рецензии на специализированных сайтах - кинопоиск, афиша, imdb, и транслирую их по своему каналу. Иногда пишу рецензии сам.
        |
        |Буду рад, если вы подпишитесь на канал. Чем больше у меня друзей, тем ближе я к $1000000.
        |
        |Очень люблю новые знакомства, Пока что до отпуска не имею возможности организовать встречу с поклонниками. Но готов пообщаться с вами в свободное от работы время здесь в царстве онлайна.
        |
        |<b>Кого ищу:</b> <i>друзей, девушку для романтических посиделок вечером в кинотеатре</i>
        |
    """.trimMargin()

    val START_MESSAGE_LINK_TEXT = "Вот, кстати мой канал: КиноLenta"
    val CHANNEL_LINK = "https://t.me/lentakino"
    val PLACEHOLDER = "Перейти!"
    val SLEEP_BEFORE_LINK_MS = 20000L

    @PostConstruct
    fun init() {
    }

    @Bean
    fun getBotOptions(): DefaultBotOptions {
        return botOptions
    }
}
