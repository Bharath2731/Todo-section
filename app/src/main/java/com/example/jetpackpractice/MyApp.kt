package com.example.jetpackpractice

import android.app.Application
import androidx.compose.runtime.compositionLocalOf
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class MyApp : Application() {
    companion object {
        lateinit var realm: Realm
    }
    override fun onCreate() {
        super.onCreate()
        realm = Realm.open(
            configuration = RealmConfiguration.create(
                schema = setOf(ToDo::class,CompletedTodo::class)
            )
        )
    }
}