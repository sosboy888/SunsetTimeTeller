package com.sunsettime.sosboy888.sunsettimeteller

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
    protected fun getSunset(view: View){
        var city=cityText.text.toString()
        val url="https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+city+"%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"
        MyAsyncTask().execute(url)
    }
    inner class MyAsyncTask:AsyncTask<String,String,String>() {

        override fun doInBackground(vararg p0: String?): String {
            try {
                val url = URL(p0[0])
                val httpConnect = url.openConnection() as HttpURLConnection
                httpConnect.connectTimeout = 7000
                var inString = convertURLToString(httpConnect.inputStream)
                publishProgress(inString)
            } catch (ex: Exception) {
            }
            return " "
        }



        override fun onPostExecute(result: String?) {

        }

        override fun onProgressUpdate(vararg values: String?) {
            try{
                var json=JSONObject(values[0])
                val query=json.getJSONObject("query")
                var results=query.getJSONObject("results")
                var channel=query.getJSONObject("channel")
                val astronomy=query.getJSONObject("astronomy")
                var sunrise=astronomy.getString("sunrise")
                sunSetTime.text = "Sunrise time is :"+sunrise


            }catch(ex:Exception){}
        }

    }
    fun convertURLToString(inputstream:InputStream):String{
        val bufferReader=BufferedReader(InputStreamReader(inputstream))
        var line:String
        var allString=""
        try{
            do{
               line=bufferReader.readLine()
                if(line!=null){
                    allString+=line
                }
                //no access to UI

            }while(line!=null)
            inputstream.close()
        }catch (ex:Exception){

        }


        return allString

    }
}

