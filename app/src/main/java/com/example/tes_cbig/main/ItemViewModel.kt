package com.example.tes_cbig.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tes_cbig.database.AppDatabase
import com.example.tes_cbig.database.Item
import kotlinx.coroutines.launch

class ItemViewModel(
    application: Application,
    private val repository: ItemRepository
) : AndroidViewModel(application) {

    val allItems: LiveData<List<Item>> = repository.allItems

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun refreshItems(token: String) {
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            try {
                repository.refreshItems(token)
                    .onSuccess {
                        _errorMessage.value = null
                    }
                    .onFailure {
                        _errorMessage.value = "Error: ${it.message}"
                    }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun insert(item: Item) = viewModelScope.launch {
        repository.insert(item)
    }

    fun update(item: Item) = viewModelScope.launch {
        repository.update(item)
    }

    fun delete(item: Item) = viewModelScope.launch {
        repository.delete(item)
    }
}