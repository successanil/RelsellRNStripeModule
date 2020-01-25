/*
 * Copyright (c) 2019. Relsell Global
 */

/*
 * Copyright (c) 2018. Relsell Global
 */

package com.stripedemo

import android.os.AsyncTask
import android.os.Handler
import android.os.Message
import android.util.Log
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class PaymentTask(urlStr: String, internal var postData: String, private val callingApp: Handler) : AsyncTask<Void, Void, String>() {

    private var urlStr = ""
    private var responseString: StringBuffer? = null
    private var TAG : String? = PaymentTask::class.simpleName;


    init {
        this.urlStr = urlStr
    }

    override fun doInBackground(vararg voids: Void): String? {


        // HTTP

        try {


            val url = URL(urlStr)

            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true


            val os = connection.outputStream
            val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
            writer.write(postData)
            writer.flush()
            writer.close()
            os.close()

            val mConnectionCode = connection.responseCode

//            Log.v(TAG,"payment task invoked 1");


            if (mConnectionCode == HttpURLConnection.HTTP_OK) {

//                Log.v(TAG,"payment task invoked 2");

                val inputStream = connection.inputStream
                val rd = BufferedReader(InputStreamReader(inputStream))



                responseString = StringBuffer()
                var line: String? = null;
                while ({ line = rd.readLine(); line }() != null) {
                    responseString!!.append(line)
                }
                Log.v(TAG,responseString.toString());

                val m = Message()
                if (responseString != null) {
                    m.obj = responseString
                    m.what = Constants.PAYMENT_SUCCESS_CODE // success case need to be improved more
                } else {
                    m.what = 100
                    m.obj = "NO PAYMENT"
                }


                callingApp.sendMessage(m)


            }
        } catch (e: Exception) {

            Log.v(TAG,"payment task invoked "+e)

            val m = Message()
            m.what = 100
            callingApp.sendMessage(m)

        }

        return null
    }

}
