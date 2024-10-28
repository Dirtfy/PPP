package com.dirtfy.ppp.common.di

import com.dirtfy.ppp.data.api.AccountApi
import com.dirtfy.ppp.data.api.MenuApi
import com.dirtfy.ppp.data.api.RecordApi
import com.dirtfy.ppp.data.api.TableApi
import com.dirtfy.ppp.data.api.impl.feature.account.firebase.AccountFireStore
import com.dirtfy.ppp.data.api.impl.feature.menu.firebase.MenuFireStore
import com.dirtfy.ppp.data.api.impl.feature.record.firebase.RecordFireStore
import com.dirtfy.ppp.data.api.impl.feature.table.firebase.TableFireStore
import com.dirtfy.ppp.data.logic.AccountBusinessLogic
import com.dirtfy.ppp.data.logic.MenuBusinessLogic
import com.dirtfy.ppp.data.logic.RecordBusinessLogic
import com.dirtfy.ppp.data.logic.TableBusinessLogic
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindsAccountApi(
        accountImplementation: AccountFireStore
    ): AccountApi

    @Binds
    abstract fun bindsMenuApi(
        menuImplementation: MenuFireStore
    ): MenuApi

    @Binds
    abstract fun bindsRecordApi(
        recordImplementation: RecordFireStore
    ): RecordApi

    @Binds
    abstract fun bindsTableApi(
        tableImplementation: TableFireStore
    ): TableApi

    companion object {

        @Provides
        fun providesAccountBusinessLogic(
            accountApi: AccountApi
        ): AccountBusinessLogic {
            return AccountBusinessLogic(accountApi)
        }

        @Provides
        fun providesMenuBusinessLogic(
            menuApi: MenuApi
        ): MenuBusinessLogic {
            return MenuBusinessLogic(menuApi)
        }

        @Provides
        fun providesRecordBusinessLogic(
            recordApi: RecordApi
        ): RecordBusinessLogic {
            return RecordBusinessLogic(recordApi)
        }

        @Provides
        fun providesTableBusinessLogic(
            tableApi: TableApi,
            recordApi: RecordApi
        ): TableBusinessLogic {
            return TableBusinessLogic(tableApi, recordApi)
        }

    }
}