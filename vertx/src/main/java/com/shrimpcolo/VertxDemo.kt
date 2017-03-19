package com.shrimpcolo

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.http.HttpServer
import io.vertx.core.json.Json
import io.vertx.ext.web.Router

import java.util.Arrays

/**
 * Created by Johnny Tam on 2017/3/18.
 */
class VertxDemo : AbstractVerticle() {
    private val BOOKS = Arrays.asList(
            Book(1, "book1"),
            Book(2, "book2")
    )

    private val router by lazy { createRouter() }

    @Throws(Exception::class)
    override fun start() {
        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8080)
    }

    fun createRouter(): Router = Router.router(vertx).apply {
        get("/").handler { it.response().end("Welcome!") }
        get("/books/:id").handler {
            val id = it.request().getParam("id")
            val ret = BOOKS.find { it.id == id.toInt() }?.toJson() ?: ""
            it.response().end(ret)
        }
        get("/books").handler {
            val params = it.request().params()

            val ret = when {
                params.isEmpty -> BOOKS.toJson()
                params["name"] != null -> BOOKS.filter { it.name.contains(params["name"]) }.toJson()
                else -> ""
            }

            it.response().end(ret)
        }
    }

    fun <T : Any> T.toJson(): String = Json.encodePrettily(this)

    data class Book(val id: Int, val name: String)
}


