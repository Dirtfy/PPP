package com.dirtfy.ppp.common.di

import com.dirtfy.ppp.ui.controller.feature.account.AccountCreateController
import com.dirtfy.ppp.ui.controller.feature.account.AccountDetailController
import com.dirtfy.ppp.ui.controller.feature.account.AccountListController
import com.dirtfy.ppp.ui.controller.feature.account.AccountUpdateController
import com.dirtfy.ppp.ui.controller.feature.account.impl.viewmodel.AccountCreateControllerImpl
import com.dirtfy.ppp.ui.controller.feature.account.impl.viewmodel.AccountDetailControllerImpl
import com.dirtfy.ppp.ui.controller.feature.account.impl.viewmodel.AccountListControllerImpl
import com.dirtfy.ppp.ui.controller.feature.account.impl.viewmodel.AccountUpdateControllerImpl
import com.dirtfy.ppp.ui.controller.feature.menu.MenuListController
import com.dirtfy.ppp.ui.controller.feature.menu.MenuUpdateController
import com.dirtfy.ppp.ui.controller.feature.menu.impl.viewmodel.MenuListControllerImpl
import com.dirtfy.ppp.ui.controller.feature.menu.impl.viewmodel.MenuUpdateControllerImpl
import com.dirtfy.ppp.ui.controller.feature.record.RecordDetailController
import com.dirtfy.ppp.ui.controller.feature.record.RecordListController
import com.dirtfy.ppp.ui.controller.feature.record.impl.viewmodel.RecordDetailControllerImpl
import com.dirtfy.ppp.ui.controller.feature.record.impl.viewmodel.RecordListControllerImpl
import com.dirtfy.ppp.ui.controller.feature.table.TableListController
import com.dirtfy.ppp.ui.controller.feature.table.TableMenuController
import com.dirtfy.ppp.ui.controller.feature.table.TableOrderController
import com.dirtfy.ppp.ui.controller.feature.table.impl.viewmodel.TableListControllerImpl
import com.dirtfy.ppp.ui.controller.feature.table.impl.viewmodel.TableMenuControllerImpl
import com.dirtfy.ppp.ui.controller.feature.table.impl.viewmodel.TableOrderControllerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ControllerModule {
    @Binds
    abstract fun bindsTableListController(
        controllerImpl: TableListControllerImpl
    ): TableListController

    @Binds
    abstract fun bindsTableOrderController(
        controllerImpl: TableOrderControllerImpl
    ): TableOrderController

    @Binds
    abstract fun bindsTableMenuController(
        controllerImpl: TableMenuControllerImpl
    ): TableMenuController

    @Binds
    abstract fun bindsAccountListController(
        controllerImpl: AccountListControllerImpl
    ): AccountListController

    @Binds
    abstract fun bindsAccountCreateController(
        controllerImpl: AccountCreateControllerImpl
    ): AccountCreateController

    @Binds
    abstract fun bindsAccountDetailController(
        controllerImpl: AccountDetailControllerImpl
    ): AccountDetailController

    @Binds
    abstract fun bindsAccountUpdateController(
        controllerImpl: AccountUpdateControllerImpl
    ): AccountUpdateController

    @Binds
    abstract fun bindsRecordListController(
        controllerImpl: RecordListControllerImpl
    ): RecordListController

    @Binds
    abstract fun bindsRecordDetailController(
        controllerImpl: RecordDetailControllerImpl
    ): RecordDetailController

    @Binds
    abstract fun bindsMenuListController(
        controllerImpl: MenuListControllerImpl
    ): MenuListController

    @Binds
    abstract fun bindsMenuUpdateController(
        controllerImpl: MenuUpdateControllerImpl
    ): MenuUpdateController
}