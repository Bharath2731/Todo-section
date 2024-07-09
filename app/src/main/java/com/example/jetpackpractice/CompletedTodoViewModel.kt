package com.example.jetpackpractice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CompletedTodoViewModel:ViewModel() {
    private val realm = MyApp.realm

    val completedTodoList = realm
        .query<CompletedTodo>()
        .asFlow()
        .map { results ->
            results.list.toList()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    fun addCompletedTodo(completedTodo: CompletedTodo){
        realm.writeBlocking {
            copyToRealm(completedTodo)
        }
    }
    fun removeCompletedTodo(completedTodo: CompletedTodo){
        realm.writeBlocking {
            val item = query<CompletedTodo>("id == $0", completedTodo.id).first().find()
            if (item != null) {
                delete(item)
            }
        }
    }
    fun removeAllCompletedTodo(){
        realm.writeBlocking {
            val items = query<CompletedTodo>().find()
            delete(items)
        }
    }
    fun updateCompletedTodo(completedTodo: CompletedTodo){
        realm.writeBlocking {
            val item = query<CompletedTodo>("id == $0", completedTodo.id).first().find()
            if (item != null) {
                item.isChecked = completedTodo.isChecked
                copyToRealm(
                    ToDo().apply {
                        isChecked=completedTodo.isChecked
                        task=completedTodo.task
                        dueDate=completedTodo.dueDate
                    }
                )
                delete(item)
            }



        }
    }

}