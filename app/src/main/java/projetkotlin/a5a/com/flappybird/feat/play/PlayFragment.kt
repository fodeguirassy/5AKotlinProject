package projetkotlin.a5a.com.flappybird.feat.play

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_play.bird
import kotlinx.android.synthetic.main.fragment_play.fragment_play
import org.koin.android.ext.android.inject
import projetkotlin.a5a.com.flappybird.R
import projetkotlin.a5a.com.flappybird.mvp.AbstractFragment
import java.util.Random
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates


class PlayFragment : AbstractFragment(), PlayContract {

    override val presenter: PlayPresenter by inject()
    override val defaultLayout: Int = R.layout.fragment_play

    private var screenWidth: Int by Delegates.notNull()
    private var screenHeight: Int by Delegates.notNull()

    private val birdXTranslation: ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(bird, View.TRANSLATION_X,
                bird.x, bird.x + 50).apply {

            /*
            addUpdateListener {
                val currentLayoutParams = bird.layoutParams as ViewGroup.MarginLayoutParams
                val newMargin = it.animatedValue as Float
                currentLayoutParams.bottomMargin -= newMargin.toInt()
                bird.layoutParams = currentLayoutParams
            }
            */

        }
    }

    private val birdYTranslation: ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(bird, View.TRANSLATION_Y,
                bird.y, bird.y + 500f).apply {
            /*
            addUpdateListener {
                val currentLayoutParams = bird.layoutParams as ViewGroup.MarginLayoutParams
                val newMargin = it.animatedValue as Float
                currentLayoutParams.bottomMargin -= newMargin.toInt()
                bird.layoutParams = currentLayoutParams
            }
            */
        }
    }

    val animSet = AnimatorSet()
    val defaultAnimSet = AnimatorSet().apply {
        duration = 10000
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?) {
            }

            override fun onAnimationCancel(animator: Animator?) {
                animSet.start()
            }

            override fun onAnimationStart(p0: Animator?) {}
        })

    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val screenDisplayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(screenDisplayMetrics)
        screenWidth = screenDisplayMetrics.widthPixels
        screenHeight = screenDisplayMetrics.heightPixels

        Log.v("@XXX", "${bird.x}")
        Log.v("@YYY", "${bird.y}")

        defaultAnimSet.playTogether(birdYTranslation, birdXTranslation)
        defaultAnimSet.start()

        Observable.interval(2, TimeUnit.SECONDS)
                .timeInterval()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    val topPipeImageView = setPipeImageView(getString(R.string.pipe_type_top))
                    fragment_play.addView(topPipeImageView)
                    animatePipe(topPipeImageView)

                    val bottomPipeImageView = setPipeImageView(getString(R.string.pipe_type_bottom))
                    fragment_play.addView(bottomPipeImageView)
                    animatePipe(bottomPipeImageView)

                }, {
                    Log.v("@test", "${it.printStackTrace()}")
                })


        //birdXTranslation.start()

        val animY = ObjectAnimator.ofFloat(bird, View.TRANSLATION_Y,
                bird.y, bird.y - 100).apply {
            /*
            addUpdateListener {
                val currentLayoutParams = bird.layoutParams as ViewGroup.MarginLayoutParams
                val newMargin = it.animatedValue as Float
                currentLayoutParams.bottomMargin -= newMargin.toInt()
                bird.layoutParams = currentLayoutParams
            }
            */
        }

        animSet.apply {
            duration = 1000
            playTogether(birdXTranslation, animY)
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {}

                override fun onAnimationEnd(animator: Animator?) {
                    // birdXTranslation.start()
                    defaultAnimSet.start()
                }

                override fun onAnimationCancel(p0: Animator?) {

                }

                override fun onAnimationStart(p0: Animator?) {
                }

            })
        }


        fragment_play.setOnTouchListener { _, motionEvent ->
            defaultAnimSet.cancel()
            //animSet.start()
            (activity as AppCompatActivity).onTouchEvent(motionEvent)
        }

    }

    private fun setPipeImageView(type: String): ImageView {
        val imageView = ImageView(context)

        val currentLayoutParams = ConstraintLayout.LayoutParams(Constraints.LayoutParams.WRAP_CONTENT,
                Constraints.LayoutParams.WRAP_CONTENT)

        when (type) {
            getString(R.string.pipe_type_top) -> {
                currentLayoutParams.topToTop = fragment_play.id
                currentLayoutParams.endToEnd = fragment_play.id
                imageView.y = imageView.top - (Random().nextFloat() * (200 - 0) + 0)
                imageView.setImageResource(R.drawable.pipe_reversed)
            }
            getString(R.string.pipe_type_bottom) -> {
                currentLayoutParams.bottomToBottom = fragment_play.id
                currentLayoutParams.endToEnd = fragment_play.id
                imageView.y = imageView.bottom + (Random().nextFloat() * (200 - 0) + 0)
                imageView.setImageResource(R.drawable.pipe)
            }
        }

        imageView.layoutParams = currentLayoutParams
        imageView.x = screenWidth - 5f

        return imageView
    }

    private fun animatePipe(pipeImageView: ImageView) {
        ObjectAnimator.ofFloat(pipeImageView, getString(R.string.animation_tanslation_x),
                pipeImageView.left - screenWidth.toFloat()).apply {
            duration = 10000
            start()
        }
    }

}
