package projetkotlin.a5a.com.flappybird.feat.play

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_play.bird
import kotlinx.android.synthetic.main.fragment_play.fragment_play
import org.koin.android.ext.android.inject
import projetkotlin.a5a.com.flappybird.R
import projetkotlin.a5a.com.flappybird.model.Pipe
import projetkotlin.a5a.com.flappybird.model.PipeDrawable
import projetkotlin.a5a.com.flappybird.mvp.AbstractFragment
import projetkotlin.a5a.com.flappybird.utils.AppConstants
import java.util.Random
import kotlin.properties.Delegates


class PlayFragment : AbstractFragment(), PlayContract {

    override val presenter: PlayPresenter by inject()
    override val defaultLayout: Int = R.layout.fragment_play

    private var screenWidth: Int by Delegates.notNull()
    private var screenHeight: Int by Delegates.notNull()

    private var translateXValue: Float by Delegates.notNull()
    private var translateYValue: Float by Delegates.notNull()
    private var translateYCurrentValue: Float by Delegates.notNull()

    private val birdXTranslation: ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(bird, View.TRANSLATION_X,
                bird.x, bird.x + 25).apply {

            addUpdateListener {
                translateXValue = it.animatedValue as Float
            }
        }
    }

    private val birdYTranslation: ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(bird, View.TRANSLATION_Y,
                bird.y, bird.y + 500f).apply {

            addUpdateListener {
                translateYValue = it.animatedValue as Float
            }
        }
    }

    private val currentBirdYTranslation: ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(bird, View.TRANSLATION_Y,
                bird.y, bird.y - 300).apply {
            addUpdateListener {
                translateYCurrentValue = it.animatedValue as Float
            }
        }
    }

    val animSet = AnimatorSet()
    val defaultAnimSet = AnimatorSet().apply {
        duration = AppConstants.DEFAULT_ANIMATOR_SET_DURATION
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationEnd(p0: Animator?) {}
            override fun onAnimationCancel(animator: Animator?) {

                val currentLayoutParams = bird.layoutParams as ConstraintLayout.LayoutParams
                val marginStart = currentLayoutParams.marginStart + translateXValue
                val marginTop = currentLayoutParams.topMargin + translateYValue
                val marginBottom = currentLayoutParams.bottomMargin - translateYValue

                (bird.layoutParams as ConstraintLayout.LayoutParams).setMargins(marginStart.toInt(),
                        marginTop.toInt(), currentLayoutParams.marginEnd,
                        marginBottom.toInt())

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

        /*
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

        animSet.apply {
            duration = AppConstants.CURRENT_ANIMATOR_SET_DURATION
            playTogether(currentBirdYTranslation, birdXTranslation)
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {}
                override fun onAnimationEnd(animator: Animator?) {

                    val currentLayoutParams = bird.layoutParams as ConstraintLayout.LayoutParams
                    val marginStart = currentLayoutParams.marginStart + translateXValue
                    val marginTop = currentLayoutParams.topMargin - translateYValue
                    val marginBottom = currentLayoutParams.bottomMargin + translateYValue

                    (bird.layoutParams as ConstraintLayout.LayoutParams).setMargins(currentLayoutParams.marginStart,
                            marginTop.toInt(), currentLayoutParams.marginEnd,
                            marginBottom.toInt())

                    defaultAnimSet.start()
                }

                override fun onAnimationCancel(p0: Animator?) {}
                override fun onAnimationStart(p0: Animator?) {}
            })
        }

        fragment_play.setOnTouchListener { _, motionEvent ->
            defaultAnimSet.cancel()
            (activity as AppCompatActivity).onTouchEvent(motionEvent)
        }
        */

        presenter.initPipesObservables()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val topPipe = presenter.setPipe(it.value(), getString(R.string.pipe_type_top))
                    //presenter.onPipeReady(topPipe)
                    drawPipe(topPipe)
                    val bottomPipe = presenter.setPipe(it.value(), getString(R.string.pipe_type_bottom))
                    //presenter.onPipeReady(bottomPipe)
                    drawPipe(bottomPipe)

                }, {
                    Log.v("@Observ_Err", "${it.printStackTrace()}")
                })
    }

    //TODO not in the contract because CONTEXT is lost when calling Presenter
    private fun drawPipe(pipe: Pipe) {

            val imageView = ImageView(context)

            val currentLayoutParams = ConstraintLayout.LayoutParams(
                    Constraints.LayoutParams.WRAP_CONTENT,
                    Constraints.LayoutParams.WRAP_CONTENT)

            when (pipe.type) {
                getString(R.string.pipe_type_top) -> {
                    currentLayoutParams.topToTop = fragment_play.id
                    currentLayoutParams.endToEnd = fragment_play.id
                    imageView.y = imageView.top - pipe.height
                    imageView.setImageResource(PipeDrawable.TOP.drawable)
                }
                getString(R.string.pipe_type_bottom) -> {
                    currentLayoutParams.bottomToBottom = fragment_play.id
                    currentLayoutParams.endToEnd = fragment_play.id
                    imageView.y = imageView.bottom + pipe.height
                    imageView.setImageResource(PipeDrawable.BOTTOM.drawable)
                }
            }

            imageView.layoutParams = currentLayoutParams
            imageView.x = screenWidth - AppConstants.PIPE_RIGHT_OFFSET

            fragment_play.addView(imageView)
            animatePipe(imageView)
    }

    private fun setPipeImageView(type: String): ImageView {
        val imageView = ImageView(context)

        val currentLayoutParams = ConstraintLayout.LayoutParams(Constraints.LayoutParams.WRAP_CONTENT,
                Constraints.LayoutParams.WRAP_CONTENT)

        when (type) {
            getString(R.string.pipe_type_top) -> {
                currentLayoutParams.topToTop = fragment_play.id
                currentLayoutParams.endToEnd = fragment_play.id
                imageView.y = imageView.top - (Random().nextFloat() * (350 - 0) + 0)
                imageView.setImageResource(R.drawable.pipe_reversed)
            }
            getString(R.string.pipe_type_bottom) -> {
                currentLayoutParams.bottomToBottom = fragment_play.id
                currentLayoutParams.endToEnd = fragment_play.id
                imageView.y = imageView.bottom + (Random().nextFloat() * (350 - 0) + 0)
                imageView.setImageResource(R.drawable.pipe)
            }
        }

        imageView.layoutParams = currentLayoutParams
        imageView.x = screenWidth - AppConstants.PIPE_RIGHT_OFFSET

        return imageView
    }

    private fun animatePipe(pipeImageView: ImageView) {
        ObjectAnimator.ofFloat(pipeImageView, getString(R.string.animation_tanslation_x),
                pipeImageView.left - screenWidth.toFloat()).apply {
            duration = AppConstants.PIPE_ANIMATION_DURATION
            start()
        }
    }
}
