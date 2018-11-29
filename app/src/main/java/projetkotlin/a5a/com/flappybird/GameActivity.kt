package projetkotlin.a5a.com.flappybird

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewCompat.setTranslationX
import android.opengl.ETC1.getWidth
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import android.widget.ImageView


class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val cloudOne = findViewById<ImageView>(R.id.cloudOneBackground)
        val cloudTwo = findViewById<ImageView>(R.id.cloudTwoBackground)
        val cloudThree = findViewById<ImageView>(R.id.cloudThreeBackground)


        val animator = ValueAnimator.ofFloat(-1.0f, 3.0f)
        animator.repeatCount = ValueAnimator.INFINITE
        animator.interpolator = LinearInterpolator()
        animator.duration = 10000L
        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Float
            val width = cloudOne.getWidth()
            val translationX = width * progress
            cloudOne.setTranslationX(translationX)
            cloudTwo.setTranslationX(translationX + width)
            cloudThree.setTranslationX(translationX + width)
        }
        animator.start()
    }
}
