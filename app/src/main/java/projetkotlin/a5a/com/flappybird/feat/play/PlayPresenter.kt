package projetkotlin.a5a.com.flappybird.feat.play

import io.reactivex.Observable
import io.reactivex.schedulers.Timed
import projetkotlin.a5a.com.flappybird.model.Pipe
import projetkotlin.a5a.com.flappybird.model.PipeDrawable
import projetkotlin.a5a.com.flappybird.mvp.BasePresenter
import java.util.concurrent.TimeUnit


class PlayPresenter(val view: PlayContract) : BasePresenter {


    fun initPipesObservables() : Observable<Timed<Long>> {
        return Observable.interval(2, TimeUnit.SECONDS)
                .timeInterval()
    }

    fun setPipe(pipeId: Long, type : String) : Pipe =
            Pipe(type, PipeDrawable.valueOf(type), pipeId)

}