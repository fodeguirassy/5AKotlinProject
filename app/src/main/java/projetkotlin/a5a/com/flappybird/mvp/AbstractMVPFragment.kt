package projetkotlin.a5a.com.flappybird.mvp

import android.os.Bundle
import android.view.View
import org.koin.android.ext.android.inject

open class AbstractMVPFragment : AbstractFragment() {

    override val defaultLayout: Int = 0

    open val presenter : BasePresenter by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.viewCreated()
    }

    override fun onResume() {
        super.onResume()
        presenter.resume()
    }

    override fun onPause() {
        super.onPause()
        presenter.pause()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }


}