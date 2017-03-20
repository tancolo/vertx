package com.shrimpcolo

import io.vertx.core.json.Json
import io.vertx.rxjava.core.AbstractVerticle
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
            Book(1, "book1"),
            Book(2, "book2")
    )

    private val router by lazy { createRouter() }

    @Throws(Exception::class)
    override fun start() {
        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8080)

        searchBooks("黑客")
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

            val ret = when {
                params.isEmpty -> BOOKS.toJson().toSingletonObservable().toSingle()
//                params["name"] != null -> BOOKS.filter { it.name.contains(params["name"]) }.toJson()
                params["name"] != null -> searchBooks(params["name"])
                else -> "".toSingletonObservable().toSingle()
            }

            ret.subscribe { ctx.response().end(it) }
        }
    }

    fun searchBooks(bookName: String): Single<String> = WebClient.create(vertx)
            .getAbs("https://api.douban.com/v2/book/search?q=$bookName")
            .`as`(BodyCodec.string())
            .rxSend()
            .map { it.body() }

    fun <T : Any> T.toJson(): String = Json.encodePrettily(this)

    data class Book(val id: Int, val name: String)
}


