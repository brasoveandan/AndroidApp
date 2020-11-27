package ro.ubbcluj.scs.bdir2463.androidapp.todo.person

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ro.ubbcluj.scs.bdir2463.androidapp.core.Result
import ro.ubbcluj.scs.bdir2463.androidapp.core.TAG
import ro.ubbcluj.scs.bdir2463.androidapp.todo.data.Person
import ro.ubbcluj.scs.bdir2463.androidapp.todo.data.PersonRepository
import ro.ubbcluj.scs.bdir2463.androidapp.todo.data.local.TodoDatabase

class PersonEditViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    val personRepository: PersonRepository

    init {
        val personDao = TodoDatabase.getDatabase(application, viewModelScope).personDao()
        personRepository = PersonRepository(personDao)
    }

    fun getPersonById(personId: String): LiveData<Person> {
        Log.v(TAG, "getPersonById...")
        return personRepository.getById(personId)
    }

    fun saveOrUpdatePerson(person: Person) {
        viewModelScope.launch {
            Log.v(TAG, "saveOrUpdatePerson...");
            mutableFetching.value = true
            mutableException.value = null
            val result: Result<Person>
            if (person._id.isNotEmpty()) {
                result = personRepository.update(person)
            } else {
                result = personRepository.save(person)
            }
            when (result) {
                is Result.Success -> {
                    Log.d(TAG, "saveOrUpdatePerson succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "saveOrUpdatePerson failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }

    fun deletePerson(personId: String) {
        viewModelScope.launch {
            mutableFetching.value = true
            mutableException.value = null
            val result: Result<Boolean> = personRepository.delete(personId)
            when (result) {
                is Result.Success -> {
                    Log.d(TAG, "delete succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "delete failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }
}