package projetkotlin.a5a.com.flappybird.feat.play

import io.reactivex.Observable
import io.reactivex.schedulers.Timed
import projetkotlin.a5a.com.flappybird.model.Bird
import projetkotlin.a5a.com.flappybird.model.Pipe
import projetkotlin.a5a.com.flappybird.model.PipeDrawable
import projetkotlin.a5a.com.flappybird.mvp.BasePresenter
import java.util.Random
import java.util.concurrent.TimeUnit

class PlayPresenter(val view: PlayContract) : BasePresenter {

    private var currentPipesPair = mutableMapOf<Long, Pair<Pipe, Pipe>>()
    private var bird = Bird()
    private var score: Int = 0

    fun initPipesObservables(): Observable<Timed<Long>> {
        return Observable.interval(2, TimeUnit.SECONDS)
                .timeInterval()
    }

    fun setPipe(type: String): Pipe {
        return Pipe(type, PipeDrawable.valueOf(type))
    }

    fun onRedrawBirdRequested(isDefaultAnimatorSet: Boolean) {
        view.redrawBirdLayout(isDefaultAnimatorSet)
    }

    fun addPipePair(pipePair: Pair<Pipe, Pipe>) {

        val sharedId = Random().nextLong() * 100000
        pipePair.toList().forEach { it.pipeSharedId = sharedId }
        currentPipesPair[sharedId] = pipePair

        pipePair.toList().forEach { view.drawPipe(it) }
    }

    fun updatePairLeftPosition(pipeImageViewTag: Long, position: Int) {

        val pipesPairToList = currentPipesPair
                .getValue(pipeImageViewTag)

        pipesPairToList.first.currentXPosition = position
        pipesPairToList.second.currentXPosition = position

        if (pipesPairToList.first.currentXPosition < bird.xPosition) {
            score++
            currentPipesPair.remove(pipeImageViewTag)
        }
    }

    fun pipePairStillVisible(pipePairImageViewTag: Long): Boolean {
        return pipePairImageViewTag in currentPipesPair.keys
    }

    fun updateBirdPosition(currentX: Float, currentY: Float) {
        bird.xPosition = currentX
        bird.yPosition = currentY
    }

    fun incrementScore() {
        view.setScore(score)
    }
}