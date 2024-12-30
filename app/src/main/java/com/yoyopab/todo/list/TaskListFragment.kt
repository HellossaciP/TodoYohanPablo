package com.yoyopab.todo.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.yoyopab.todo.R
import com.yoyopab.todo.databinding.FragmentTaskListBinding
import com.yoyopab.todo.detail.DetailActivity
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
    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
    private val adapter = TaskListAdapter()
    // Not used yet
    // private val diffCallbacks = MyItemsDiffCallback
    private lateinit var binding: FragmentTaskListBinding
    val createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as Task?
        if (task != null){
            taskList = taskList + task
            adapter.submitList(taskList)
        }
    }
    val editTaskLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val editedTask = result.data?.getSerializableExtra("task") as? Task
            if (editedTask != null) {
                taskList = taskList.map { if (it.id == editedTask.id) editedTask else it }
                adapter.submitList(taskList)
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
        adapter.submitList(taskList)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recycler.adapter = adapter
        adapter.onClickDelete = {task ->
            taskList = taskList - task
            adapter.submitList(taskList)
        }
        adapter.onClickEdit = {task ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("task", task)
            editTaskLauncher.launch(intent)
        }
        binding.floatingActionButton.setOnClickListener() {
            createTask.launch(Intent(context, DetailActivity::class.java))
        }


        adapter.submitList(taskList)
    }

    companion object {
        const val TASK_KEY = "task"
    }
}
