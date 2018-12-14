package projetkotlin.a5a.com.flappybird

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.player_name_dialog.player_name
import kotlinx.android.synthetic.main.player_name_dialog.view.player_name

class MainActivity : AppCompatActivity() {

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        /*
        var appTitle = findViewById<TextView>(R.id.appTitle)
        var buttonPlay = findViewById<ImageButton>(R.id.buttonPlay)

        setFont(appTitle,"GrinchedRegular.otf")

        buttonPlay.setOnClickListener{
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
        */

        Navigation.findNavController(findViewById(R.id.navHostFragment)).navigate(R.id.fragment_start)
    }


    private fun setFont(textView: TextView, fontName: String?) {
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