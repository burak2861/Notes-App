package com.example.notesapp.ui.list.usecases

import com.example.notesapp.common.BaseUseCase
import com.example.notesapp.common.Response
import com.example.notesapp.database.NoteRepository
import com.example.notesapp.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchNotesUseCase @Inject constructor(private val noteRepository: NoteRepository) :
    BaseUseCase<SearchNotesUseCase.Params, Response<List<Note>>>() {

    override fun execute(input: Params): Flow<Response<List<Note>>> = flow {
        emit(
            noteRepository.searchNotes(input.key)
        )
    }

    data class Params(
        val key: String
    )
}