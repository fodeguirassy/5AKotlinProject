package projetkotlin.a5a.com.flappybird

import android.content.Intent
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        var appTitle = findViewById<TextView>(R.id.appTitle)
        var buttonPlay = findViewById<ImageButton>(R.id.buttonPlay)

        setFont(appTitle,"GrinchedRegular.otf")

        buttonPlay.setOnClickListener{
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }

    fun setFont(textView: TextView, fontName: String?) {
        if (fontName != null) {
            try {
                val typeface = Typeface.createFromAsset(assets, "fonts/$fontName")
                textView.typeface = typeface
            } catch (e: Exception) {
                Log.e("FONT", "$fontName not found", e)
            }

        }
    }
}