package projetkotlin.a5a.com.flappybird.feat.chart

import projetkotlin.a5a.com.flappybird.persistence.room.User

interface ChartContract {
    fun setTopTenScores(bestScores : List<User>)
    fun toastDatabaseError()
}