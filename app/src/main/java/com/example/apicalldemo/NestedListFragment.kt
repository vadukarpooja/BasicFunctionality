package com.example.apicalldemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.apicalldemo.databinding.FragmentNestedListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NestedListFragment : Fragment() {
    lateinit var binding:FragmentNestedListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding  = FragmentNestedListBinding.inflate(inflater,container,false)
        return binding.root

    }


}