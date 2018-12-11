package projetkotlin.a5a.com.flappybird.feat.play

import android.annotation.SuppressLint
import android.util.DisplayMetrics
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import projetkotlin.a5a.com.flappybird.model.Pipe
import projetkotlin.a5a.com.flappybird.mvp.BasePresenter
import java.util.concurrent.TimeUnit


class PlayPresenter(val view: PlayContract) : BasePresenter {


    @SuppressLint("CheckResult")
    fun initPipes() {

        /*
        Observable.interval(2, TimeUnit.SECONDS)
                .timeInterval()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({

                    Log.v("@test", "Emitted $it")
                    val topPipe = Pipe()
                    val bottomPipe = Pipe()

                    view.displayPipePair(listOf(topPipe, bottomPipe))

                }, {
                    Log.v("@test", "Error")
                })

        */
    }

    override fun viewCreated() {
        super.viewCreated()
    }

}