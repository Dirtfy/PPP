package com.dirtfy.ppp.common.di

import com.dirtfy.ppp.ui.controller.feature.table.TableMenuController
import com.dirtfy.ppp.ui.controller.feature.table.TableMergeController
import com.dirtfy.ppp.ui.controller.feature.table.TableOrderController
import com.dirtfy.ppp.ui.controller.feature.table.impl.viewmodel.TableMenuControllerImpl
import com.dirtfy.ppp.ui.controller.feature.table.impl.viewmodel.TableMergeControllerImpl
import com.dirtfy.ppp.ui.controller.feature.table.impl.viewmodel.TableOrderControllerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ControllerModule {
    @Binds
    abstract fun bindsTableMergeController(
        controllerImpl: TableMergeControllerImpl
    ): TableMergeController

    @Binds
    abstract fun bindsTableOrderController(
        controllerImpl: TableOrderControllerImpl
    ): TableOrderController

    @Binds
    abstract fun bindsTableMenuController(
        controllerImpl: TableMenuControllerImpl
    ): TableMenuController
}