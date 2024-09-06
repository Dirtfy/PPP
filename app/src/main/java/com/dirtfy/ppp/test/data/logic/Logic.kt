package com.dirtfy.ppp.test.data.logic

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface Logic {

    fun <R,> asFlow(
        job: suspend () -> R,
        ifCatch: suspend (Throwable) -> Unit
    ) = flow {
        emit(job())
    }.catch {
        ifCatch(it)
        println(it.message)
    }.flowOn(Dispatchers.Default)
}