package com.example.notesapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import androidx.room.*
import com.example.notesapp.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface NoteDao {
    @Query("SELECT * FROM note ORDER BY date DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY date DESC")
    fun searchNotes(query: String): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id = :noteId")
    suspend fun getNoteById(noteId: Long): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg note: Note)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg note: Note)

    @Query("UPDATE note SET title = :title, content = :content WHERE id = :id")
    fun updateWithQuery(id:Long,title:String,content:String)

    @Delete
    fun delete(vararg note: Note)
}