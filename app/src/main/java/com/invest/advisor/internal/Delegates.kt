package com.invest.advisor.internal

import kotlinx.coroutines.*


/**
 * Created by qsufff on 7/29/2020.
 */

fun <T> lazyDeferred(block: suspend CoroutineScope.() -> T) : Lazy<Deferred<T>>{
    return lazy {
        GlobalScope.async(start = CoroutineStart.LAZY) {
            block.invoke(this)
        }
    }

}