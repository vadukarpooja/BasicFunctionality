package com.example.apicalldemo.chat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apicalldemo.BaseApplication
import com.example.apicalldemo.ChatConnectActivity
import com.example.apicalldemo.R
import com.example.apicalldemo.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject


@AndroidEntryPoint
class ChatFragment : Fragment(){
    private var socket: Socket? = null
    private var mUsername: String? = null
    lateinit var binding: FragmentChatBinding
    private val mTypingHandler = Handler()
    private var mTyping = false
    private var isConnected = true
    private lateinit var mAdapter: RecyclerView.Adapter<*>
    private val mMessages: MutableList<DataModelMessage> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = requireActivity().application as BaseApplication
        socket = app.socket
        socket!!.on(Socket.EVENT_CONNECT, onConnect)
        socket!!.on(Socket.EVENT_DISCONNECT, onDisconnect)
        socket!!.on(Socket.EVENT_ERROR, onConnectError)
        socket!!.on("new message", onNewMessage)
        socket!!.on("user joined", onUserJoined)
        socket!!.on("user left", onUserLeft)
        socket!!.on("typing", onTyping)
        socket!!.connect()
        startSignIn()
    }

    private fun startSignIn() {
        mUsername = null
        val intent1 = Intent(requireActivity(), ChatConnectActivity::class.java)
        launcher.launch(intent1)
    }

    override fun onDestroy() {
        super.onDestroy()
        socket!!.disconnect()
        socket!!.off(Socket.EVENT_CONNECT, onConnect)
        socket!!.off(Socket.EVENT_DISCONNECT, onDisconnect)
        socket!!.off(Socket.EVENT_ERROR, onConnectError)
        socket!!.off("new message", onNewMessage)
        socket!!.off("user joined", onUserJoined)
        socket!!.off("typing", onTyping)
        socket!!.off("user left", onUserLeft)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_item, menu)
                return
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.action_leave -> {
                        leave()
                    }
                }
                return true
            }
        }, viewLifecycleOwner)
        binding.out.setOnClickListener {
            leave()
        }
        binding.messages.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter
        }

        binding.edtMessage.setOnEditorActionListener(TextView.OnEditorActionListener { v, id, event ->
            if (id == R.id.send || id == EditorInfo.IME_NULL) {
                attemptSend()
                Log.e(javaClass.simpleName, "onViewCreated: send ")
                return@OnEditorActionListener true
            }
            false
        })
        binding.edtMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                Log.e(javaClass.simpleName, "onTextChanged: " + mUsername)
                if (null == mUsername) return
                if (!socket!!.connected()) return
                if (!mTyping) {
                    mTyping = true
                    socket!!.emit("typing")
                }
                mTypingHandler.removeCallbacks(onTypingTimeout)
                mTypingHandler.postDelayed(
                    onTypingTimeout,
                    TYPING_TIMER_LENGTH.toLong()
                )
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.sendButton.setOnClickListener {
            attemptSend()
            Log.e(javaClass.simpleName, "onViewCreated: message")
        }

    }

    private var launcher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult(),
        object : ActivityResultCallback<ActivityResult?> {
            override fun onActivityResult(result: ActivityResult?) {
                if (result != null) {
                    if (result.resultCode == Activity.RESULT_OK) {
                        // Here, no request code
                        if (result.data != null) {
                            val data = result.data
                            if (data != null) {
                                mUsername = data.getStringExtra("username")
                                Log.e(javaClass.simpleName, "onActivityResult: $mUsername")
                                val numUsers = data.getIntExtra("numUsers", 1)
                                addLog(resources.getString(R.string.message_welcome))
                                addParticipantsLog(numUsers)
                                Log.e(javaClass.simpleName, "onActivityResult: numUsers $numUsers")
                            }
                        }
                    }
                }
            }
        })
    private fun addLog(message: String) {

        mMessages.add(DataModelMessage.Builder(DataModelMessage.TYPE_LOG).message(message).build())
        mAdapter.notifyItemInserted(mMessages.size - 1)
        scrollToBottom()
    }

    private fun addParticipantsLog(numUsers: Int) {

        addLog(resources.getQuantityString(R.plurals.message_participants, numUsers, numUsers))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mAdapter = ChatAdapter(requireContext(), mMessages)

    }

    private val onUserLeft = Emitter.Listener { args ->

        requireActivity().runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            val username: String
            val numUsers: Int
            try {
                username = data.getString("username")
                numUsers = data.getInt("numUsers")
            } catch (e: JSONException) {
                javaClass.simpleName
                return@Runnable
            }
            Log.e(javaClass.simpleName, ": $numUsers")
            /*  addLog(resources.getString(R.string.message_user_left, username))
              addParticipantsLog(numUsers)*/
            removeTyping(username)
        })
    }

    private fun attemptSend() {
        Log.e(javaClass.simpleName, "attemptSend: mUsername " + mUsername)
        if (null == mUsername) return
        if (!socket!!.connected()) return
        mTyping = false
        val message = binding.edtMessage.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(message)) {
            binding.edtMessage.requestFocus()
            return
        }
        binding.edtMessage.setText("")
        Log.e(javaClass.simpleName, "attemptSend 1: $message")
        Log.e(javaClass.simpleName, "attemptSend: " + mUsername)
        addMessage(mUsername!!, message)
        // perform the sending message attempt.
        socket!!.emit("new message", message)

    }

    private val onUserJoined = Emitter.Listener { args ->

        requireActivity().runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            val username: String
            val numUsers: Int
            try {
                username = data.getString("username")
                numUsers = data.getInt("numUsers")
            } catch (e: JSONException) {
                Log.e(javaClass.simpleName, ": " + e.message)
                return@Runnable
            }
            Log.e(javaClass.simpleName, ": $username$numUsers")
            /* addLog(resources.getString(R.string.message_user_joined, username))
             addParticipantsLog(numUsers)*/
        })
    }


    private fun addMessage(userName: String, message: String) {
        Log.e(javaClass.simpleName, "addMessage: $userName,$message")
        mMessages.add(
            DataModelMessage.Builder(DataModelMessage.TYPE_MESSAGE).username(userName)
                .message(message).build()
        )
        mAdapter.notifyItemInserted(mMessages.size - 1)
        scrollToBottom()
    }

    private fun scrollToBottom() {

        binding.messages.scrollToPosition(mAdapter.itemCount - 1)
    }

    private val onConnect = Emitter.Listener {

        requireActivity().runOnUiThread {
            if (!isConnected) {
                if (null != mUsername) socket!!.emit("add user", mUsername)
                Toast.makeText(
                    requireActivity().applicationContext,
                    R.string.connect, Toast.LENGTH_LONG
                ).show()
                isConnected = true
            }
        }
    }

    private val onDisconnect = Emitter.Listener {

        requireActivity().runOnUiThread {
            Log.e(javaClass.simpleName, "diconnected")
            isConnected = false
            Toast.makeText(
                requireActivity().applicationContext,
                R.string.disconnect, Toast.LENGTH_LONG
            ).show()
        }
    }

    private val onConnectError = Emitter.Listener {

        requireActivity().runOnUiThread {
            Log.e(javaClass.simpleName, "Error connecting")
            Toast.makeText(
                requireActivity().applicationContext,
                R.string.error_connect, Toast.LENGTH_LONG
            ).show()
        }
    }
    private val onNewMessage = Emitter.Listener { args ->

        requireActivity().runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            val username: String
            val message: String
            try {
                username = data.getString("username")
                message = data.getString("message")
            } catch (e: JSONException) {
                Log.e(javaClass.simpleName, ": " + e.message)
                return@Runnable
            }
            removeTyping(username)
            addMessage(username, message)
        })
    }

    private fun removeTyping(username: String) {

        for (i in mMessages.indices.reversed()) {
            val message = mMessages[i]
            if (message.type == DataModelMessage.TYPE_ACTION && message.userName == username) {
                mMessages.removeAt(i)
                mAdapter.notifyItemRemoved(i)
            }
        }
    }

    private val onTypingTimeout = Runnable {

        if (!mTyping) return@Runnable
        mTyping = false
        socket!!.emit("stop typing")
    }

    private val onTyping = Emitter.Listener { args ->

        requireActivity().runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            val username: String
            username = try {
                data.getString("username")
            } catch (e: JSONException) {
                Log.e(javaClass.simpleName, ": " + e.message)
                return@Runnable
            }
            addTyping(username)
        })
    }

    private fun addTyping(username: String) {

        mMessages.add(
            DataModelMessage.Builder(DataModelMessage.TYPE_ACTION).username(username).build()
        )
        mAdapter.notifyItemInserted(mMessages.size - 1)
        scrollToBottom()
    }
    companion object {
        private const val REQUEST_LOGIN = 0
        private const val TYPING_TIMER_LENGTH = 600
    }
    private fun leave() {

        mUsername = null
        socket!!.disconnect()
        socket!!.connect()
        startSignIn()
    }
}