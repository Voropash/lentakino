package ru.javahero.kinolenta.bot.util.keyboard

import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton

@Component
class UrlKeyboard {

    fun buildUrlKeyboard(url: String, placeholder: String): InlineKeyboardMarkup {
        val keyboardMarkup = InlineKeyboardMarkup()
        val keyboard = ArrayList<List<InlineKeyboardButton>>()
        val buttons = ArrayList<InlineKeyboardButton>()
        buttons.add(InlineKeyboardButton(placeholder)
                .setUrl(url))
        keyboard.add(buttons)
        keyboardMarkup.keyboard = keyboard
        return keyboardMarkup
    }
}
