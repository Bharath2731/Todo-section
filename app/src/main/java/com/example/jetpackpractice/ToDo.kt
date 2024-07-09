package com.example.jetpackpractice

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.coroutines.flow.MutableStateFlow
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId
import java.time.LocalDateTime
import java.time.ZoneOffset

data class ToDoList(
    var todos:List<ToDo>
)
class ToDo: RealmObject{
    @PrimaryKey
    var id :ObjectId = BsonObjectId()
    var isChecked:Boolean=false
    var task:String?=null
    var dueDateLong: Long? = null
    @Ignore
    var dueDate: LocalDateTime?
        get() = dueDateLong?.let { LocalDateTime.ofEpochSecond(it,0, ZoneOffset.UTC) }
        set(value) {
            dueDateLong = value?.toEpochSecond(ZoneOffset.UTC)
        }
}
class CompletedTodo: RealmObject{
    @PrimaryKey
    var id :ObjectId = BsonObjectId()
    var isChecked:Boolean=true
    var task:String?=null
    var dueDateLong: Long? = null
    @Ignore
    var dueDate: LocalDateTime?
        get() = dueDateLong?.let { LocalDateTime.ofEpochSecond(it,0, ZoneOffset.UTC) }
        set(value) {
            dueDateLong = value?.toEpochSecond(ZoneOffset.UTC)
        }
}
object MyToDo{
    val todos:MutableStateFlow<List<ToDo>?> = MutableStateFlow(null)
    val completedTodos:MutableStateFlow<List<ToDo>?> = MutableStateFlow(null)
}