package com.example.notesapp.database

import com.example.notesapp.common.Response
import com.example.notesapp.model.Note
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {
    override suspend fun getAll(): Response<List<Note>> {
        return try {
            Response.Success(noteDao.getAllNotes())
        } catch (e: Exception) {
            Response.Error(e.message.orEmpty())
        }
    }

    override suspend fun searchNotes(query: String): Response<List<Note>> {
        return try {
            Response.Success(noteDao.searchNotes(query))
        } catch (e: Exception) {
            Response.Error(e.message.orEmpty())
        }
    }

    override suspend fun insert(note: Note): Response<Unit> {
        return try {
            noteDao.insert(note)
            Response.Success(Unit)
        } catch (e: Exception) {
            Response.Error(e.message.orEmpty())
        }
    }

    override suspend fun update(note: Note): Response<Unit> {
        return try {
            noteDao.update(note)
            Response.Success(Unit)
        } catch (e: Exception) {
            Response.Error(e.message.orEmpty())
        }
    }

    override suspend fun delete(note: Note): Response<Unit> {
        return try {
            noteDao.delete(note)
            Response.Success(Unit)
        } catch (e: Exception) {
            Response.Error(e.message.orEmpty())
        }
    }
}