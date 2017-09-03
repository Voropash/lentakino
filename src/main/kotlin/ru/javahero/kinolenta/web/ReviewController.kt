package ru.javahero.kinolenta.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import ru.javahero.kinolenta.model.ReviewRepository

@RestController
class ReviewController(val repository: ReviewRepository) {

	@GetMapping("/")
	fun findAll() = repository.findAll()
}
