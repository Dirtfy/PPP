package com.dirtfy.ppp.common.di

import com.dirtfy.ppp.data.api.AccountApi
import com.dirtfy.ppp.data.api.MenuApi
import com.dirtfy.ppp.data.api.RecordApi
import com.dirtfy.ppp.data.api.TableApi
import com.dirtfy.ppp.data.api.impl.feature.account.firebase.AccountFireStore
import com.dirtfy.ppp.data.api.impl.feature.menu.firebase.MenuFireStore
import com.dirtfy.ppp.data.api.impl.feature.record.firebase.RecordFireStore
import com.dirtfy.ppp.data.api.impl.feature.table.firebase.TableFireStore
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
    ): AccountApi

    @Binds
    abstract fun bindsMenuRepository(
        menuImplementation: MenuFireStore
    ): MenuApi

    @Binds
    abstract fun bindsRecordRepository(
        recordImplementation: RecordFireStore
    ): RecordApi

    @Binds
    abstract fun bindsTableRepository(
        tableImplementation: TableFireStore
    ): TableApi
}