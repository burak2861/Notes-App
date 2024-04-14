package com.example.notesapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.*
import com.example.notesapp.model.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM note ORDER BY date DESC")
    suspend fun getAllNotes(): List<Note>

    @Query("SELECT * FROM note WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY date DESC")
    suspend fun searchNotes(query: String): List<Note>

    @Query("SELECT * FROM note WHERE id = :noteId")
    suspend fun getNoteById(noteId: Long): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg note: Note)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(vararg note: Note)

    @Query("UPDATE note SET title = :title, content = :content WHERE id = :id")
    suspend fun updateWithQuery(id:Long,title:String,content:String)

    @Delete
    suspend fun delete(vararg note: Note)
}