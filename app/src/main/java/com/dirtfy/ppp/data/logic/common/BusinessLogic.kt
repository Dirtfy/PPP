package com.dirtfy.ppp.data.logic.common

import android.util.Log
import com.dirtfy.ppp.common.exception.CustomException
import com.dirtfy.ppp.common.exception.ExternalException
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface BusinessLogic {

    fun <T> Flow<T>.convertExceptionAsCheckedException() =
        this.catch { e ->
            Log.e("BusinessLogic-convertExceptionAsCheckedException",
                "error catch\n " +
                    "${e.message}")

            when(e) {
                is CustomException -> throw e
                is FirebaseFirestoreException -> throw ExternalException.ServerError()
                else -> {
                    e.message.let {
                        if (it == null)
                            throw ExternalException.UnknownError()
                        else if (it.contains("network"))
                            throw ExternalException.NetworkError()
                        else
                            throw ExternalException.UnknownError()
                    }
                }
            }
        }

    fun <T> operate(func: suspend () -> T) = flow {
        emit(func())
    }.flowOn(Dispatchers.Default).convertExceptionAsCheckedException()

}