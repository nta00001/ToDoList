package com.example.todolist.presentation.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.todolist.databinding.FragmetTodoBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodolistFragment : Fragment() {
    private var _binding: FragmetTodoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TodoViewModel by viewModels()

    private val adapter = TodoAdapter(
        onCheckChanged = { viewModel.toggle(it) },
        onDelete = { viewModel.delete(it) }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmetTodoBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        binding.btnAdd.setOnClickListener {
            val title = binding.etTitle.text.toString()
            if (title.isNotBlank()) {
                viewModel.add(title)
                binding.etTitle.text.clear()
            } else {
                Toast.makeText(requireContext(), "Title is empty", Toast.LENGTH_SHORT).show()
            }
        }

        // Sử dụng repeatOnLifecycle để thu thập flow một cách an toàn
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todos.collectLatest { todos ->
                    adapter.submitList(todos)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}