package ru.javahero.top.model

import org.springframework.data.repository.PagingAndSortingRepository

interface PostRepository : PagingAndSortingRepository<Post, Int>
