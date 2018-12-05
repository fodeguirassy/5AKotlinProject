package projetkotlin.a5a.com.flappybird

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.LinearInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_game)

        val cloudOne = findViewById<ImageView>(R.id.cloudOneBackground)
        var birdStart = 0.0f
        var birdEnd = 0.3f
        val cloudTwo = findViewById<ImageView>(R.id.cloudTwoBackground)
        val cloudThree = findViewById<ImageView>(R.id.cloudThreeBackground)

        val buttonUp = findViewById<ImageButton>(R.id.buttonUp)
        val buttonDown = findViewById<ImageButton>(R.id.buttonDown)

        val bird = findViewById<ImageView>(R.id.iconBird);


        buttonUp.setOnClickListener{

            if (birdStart > -1.5) {
                val animator = ValueAnimator.ofFloat(birdStart, birdStart - birdEnd)
                birdStart = birdStart - birdEnd
                animator.interpolator = LinearInterpolator()
                animator.duration = 900L
                animator.addUpdateListener { animation ->
                    val progress = animation.animatedValue as Float
                    val height = bird.height
                    val translationY = height * progress
                    bird.setTranslationY(translationY)

                }
                animator.start()
            }
        }

        buttonDown.setOnClickListener{
            if(birdStart <= 1.2) {
                val animator = ValueAnimator.ofFloat(birdStart, birdStart + birdEnd)
                birdStart = birdStart + birdEnd
                animator.interpolator = LinearInterpolator()
                animator.duration = 900L
                animator.addUpdateListener { animation ->
                    val progress = animation.animatedValue as Float
                    val height = bird.height
                    val translationY = height * progress
                    bird.setTranslationY(translationY)

                }
                animator.start()
            }
        }


        val animator = ValueAnimator.ofFloat(-5.0f, 4.0f)
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
