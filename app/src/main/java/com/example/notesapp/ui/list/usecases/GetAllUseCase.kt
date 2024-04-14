package com.example.notesapp.ui.list.usecases

import com.example.notesapp.common.BaseUseCase
import com.example.notesapp.common.Response
import com.example.notesapp.database.NoteRepository
import com.example.notesapp.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllUseCase @Inject constructor(private val noteRepository: NoteRepository) :
    BaseUseCase<Unit, Response<List<Note>>>() {

    override fun execute(input: Unit): Flow<Response<List<Note>>> = flow {
        emit(
            noteRepository.getAll()
        )
    }
}
