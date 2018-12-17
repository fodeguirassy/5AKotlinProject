package projetkotlin.a5a.com.flappybird.feat.chart

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.chart_item.view.player_name
import kotlinx.android.synthetic.main.chart_item.view.score
import kotlinx.android.synthetic.main.chart_item.view.score_date
import kotlinx.android.synthetic.main.chart_item.view.score_index
import projetkotlin.a5a.com.flappybird.R
import projetkotlin.a5a.com.flappybird.persistence.room.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ChartListAdapter(private val appContext: Context, private val topUsers : List<User>): BaseAdapter() {

    @SuppressLint("ViewHolder")
    override fun getView(index: Int, view: View?, viewGroup: ViewGroup?): View {
        val inflatedView = LayoutInflater.from(appContext).inflate(R.layout.chart_item, viewGroup, false)
        val currentRank = index + 1
        val currentUser = topUsers[index]
        inflatedView.score_index.text = "$currentRank"
        inflatedView.player_name.text = currentUser.playerName
        inflatedView.score.text = "${currentUser.playerScore}"
        inflatedView.score_date.text = SimpleDateFormat("d MMM yyyy hh:mm:ss", Locale.FRANCE).format(Date(currentUser.scoreDate)).toString()
        return inflatedView
    }

    override fun getItem(index: Int): Any {
        return topUsers[index]
    }

    override fun getItemId(index: Int): Long {
        return topUsers[index].uid.toLong()
    }

    override fun getCount(): Int {
        return topUsers.size
    }


}