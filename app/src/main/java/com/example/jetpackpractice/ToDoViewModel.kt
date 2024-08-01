package com.example.jetpackpractice

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.mongodb.kbson.ObjectId
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class ToDoViewModel(application : Application) : AndroidViewModel(application){
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

    private fun scheduleReminder(id: String,todo: ToDo) {
        if (todo != null && todo.dueDate != null) {
            println("taskDelay  ${todo.id.toHexString()} ${todo.task} ${todo.dueDate}")
            val delay = Duration.between(LocalDateTime.now(), todo.dueDate).toMillis()
            println("taskDelay: $delay ")
            val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(workDataOf("todoId" to id))
                .build()

            WorkManager.getInstance(getApplication()).enqueue(workRequest)
        }
    }

    fun addTodo(todo: ToDo){
        println("taskDelay added  ${todo.id.toHexString()} ${todo.task} ${todo.dueDate}")
        var insertedTodo: String? =null
        realm.writeBlocking {
            insertedTodo =copyToRealm(todo).id.toHexString()
        }
        scheduleReminder(insertedTodo!!,todo)
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
                    isRemainderShown=false
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