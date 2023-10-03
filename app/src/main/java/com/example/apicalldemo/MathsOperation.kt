package com.example.apicalldemo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.example.apicalldemo.adapter.MatchOperationAdapter
import com.example.apicalldemo.databinding.FragmentMethemeticsOprationBinding
import com.example.apicalldemo.models.IssuesModel
import com.example.apicalldemo.utils.list


class MathsOperation : Fragment() {
    lateinit var binding: FragmentMethemeticsOprationBinding
    lateinit var adapter: MatchOperationAdapter
    private var operationValue: Int = 0
    private var operationValueF: Double = 0.0
    private var valueSumNumber: Int = 0
    private var valueNumber: Int = 0
    private var valueSumNumberF: Double = 0.0
    private var valueNumberF: Double = 0.0
    private var isSubClick: Boolean = false
    private var isAddClick: Boolean = false
    private var isMultiClick: Boolean = false
    private var isDivClick: Boolean = false
    var value:Double = 0.0
    var updateValue:Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMethemeticsOprationBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.radio.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            binding.recycle.visibility = View.VISIBLE// find which radio button is selected
            when (checkedId) {
                R.id.rbSubtraction -> {
                    isSubClick = true
                    isDivClick = false
                    isMultiClick = false
                    isAddClick = false
                    Log.e(javaClass.simpleName, "rbSubtraction: ")
                }

                R.id.rbAddition -> {
                    isAddClick = true
                    isSubClick = false
                    isMultiClick = false
                    isDivClick = false
                    Log.e(javaClass.simpleName, "rbAddition:")
                }

                R.id.rbMultiplication -> {
                    isMultiClick = true
                    isSubClick = false
                    isAddClick = false
                    isDivClick = false
                    Log.e(javaClass.simpleName, "rbMultiplication: ")
                }

                R.id.rbDivision -> {
                    isDivClick = true
                    isSubClick =false
                    isAddClick = false
                    isMultiClick = false
                    Log.e(javaClass.simpleName, "rbDivision: ")
                }
            }

        })

        val random = list().shuffled()
        adapter = MatchOperationAdapter(random as ArrayList<IssuesModel>, onClick = {
            binding.number.text = it.number.toString()
            if (binding.sumNumber.text.isEmpty()) {
                binding.sumNumber.text = it.number.toString()
            } else {
                if(!isDivClick){
                    valueSumNumber = Integer.valueOf(binding.sumNumber.text.toString())
                    valueNumber = Integer.valueOf(binding.number.text.toString())
                    if (isSubClick) {
                        operationValue = valueSumNumber - valueNumber
                        Log.e(javaClass.simpleName, "isSubClick: $operationValue")
                    } else if (isAddClick) {
                        operationValue = valueSumNumber + valueNumber
                        Log.e(javaClass.simpleName, "isAddClick: $operationValue")
                    } else if (isMultiClick) {
                        operationValue = valueSumNumber * valueNumber
                        Log.e(javaClass.simpleName, "isMultiClick: $operationValue")
                    } else if (isDivClick) {
                        isDivClick = true
                        Log.e(javaClass.simpleName, "isDivClick: $isDivClick")
                    }
                }
                if (isDivClick){
                    calculate(binding.number,binding.sumNumber)
                }
                else{
                    binding.sumNumber.text = operationValue.toString()
                }
                Log.e(
                    javaClass.simpleName,
                    "valueNumber: $valueSumNumber valueSumNumber: $valueNumber operationValue$operationValue operationValueF$operationValueF"
                )
            }

        })
        binding.recycle.adapter = adapter
    }

     private fun calculate(editTextNumber: AppCompatTextView, editTextSumNumber: AppCompatTextView) {
        val value1: Double = editTextSumNumber.text.toString().toDouble()
        val value2: Double = editTextNumber.text.toString().toDouble()
        val calculatedValue = value1 / value2
        editTextSumNumber.text = calculatedValue.toString()
        Log.e(javaClass.simpleName, "value1  $value1  value2 $value2 calculate $calculatedValue")
    }
}