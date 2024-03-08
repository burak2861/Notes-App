package com.example.notesapp.detail

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.notesapp.databinding.FragmentNoteDetailBinding
import com.example.notesapp.model.Note
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

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

    override fun onStop() {
        saveNote()
        super.onStop()
    }

    private fun saveNote() {
        val title = binding.titleEditText.text.toString()
        val content = binding.contentEditText.text.toString()

        if (title.isNotBlank() || content.isNotBlank()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getParcelable("note", Note::class.java)?.let {
                    viewModel.updateData(Note(it.id, title, content))
                } ?: run {
                    viewModel.insertData(Note(title = title, content = content))
                }
            } else {
                arguments?.getParcelable<Note>("note")?.let {
                    viewModel.updateData(Note(it.id, title, content))
                } ?: run {
                    viewModel.insertData(Note(title = title, content = content))
                }
            }
        }
    }
}


