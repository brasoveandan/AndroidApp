package ro.ubbcluj.scs.bdir2463.androidapp.todo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ro.ubbcluj.scs.bdir2463.androidapp.todo.data.Person

@Database(entities = [Person::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun personDao(): PersonDao

    companion object {
        @Volatile
        private var INSTANCE: TodoDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TodoDatabase {
            val inst = INSTANCE
            if (inst != null) {
                return inst
            }
            val instance =
                Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todo_db"
                )
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
            INSTANCE = instance
            return instance
        }

        private class WordDatabaseCallback(private val scope: CoroutineScope) :
            RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.personDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(personDao: PersonDao) {
            personDao.deleteAll()
            val person = Person("1", "Nume", "Prenume", "Telefon", "Ocupatie")
            personDao.insert(person)
        }
    }

}