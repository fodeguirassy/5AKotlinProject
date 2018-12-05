package projetkotlin.a5a.com.flappybird.feat.play

import kotlinx.android.synthetic.main.fragment_play.view.fragment_play
import org.koin.android.ext.android.inject
import projetkotlin.a5a.com.flappybird.R
import projetkotlin.a5a.com.flappybird.mvp.AbstractFragment


class PlayFragment : AbstractFragment() {

    override val presenter: PlayPresenter by inject()
    override val defaultLayout: Int = R.layout.fragment_play
    override val currentFragmentTag: String = "playFragment"

}
