package com.example.jetpackpractice

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.realm.kotlin.ext.query
import org.mongodb.kbson.BsonObjectId

class ReminderWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    private val realm = MyApp.realm
    override fun doWork(): Result {
        val todoId = inputData.getString("todoId")
        var todo: ToDo? = null
        // Get the ToDo or CompletedTodo object from the database using todoId
        // ...
        todo = realm.query<ToDo>("id == $0", todoId?.let { BsonObjectId(it) }).first().find()
        realm.writeBlocking {
            if (todo != null) {
                findLatest(todo)?.isRemainderShown = true
            }
        }
        // Show the popup message
        // You can use a BroadcastReceiver or a system notification to show the popup

        // Update the isReminderShown flag in the database to prevent showing the reminder again
        // ...

        return Result.success()
    }
}