package edu.fullerton.bookishh.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDetailsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserDetails)

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getAllById(id: Int): List<UserDetails>

    @Query("UPDATE users SET videoFilePath = NULL WHERE id = :id ")
    suspend fun deleteVideoFile(id: Int)

    @Query("UPDATE users SET resumeFilePath = NULL WHERE id = :id ")
    suspend fun deleteResumeFile(id: Int)

}