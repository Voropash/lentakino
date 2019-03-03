package ru.javahero.top.schedule

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.javahero.top.bot.TopBot


@Component
class SheduleTask(val topBot: TopBot) {

    @Scheduled(cron = "0 02 9,21 * * *")
    fun sendNewTask() {
//        do {
//            val sendNewReviewToChannel = topBot.sendNewReviewToChannel()
//        } while (sendNewReviewToChannel == null)
    }
}
