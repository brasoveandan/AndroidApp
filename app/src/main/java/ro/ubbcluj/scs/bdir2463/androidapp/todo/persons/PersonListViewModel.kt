package ro.ubbcluj.scs.bdir2463.androidapp.todo.persons

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

class PersonListViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val persons: LiveData<List<Person>>
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    val personRepository: PersonRepository

    init {
        val personDao = TodoDatabase.getDatabase(application, viewModelScope).personDao()
        personRepository = PersonRepository(personDao)
        persons = personRepository.persons
    }

    fun refresh() {
        viewModelScope.launch {
            Log.v(TAG, "refresh...");
            mutableLoading.value = true
            mutableException.value = null
            when (val result = personRepository.refresh()) {
                is Result.Success -> {
                    Log.d(TAG, "refresh succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "refresh failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableLoading.value = false
        }
    }
}