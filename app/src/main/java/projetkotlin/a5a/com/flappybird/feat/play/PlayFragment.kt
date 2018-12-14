package projetkotlin.a5a.com.flappybird.feat.play

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_play.bird
import kotlinx.android.synthetic.main.fragment_play.final_score
import kotlinx.android.synthetic.main.fragment_play.fragment_play
import kotlinx.android.synthetic.main.fragment_play.fragment_play_score
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val playerName = PlayFragmentArgs.fromBundle(arguments).playerName
        presenter.setCurrentUserName(playerName)
    }

    private val birdXTranslation: ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(bird, View.TRANSLATION_X,
                bird.x, bird.x + AppConstants.BIRD_X_TRANSLATION_GAP).apply {
            addUpdateListener {
                translateXValue = it.animatedValue as Float
                presenter.updateBirdPosition(bird.x, bird.y, bird.left)
            }
        }
    }

    private val birdYTranslation: ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(bird, View.TRANSLATION_Y,
                bird.y, bird.y + AppConstants.BIRD_Y_TRANSLATION_GAP).apply {
            addUpdateListener {
                translateYValue = it.animatedValue as Float
                presenter.updateBirdPosition(bird.x, bird.y, bird.left)
            }
        }
    }

    private val currentBirdYTranslation: ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(bird, View.TRANSLATION_Y,
                bird.y, bird.y - AppConstants.BIRD_CURRENT_Y_TRANSLATION_GAP)
                .apply {
                    addUpdateListener {
                        presenter.updateBirdPosition(bird.x, bird.y, bird.left)
                    }
                }
    }

    private val currentAnimatorSet: AnimatorSet by lazy {
        AnimatorSet().apply {
            duration = AppConstants.CURRENT_ANIMATOR_SET_DURATION
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {}
                override fun onAnimationEnd(animator: Animator?) {
                    presenter.onRedrawBirdRequested(false)
                    defaultAnimatorSet.start()
                }

                override fun onAnimationCancel(p0: Animator?) {}
                override fun onAnimationStart(p0: Animator?) {}
            })
        }
    }
    private val defaultAnimatorSet: AnimatorSet by lazy {
        AnimatorSet().apply {
            duration = AppConstants.DEFAULT_ANIMATOR_SET_DURATION
            playTogether(birdYTranslation, birdXTranslation)
            start()
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {}
                override fun onAnimationEnd(p0: Animator?) {}
                override fun onAnimationCancel(animator: Animator?) {
                    presenter.onRedrawBirdRequested(true)
                    currentAnimatorSet.start()
                }

                override fun onAnimationStart(p0: Animator?) {}
            })
        }
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val screenDisplayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(screenDisplayMetrics)
        screenWidth = screenDisplayMetrics.widthPixels
        screenHeight = screenDisplayMetrics.heightPixels

        defaultAnimatorSet.playTogether(birdYTranslation, birdXTranslation)
        defaultAnimatorSet.start()
        currentAnimatorSet.playTogether(currentBirdYTranslation, birdXTranslation)

        fragment_play.setOnTouchListener { _, motionEvent ->
            defaultAnimatorSet.cancel()
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

    override fun stopAnimations() {
        birdXTranslation.end()
        if (currentAnimatorSet.isRunning) {
            currentAnimatorSet.end()
            birdYTranslation.end()
        }
        if (defaultAnimatorSet.isRunning) {
            defaultAnimatorSet.end()
            currentBirdYTranslation.end()
        }
    }

    override fun onStop() {
        presenter.stop()
        super.onStop()
    }

    override fun freeDisposable() {
        pipesDisposable.dispose()
    }

    override fun redrawBirdLayout(isDefaultAnimatorSet: Boolean) {
        val currentLayoutParams = bird.layoutParams as ConstraintLayout.LayoutParams
        var marginStart = currentLayoutParams.marginStart.toFloat()
        val marginEnd = currentLayoutParams.marginEnd.toFloat()
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
        stopAnimations()
        visibility_group.visibility = View.VISIBLE
        fragment_play_score.visibility = View.GONE
        final_score.text = getString(R.string.final_score,
                fragment_play_score.text.takeIf {
                    it.toString().toInt() != R.string.default_score
                } ?: R.string.default_score)
    }


    private fun animatePipe(pipeImageView: ImageView) {
        ObjectAnimator.ofFloat(pipeImageView, getString(R.string.animation_tanslation_x),
                pipeImageView.left - screenWidth.toFloat()).apply {
            duration = AppConstants.PIPE_ANIMATION_DURATION
            start()
            addUpdateListener {

                bird?.let {

                    val birdRect = Rect()
                    val pipeRect = Rect()

                    it.getHitRect(birdRect)
                    pipeImageView.getHitRect(pipeRect)

                    when {
                        Rect.intersects(birdRect, pipeRect) -> {
                            presenter.onGameOver()
                        }
                        presenter.pipePairStillVisible(pipeImageView.tag as Long) -> {
                            presenter.updatePipesPosition(pipeImageView.tag as Long,
                                    pipeImageView.x.toInt(), pipeImageView.right)
                        }
                        else -> {
                            presenter.updateScore()
                        }
                    }
                }
            }
        }
    }
}
