package projetkotlin.a5a.com.flappybird.persistence.room

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [User::class], version = 1)
abstract class FlappyDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
}