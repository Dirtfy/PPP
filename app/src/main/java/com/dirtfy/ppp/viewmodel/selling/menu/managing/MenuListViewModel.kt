package com.dirtfy.ppp.viewmodel.selling.menu.managing

import com.dirtfy.ppp.model.Repository
import com.dirtfy.ppp.model.selling.menu.managing.MenuData
import com.dirtfy.ppp.model.selling.menu.managing.MenuRepository
import com.dirtfy.ppp.viewmodel.ListViewModel

class MenuListViewModel: ListViewModel<MenuData>() {

    override val repository: Repository<MenuData>
        get() = MenuRepository
}