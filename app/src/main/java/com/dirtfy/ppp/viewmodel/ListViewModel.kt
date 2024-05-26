package com.dirtfy.ppp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.model.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class ListViewModel<T>: ViewModel() {

    abstract val repository: Repository<T>

    private val TAG = "ListViewModel"

    private val _mutableDataList = mutableListOf<T>()
    private val _dataList: MutableStateFlow<List<T>> =
        MutableStateFlow(listOf())
    val dataList: StateFlow<List<T>>
        get() = _dataList

    private fun changeData(edit: () -> Unit) {
        edit()
        val newList = mutableListOf<T>()
        newList.addAll(_mutableDataList)
        _dataList.value = newList
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