package edu.fullerton.bookishh.db

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

private const val TAG = "MyDatabaseRepository"
private const val DATABASE_NAME = "my-database"
class MyDatabaseRepository(context :Context) {
    private val database: MyDatabase = Room.databaseBuilder(
        context.applicationContext,
        MyDatabase::class.java,
        DATABASE_NAME
    )
        .fallbackToDestructiveMigration()
        .build()

    //	Data Access Object
    private val myDao = database.MyDao()

    //	Executor makes it easier to run stuff in a background thread
    private val executor = Executors.newSingleThreadExecutor()

    private var currentUserIndex: Int = 0

    fun addUserDetails(name: String?, videoFile: String?, resumeFile: String?) {
        val user = UserDetails(null, name, videoFile, resumeFile)
        GlobalScope.launch {
            myDao.insert(user)
        }
    }
}