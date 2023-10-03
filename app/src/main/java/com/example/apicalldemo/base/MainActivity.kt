package com.example.apicalldemo.base

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.apicalldemo.R
import com.example.apicalldemo.bluetoothConnection.BluetoothChat
import com.example.apicalldemo.crusOpration.ListUpdate
import com.example.apicalldemo.databinding.ActivityMainBinding
import com.example.apicalldemo.imagePick.LoginActivity
import com.example.apicalldemo.menualDependancyInjection.Car
import com.example.apicalldemo.menualDependancyInjection.Car2
import com.example.apicalldemo.menualDependancyInjection.Engin
import com.example.apicalldemo.readContectList.ReadContactList
import com.example.apicalldemo.socket.ChatConnectActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity() : AppCompatActivity() {
    private lateinit var car: Car
    private lateinit var car2: Car2
    private lateinit var engin: Engin
    lateinit var binding: ActivityMainBinding
    var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    var navHostFragment: NavHostFragment? = null
    var navigationController: NavController? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navigationController = navHostFragment!!.navController
        car = Car()
        engin = Engin()
        car2 = Car2(engin)


        car.start("car")
        car2.start("car")

        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, binding.rootView, binding.toolbar,R.string.nav_open, R.string.nav_close)

        binding.rootView.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle?.syncState()


        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationController!!.addOnDestinationChangedListener(NavController.OnDestinationChangedListener {
                controller, destination, _ ->

            if (destination.id == R.id.splashFragment) {
                binding.toolbar.visibility = View.GONE
            } else {
                binding.toolbar.visibility = View.VISIBLE
            }
        })
        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.crudOperation -> {
                    val intent = Intent(applicationContext,ListUpdate::class.java)
                    startActivity(intent)
                    binding.rootView.closeDrawer(GravityCompat.START)
                }
                R.id.pagination ->{
                    navigationController?.navigate(R.id.issueListFragment)
                    binding.rootView.closeDrawer(GravityCompat.START)
                }
                R.id.readContactList->{
                    val intent = Intent(applicationContext,ReadContactList::class.java)
                    startActivity(intent)
                    binding.rootView.closeDrawer(GravityCompat.START)
                }
                R.id.synTabList ->{
                    navigationController?.navigate(R.id.nestedListFragment)
                    binding.rootView.closeDrawer(GravityCompat.START)
                }
                R.id.chat ->{
                    navigationController?.navigate(R.id.chatFragment)
                    binding.rootView.closeDrawer(GravityCompat.START)
                }
                R.id.bluetooth ->{
                    val intent = Intent(applicationContext,BluetoothChat::class.java)
                    startActivity(intent)
                    binding.rootView.closeDrawer(GravityCompat.START)
                }
                R.id.imagePick->{
                    val intent = Intent(applicationContext,LoginActivity::class.java)
                    startActivity(intent)
                    binding.rootView.closeDrawer(GravityCompat.START)
                }
                R.id.backGroundCountUpdate ->{
                    navigationController?.navigate(R.id.updateCountFragment)
                    binding.rootView.closeDrawer(GravityCompat.START)
                }
                R.id.mathsOperation ->{
                    navigationController?.navigate(R.id.mathsOperation2)
                    binding.rootView.closeDrawer(GravityCompat.START)
                }
            }
            return@setNavigationItemSelectedListener true

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle?.onOptionsItemSelected(item) == true) {
            return true
        }

        return super.onOptionsItemSelected(item);
    }

}
