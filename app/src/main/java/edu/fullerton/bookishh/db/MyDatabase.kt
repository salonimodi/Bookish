package edu.fullerton.bookishh.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserDetails::class
    ],
    version = 1
)

abstract class MyDatabase: RoomDatabase() {
    abstract fun MyDao(): UserDetailsDAO

}