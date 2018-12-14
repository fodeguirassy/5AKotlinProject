package projetkotlin.a5a.com.flappybird.persistence.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Query("Select * from user")
    fun getAllUSers() : List<User>

    @Insert
    fun saveUser(vararg  users : User)

    @Delete
    fun deleteUser(user : User)

}