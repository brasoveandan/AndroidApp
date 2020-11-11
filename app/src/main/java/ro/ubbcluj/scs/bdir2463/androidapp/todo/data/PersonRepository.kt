package ro.ubbcluj.scs.bdir2463.androidapp.todo.data

import androidx.lifecycle.LiveData
import ro.ubbcluj.scs.bdir2463.androidapp.core.Result
import ro.ubbcluj.scs.bdir2463.androidapp.todo.data.local.PersonDao
import ro.ubbcluj.scs.bdir2463.androidapp.todo.data.remote.PersonApi

class PersonRepository(private val personDao: PersonDao) {

    val persons = personDao.getAll()

    suspend fun refresh(): Result<Boolean> {
        try {
            val persons = PersonApi.service.find()
            for (person in persons) {
                personDao.insert(person)
            }
            return Result.Success(true)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    fun getById(personId: String): LiveData<Person> {
        return personDao.getById(personId)
    }

    suspend fun save(person: Person): Result<Person> {
        try {
            val createdPerson = PersonApi.service.create(person)
            personDao.insert(createdPerson)
            return Result.Success(createdPerson)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun update(person: Person): Result<Person> {
        try {
            val updatedPerson = PersonApi.service.update(person._id, person)
            personDao.update(updatedPerson)
            return Result.Success(updatedPerson)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }
}