package projetkotlin.a5a.com.flappybird.feat.start

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_start.buttonPlay
import kotlinx.android.synthetic.main.player_name_dialog.player_name
import kotlinx.android.synthetic.main.player_name_dialog.view.player_name
import org.koin.android.ext.android.inject
import projetkotlin.a5a.com.flappybird.R
import projetkotlin.a5a.com.flappybird.mvp.AbstractMVPFragment

class StartFragment : AbstractMVPFragment()  {

    override val presenter: StartPresenter by inject()
    override val defaultLayout: Int = R.layout.fragment_start

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonPlay.setOnClickListener {

            val toastView = layoutInflater.inflate(R.layout.player_name_dialog, null, false)

            AlertDialog.Builder(context!!)
                    .setTitle(getString(R.string.player_pseudo_dialog_title))
                    .setView(toastView)
                    .setPositiveButton(getString(R.string.player_pseudo_dialog_submit_btn))
                    { _, _ ->

                        val pseudo = toastView.player_name.text.toString()

                        if (pseudo.isNotBlank() && pseudo.isNotEmpty()) {
                            val action = StartFragmentDirections.action_start_to_play(pseudo)
                            Navigation.findNavController( activity as AppCompatActivity, R.id.navHostFragment).navigate(action)
                        } else {
                            Toast.makeText(context, getString(R.string.player_toast_pseudo_mandatory), Toast.LENGTH_SHORT).show()
                        }
                    }
                    .create()
                    .show()
        }
    }
}