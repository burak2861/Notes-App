package com.example.notesapp.ui.list.usecases

import com.example.notesapp.common.BaseUseCase
import com.example.notesapp.common.Response
import com.example.notesapp.database.NoteRepository
import com.example.notesapp.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(private val noteRepository: NoteRepository) :
    BaseUseCase<DeleteNoteUseCase.DeleteNoteParams, Response<Unit>>() {

    override fun execute(input: DeleteNoteParams): Flow<Response<Unit>> = flow {
        emit(
            noteRepository.delete(input.note)
        )
    }

    data class DeleteNoteParams(
        val note: Note
    )
}
