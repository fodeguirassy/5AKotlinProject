package projetkotlin.a5a.com.flappybird.persistence.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(@PrimaryKey(autoGenerate = true) var uid: Int = 0,
        @ColumnInfo(name = "name") var playerName: String,
        @ColumnInfo(name = "score") var playerScore: Int,
        @ColumnInfo(name = "date") var scoreDate: Long)