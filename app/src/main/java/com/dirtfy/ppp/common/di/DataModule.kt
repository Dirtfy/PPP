package com.dirtfy.ppp.common.di

import com.dirtfy.ppp.data.api.AccountApi
import com.dirtfy.ppp.data.api.MenuApi
import com.dirtfy.ppp.data.api.RecordApi
import com.dirtfy.ppp.data.api.TableApi
import com.dirtfy.ppp.data.api.impl.common.firebase.FireStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun providesAccountApi(): AccountApi {
        return FireStoreManager.getInstance().accountFireStore
    }

    @Provides
    fun providesMenuApi(): MenuApi {
        return FireStoreManager.getInstance().menuFireStore
    }

    @Provides
    fun providesRecordApi(): RecordApi {
        return FireStoreManager.getInstance().recordFireStore
    }

    @Provides
    fun providesTableApi(): TableApi {
        return FireStoreManager.getInstance().tableFireStore
    }

}