package edu.fullerton.bookishh.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserDetails(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int?,
    @ColumnInfo(name = "name")
    var name: String?,
    @ColumnInfo(name = "videoFilePath")
    var videoFilePath: String?,
    @ColumnInfo(name = "resumeFilePath")
    var resumeFilePath: String?
)
