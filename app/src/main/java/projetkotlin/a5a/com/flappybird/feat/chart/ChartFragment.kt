package projetkotlin.a5a.com.flappybird.feat.chart

import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_chart.chart_list
import projetkotlin.a5a.com.flappybird.R
import projetkotlin.a5a.com.flappybird.mvp.AbstractMVPFragment
import projetkotlin.a5a.com.flappybird.persistence.room.User

class ChartFragment : AbstractMVPFragment(), ChartContract {

    override val presenter = ChartPresenter(this)
    override val defaultLayout: Int = R.layout.fragment_chart

    override fun setTopTenScores(bestScores: List<User>) {
        chart_list.adapter = ChartListAdapter(context!!, bestScores.sortedByDescending { it.playerScore })
    }

    override fun toastDatabaseError() {
        Toast.makeText(context, getString(R.string.chart_fragment_db_error),
                Toast.LENGTH_SHORT).show()
    }
}