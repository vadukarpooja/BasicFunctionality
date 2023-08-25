package com.example.apicalldemo.chat

class DataModelMessage private constructor() {

    var type = 0
        private set
    var message:String? = null
        private set
    var userName:String? = null
    private set

    class Builder(private val mType: Int) {

        private var mUsername: String? = null
        private var mMessage: String? = null

        fun username(username: String?): Builder {
            mUsername = username
            return this
        }

        fun message(message: String?): Builder {
            mMessage = message
            return this
        }

        fun build(): DataModelMessage {

            val message = DataModelMessage()
            message.type = mType
            message.userName = mUsername
            message.message = mMessage
            return message
        }

    }

    companion object {
        const val TYPE_MESSAGE = 0
        const val TYPE_LOG = 1
        const val TYPE_ACTION = 2
    }
}