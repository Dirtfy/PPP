package com.dirtfy.ppp.data.api

interface TransactionManager<TransactionType> {
    suspend fun <ReturnType> transaction(
        job: (transaction: TransactionType) -> ReturnType
    ): ReturnType
}