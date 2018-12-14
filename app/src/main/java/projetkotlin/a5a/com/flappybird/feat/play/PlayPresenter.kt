package projetkotlin.a5a.com.flappybird.feat.play

import android.annotation.SuppressLint
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Timed
import projetkotlin.a5a.com.flappybird.model.Bird
import projetkotlin.a5a.com.flappybird.model.Pipe
import projetkotlin.a5a.com.flappybird.model.PipeDrawable
import projetkotlin.a5a.com.flappybird.mvp.BasePresenter
import projetkotlin.a5a.com.flappybird.persistence.room.FlappyDatabase
import projetkotlin.a5a.com.flappybird.persistence.room.User
import projetkotlin.a5a.com.flappybird.utils.AppConstants
import java.time.Instant
import java.util.Random
import java.util.concurrent.TimeUnit

class PlayPresenter(val view: PlayContract) : BasePresenter {

    private var currentPipesPair = mutableMapOf<Long, Pair<Pipe, Pipe>>()
    private var bird = Bird()
    private var score: Int = 0
    private var collisionDetected = false

    private lateinit var currentUserName : String

    fun setCurrentUserName(name : String) {
        currentUserName = name
    }

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

    fun updatePipesPosition(pipeImageViewTag: Long, xPosition: Int, rigthPosition : Int) {

        val pipesPairToList = currentPipesPair
                .getValue(pipeImageViewTag)

        pipesPairToList.first.currentXPosition = xPosition
        pipesPairToList.second.currentXPosition = xPosition
        pipesPairToList.first.rightPosition = rigthPosition
        pipesPairToList.second.rightPosition = rigthPosition

        if (pipesPairToList.first.currentXPosition <
                (bird.leftPosition + AppConstants.BIRD_DRAWABLE_WIDTH) && (!collisionDetected)) {
            score++
            currentPipesPair.remove(pipeImageViewTag)
        }
    }

    override fun stop() {
        updateChart()
        view.freeDisposable()
        view.stopAnimations()
        super.stop()
    }

    fun pipePairStillVisible(pipePairImageViewTag: Long): Boolean {
        return pipePairImageViewTag in currentPipesPair.keys
    }

    fun updateBirdPosition(currentX: Float, currentY: Float, currentLeft : Int) {
        bird.xPosition = currentX
        bird.yPosition = currentY
        bird.leftPosition = currentLeft
    }

    fun updateScore() {
        view.setScore(score)
    }

    fun onGameOver() {
        collisionDetected = true
        updateChart()
        view.freeDisposable()
        view.stopGame()
    }

    @SuppressLint("CheckResult")
    fun updateChart() {
        val database = Room.databaseBuilder( ((view as PlayFragment).activity as AppCompatActivity).applicationContext,
                FlappyDatabase::class.java, AppConstants.FLAPPY_DATABASE_NAME).build()

        Observable.just(database)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val dao = it.userDao()
                    val users = dao.getAllUSers()
                    users.asSequence().filter { it.playerScore < score }
                            .sortedBy { it.playerScore }
                            .firstOrNull()?.let {
                                dao.deleteUser(it)
                                val user = User(users.last().uid + 1, currentUserName, score, Instant.now().toEpochMilli())
                                dao.saveUser(user)
                            }
                }, {
                    Log.v("@DB_ERR", "${it.printStackTrace()}")
                })
    }
}