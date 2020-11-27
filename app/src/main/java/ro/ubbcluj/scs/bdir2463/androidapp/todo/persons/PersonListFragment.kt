package ro.ubbcluj.scs.bdir2463.androidapp.todo.persons

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_person_list.*
import ro.ubbcluj.scs.bdir2463.androidapp.R
import ro.ubbcluj.scs.bdir2463.androidapp.auth.data.AuthRepository
import ro.ubbcluj.scs.bdir2463.androidapp.core.Constants
import ro.ubbcluj.scs.bdir2463.androidapp.core.TAG

class PersonListFragment : Fragment() {
    private lateinit var personListAdapter: PersonListAdapter
    private lateinit var personsModel: PersonListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_person_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        Log.v(TAG, Constants.instance()?.fetchValueString("token").toString())
        if (Constants.instance()?.fetchValueString("token") == null) {
            findNavController().navigate(R.id.fragment_login)
        }
        setupPersonList()
        fab.setOnClickListener {
            Log.v(TAG, "add new person")
            findNavController().navigate(R.id.PersonEditFragment)
        }
        logout.setOnClickListener {
            Log.v(TAG, "LOGOUT")
            AuthRepository.logout()
            findNavController().navigate(R.id.fragment_login)
        }
    }

    private fun setupPersonList() {
        personListAdapter = PersonListAdapter(this)
        person_list.adapter = personListAdapter
        personsModel = ViewModelProvider(this).get(PersonListViewModel::class.java)
        personsModel.persons.observe(viewLifecycleOwner, { persons ->
            Log.v(TAG, "update persons")
            personListAdapter.persons = persons
        })
        personsModel.loading.observe(viewLifecycleOwner, { loading ->
            Log.i(TAG, "update loading")
            progress.visibility = if (loading) View.VISIBLE else View.GONE
        })
        personsModel.loadingError.observe(viewLifecycleOwner, { exception ->
            if (exception != null) {
                Log.i(TAG, "update loading error")
                val message = "Loading exception ${exception.message}"
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        })
        personsModel.refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy")
    }
}