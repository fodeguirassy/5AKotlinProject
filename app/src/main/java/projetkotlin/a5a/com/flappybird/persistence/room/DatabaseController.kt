package projetkotlin.a5a.com.flappybird.persistence.room

import android.content.Context
import androidx.room.Room
import projetkotlin.a5a.com.flappybird.utils.AppConstants


object DatabaseController {

    fun initDatabase(appContext: Context) : FlappyDatabase =
            Room.databaseBuilder(appContext,
                    FlappyDatabase::class.java, AppConstants.FLAPPY_DATABASE_NAME).build()
}