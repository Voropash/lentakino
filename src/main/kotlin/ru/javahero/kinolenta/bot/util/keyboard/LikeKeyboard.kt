package ru.javahero.kinolenta.bot.util.keyboard

import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.javahero.kinolenta.bot.BotConfig

@Component
class LikeKeyboard(val botConfig: BotConfig) {

    //
    // TODO: change like and dislike emoji/text
    //
    private val LIKE_EMOJI = " \uD83D\uDC4D\uD83C\uDFFC"
    private val DISLIKE_EMOJI = " \uD83D\uDC4E\uD83C\uDFFC"

    fun buildLikeKeyboard(likes: Int, dislikes: Int): InlineKeyboardMarkup {
        val keyboardMarkup = InlineKeyboardMarkup()
        val keyboard = ArrayList<List<InlineKeyboardButton>>()
        val buttons = ArrayList<InlineKeyboardButton>()
        //
        // Set 2 buttons with like and dislike text (emoji) and incremented (+1) callback date value
        //
        buttons.add(InlineKeyboardButton(likes.toString() + LIKE_EMOJI)
                .setCallbackData(botConfig.LIKE_VALUE + ";" + (likes + 1) + ";" + dislikes))
        buttons.add(InlineKeyboardButton(dislikes.toString() + DISLIKE_EMOJI)
                .setCallbackData(botConfig.DISLIKE_VALUE + ";" + likes + ";" + (dislikes + 1)))
        keyboard.add(buttons)
        keyboardMarkup.keyboard = keyboard
        return keyboardMarkup
    }
}
