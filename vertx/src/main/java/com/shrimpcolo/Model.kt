package com.shrimpcolo

import io.vertx.core.json.Json

/**
 * Created by Johnny Tam on 2017/3/20.
 */
data class Info(
    val count: Int,
    val start: Int,
    val total: Int,
    val books: List<Book>?)

data class Book(
        val rating: Rating? = null,
        val author: List<String>? = null,
        val subtitle: String? = null,
        val pubdate: String? = null,
        val origin_title: String? = null,
        val image: String? = null,
        val binding: String? = null,
        val catalog: String? = null,
        val pages: String? = null,
        val images: Image? = null,
        val alt: String? = null,
        val id: String? = null,
        val publisher: String? = null,
        val isbn10: String? = null,
        val isbn13: String? = null,
        val title: String? = null,
        val url: String? = null,
        val alt_title: String? = null,
        val author_intro: String? = null,
        val summary: String? = null,
        val price: String? = null,
        val ebook_url: String? = null,
        val ebook_price: String? = null,
        val series: Series? = null,
        val tags: List<Tag>? = null,
        val translator: List<String>? = null)

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

fun <T : Any> T.toJson(): String = Json.encodePrettily(this)

