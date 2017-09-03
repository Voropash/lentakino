package ru.javahero.kinolenta.schedule

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.javahero.kinolenta.bot.KinolentaBot


@Component
class SheduleTask(val kinolentaBot: KinolentaBot) {

    @Scheduled(cron = "0 02 9,13,17,21 * * *")
    fun sendNewTask() {
        do {
            val sendNewReviewToChannel = kinolentaBot.sendNewReviewToChannel()
        } while (sendNewReviewToChannel == null)
    }
}
