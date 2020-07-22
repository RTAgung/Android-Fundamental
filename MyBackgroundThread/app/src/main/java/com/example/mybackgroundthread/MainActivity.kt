package com.example.mybackgroundthread

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.lang.Exception
import java.lang.ref.WeakReference


class MainActivity : AppCompatActivity() {

    companion object {
        private const val INPUT_STRING = "Halo Ini Demo AsyncTask!!"
        private const val LOG_ASYNC = "DemoAsync"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_status.setText(R.string.status_pre)
        tv_desc.text = INPUT_STRING

        GlobalScope.launch(Dispatchers.Default) {
            // background thread
            val input = INPUT_STRING

            var output: String? = null

            Log.d(LOG_ASYNC, "status : doInBackground")
            try {
                output = "$input Selamat Belajar!!"
                delay(2000)

                //pindah ke Main thread untuk update UI
                withContext(Dispatchers.Main) {
                    tv_status.setText(R.string.status_post)
                    tv_desc.text = output
                }
            } catch (e: Exception) {
                Log.d(LOG_ASYNC, e.message)
            }
        }
    }
}


//class MainActivity : AppCompatActivity(), MyAsyncCallback {
//
//    companion object{
//        private const val INPUT_STRING = "Halo ini Demo AsyncTask!!"
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val demoAsync = DemoAsync(this)
//        demoAsync.execute(INPUT_STRING)
//    }
//
//    override fun onPreExecute() {
//        tv_status.setText(R.string.status_pre)
//        tv_desc.text = INPUT_STRING
//    }
//
//    override fun onPostExecute(result: String) {
//        tv_status.setText(R.string.status_post)
//        tv_desc.text = result
//    }
//
//    private class DemoAsync(myListener: MyAsyncCallback) : AsyncTask<String, Void, String>() {
//        companion object {
//            private const val LOG_ASYNC = "DemoAsync"
//        }
//
//        private val myListener: WeakReference<MyAsyncCallback> = WeakReference(myListener)
//
//        override fun onPreExecute() {
//            super.onPreExecute()
//            Log.d(LOG_ASYNC, "status : onPreExecute")
//
//            val myListener = myListener.get()
//            myListener?.onPreExecute()
//        }
//
//        override fun doInBackground(vararg params: String?): String {
//            Log.d(LOG_ASYNC, "status : doInBackground")
//
//            var output: String? = null
//
//            try {
//                val input = params[0]
//                output = "$input Selamat Belajar"
//                Thread.sleep(2000)
//            } catch (e: Exception) {
//                Log.d(LOG_ASYNC, e.message)
//            }
//            return output.toString()
//        }
//
//        override fun onPostExecute(result: String?) {
//            super.onPostExecute(result)
//            Log.d(LOG_ASYNC, "status : onPostExecute")
//
//            val myListener = myListener.get()
//            myListener?.onPostExecute(result.toString())
//        }
//    }
//}
//
//internal interface MyAsyncCallback {
//    fun onPreExecute()
//    fun onPostExecute(result: String)
//}
