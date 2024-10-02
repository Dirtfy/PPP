package com.dirtfy.ppp.common.di

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
    abstract fun bindsAccountRepository(
        accountImplementation: AccountFireStore
    ): AccountRepository

    @Binds
    abstract fun bindsMenuRepository(
        menuImplementation: MenuFireStore
    ): MenuRepository

    @Binds
    abstract fun bindsRecordRepository(
        recordImplementation: RecordFireStore
    ): RecordRepository

    @Binds
    abstract fun bindsTableRepository(
        tableImplementation: TableFireStore
    ): TableRepository
}