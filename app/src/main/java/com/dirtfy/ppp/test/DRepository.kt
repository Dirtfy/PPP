package com.dirtfy.ppp.test

import com.dirtfy.ppp.common.Repository

object DRepository: Repository<DClass> {

    private val list = mutableListOf<DClass>()

    override suspend fun create(data: DClass): DClass {
        list.add(data)
        return data
    }

    override suspend fun read(filter: (DClass) -> Boolean): List<DClass> {
        val r = mutableListOf<DClass>()

        list.forEach {
            if (!filter(it)) return@forEach

            r.add(it)
        }

        return r
    }

    override suspend fun update(filter: (DClass) -> DClass) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(filter: (DClass) -> Boolean) {
        TODO("Not yet implemented")
    }
}