package com.dirtfy.ppp.data.logic.menu

import com.dirtfy.ppp.data.logic.CustomException

sealed class MenuException(
    massage: String
): CustomException(massage) {
    class NonUniqueName: MenuException("name is not unique")

    class NameLoss: MenuException("name not found")
    class PriceLoss: MenuException("price not found")
}