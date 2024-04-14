package com.example.notesapp.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.common.Response
import com.example.notesapp.model.Note
import com.example.notesapp.database.NoteRepository
import com.example.notesapp.ui.detail.usecases.InsertDataUseCase
import com.example.notesapp.ui.detail.usecases.UpdateDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val insertDataUseCase: InsertDataUseCase,
    private val updateDataUseCase: UpdateDataUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _args = NoteDetailFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val _selectedNote = MutableStateFlow<Response<Note?>>(Response.Success(null))
    val selectedNote: StateFlow<Response<Note?>> = _selectedNote

    init {
        viewModelScope.launch {
            _selectedNote.emit(Response.Success(_args.note))
        }
    }

    fun updateData(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            updateDataUseCase.execute(UpdateDataUseCase.UpdateDataParams(note))
                .launchIn(viewModelScope)
        }
    }

    fun insertData(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            insertDataUseCase.execute(InsertDataUseCase.InsertDataParams(note))
                .launchIn(viewModelScope)
        }
    }
}
