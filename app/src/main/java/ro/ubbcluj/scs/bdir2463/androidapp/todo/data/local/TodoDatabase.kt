package ro.ubbcluj.scs.bdir2463.androidapp.todo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
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
//                INSTANCE?.let { database ->
//                    scope.launch(Dispatchers.IO) {
//                        populateDatabase(database.personDao())
//                    }
            }
            }
        }

        suspend fun populateDatabase(personDao: PersonDao) {
            personDao.deleteAll()
            val person1 = Person("1", "Nume1", "Prenume1", "Telefon1", "Ocupatie1")
            val person2 = Person("2", "Nume2", "Prenume2", "Telefon2", "Ocupatie2")
            val person3 = Person("3", "Nume3", "Prenume3", "Telefon3", "Ocupatie3")
            val person4 = Person("4", "Nume4", "Prenume4", "Telefon4", "Ocupatie4")
            val person5 = Person("5", "Nume5", "Prenume5", "Telefon5", "Ocupatie5")
            val person6 = Person("6", "Nume6", "Prenume6", "Telefon6", "Ocupatie6")
            val person7 = Person("7", "Nume7", "Prenume7", "Telefon7", "Ocupatie7")
            personDao.insert(person1)
            personDao.insert(person2)
            personDao.insert(person3)
            personDao.insert(person4)
            personDao.insert(person5)
            personDao.insert(person6)
            personDao.insert(person7)
        }
    }