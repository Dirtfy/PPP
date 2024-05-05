package com.dirtfy.ppp.ordering.menuManaging.viewmodel

import com.dirtfy.ppp.common.Repository
import com.dirtfy.ppp.common.viewmodel.ListViewModel
import com.dirtfy.ppp.ordering.menuManaging.model.MenuData
import com.dirtfy.ppp.ordering.menuManaging.model.MenuRepository

class MenuListViewModel: ListViewModel<MenuData>() {

    override val repository: Repository<MenuData>
        get() = MenuRepository
}