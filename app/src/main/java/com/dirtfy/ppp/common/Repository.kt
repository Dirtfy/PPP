package com.dirtfy.ppp.common

import kotlinx.coroutines.flow.Flow

interface Repository<T> {

    suspend fun create(data: T): T
    suspend fun read(filter: (T) -> Boolean): List<T>
    suspend fun update(filter: (T) -> T)
    suspend fun delete(filter: (T) -> Boolean)
}