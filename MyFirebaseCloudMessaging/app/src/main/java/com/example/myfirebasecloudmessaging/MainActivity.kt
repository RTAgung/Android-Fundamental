package com.example.myfirebasecloudmessaging

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    companion object{
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_subscribe.setOnClickListener(this)
        btn_token.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_subscribe -> {
                FirebaseMessaging.getInstance().subscribeToTopic("news")
                val msg = getString(R.string.msg_subscribed)
                Log.d(TAG, msg)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
            R.id.btn_token -> {
                FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
                    val deviceToken = it.token
                    val msg = getString(R.string.msg_token_fmt, deviceToken)
                    Log.d(TAG, msg)
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}