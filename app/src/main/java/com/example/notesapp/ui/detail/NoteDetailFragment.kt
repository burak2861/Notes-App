package com.example.notesapp.ui.detail

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        if (title.isBlank() && content.isBlank()) {
            return
        }
        val newHashCode = (title + content).hashCode()

        val parcelable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(NOTE, Note::class.java)
        } else {
            arguments?.getParcelable(NOTE)
        }

        parcelable?.let {
            if (it.myHashCode != newHashCode) {
                viewModel.updateData(
                    Note(
                        id = it.id,
                        title = title,
                        content = content,
                        myHashCode = (title + content).hashCode()
                    )
                )
            }
        } ?: run {
            viewModel.insertData(
                Note(
                    title = title,
                    content = content,
                    myHashCode = (title + content).hashCode()
                )
            )
        }
    }

    companion object {
        private const val NOTE = "note"
    }
}


