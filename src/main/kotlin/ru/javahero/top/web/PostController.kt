package ru.javahero.top.web

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import ru.javahero.top.model.PostRepository


@Controller
class PostController(val repository: PostRepository) {

    val POSTS_PER_PAGE = 5
    val CHARS_AT_INDEX = 1000

    @RequestMapping("/{id}")
    fun index(@PathVariable("id") id: Int, model: Model): String {
        val pageRequest = PageRequest(id, POSTS_PER_PAGE, Sort(Sort.Direction.DESC, "id"))
        val posts = repository.findAll(pageRequest)
        posts.forEach { post -> if (post.text.length > CHARS_AT_INDEX + 1) post.text = post.text.substring(0, CHARS_AT_INDEX) + "..." }
        model.addAttribute("posts", posts)
        model.addAttribute("page", id)
        model.addAttribute("totalPages", posts.totalPages)
        return "index"
    }

    @RequestMapping("/")
    fun index(model: Model): String {
        val pageRequest = PageRequest(0, POSTS_PER_PAGE, Sort(Sort.Direction.DESC, "id"))
        val posts = repository.findAll(pageRequest)
        posts.forEach { post -> if (post.text.length > CHARS_AT_INDEX + 1) post.text = post.text.substring(0, CHARS_AT_INDEX) + "..." }
        model.addAttribute("posts", posts)
        model.addAttribute("page", 0)
        model.addAttribute("totalPages", posts.totalPages)
        return "index"
    }

    @RequestMapping("/post/{id}")
    fun post(@PathVariable("id") id: Int, model: Model): String {
        val post = repository.findOne(id)
        model.addAttribute("post", post)
        return "post"
    }
}
