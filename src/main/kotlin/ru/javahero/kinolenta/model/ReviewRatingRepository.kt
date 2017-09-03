package ru.javahero.kinolenta.model

import org.springframework.data.repository.CrudRepository

interface ReviewRatingRepository : CrudRepository<ReviewRating, Long> {

    fun countByMessageIdAndChoise(messageId: Int, choise: Int): Int

    fun findOneByMessageIdAndUserId(messageId: Int, userId: Int): ReviewRating?
}
