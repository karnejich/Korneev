package ru.korneev.myapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.korneev.myapplication.repository.Repository

class MainViewModel(private val repository: Repository): ViewModel() {

    val myRespone: MutableLiveData<DevelopJson> = MutableLiveData()

    fun getPost(){
        viewModelScope.launch {
            val response : DevelopJson = repository.getPost()
            myRespone.value = response
        }
    }

}