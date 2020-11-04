package ro.ubbcluj.scs.bdir2463.androidapp.todo.person

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ro.ubbcluj.scs.bdir2463.androidapp.core.TAG
import ro.ubbcluj.scs.bdir2463.androidapp.todo.data.Person
import ro.ubbcluj.scs.bdir2463.androidapp.todo.data.PersonRepository


class PersonEditViewModel : ViewModel() {
    private val mutablePerson =
        MutableLiveData<Person>().apply { value = Person("", "", "", "", "") }
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val person: LiveData<Person> = mutablePerson
    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    fun loadPerson(personId: String) {
        viewModelScope.launch {
            Log.i(TAG, "loadPerson...")
            mutableFetching.value = true
            mutableException.value = null
            try {
                mutablePerson.value = PersonRepository.load(personId)
                Log.i(TAG, "loadPerson succeeded")
                mutableFetching.value = false
            } catch (e: Exception) {
                Log.w(TAG, "loadPerson failed", e)
                mutableException.value = e
                mutableFetching.value = false
            }
        }
    }

    fun saveOrUpdatePerson(nume: String, prenume: String, telefon: String, ocupatie: String) {
        viewModelScope.launch {
            Log.i(TAG, "saveOrUpdatePerson...");
            val person = mutablePerson.value ?: return@launch
            person.nume = nume
            person.prenume = prenume
            person.telefon = telefon
            person.ocupatie = ocupatie
            mutableFetching.value = true
            mutableException.value = null
            try {
                if (person.id.isNotEmpty()) {
                    mutablePerson.value = PersonRepository.update(person)
                } else {
                    mutablePerson.value = PersonRepository.save(person)
                }
                Log.i(TAG, "saveOrUpdatePerson succeeded");
                mutableCompleted.value = true
                mutableFetching.value = false
            } catch (e: Exception) {
                Log.w(TAG, "saveOrUpdatePerson failed", e);
                mutableException.value = e
                mutableFetching.value = false
            }
        }
    }
}
