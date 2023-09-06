package com.example.apicalldemo

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.apicalldemo.databinding.FragmentEditBinding
import com.example.apicalldemo.models.ColorsModel1
import com.example.apicalldemo.models.Images
import com.google.gson.Gson
import java.util.Collections
import kotlin.math.log


class EditFragment : Fragment() {
    lateinit var binding: FragmentEditBinding
    val list: ArrayList<ColorsModel1> = arrayListOf()
    val imgList:ArrayList<Images> = arrayListOf()
    var dupValue: Int = 0
    var value: ColorsModel1 = ColorsModel1()
    var editIndex: ColorsModel1 = ColorsModel1()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addList()
        updateList()

    }

    private fun addList() {
        binding.addBtn.setOnClickListener {
            if (TextUtils.isEmpty(binding.edtAdd.text)) {
                Toast.makeText(requireContext(), "Enter Add Number", Toast.LENGTH_SHORT).show()
            } else {
                dupValue = list.count { it.name == binding.edtAdd.text.toString() }
                Log.e(javaClass.simpleName, "dupValue: $dupValue")
                val data = binding.edtAdd.text.toString()
                val colorsModel1 = ColorsModel1(name = data)
                colorsModel1.let {
                    list.add(it)
                    Log.e(javaClass.simpleName, "onViewCreated it: $it")
                }
                findNavController().navigate(EditFragmentDirections.actionEditFragmentToListFragment(list = Gson().toJson(list)))
                Log.e(javaClass.simpleName, "Gson List: ${Gson().toJson(list)}")
                Log.e(javaClass.simpleName, "add List: $list")
                Log.e(javaClass.simpleName, "updateList same value: ${Collections.frequency(list,value)}")
                /*if (dupValue == 1) {
                    Toast.makeText(requireContext(), "value is already added", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val data = binding.edtAdd.text.toString()
                    val colorsModel1 = ColorsModel1(name = data)
                    colorsModel1.let {
                        list.add(it)
                        Log.e(javaClass.simpleName, "onViewCreated it: $it")
                    }
                    findNavController().navigate(EditFragmentDirections.actionEditFragmentToListFragment(list = Gson().toJson(list)))
                    Log.e(javaClass.simpleName, "Gson List: ${Gson().toJson(list)}")
                    Log.e(javaClass.simpleName, "add List: $list")
                }*/
                binding.edtAdd.text?.clear()
            }
        }

        binding.btnDelete.setOnClickListener {
            if (TextUtils.isEmpty(binding.edtDelete.text)) {
                Toast.makeText(requireContext(), "Enter delete number", Toast.LENGTH_SHORT).show()
            } else if (list.isEmpty()) {
                Toast.makeText(requireContext(), "No Data Available", Toast.LENGTH_SHORT).show()
            } else {
                for (i in list) {
                    if (i.name.contains(binding.edtDelete.text.toString())) {
                        list.remove(i)
                        value = i
                        Log.e(javaClass.simpleName, "remove list: $list")
                        findNavController().navigate(
                            EditFragmentDirections.actionEditFragmentToListFragment(
                                list = Gson().toJson(list)
                            )
                        )
                        Toast.makeText(
                            requireContext(),
                            "Number ${i.name} is remove",
                            Toast.LENGTH_SHORT
                        ).show()
                        break
                    }
                }
                if (!value.name.contains(binding.edtDelete.text.toString())) {
                    Log.e(javaClass.simpleName, "onViewCreated: ")
                    Toast.makeText(
                        requireContext(),
                        "this number ${binding.edtDelete.text.toString()} is not added",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            binding.edtDelete.text?.clear()
        }
    }

    private fun updateList() {
        var isNotAdd:ColorsModel1 = ColorsModel1()
        binding.btnUpdate.setOnClickListener {
            if (list.isEmpty()){
                Toast.makeText(requireContext(), "No Data Available", Toast.LENGTH_SHORT).show()
            }
            else if (TextUtils.isEmpty(binding.edtAdd1.text)) {
                Toast.makeText(requireContext(), "Enter Add Number", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(binding.edtNumber.text)) {
                Toast.makeText(requireContext(), "Enter Add Number", Toast.LENGTH_SHORT).show()
            }
            else {
                for (i in list){
                    if (i.name == binding.edtAdd1.text.toString()){
                        list[list.indexOf(i)] = ColorsModel1(name = binding.edtNumber.text.toString())
                        isNotAdd = i
                        findNavController().navigate(
                            EditFragmentDirections.actionEditFragmentToListFragment(
                                list = Gson().toJson(list)
                            )
                        )
                        break
                    }

                }
                if (!isNotAdd.name.contains(binding.edtAdd1.text.toString())) {
                    Toast.makeText(
                        requireContext(),
                        "this number ${binding.edtAdd1.text.toString()} is not added",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Log.e(javaClass.simpleName, "updateList final: $list")

            }
            binding.edtAdd1.text?.clear()
            binding.edtNumber.text?.clear()
        }
    }
}


