package com.example.radiostation

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        private var instance: MainActivity? = null

        public fun getInstance(): MainActivity {
            return instance!!
        }
    }

    fun checkInternetConnection(): Boolean
    {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        instance = this
        var handler = Handler()

        if (checkInternetConnection() == false) {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Failure")
            alert.setMessage("Not Connected To Internet")
            alert.setPositiveButton("OK", null)
            alert.show()
        } else{
            switch1.setOnCheckedChangeListener(handler)
            slider.setOnSeekBarChangeListener(handler)
            play.setOnClickListener(handler)
        }
    }

    class Handler : View.OnClickListener, CompoundButton.OnCheckedChangeListener,
        SeekBar.OnSeekBarChangeListener {
        var name = "Now Playing"
        var txt = "AM";
        var radioName = MainActivity.getInstance().radioName
        var radioImage = MainActivity.getInstance().radioImage

        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            var text = buttonView?.getText()
            println("Is checked " + isChecked)
            println(text)
            if (text == "AM") {
                txt = "FM"
                buttonView?.setText("FM")
            } else {
                txt = "AM"
                buttonView?.setText("AM")
            }
            radioName.setText("Now Playing")
            var id = MainActivity.getInstance().resources.getIdentifier("launch","drawable",MainActivity.getInstance().packageName)
            radioImage.setImageResource(id)
        }

        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            var radioName = MainActivity.getInstance().radioName
            var radioImage = MainActivity.getInstance().radioImage
            var num = progress
            //radioName.setText(text)
//        radioImage.setImageResource(id)

            if (txt == "AM") {
                if (num >= 156 && num <= 159) {
                    name = "WABC"
                } else if (num >= 160 && num <= 163) {
                    name = "KABC"
                } else if (num >= 174 && num <= 177) {
                    name = "WBAP"
                } else if (num >= 193 && num <= 197) {
                    name = "WLS"
                } else if (num >= 210 && num <= 213) {
                    name = "KARNAM"
                } else {
                    name = "Now Playing"
                }
            } else if (txt == "FM") {
                if (num >= 178 && num <= 181) {
                    name = "WXYT"
                } else if (num >= 203 && num <= 206) {
                    name = "KURB"
                } else if (num >= 209 && num <= 212) {
                    name = "WKIM"
                } else if (num >= 220 && num <= 223) {
                    name = "WWTN"
                } else if (num >= 250 && num <= 253) {
                    name = "KDXE"
                } else if (num >= 269 && num <= 272) {
                    name = "KARNFM"
                } else if (num >= 350 && num <= 353) {
                    name = "KLAL"
                } else {
                    name = "Now Playing"

                }
            }
            radioName.setText(name) 
//        println("///////////          " +radioName.toString() + "           /////////////")
            if (name == "Now Playing") {
                var id = MainActivity.getInstance().resources.getIdentifier(
                    "launch",
                    "drawable",
                    MainActivity.getInstance().packageName
                )
                radioImage.setImageResource(id)
            } else {
                var id = MainActivity.getInstance().resources.getIdentifier(
                    name.toLowerCase(),
                    "drawable",
                    MainActivity.getInstance().packageName
                )
                radioImage.setImageResource(id)
            }
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {
            println("Start")
        }

        override fun onStopTrackingTouch(p0: SeekBar?) {
            println("End")
        }


        override fun onClick(p0: View?) {
            //println("switched")
            var text = (p0 as Button).getText().toString()
            println(text)
            println(name)
            if (text == "PLAY" && name != "Now Playing") {
                var wv = MainActivity.getInstance().wv
                wv.loadUrl("http://playerservices.streamtheworld.com/api/livestream-redirect/" + name + txt + ".mp3")
            } else {
                println("do nth")
            }
        }
    }
}