package com.example.notesapp.database

import com.example.notesapp.common.Response
import com.example.notesapp.model.Note

interface NoteRepository {

    suspend fun getAll(): Response<List<Note>>

    suspend fun searchNotes(query: String): Response<List<Note>>

    suspend fun delete(note: Note) : Response<Unit>

    suspend fun insert(note: Note) : Response<Unit>

    suspend fun update(note: Note) : Response<Unit>
}