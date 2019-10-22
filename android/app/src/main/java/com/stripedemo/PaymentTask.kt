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
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

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

            val connection = url.openConnection() as HttpsURLConnection
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


            if (mConnectionCode == HttpURLConnection.HTTP_OK) {

                val inputStream = connection.inputStream
                val rd = BufferedReader(InputStreamReader(inputStream))



                responseString = StringBuffer()
                var line: String? = null;
                while ({ line = rd.readLine(); line }() != null) {
                    responseString!!.append(line)
                }

                val m = Message()
                if (responseString != null && responseString!!.toString().equals("true", ignoreCase = true)) {
                    m.what = Constants.PAYMENT_SUCCESS_CODE // success case need to be improved more
                } else {
                    m.what = 100
                }

                callingApp.sendMessage(m)


            }
        } catch (e: Exception) {

            val m = Message()
            m.what = 100
            callingApp.sendMessage(m)

        }

        return null
    }

}
