package projetkotlin.a5a.com.flappybird.feat.play

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import androidx.navigation.Navigation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_play.bird
import kotlinx.android.synthetic.main.fragment_play.final_score
import kotlinx.android.synthetic.main.fragment_play.fragment_play
import kotlinx.android.synthetic.main.fragment_play.fragment_play_score
import kotlinx.android.synthetic.main.fragment_play.game_over
import kotlinx.android.synthetic.main.fragment_play.visibility_group
import projetkotlin.a5a.com.flappybird.R
import projetkotlin.a5a.com.flappybird.model.Pipe
import projetkotlin.a5a.com.flappybird.model.PipeDrawable
import projetkotlin.a5a.com.flappybird.mvp.AbstractMVPFragment
import projetkotlin.a5a.com.flappybird.utils.AppConstants
import kotlin.properties.Delegates


class PlayFragment : AbstractMVPFragment(), PlayContract {

    override val presenter = PlayPresenter(this)
    override val defaultLayout: Int = R.layout.fragment_play
    private lateinit var pipesDisposable: Disposable

    private var screenWidth: Int by Delegates.notNull()
    private var screenHeight: Int by Delegates.notNull()

    private var translateXValue: Float by Delegates.notNull()
    private var translateYValue: Float by Delegates.notNull()

    private val birdXTranslation: ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(bird, View.TRANSLATION_X,
                bird.x, bird.x + AppConstants.BIRD_X_TRANSLATION_GAP).apply {
            addUpdateListener {
                translateXValue = it.animatedValue as Float
                presenter.updateBirdPosition(bird.x, bird.y)
            }
        }
    }

    private val birdYTranslation: ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(bird, View.TRANSLATION_Y,
                bird.y, bird.y + AppConstants.BIRD_Y_TRANSLATION_GAP).apply {
            addUpdateListener {
                translateYValue = it.animatedValue as Float
                presenter.updateBirdPosition(bird.x, bird.y)
            }
        }
    }

    private val currentBirdYTranslation: ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(bird, View.TRANSLATION_Y,
                bird.y, bird.y - AppConstants.BIRD_CURRENT_Y_TRANSLATION_GAP)
                .apply {
                    addUpdateListener {
                        presenter.updateBirdPosition(bird.x, bird.y)
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
                presenter.onRedrawBirdRequested(true)
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

        defaultAnimSet.playTogether(birdYTranslation, birdXTranslation)
        defaultAnimSet.start()

        animSet.apply {
            duration = AppConstants.CURRENT_ANIMATOR_SET_DURATION
            playTogether(currentBirdYTranslation, birdXTranslation)
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {}
                override fun onAnimationEnd(animator: Animator?) {
                    presenter.onRedrawBirdRequested(false)
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

        pipesDisposable = presenter.initPipesObservables()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    val topPipe = presenter.setPipe(getString(R.string.pipe_type_top))
                    val bottomPipe = presenter.setPipe(getString(R.string.pipe_type_bottom))

                    presenter.addPipePair(Pair(topPipe, bottomPipe))

                }, {
                    Toast.makeText(context, getString(R.string.pipe_observable_error), Toast.LENGTH_SHORT).show()
                })
    }

    override fun redrawBirdLayout(isDefaultAnimatorSet: Boolean) {
        val currentLayoutParams = bird.layoutParams as ConstraintLayout.LayoutParams
        var marginStart = currentLayoutParams.marginStart.toFloat()
        var marginEnd = currentLayoutParams.marginEnd.toFloat()
        var marginTop = currentLayoutParams.topMargin.toFloat()
        var marginBottom = currentLayoutParams.bottomMargin.toFloat()

        when (isDefaultAnimatorSet) {
            true -> {
                marginStart = currentLayoutParams.marginStart + translateXValue
                marginTop = currentLayoutParams.topMargin + translateYValue
                marginBottom = currentLayoutParams.bottomMargin - translateYValue
            }
            false -> {
                marginTop = currentLayoutParams.topMargin - translateYValue
                marginBottom = currentLayoutParams.bottomMargin + translateYValue
            }
        }

        (bird.layoutParams as ConstraintLayout.LayoutParams).setMargins(marginStart.toInt(),
                marginTop.toInt(), marginEnd.toInt(),
                marginBottom.toInt())

    }

    override fun drawPipe(pipe: Pipe) {

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

        imageView.tag = pipe.pipeSharedId
        pipe.currentXPosition = imageView.x.toInt()

        fragment_play.addView(imageView)
        animatePipe(imageView)
    }

    override fun setScore(score: Int) {
        fragment_play_score.text = "$score"
    }

    override fun stopGame() {
        pipesDisposable.dispose()
        birdXTranslation.end()
        if (animSet.isRunning) {
            animSet.end()
            birdYTranslation.end()
        }
        if (defaultAnimSet.isRunning) {
            defaultAnimSet.end()
            currentBirdYTranslation.end()
        }
        visibility_group.visibility = View.VISIBLE
        fragment_play_score.visibility = View.GONE
        final_score.text = getString(R.string.final_score, fragment_play_score.text)
    }


    private fun animatePipe(pipeImageView: ImageView) {
        ObjectAnimator.ofFloat(pipeImageView, getString(R.string.animation_tanslation_x),
                pipeImageView.left - screenWidth.toFloat()).apply {
            duration = AppConstants.PIPE_ANIMATION_DURATION
            start()
            addUpdateListener {

                val birdRect = Rect()
                val pipeRect = Rect()

                bird.getHitRect(birdRect)
                pipeImageView.getHitRect(pipeRect)

                when {

                    Rect.intersects(birdRect, pipeRect) -> {
                        presenter.onGameOver()
                       // cancel()
                    }
                    presenter.pipePairStillVisible(pipeImageView.tag as Long) -> {
                        presenter.updatePairLeftPosition(pipeImageView.tag as Long, pipeImageView.x.toInt())
                    }
                    else -> {
                        presenter.updateScore()
                    }
                }
            }
        }
    }
}
