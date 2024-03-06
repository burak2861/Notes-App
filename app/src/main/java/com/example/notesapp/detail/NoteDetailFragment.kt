package com.example.notesapp.detail

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.notesapp.databinding.FragmentNoteDetailBinding
import com.example.notesapp.model.Note
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteDetailFragment : Fragment() {

    private lateinit var binding: FragmentNoteDetailBinding
    private val viewModel: NoteDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        observe()

    }

    private fun observe() {
        lifecycleScope.launchWhenResumed {
            viewModel.selectedNote.collect {
                setData(it)
            }
        }
    }

    private fun setData(note: Note?) {
        note?.let {
            binding.titleEditText.setText(it.title)
            binding.contentEditText.setText(it.content)
        }
    }

    private fun setupViews() {

        val noteId = arguments?.getLong("noteId") ?: -1
        if (noteId != -1L) {
            viewModel.loadNoteById(noteId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        saveNote()
    }

    private fun saveNote() {
        val title = binding.titleEditText.text.toString()
        val content = binding.contentEditText.text.toString()

        if (title.isNotBlank() || content.isNotBlank()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getParcelable("note", Note::class.java)?.let {
                    viewModel.saveOrUpdateNote(Note(it.id,title,content,it.date))
                }
            }else{
                arguments?.getParcelable<Note>("note")?.let {
                    viewModel.saveOrUpdateNote(Note(it.id,title,content,it.date))
                }
            }
        }
    }
}


