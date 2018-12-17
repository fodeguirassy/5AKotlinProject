package projetkotlin.a5a.com.flappybird.persistence.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Single

@Dao
interface UserDao {

    @Query("Select * from user")
    fun getAllUsers(): Single<List<User>>

    @Insert
    fun saveUser(user: User): Long

    @Delete
    fun deleteUser(user: User)

    @Delete
    fun deleteDatabase(users: List<User>)
}