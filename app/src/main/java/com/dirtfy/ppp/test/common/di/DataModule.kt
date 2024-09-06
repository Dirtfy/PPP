package com.dirtfy.ppp.test.common.di

import com.dirtfy.ppp.test.data.logic.AccountLogic
import com.dirtfy.ppp.test.data.logic.MenuLogic
import com.dirtfy.ppp.test.data.logic.RecordLogic
import com.dirtfy.ppp.test.data.logic.TableLogic
import com.dirtfy.ppp.test.data.logic.impl.AccountService
import com.dirtfy.ppp.test.data.logic.impl.MenuService
import com.dirtfy.ppp.test.data.logic.impl.RecordService
import com.dirtfy.ppp.test.data.logic.impl.TableService
import com.dirtfy.ppp.test.data.source.impl.firestore.menu.MenuFireStore
import com.dirtfy.ppp.test.data.source.AccountSource
import com.dirtfy.ppp.test.data.source.MenuSource
import com.dirtfy.ppp.test.data.source.RecordSource
import com.dirtfy.ppp.test.data.source.TableSource
import com.dirtfy.ppp.test.data.source.impl.firestore.account.AccountFireStore
import com.dirtfy.ppp.test.data.source.impl.firestore.record.RecordFireStore
import com.dirtfy.ppp.test.data.source.impl.firestore.table.TableFireStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindMenuSource(
        menuFireStore: MenuFireStore
    ): MenuSource

    @Binds
    abstract fun bindTableSource(
        tableFireStore: TableFireStore
    ): TableSource

    @Binds
    abstract fun bindRecordSource(
        recordFireStore: RecordFireStore
    ): RecordSource

    @Binds
    abstract fun bindAccountSource(
        accountFireStore: AccountFireStore
    ): AccountSource

    @Binds
    abstract fun bindMenuLogic(
        menuService: MenuService
    ): MenuLogic

    @Binds
    abstract fun bindTableLogic(
        tableService: TableService
    ): TableLogic

    @Binds
    abstract fun bindRecordLogic(
        recordService: RecordService
    ): RecordLogic

    @Binds
    abstract fun bindAccountLogic(
        accountService: AccountService
    ): AccountLogic
}