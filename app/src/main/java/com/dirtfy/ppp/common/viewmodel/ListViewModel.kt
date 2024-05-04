package com.dirtfy.ppp.common.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.accounting.accounting.model.AccountData
import com.dirtfy.ppp.common.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class ListViewModel<T>: ViewModel() {

    abstract val repository: Repository<T>

    private val TAG = "ListViewModel"

    private val _mutableDataList = mutableListOf<T>()
    private val _dataList: MutableLiveData<List<T>> =
        MutableLiveData(listOf())
    val dataList: LiveData<List<T>>
        get() = _dataList

    init {
        dataList.observeForever {
            Log.d("$TAG:init", "dataList: $it")
        }
    }

    private fun changeData(edit: () -> Unit) {
        edit()
        Log.d("$TAG:changeData", "${_dataList.value}")
        Log.d("$TAG:changeData", "${_mutableDataList.toList()}")
        Log.d("$TAG:changeData", "${_dataList.value?.equals(_mutableDataList.toList())}")
        val newList = mutableListOf<T>()
        newList.addAll(_mutableDataList)
        _dataList.value = newList
        Log.d("$TAG:changeData", "${_dataList.value?.equals(_mutableDataList.toList())}")
        Log.d("$TAG:changeData", "${_dataList.value}")
        Log.d("$TAG:changeData", "${_mutableDataList.toList()}")
    }

    fun insertData(data: T) {
        viewModelScope.launch {
            val insertedData = repository.create(data)

            changeData {
                _mutableDataList.add(insertedData)
            }
        }
    }

    fun reloadData(filter: (T) -> Boolean) {
        viewModelScope.launch {
            val reloadedData = repository.read(filter)

            changeData {
                _mutableDataList.clear()
                _mutableDataList.addAll(reloadedData)
            }
        }
    }

    fun appendData(filter: (T) -> Boolean) {
        viewModelScope.launch {
            val loadedData = repository.read(filter)

            changeData {
                _mutableDataList.addAll(loadedData)
            }
        }
    }

    fun removeData(filter: (T) -> Boolean) {
        changeData {
            _mutableDataList.removeIf(filter)
        }
    }

    fun removeDataFromRepository(filter: (T) -> Boolean) {
        viewModelScope.launch {
            repository.delete(filter)

            changeData {
                _mutableDataList.removeIf(filter)
            }
        }
    }

    fun updateData(filter: (T) -> T) {
        viewModelScope.launch {
            repository.update(filter)

            changeData {
                _mutableDataList.replaceAll(filter)
            }
        }
    }

    fun clearData() {
        changeData {
            _mutableDataList.clear()
        }
    }

}