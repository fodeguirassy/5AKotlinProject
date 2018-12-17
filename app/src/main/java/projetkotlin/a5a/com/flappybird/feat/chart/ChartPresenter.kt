package projetkotlin.a5a.com.flappybird.feat.chart

import android.annotation.SuppressLint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import projetkotlin.a5a.com.flappybird.mvp.BasePresenter
import projetkotlin.a5a.com.flappybird.persistence.room.DatabaseController


class ChartPresenter(val view: ChartContract) : BasePresenter {

    @SuppressLint("CheckResult")
    override fun resume() {
        super.resume()
        DatabaseController.getAllFlappyUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            view.setTopTenScores(it)
                        },
                        {
                            view.toastDatabaseError()
                        }
                )
    }

}