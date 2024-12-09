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
import kotlinx.coroutines.flow.retryWhen

interface BusinessLogic {

    fun <T> Flow<T>.convertExceptionAsCheckedException() =
    this.retryWhen { cause, attempt ->
            Log.e("BusinessLogic-exceptionWithRetry: ", "error catch\n $cause")
            when(cause) {
                is FirebaseFirestoreException -> {
                    if (cause.message!!.contains("Unable to resolve host firestore.googleapis.com"))
                        false
                    else {
                        println("FirebaseFirestoreException: Retry attemp ${attempt + 1} ")
                        attempt <= 3
                    }
                }
                else -> {
                    println("NotFirebaseFirestoreException: Not Retry")
                    false
                }
            }
        }.catch { e ->
        Log.e("BusinessLogic-convertExceptionAsCheckedException",
            "error catch\n $e"
        )

            when(e) {
                is CustomException -> throw e
                is FirebaseFirestoreException -> {
                    if (e.message!!.contains("Unable to resolve host firestore.googleapis.com"))
                        throw ExternalException.NetworkError()
                    else throw ExternalException.ServerError()
                }
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