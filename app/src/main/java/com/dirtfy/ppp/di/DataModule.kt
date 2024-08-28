package com.dirtfy.ppp.di

import com.dirtfy.ppp.data.source.firestore.account.AccountFireStore
import com.dirtfy.ppp.data.source.firestore.menu.MenuFireStore
import com.dirtfy.ppp.data.source.firestore.record.RecordFireStore
import com.dirtfy.ppp.data.source.firestore.table.TableFireStore
import com.dirtfy.ppp.data.source.repository.AccountRepository
import com.dirtfy.ppp.data.source.repository.MenuRepository
import com.dirtfy.ppp.data.source.repository.RecordRepository
import com.dirtfy.ppp.data.source.repository.TableRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindMenuRepository(
        menuFireStore: MenuFireStore
    ): MenuRepository

    @Binds
    abstract fun bindTableRepository(
        tableFireStore: TableFireStore
    ): TableRepository

    @Binds
    abstract fun bindRecordRepository(
        recordFireStore: RecordFireStore
    ): RecordRepository

    @Binds
    abstract fun bindAccountRepository(
        accountFireStore: AccountFireStore
    ): AccountRepository
}