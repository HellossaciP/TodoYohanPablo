package com.yoyopab.todo.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.yoyopab.todo.R
import com.yoyopab.todo.data.Api
import com.yoyopab.todo.data.TaskListViewModel
import com.yoyopab.todo.databinding.FragmentTaskListBinding
import com.yoyopab.todo.detail.DetailActivity
import kotlinx.coroutines.launch
import java.util.UUID

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TaskListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskListFragment : Fragment() {

    private val viewModel: TaskListViewModel by viewModels()
    private val adapter = TaskListAdapter()
    // Not used yet
    // private val diffCallbacks = MyItemsDiffCallback
    private lateinit var binding: FragmentTaskListBinding
    val createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as Task?
        if (task != null){
            viewModel.add(task)
        }
    }
    val editTaskLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val editedTask = result.data?.getSerializableExtra("task") as? Task
            if (editedTask != null) {
                viewModel.update(editedTask)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentTaskListBinding.inflate(layoutInflater)
        val rootView = binding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recycler.adapter = adapter
        adapter.onClickDelete = {task ->
            viewModel.delete(task)
        }
        adapter.onClickEdit = {task ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("task", task)
            editTaskLauncher.launch(intent)
        }
        binding.floatingActionButton.setOnClickListener() {
            createTask.launch(Intent(context, DetailActivity::class.java))
        }

        lifecycleScope.launch { // on lance une coroutine car `collect` est `suspend`
            viewModel.tasksStateFlow.collect { newList ->
                adapter.submitList(newList)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            // Ici on ne va pas gérer les cas d'erreur donc on force le crash avec "!!"
            val user = Api.userWebService.fetchUser().body()!!
            binding.userTextView.text = user.name
        }
        viewModel.refresh()
    }

    companion object {
        const val TASK_KEY = "task"
    }
}
