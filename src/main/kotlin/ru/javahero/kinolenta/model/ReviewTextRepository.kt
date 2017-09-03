package ru.javahero.kinolenta.model

import org.springframework.data.repository.CrudRepository

interface ReviewTextRepository : CrudRepository<ReviewText, Int> {

    fun findOneByMessageId(messageId: Int): ReviewText?
}
