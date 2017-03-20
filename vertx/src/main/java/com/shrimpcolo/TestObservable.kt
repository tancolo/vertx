package com.shrimpcolo

import rx.Observable

/**
 * Created by Johnny Tam on 2017/3/19.
 */
fun main(args: Array<String>) {
    syncCode()
    asyncCode()
    rxCode()
}

fun rxCode() {
    val result = "123456"
    val resultObservable = Observable.just(result)  // input

    resultObservable.subscribe(::println)  // output
}

fun asyncCode() {
    val result = "123456"

    result.let(::println)
}

fun syncCode() {
    val result = "123456"

    println(result)
}
