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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apicalldemo.BaseApplication
import com.example.apicalldemo.ChatConnectionActivity
import com.example.apicalldemo.R
import com.example.apicalldemo.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

@AndroidEntryPoint
class ChatFragment : Fragment() {
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
        setHasOptionsMenu(true)
        val app = requireActivity().application as BaseApplication
        socket = app.socket
        socket?.on(Socket.EVENT_CONNECT, onConnect)
        socket?.on(Socket.EVENT_ERROR, onConnectError)
        socket?.on("new message", onNewMessage)
        socket?.connect()
        startSignIn()
    }

    private fun startSignIn() {

        mUsername = null
        val intent = Intent(requireActivity(), ChatConnectionActivity::class.java)
        startActivityForResult(intent, REQUEST_LOGIN)
    }
    override fun onDestroy() {
        super.onDestroy()
        socket?.disconnect()
        socket?.off(Socket.EVENT_CONNECT, onConnect)
        socket?.off(Socket.EVENT_ERROR, onConnectError)
        socket?.off("new message", onNewMessage)

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


        binding.messages.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter
        }

        binding.edtMessage.setOnEditorActionListener(TextView.OnEditorActionListener { v, id, event ->
            if (id == R.id.send || id == EditorInfo.IME_NULL) {
                attemptSend()
                return@OnEditorActionListener true
            }
            false
        })
        binding.edtMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

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

        val sendButton = view.findViewById<View>(R.id.send_button) as ImageButton
        sendButton.setOnClickListener { attemptSend() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mAdapter = ChatAdapter(requireContext(), mMessages)

    }

    private fun attemptSend() {

        if (null == mUsername) return
        if (!socket!!.connected()) return
        mTyping = false
        val message = binding.edtMessage.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(message)) {
            binding.edtMessage.requestFocus()
            return
        }
        binding.edtMessage.setText("")
        addMessage(mUsername!!, message)

        // perform the sending message attempt.
        socket!!.emit("new message", message)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (Activity.RESULT_OK != resultCode) {
            requireActivity().finish()
            return
        }
        mUsername = data!!.getStringExtra("username")
    }

    private fun addMessage(userName: String, message: String) {
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
            Log.i(javaClass.simpleName, "diconnected")
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_leave) {
            //leave()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    companion object {
        private const val REQUEST_LOGIN = 0
        private const val TYPING_TIMER_LENGTH = 600
    }
}