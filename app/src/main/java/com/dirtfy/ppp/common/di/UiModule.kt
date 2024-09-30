package com.dirtfy.ppp.common.di

import android.app.Activity
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dirtfy.ppp.ui.presenter.controller.MenuController
import com.dirtfy.ppp.ui.presenter.controller.account.AccountController
import com.dirtfy.ppp.ui.presenter.controller.account.AccountCreateController
import com.dirtfy.ppp.ui.presenter.controller.account.AccountDetailController
import com.dirtfy.ppp.ui.presenter.controller.account.AccountUpdateController
import com.dirtfy.ppp.ui.presenter.controller.record.RecordController
import com.dirtfy.ppp.ui.presenter.controller.record.RecordDetailController
import com.dirtfy.ppp.ui.presenter.controller.table.TableController
import com.dirtfy.ppp.ui.presenter.controller.table.TableOrderController
import com.dirtfy.ppp.ui.presenter.viewmodel.MenuViewModel
import com.dirtfy.ppp.ui.presenter.viewmodel.account.AccountCreateViewModel
import com.dirtfy.ppp.ui.presenter.viewmodel.account.AccountDetailViewModel
import com.dirtfy.ppp.ui.presenter.viewmodel.account.AccountUpdateViewModel
import com.dirtfy.ppp.ui.presenter.viewmodel.account.AccountViewModel
import com.dirtfy.ppp.ui.presenter.viewmodel.record.RecordDetailViewModel
import com.dirtfy.ppp.ui.presenter.viewmodel.record.RecordViewModel
import com.dirtfy.ppp.ui.presenter.viewmodel.table.TableOrderViewModel
import com.dirtfy.ppp.ui.presenter.viewmodel.table.TableViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

/**
 * UI 레이어에 DI를 하기 위해
 * 컨트롤러에 의존하도록 하고 싶다.
 * 근데 구현체가 뷰모델임
 * 그래서 constructor inject 못하고,
 * 일반 provide 어노테이션 달린 함수도 못함. 뷰모델 클래스 생성 불가
 * 이를 해결하기 위해 알아낸 정보
 * 뷰모델 팩토리를 쓰면된다
 * 근데 그럼 뷰 레이어에서 컨트롤러를 뷰모델 타입으로 구현한 것이 보인다.
 */
@Module
@InstallIn(ActivityComponent::class)
class UiModule {

    @Provides
    fun providesMenuController(
        @ActivityContext context: Context
    ): MenuController {
        val viewModel = ViewModelProvider(context as ComponentActivity)[MenuViewModel::class.java]
        return viewModel
    }

    @Provides
    fun providesAccountController(
        @ActivityContext context: Context
    ): AccountController {
        val viewModel = ViewModelProvider(context as ComponentActivity)[AccountViewModel::class.java]
        return viewModel
    }

    @Provides
    fun providesAccountCreateController(
        @ActivityContext context: Context
    ): AccountCreateController {
        val viewModel = ViewModelProvider(context as ComponentActivity)[AccountCreateViewModel::class.java]
        return viewModel
    }

    @Provides
    fun providesAccountDetailController(
        @ActivityContext context: Context
    ): AccountDetailController {
        val viewModel = ViewModelProvider(context as ComponentActivity)[AccountDetailViewModel::class.java]
        return viewModel
    }

    @Provides
    fun providesAccountUpdateController(
        @ActivityContext context: Context
    ): AccountUpdateController {
        val viewModel = ViewModelProvider(context as ComponentActivity)[AccountUpdateViewModel::class.java]
        return viewModel
    }

    @Provides
    fun providesRecordController(
        @ActivityContext context: Context
    ): RecordController {
        val viewModel = ViewModelProvider(context as ComponentActivity)[RecordViewModel::class.java]
        return viewModel
    }

    @Provides
    fun providesRecordDetailController(
        @ActivityContext context: Context
    ): RecordDetailController {
        val viewModel = ViewModelProvider(context as ComponentActivity)[RecordDetailViewModel::class.java]
        return viewModel
    }

    @Provides
    fun providesTableController(
        @ActivityContext context: Context
    ): TableController {
        val viewModel = ViewModelProvider(context as ComponentActivity)[TableViewModel::class.java]
        return viewModel
    }

    @Provides
    fun providesTableOrderController(
        @ActivityContext context: Context
    ): TableOrderController {
        val viewModel = ViewModelProvider(context as ComponentActivity)[TableOrderViewModel::class.java]
        return viewModel
    }
}