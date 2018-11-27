package projetkotlin.a5a.com.flappybird

import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        var appTitle = findViewById<TextView>(R.id.appTitle)

        setFont(appTitle,"GrinchedRegular.otf")
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
