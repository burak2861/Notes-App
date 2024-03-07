package com.example.notesapp.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.model.Note
import com.example.notesapp.database.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val repository: NoteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _args = NoteDetailFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val _selectedNote = MutableStateFlow<Note?>(null)
    val selectedNote: StateFlow<Note?> = _selectedNote

    init {
        viewModelScope.launch {
            _selectedNote.emit(_args.note)
        }
    }

    fun updateData(note: Note) {
        viewModelScope.launch {
            repository.update(note)
        }
    }

    fun insertData(note: Note) {
        viewModelScope.launch {
            repository.insert(note)
        }
    }
}
