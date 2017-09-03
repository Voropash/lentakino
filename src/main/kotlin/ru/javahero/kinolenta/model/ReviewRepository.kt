package ru.javahero.kinolenta.model

import org.springframework.data.repository.PagingAndSortingRepository

interface ReviewRepository : PagingAndSortingRepository<Review, Long> {
}
