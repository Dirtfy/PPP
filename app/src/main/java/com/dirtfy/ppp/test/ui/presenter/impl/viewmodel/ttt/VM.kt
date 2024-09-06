package com.dirtfy.ppp.test.ui.presenter.impl.viewmodel.ttt

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VM @Inject constructor(
    private val h: H
): ViewModel() {
}