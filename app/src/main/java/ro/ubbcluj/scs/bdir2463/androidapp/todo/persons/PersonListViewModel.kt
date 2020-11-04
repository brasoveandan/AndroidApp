package ro.ubbcluj.scs.bdir2463.androidapp.todo.persons

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ro.ubbcluj.scs.bdir2463.androidapp.core.TAG
import ro.ubbcluj.scs.bdir2463.androidapp.todo.data.Person
import ro.ubbcluj.scs.bdir2463.androidapp.todo.data.PersonRepository


class PersonListViewModel : ViewModel() {
    private val mutablePersons = MutableLiveData<List<Person>>().apply { value = emptyList() }
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val persons: LiveData<List<Person>> = mutablePersons
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    fun createPerson(position: Int): Unit {
        val list = mutableListOf<Person>()
        list.addAll(mutablePersons.value!!)
        list.add(Person(position.toString(), "Person " + position, "", "", ""))
        mutablePersons.value = list
    }

    fun loadPersons() {
        viewModelScope.launch {
            Log.v(TAG, "loadPersons...");
            mutableLoading.value = true
            mutableException.value = null
            try {
                mutablePersons.value = PersonRepository.loadAll()
                Log.d(TAG, "loadPersons succeeded");
                mutableLoading.value = false
            } catch (e: Exception) {
                Log.w(TAG, "loadPersons failed", e);
                mutableException.value = e
                mutableLoading.value = false
            }
        }
    }
}
