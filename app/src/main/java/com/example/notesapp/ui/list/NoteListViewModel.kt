package com.example.notesapp.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.common.Response
import com.example.notesapp.model.Note
import com.example.notesapp.ui.list.usecases.DeleteNoteUseCase
import com.example.notesapp.ui.list.usecases.GetAllUseCase
import com.example.notesapp.ui.list.usecases.SearchNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val getAllUseCase: GetAllUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val searchNotesUseCase: SearchNotesUseCase
) : ViewModel() {

    private val _allNotesFlow = MutableStateFlow<Response<List<Note>>>(Response.Loading)
    val allNotesFlow: StateFlow<Response<List<Note>>> = _allNotesFlow

    private val _deleteNoteFlow = MutableStateFlow<Response<String>>(Response.Loading)
    val deleteNoteFlow: StateFlow<Response<String>> = _deleteNoteFlow

    private val _searchNoteFlow = MutableStateFlow<Response<List<Note>>>(Response.Loading)
    val searchNoteFlow: StateFlow<Response<List<Note>>> = _searchNoteFlow

    init {
        getAllNotes()
    }

    fun getAllNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(DELAY)
            getAllUseCase.execute(Unit).onEach { result ->
                when (result) {
                    is Response.Success -> {
                        _allNotesFlow.value = Response.Success(result.data)
                    }

                    is Response.Error -> {
                        _allNotesFlow.value = Response.Error(result.message)
                    }

                    Response.Loading -> {
                        // NO-OP
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun deleteNote(note: Note) {
        _deleteNoteFlow.value = Response.Loading
        viewModelScope.launch(Dispatchers.IO) {
            deleteNoteUseCase.execute(DeleteNoteUseCase.DeleteNoteParams(note)).onEach {
                when (it) {
                    is Response.Success -> {
                        getAllNotes()
                        _deleteNoteFlow.value = Response.Success("Başarıyla silindi")
                    }

                    is Response.Error -> {
                        _deleteNoteFlow.value = Response.Error("Bir hata oluştu")
                    }

                    is Response.Loading -> {
                        // NO-OP
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun searchNotes(key: String?) {
        if (key.isNullOrEmpty()) {
            _searchNoteFlow.value = _allNotesFlow.value
        } else {
            _searchNoteFlow.value = Response.Loading
            viewModelScope.launch(Dispatchers.IO) {
                searchNotesUseCase.execute(SearchNotesUseCase.Params(key = key)).onEach { result ->
                    when (result) {
                        is Response.Success -> {
                            if (result.data.isEmpty()) {
                                _searchNoteFlow.value = Response.Error("Bulunamadı")
                                _searchNoteFlow.value = Response.Success(emptyList())
                            } else {
                                _searchNoteFlow.value = Response.Success(result.data)
                            }
                        }

                        is Response.Error -> {
                            _searchNoteFlow.value = Response.Error(result.message)
                        }

                        Response.Loading -> {
                            // NO-OP
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    companion object {
        private const val DELAY = 500L
    }
}
