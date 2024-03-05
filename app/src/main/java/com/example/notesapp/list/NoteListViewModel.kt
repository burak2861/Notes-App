package com.example.notesapp.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.model.Note
import com.example.notesapp.database.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(private val repository: NoteRepository) : ViewModel() {

    private val _allNotes = MutableStateFlow<List<Note>>(emptyList())
    val allNotes: StateFlow<List<Note>> = _allNotes

    init {
        getAllNotes()
    }

    fun getAllNotes() {
        viewModelScope.launch {
            repository.getAllNotes.collect { notes ->
                _allNotes.value = notes
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }

    fun searchNotes(query: String) {
        viewModelScope.launch {
            repository.searchNotes(query).collect { filteredNotes ->
                _allNotes.value = filteredNotes
            }
        }
    }
}
