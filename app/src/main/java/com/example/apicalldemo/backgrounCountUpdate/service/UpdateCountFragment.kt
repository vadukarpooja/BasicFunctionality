package com.example.apicalldemo.backgrounCountUpdate.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.apicalldemo.databinding.FragmentUpdateCountBinding
import com.example.apicalldemo.utils.BROADCAST


class UpdateCountFragment : Fragment() , CountInterFace {
   lateinit var binding:FragmentUpdateCountBinding
    lateinit var foregroundService: ForegroundService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateCountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        foregroundService = ForegroundService()
        foregroundService.localBroadcastManager =  LocalBroadcastManager.getInstance(requireContext())
        requireContext().startService(Intent(requireContext(), ForegroundService::class.java))
        val listener = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                val data = intent?.getStringExtra("count")
                binding.count.text = data
                Log.e(javaClass.simpleName, "onViewCreated onReceive: $data")
            }
        }
        val intentFilter = IntentFilter(BROADCAST)
        foregroundService.localBroadcastManager.registerReceiver(listener, intentFilter)
    }

    override fun onUpdateCount(count: Int,localBroadcastManager:LocalBroadcastManager,context: Context){
        val intent = Intent(BROADCAST)
        intent.putExtra("count", count.toString())
        localBroadcastManager.sendBroadcast(intent)
        Log.e(javaClass.simpleName, "onUpdateCount1: $count")
    }
}