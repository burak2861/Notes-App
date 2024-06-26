package com.example.notesapp.ui.list

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.R
import com.example.notesapp.common.DelayedOnQueryTextListener
import com.example.notesapp.common.Response
import com.example.notesapp.databinding.FragmentNoteListBinding
import com.example.notesapp.ui.list.adapter.NoteAdapter
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
                findNavController().navigate(
                    NoteListFragmentDirections.actionNoteListFragmentToNoteDetailFragment(note)
                )
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
        setHasOptionsMenu(true)

        listeners()
        setupRecyclerView()
        observeNotes()
        viewModel.getAllNotes()
    }

    private fun listeners() {
        binding.addButton.setOnClickListener {
            findNavController().navigate(
                NoteListFragmentDirections.actionNoteListFragmentToNoteDetailFragment(null)
            )
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
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.allNotesFlow.collect { response ->
                    when (response) {
                        is Response.Success -> {
                            noteAdapter.submitList(response.data)
                        }

                        is Response.Error -> {
                            val message = response.message
                            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                        }

                        is Response.Loading -> {
                            // NO-OP
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.deleteNoteFlow.collect { response ->
                    when (response) {
                        is Response.Success -> {
                            val message = response.data
                            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                        }

                        is Response.Error -> {
                            val message = response.message
                            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                        }

                        is Response.Loading -> {
                            // NO-OP
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.searchNoteFlow.collect { response ->
                    when (response) {
                        is Response.Success -> {
                            noteAdapter.submitList(response.data)
                        }

                        is Response.Error -> {
                            val message = response.message
                            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                        }

                        is Response.Loading -> {
                            // NO-OP
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        val manager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager?
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setSearchableInfo(manager?.getSearchableInfo(requireActivity().componentName))
        searchView.setOnQueryTextListener(object : DelayedOnQueryTextListener() {
            override fun onDelayerQueryTextChange(query: String?) {
                if (query.isNullOrEmpty() || query.length > MIN_SEARCH_LENGTH) {
                    viewModel.searchNotes(key = query)
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val MIN_SEARCH_LENGTH = 2
    }
}
