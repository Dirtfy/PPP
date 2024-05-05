package com.dirtfy.ppp.common.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.common.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class SingleViewModel<T>: ViewModel() {

    abstract val repository: Repository<T>
    abstract val initialValue: T

    private val TAG = "SingleViewModel"

    private val _data: MutableStateFlow<T> =
        MutableStateFlow(initialValue)
    val data: StateFlow<T>
        get() = _data

    fun reloadData(filter: (T) -> Boolean) {
        viewModelScope.launch {
            val target = repository.read(filter)

            if (target.isEmpty()) {
                Log.d("$TAG:reloadData", "target empty")
                return@launch
            }

            _data.value = target[0]
        }
    }

    fun updateData(newData: T) {
        viewModelScope.launch {
            repository.update {
                return@update if (_data.value == it)
                    newData
                else
                    it
            }

            _data.value = newData
        }
    }

}