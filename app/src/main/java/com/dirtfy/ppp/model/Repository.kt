package com.dirtfy.ppp.model

interface Repository<T> {

    suspend fun create(data: T): T
    suspend fun read(filter: (T) -> Boolean): List<T>
    suspend fun update(filter: (T) -> T)
    suspend fun delete(filter: (T) -> Boolean)
}