package com.shrimpcolo

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.json.Json
import io.vertx.core.json.Json.encodePrettily
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.rxjava.core.Vertx
import io.vertx.rxjava.ext.mongo.MongoClient
import io.vertx.ext.mongo.impl.codec.json.JsonObjectCodec
import com.mongodb.async.client.MongoCollection
import io.vertx.ext.mongo.WriteOption
import io.vertx.groovy.ext.mongo.MongoClient_GroovyExtension.saveWithOptions
import rx.Observable
import rx.Single
import java.util.Objects.requireNonNull


/**
 * Created by Johnny Tam on 2017/3/20.
 */

class BookDao(vertx: Vertx) {

    companion object {
        private val TABLE_NAME = "Book"
    }

    private val mongoClient by lazy { MongoClient.createShared(vertx, createConfig()) }

    private fun createConfig(): JsonObject {
        val config = Vertx.currentContext()?.config()

        val uri = config?.getString("mongo_uri") ?: "mongodb://localhost:27017"
        val db = config?.getString("mongo_db") ?: "test"

        val mongoConfig = json {
            obj(
                    "connection_string" to uri,
                    "db_name" to db
            )
        }

        return mongoConfig
    }

    fun save(json: JsonObject, resultHandler: (AsyncResult<String>) -> Unit = {}) {
        mongoClient.save(TABLE_NAME, json, resultHandler)
    }

    fun save(book: Book, resultHandler: (AsyncResult<String>) -> Unit = {}) {
        mongoClient.save(TABLE_NAME, JsonObject(book.toJson()), resultHandler)
    }

    fun find(json: String, resultHandler: (AsyncResult<List<JsonObject>>) -> Unit) {
        find(JsonObject(json), resultHandler)
    }

    fun find(json: JsonObject, resultHandler: (AsyncResult<List<JsonObject>>) -> Unit) {
        mongoClient.find(TABLE_NAME, json, resultHandler)
    }

    fun find(json: JsonObject): Single<List<Book>> = mongoClient
            .rxFind(TABLE_NAME, json)
            .map { it.map { it.remove("_id"); jacksonObjectMapper().readValue<Book>(it.toString()) } }

    fun delete(json: String, resultHandler: (AsyncResult<Void>) -> Unit = {}) {
        delete(JsonObject(json), resultHandler)
    }

    fun delete(json: JsonObject, resultHandler: (AsyncResult<Void>) -> Unit = {}) {
        mongoClient.remove(TABLE_NAME, json, resultHandler)
    }
}