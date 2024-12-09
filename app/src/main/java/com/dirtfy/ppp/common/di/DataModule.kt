package com.dirtfy.ppp.common.di

import com.dirtfy.ppp.data.api.AccountApi
import com.dirtfy.ppp.data.api.ApiProvider
import com.dirtfy.ppp.data.api.MenuApi
import com.dirtfy.ppp.data.api.RecordApi
import com.dirtfy.ppp.data.api.TableApi
import com.dirtfy.ppp.data.api.TransactionManager
import com.dirtfy.ppp.data.api.impl.common.firebase.FireStoreProvider
import com.dirtfy.ppp.data.api.impl.common.firebase.FireStoreTransactionManager
import com.google.firebase.firestore.Transaction
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun providesApiProvider(): ApiProvider {
        return FireStoreProvider.getInstance()
    }

    @Provides
    fun providesTransactionManager(): TransactionManager<Transaction> {
        return FireStoreTransactionManager()
    }

    @Provides
    fun providesAccountApi(): AccountApi {
        return providesApiProvider().accountApi
    }

    @Provides
    fun providesMenuApi(): MenuApi {
        return providesApiProvider().menuApi
    }

    @Provides
    fun providesRecordApi(): RecordApi {
        return providesApiProvider().recordApi
    }

    @Provides
    fun providesTableApi(): TableApi {
        return providesApiProvider().tableApi
    }

}