package ro.ubbcluj.scs.bdir2463.androidapp.todo.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import ro.ubbcluj.scs.bdir2463.androidapp.todo.data.Person

@Dao
interface PersonDao {
    @Query("SELECT * from persons ORDER BY nume ASC")
    fun getAll(): LiveData<List<Person>>

    @Query("SELECT * FROM persons WHERE _id=:id ")
    fun getById(id: String): LiveData<Person>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(person: Person)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(person: Person)

    @Query("DELETE FROM persons")
    suspend fun deleteAll()
}