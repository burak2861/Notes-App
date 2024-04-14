package com.example.notesapp.ui.detail.usecases

import com.example.notesapp.common.BaseUseCase
import com.example.notesapp.common.Response
import com.example.notesapp.database.NoteRepository
import com.example.notesapp.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InsertDataUseCase@Inject constructor(private val noteRepository: NoteRepository) :
    BaseUseCase<InsertDataUseCase.InsertDataParams, Response<Unit>>() {

    override fun execute(input:InsertDataParams): Flow<Response<Unit>> = flow {
        emit(
            noteRepository.insert(input.note)
        )
    }

    data class InsertDataParams(
        val note: Note
    )
}
