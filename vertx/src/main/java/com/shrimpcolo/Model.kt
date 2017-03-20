package com.shrimpcolo

/**
 * Created by Johnny Tam on 2017/3/20.
 */
data class Info(
    val count: Int,
    val start: Int,
    val total: Int,
    val books: List<Book>?)

data class Book(
        val rating: Rating?,
        val author: List<String>?,
        val subtitle: String?,
        val pubdate: String?,
        val origin_title: String?,
        val image: String?,
        val binding: String?,
        val catalog: String?,
        val pages: String?,
        val images: Image?,
        val alt: String?,
        val id: String?,
        val publisher: String?,
        val isbn10: String?,
        val isbn13: String?,
        val title: String?,
        val url: String?,
        val alt_title: String?,
        val author_intro: String?,
        val summary: String?,
        val price: String?,
        val ebook_url: String?,
        val ebook_price: String?,
        val series: Series?,
        val tags: List<Tag>?,
        val translator: List<String>?)

data class Rating(
    val max: Int,
    val numRaters: Int,
    val average: String?,
    val min: Int)

data class Image(
    val small: String?,
    val large: String?,
    val medium: String?)

data class Series(
    val id: String?,
    val title: String?)

data class Tag(
    val count: Int,
    val name: String?,
    val title: String?)

