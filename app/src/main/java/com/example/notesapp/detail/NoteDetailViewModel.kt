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
class NoteDetailViewModel @Inject constructor(private val repository: NoteRepository, savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _args = NoteDetailFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val _selectedNote = MutableStateFlow<Note?>(null)
    val selectedNote: StateFlow<Note?> = _selectedNote

    init {
        viewModelScope.launch {
            _selectedNote.emit(_args.note)
        }
    }

    fun loadNoteById(noteId: Long) {
        viewModelScope.launch {
            if (noteId != -1L) {
                val note = repository.getNoteById(noteId)
                _selectedNote.value = note
            } else {
                _selectedNote.value = Note(id = 1, title = "", content = "", date = Date())
            }
        }
    }
fun getNotes(){
    viewModelScope.launch {
        repository.getAllNotes.collect { notes ->
           val a = notes
        }
    }
}
    fun saveOrUpdateNote(note: Note) {
        viewModelScope.launch {
            if (note.id != -1L) {
                repository.update(note)
            } else {
                repository.insert(note)
            }
        }
    }
}
