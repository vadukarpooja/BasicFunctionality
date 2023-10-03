package com.example.apicalldemo.paginationwithswiptorefresh

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apicalldemo.adapter.IssueListAdapter
import com.example.apicalldemo.base.MainActivity
import com.example.apicalldemo.databinding.FragmentIssueListBinding
import com.example.apicalldemo.models.IssuesModel
import com.example.apicalldemo.utils.Status
import com.example.apicalldemo.viewModel.IssueListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class IssueListFragment : Fragment() {
    lateinit var binding: FragmentIssueListBinding
    val viewModel: IssueListViewModel by viewModels()
    lateinit var adapter: IssueListAdapter
    private var count: Int = 0
    private var perPage: Int = 15
    private var page: Int = 1
    private var isLod: Boolean = false
    var update: Int? = null
    var list: ArrayList<IssuesModel> = arrayListOf()
    var isUserScrolling: Boolean = false
    var linearLayoutManager: LinearLayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIssueListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = IssueListAdapter(arrayListOf(), onClick = {})
        viewModel.getIssueList(perPage, page)
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getIssueList(perPage, page)
        }

        binding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v: NestedScrollView, _: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1)
                        .measuredHeight - v.measuredHeight &&
                    scrollY > oldScrollY
                ) {
                    Log.e(javaClass.simpleName, "lastIndex:")
                    if (update == null) {
                        update = page + 1
                    } else {
                        count = update!!
                        Log.e(javaClass.simpleName, "limit if: ${count++}")
                        update = count++
                        Toast.makeText(requireContext(), "15 data added", Toast.LENGTH_SHORT).show()
                    }
                    if (update == 11) {
                        Toast.makeText(requireContext(), "Data limit is 100 ", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Log.e(javaClass.simpleName, "limit if: $update")
                        binding.idPBLoading.visibility = View.VISIBLE
                        viewModel.getIssueList(perPage, update!!)
                    }
                }
            }
        } as NestedScrollView.OnScrollChangeListener)

        viewModel.res.observe(requireActivity(), Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null) {
                        binding.idPBLoading.visibility = View.GONE
                        binding.recycleIssueList.visibility = View.VISIBLE
                        list = it.data
                        if (isLod) {
                            if (binding.swipeRefresh.isRefreshing) {
                                binding.swipeRefresh.isRefreshing = false
                                adapter.updateDataRefresh(it.data)
                            } else {
                                adapter.updateData(it.data)
                            }
                            Log.e(javaClass.simpleName, "update list " + it.data)
                            Log.e(javaClass.simpleName, "update listSize: " + it.data.size)
                        } else {
                            isLod = true
                            adapter = IssueListAdapter(it.data, onClick = {})
                            binding.recycleIssueList.adapter = adapter
                            Log.e(javaClass.simpleName, "Add list " + it.data)
                            Log.e(javaClass.simpleName, "Add list: " + it.data.size)
                        }

                    }
                }

                Status.LOADING -> {
                    binding.idPBLoading.visibility = View.VISIBLE
                    // binding.recycleIssueList.visibility = View.GONE
                }

                Status.ERROR -> {
                    binding.recycleIssueList.visibility = View.GONE
                    binding.idPBLoading.visibility = View.GONE
                }
            }
        })
    }


}