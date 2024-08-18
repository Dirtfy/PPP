package com.dirtfy.ppp.viewmodel.selling.menu.managing

import com.dirtfy.ppp.common.Repository
import com.dirtfy.ppp.contract.model.selling.MenuModelContract.DTO.Menu
import com.dirtfy.ppp.model.selling.menu.managing.MenuRepository
import com.dirtfy.ppp.viewmodel.ListViewModel

class MenuListViewModel: ListViewModel<Menu>() {

    override val repository: Repository<Menu>
        get() = MenuRepository
}