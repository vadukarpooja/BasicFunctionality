package com.example.apicalldemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.apicalldemo.adapter.ChildListAdapter
import com.example.apicalldemo.databinding.FragmentNestedListBinding
import com.example.apicalldemo.models.Category
import com.example.apicalldemo.models.Item
import com.example.apicalldemo.viewModel.IssueListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NestedListFragment : Fragment() {
    lateinit var binding: FragmentNestedListBinding
    lateinit var adapter: ChildListAdapter
    val viewModel: IssueListViewModel by viewModels()
    var list: MutableList<Category> = mutableListOf()
    private var isUserScrolling = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNestedListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ChildListAdapter(arrayListOf(), arrayListOf(), onClick = {})
          list = mutableListOf(
            Category(
                "Category 1",
                Item("Item 1"),
                Item("Item 2"),
                Item("Item 3"),
                Item("Item 4"),
                Item("Item 5"),
                Item("Item 6"),
                Item("Item 1"),
                Item("Item 2"),
                Item("Item 3"),
                Item("Item 4"),
                Item("Item 5"),
                Item("Item 6")
            ),
            Category(
                "Category 2",
                Item("Item 1"),
                Item("Item 2"),
                Item("Item 3"),
                Item("Item 4"),
                Item("Item 1"),
                Item("Item 2"),
                Item("Item 3"),
                Item("Item 4"),
                Item("Item 5"),
                Item("Item 6")
            ),
            Category(
                "Category 3",
                Item("Item 1"),
                Item("Item 2"),
                Item("Item 3"),
                Item("Item 4"),
                Item("Item 5"),
                Item("Item 6"),
                Item("Item 7"),
                Item("Item 8"),
                Item("Item 1"),
                Item("Item 2"),
                Item("Item 3"),
                Item("Item 4"),
                Item("Item 5"),
                Item("Item 6")
            ),
            Category(
                "Category 4",
                Item("Item 1"),
                Item("Item 2"),
                Item("Item 3"),
                Item("Item 4"),
                Item("Item 5"),
                Item("Item 6"),
                Item("Item 1"),
                Item("Item 2"),
                Item("Item 3"),
                Item("Item 4"),
                Item("Item 5"),
                Item("Item 6")
            ),
            Category(
                "Category 5",
                Item("Item 1"),
                Item("Item 2"),
                Item("Item 4"),
                Item("Item 5"),
                Item("Item 1"),
                Item("Item 2"),
                Item("Item 3"),
                Item("Item 4"),
                Item("Item 5"),
                Item("Item 6")
            ),
        )
        initTabLayout()
        initRecycler()
        initMediator()

    }



   private fun initTabLayout(){
      list.forEach {
           binding.tabLayout.addTab(binding.tabLayout.newTab().setText(it.name))
       }
   }
    private fun initRecycler() {
        adapter = ChildListAdapter(list, arrayListOf(), onClick = {
        })
        binding.recycleIssueList.adapter = adapter
    }

    private fun initMediator() {
        TabbedListMediator(
            binding.recycleIssueList,
            binding.tabLayout,
            list.indices.toList()
        ).attach()
    }
}