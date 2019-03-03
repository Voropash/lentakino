package ru.javahero.top.model

import javax.persistence.*

@Entity(name = "post")
class Post(
        @Column(name="author")
        val author: String,

        @Column(name="date")
        val date: String,

        @Column(name="text", length=40096)
        var text: String,

        @Column(name="channel")
        val channel: String,

        @Column(name="image")
        var image: String?,

        @Column(name="tags")
        val tags: String?,

        @Column(name="title")
        var title: String?,

        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Int = -1)
