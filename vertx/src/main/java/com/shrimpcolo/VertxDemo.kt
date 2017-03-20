package com.shrimpcolo

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.vertx.core.json.Json
import io.vertx.rxjava.core.AbstractVerticle
import io.vertx.rxjava.core.MultiMap
import io.vertx.rxjava.core.http.HttpServerResponse
import io.vertx.rxjava.ext.web.Router
import io.vertx.rxjava.ext.web.client.WebClient
import io.vertx.rxjava.ext.web.codec.BodyCodec
import rx.Observable
import rx.Single
import rx.lang.kotlin.toSingletonObservable

/**
 * Created by Johnny Tam on 2017/3/18.
 */
class VertxDemo : AbstractVerticle() {
    private val BOOKS = listOf(
            MyBook(1, "book1"),
            MyBook(2, "book2")
    )

    private val router by lazy { createRouter() }

    @Throws(Exception::class)
    override fun start() {
        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8080)

//        searchBooks("黑客").subscribe(::println, Throwable::printStackTrace)
    }

    fun createRouter(): Router = Router.router(vertx).apply {
        get("/").handler { it.response().end("Welcome!") }
        get("/books/:id").handler {
            val id = it.request().getParam("id")
            val ret = BOOKS.find { it.id == id.toInt() }?.toJson() ?: ""
            it.response().end(ret)
        }
        get("/books").handler { ctx ->
            val params = ctx.request().params()

            val ret = if (params.isEmpty) {
                BOOKS.toJson().toSingletonObservable().toSingle()
            } else {
                searchBooks(params,
                        { params, (rating) -> (params["score"]?.toFloat() ?: 0f) <= (rating?.average?.toFloat() ?: 0f) },
                        { params, (_, author) -> params["author"]?.let { query -> author?.any { it.contains(query) } } ?: true })
            }

            ret.onErrorReturn { it.printStackTrace(); "Not Found" }.subscribe(ctx.response()::end, Throwable::printStackTrace)
        }
    }

    fun searchBooks(params: MultiMap, vararg predicates: (MultiMap, Book) -> Boolean): Single<String> {
        val name = params["name"]
        val info = name?.let { searchBooks(it) }
                ?.map {
                    val books = it.books?.filter { book -> predicates.all { it(params, book) } }
                    val size = books?.size ?: 0
                    Info(size, it.start, it.total, books)
                }

        return info?.map(Info::toString) ?: "Not Found".toSingletonObservable().toSingle()
    }

    fun searchBooks(bookName: String): Single<Info> {
        if (bookName.isEmpty()) return Single.error(Throwable("book name is empty"))

        return WebClient.create(vertx)
                .getAbs("https://api.douban.com/v2/book/search?q=$bookName")
                .`as`(BodyCodec.string())
                .rxSend()
                .map { jacksonObjectMapper().readValue<Info>(it.body()) }
    }

    fun <T : Any> T.toJson(): String = Json.encodePrettily(this)

    data class MyBook(val id: Int, val name: String)
}


