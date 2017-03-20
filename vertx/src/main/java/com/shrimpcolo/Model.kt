package com.shrimpcolo

data class Info(val total: Int,
           val books: List<Books>,
           val count: Int,
           val start: Int)

data class Books(val origin_title: String,
            val summary: String,
            val image: String,
            val images: Images,
            val author: List<String>,
            val catalog: String,
            val translator: List<String>,
            val rating: Rating,
            val alt: String,
            val binding: String,
            val title: String,
            val url: String,
            val tags: List<Tags>,
            val alt_title: String,
            val author_intro: String,
            val pages: String,
            val price: String,
            val subtitle: String,
            val isbn13: String,
            val publisher: String,
            val isbn10: String,
            val id: String,
            val pubdate: String)

data class Tags(val count: Int,
           val name: String,
           val title: String)

data class Images(val small: String,
             val large: String,
             val medium: String)

data class Rating(val average: String,
             val min: Int,
             val max: Int,
             val numRaters: Int)

