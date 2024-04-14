package com.example.notesapp.ui.detail.usecases

import com.example.notesapp.common.BaseUseCase
import com.example.notesapp.common.Response
import com.example.notesapp.database.NoteRepository
import com.example.notesapp.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateDataUseCase @Inject constructor(private val noteRepository: NoteRepository) :
    BaseUseCase<UpdateDataUseCase.UpdateDataParams, Response<Unit>>() {

    override fun execute(input: UpdateDataParams): Flow<Response<Unit>> = flow {
        emit(
            noteRepository.update(input.note)
        )
    }

    data class UpdateDataParams(
        val note: Note
    )
}
