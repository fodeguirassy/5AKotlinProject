package projetkotlin.a5a.com.flappybird.persistence.room

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.room.Room
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import projetkotlin.a5a.com.flappybird.utils.AppConstants


object DatabaseController {

    lateinit var flappyDatabase: FlappyDatabase

    fun initDatabase(appContext: Context) {
        flappyDatabase = Room.databaseBuilder(appContext,
                FlappyDatabase::class.java, AppConstants.FLAPPY_DATABASE_NAME).build()

    }

    @SuppressLint("CheckResult")
    fun getAllFlappyUsers(): Single<List<User>> {
        return flappyDatabase.userDao().getAllUsers()
                .subscribeOn(Schedulers.io())
    }

    //TODO REMOVE
    @SuppressLint("CheckResult")
    fun dumpTab() {

        getAllFlappyUsers()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            if (it.isNotEmpty()) {
                                flappyDatabase.userDao().deleteDatabase(it)
                            } else {
                            }
                        },
                        {
                            //Convenient logging
                            Log.v("SUERS_ERR", "${it.printStackTrace()}")

                        })


    }


}