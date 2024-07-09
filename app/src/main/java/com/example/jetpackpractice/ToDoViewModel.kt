package com.example.jetpackpractice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ToDoViewModel : ViewModel(){
    private val realm = MyApp.realm

    val todoList = realm
        .query<ToDo>()
        .asFlow()
        .map { results ->
            results.list.toList()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )



    fun addTodo(todo: ToDo){
        println("task ${todo.task} data: ${todo.dueDate}")
        realm.writeBlocking {
            copyToRealm(todo)
        }
        //remove all
//        removeAll()
    }
    fun removeTodo(todo: ToDo){
        realm.writeBlocking {
            val item = query<ToDo>("id == $0", todo.id).first().find()
            if (item != null) {
                delete(item)
            }
        }
    }
    fun removeAll(){
        realm.writeBlocking {
            val items = query<ToDo>().find()
            delete(items)
        }
    }
    fun updateTodo(todo: ToDo){
        realm.writeBlocking {
            copyToRealm(
                CompletedTodo().apply {
                    isChecked=true
                    task=todo.task
                    dueDate=todo.dueDate
                }
            )
        }
        realm.writeBlocking {
            val item = query<ToDo>("id == $0", todo.id).first().find()
            if (item != null) {
                delete(item)
            }
        }
    }
}