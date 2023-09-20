package com.example.apicalldemo

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
import androidx.recyclerview.widget.RecyclerView
import com.example.apicalldemo.adapter.IssueListAdapter
import com.example.apicalldemo.databinding.FragmentIssueListBinding
import com.example.apicalldemo.models.IssuesModel
import com.example.apicalldemo.viewModel.IssueListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class IssueListFragment : Fragment() {
    lateinit var binding: FragmentIssueListBinding
    val viewModel: IssueListViewModel by viewModels()
    lateinit var adapter: IssueListAdapter
    private var count: Int = 1
    private var perPage: Int = 10
    private var page: Int = 1
    private var isLod:Boolean = false
    var update: Int? = null
    var list: ArrayList<IssuesModel> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIssueListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = IssueListAdapter(arrayListOf())
        viewModel.getIssueList(perPage, page)
        binding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v: NestedScrollView, _: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1)
                        .measuredHeight - v.measuredHeight &&
                    scrollY > oldScrollY
                ) {
                    Log.e(javaClass.simpleName, "lastIndex:")

                    update = page+1
                    Toast.makeText(requireContext(), "10 data added", Toast.LENGTH_SHORT).show()
                    Log.e(javaClass.simpleName, "limit if: $update")
                    binding.idPBLoading.visibility = View.VISIBLE
                    viewModel.getIssueList(perPage, update!!)
                   /* if (update == null) {
                        update = count
                    }else {

                        Toast.makeText(requireContext(), "$update data Add else", Toast.LENGTH_SHORT).show()

                        Log.e(javaClass.simpleName, "limit else: $update")
                    }*/
                }
            }
        } as NestedScrollView.OnScrollChangeListener)
        /*for (i in list.indices){
            count = i
            Log.e(javaClass.simpleName, "count: $count")
            adapter.addItem(count,list[i])
        }
        binding.recycleIssueList.adapter = adapter
*/

        viewModel.res.observe(requireActivity(), Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null) {
                        binding.idPBLoading.visibility = View.GONE
                        binding.recycleIssueList.visibility = View.VISIBLE
                        if (isLod){
                            adapter.updateData(it.data.data)
                        }
                        else{
                            isLod = true
                            adapter = IssueListAdapter(it.data.data)
                            binding.recycleIssueList.adapter = adapter

                        }
                        Log.e(javaClass.simpleName, "listSize " + it.data)
                    }
                }

                Status.LOADING -> {
                    binding.idPBLoading.visibility = View.VISIBLE
                }

                Status.ERROR -> {
                    binding.recycleIssueList.visibility = View.GONE
                    binding.idPBLoading.visibility = View.GONE
                }
            }
        })
    }

    fun listAdd(list:ArrayList<IssuesModel>){

    }
}