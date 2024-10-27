package com.dirtfy.ppp.data.logic.common

import com.dirtfy.ppp.common.exception.CustomException
import com.dirtfy.ppp.common.exception.ExternalException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface BusinessLogic {

    fun <T> Flow<T>.convertExceptionAsCheckedException() =
        this.catch { e ->
            if (e is CustomException) throw e
            else {
                e.message.let {
                    if (it == null)
                        throw ExternalException.UnknownError()
                    else if (it.contains("fire"))
                        throw ExternalException.ServerError()
                    else if (it.contains("network"))
                        throw ExternalException.NetworkError()
                    else
                        throw ExternalException.UnknownError()
                }
            }
        }

    fun <T> operate(func: suspend () -> T) = flow {
        emit(func())
    }.flowOn(Dispatchers.Default).convertExceptionAsCheckedException()


}