package ru.javahero.kinolenta.model

import javax.persistence.*

@Entity(name = "reviews")
class Review(
        @Column(name="reviewtext")
        val reviewText: String,

        @Column(name="author")
        val author: String,

        @Column(name="reviewdate")
        val reviewdate: String,

        @Column(name="imagelink")
        val imageLink: String,

        @Column(name="imagebase64")
        val imageBase64: ByteArray,

        @Column(name="filmname")
        val filmName: String,

        @Column(name="infos")
        val infos: String,

        @Column(name="kprating")
        val kpRating: String,

        @Column(name="kpratingcount")
        val kpRatingCount: String,

        @Column(name="imdbrating")
        val imdbRating: String,

        @Column(name="actorlist")
        val actorList: String,

        @Column(name="alternativeheadline")
        val alternativeHeadline: String,

        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Int = -1) {
}
