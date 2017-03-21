package com.shrimpcolo

import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import rx.Observable
import rx.Single
import rx.lang.kotlin.toSingletonObservable

/**
 * Created by Johnny Tam on 2017/3/21.
 */
fun main(args: Array<String>) {
    val db = "db".toSingletonObservable().toSingle()//.flatMap { Single.error<String>(Throwable("empty")) }.onErrorReturn { null }
    val network =  Single.create<String> { println("send network");it.onSuccess("network") } //"network".toSingletonObservable().toSingle()

    Single.concat(db, network).first { it != null }.toSingle().subscribe(::println, ::println)

    val json = json {
        obj("id" to "111", "name" to "book")
    }

    json.remove("id")
    println(json)
}