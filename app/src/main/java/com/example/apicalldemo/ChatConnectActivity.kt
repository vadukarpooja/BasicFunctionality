package com.example.apicalldemo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import com.example.apicalldemo.databinding.ActivityChatConnectBinding
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject

class ChatConnectActivity : AppCompatActivity() {
    lateinit var binding:ActivityChatConnectBinding
    private var mSocket: Socket? = null
    var userName:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatConnectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val app = application as BaseApplication
        mSocket = app.socket

        binding.edtName.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->

            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })


        binding.btnConnect.setOnClickListener {
            attemptLogin()
        }
        mSocket!!.on("login", onLogin)

    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket!!.off("login",onLogin)
    }

    private val onLogin = Emitter.Listener { args ->

        val data = args[0] as JSONObject
        val numUsers: Int = try {
            data.getInt("numUsers")
        } catch (e: JSONException) {
            return@Listener
        }
        val intent = Intent()
        intent.putExtra("username", userName)
        intent.putExtra("numUsers", numUsers)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
    private fun attemptLogin() {

        // Reset errors.
        binding.edtName.error = null

        // Store values at the time of the login attempt.
        val username = binding.edtName.text.toString().trim { it <= ' ' }

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            binding.edtName.error = "Plz Enter Your Name"
            binding.edtName.requestFocus()
            return
        }
        userName = username

        // perform the user login attempt.
        mSocket!!.emit("add user", username)
    }

}