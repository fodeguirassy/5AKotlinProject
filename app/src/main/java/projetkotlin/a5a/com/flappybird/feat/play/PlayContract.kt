package projetkotlin.a5a.com.flappybird.feat.play

import projetkotlin.a5a.com.flappybird.model.Pipe
import projetkotlin.a5a.com.flappybird.mvp.BaseView

interface PlayContract : BaseView {
    fun redrawBirdLayout(isDefaultAnimatorSet: Boolean)
    fun drawPipe(pipe: Pipe)
    fun setScore(score: Int)
    fun stopGame()
    fun stopAnimations()
    fun freeDisposable()
    fun toastMessage(message: String)
}