package com.dirtfy.ppp.common

interface Repository<T> {

    suspend fun create(data: T): T
    suspend fun readAll(): List<T>
    suspend fun update(data: T)
    suspend fun delete(data: T)
}