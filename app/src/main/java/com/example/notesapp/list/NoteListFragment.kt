package com.example.notesapp.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.databinding.FragmentNoteListBinding
import com.example.notesapp.list.adapter.NoteAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class NoteListFragment : Fragment() {

    private lateinit var binding: FragmentNoteListBinding
    private val viewModel: NoteListViewModel by viewModels()
    private val noteAdapter by lazy(LazyThreadSafetyMode.NONE) {
        NoteAdapter(
            onDeleteClickListener = { note ->
                viewModel.deleteNote(note)
            },
            onItemClickListener = { note ->
                val action =
                    NoteListFragmentDirections.actionNoteListFragmentToNoteDetailFragment(note)
                findNavController().navigate(action)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listeners()
        setupRecyclerView()
        observeNotes()
        viewModel.getAllNotes()

        binding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchNotes(newText.orEmpty())
                return true
            }
        })
    }

    private fun listeners() {
        binding.addButton.setOnClickListener {
            val action =
                NoteListFragmentDirections.actionNoteListFragmentToNoteDetailFragment(null)
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView() {
        binding.noteRecyclerView.apply {
            adapter = noteAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeNotes() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.allNotes.collect { notes ->
                    noteAdapter.submitList(notes)
                }
            }
        }
    }
}