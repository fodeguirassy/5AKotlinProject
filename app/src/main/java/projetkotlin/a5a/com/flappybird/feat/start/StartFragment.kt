package projetkotlin.a5a.com.flappybird.feat.start

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_start.buttonPlay
import org.koin.android.ext.android.inject
import projetkotlin.a5a.com.flappybird.R
import projetkotlin.a5a.com.flappybird.mvp.AbstractFragment

class StartFragment : AbstractFragment()  {

    override val presenter: StartPresenter by inject()
    override val defaultLayout: Int = R.layout.fragment_start

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonPlay.setOnClickListener {
            Navigation.findNavController( activity as AppCompatActivity, R.id.navHostFragment).navigate(R.id.fragment_play)
        }
    }

}