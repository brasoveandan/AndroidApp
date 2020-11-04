package ro.ubbcluj.scs.bdir2463.androidapp.todo.data

import android.util.Log
import ro.ubbcluj.scs.bdir2463.androidapp.core.TAG
import ro.ubbcluj.scs.bdir2463.androidapp.todo.data.remote.PersonApi

object PersonRepository {
    private var cachedPersons: MutableList<Person>? = null;

    suspend fun loadAll(): List<Person> {
        Log.i(TAG, "loadAll")
        if (cachedPersons != null) {
            return cachedPersons as List<Person>;
        }
        cachedPersons = mutableListOf()
        val persons = PersonApi.service.find()
        cachedPersons?.addAll(persons)
        return cachedPersons as List<Person>
    }

    suspend fun load(personId: String): Person {
        Log.i(TAG, "load")
        val person = cachedPersons?.find { it.id == personId }
        if (person != null) {
            return person
        }
        return PersonApi.service.read(personId)
    }

    suspend fun save(person: Person): Person {
        Log.i(TAG, "save")
        val createdPerson = PersonApi.service.create(person)
        cachedPersons?.add(createdPerson)
        return createdPerson
    }

    suspend fun update(person: Person): Person {
        Log.i(TAG, "update")
        val updatedPerson = PersonApi.service.update(person.id, person)
        val index = cachedPersons?.indexOfFirst { it.id == person.id }
        if (index != null) {
            cachedPersons?.set(index, updatedPerson)
        }
        return updatedPerson
    }
}