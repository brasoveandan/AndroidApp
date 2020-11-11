package ro.ubbcluj.scs.bdir2463.androidapp.todo.person

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_person_edit.*
import ro.ubbcluj.scs.bdir2463.androidapp.R
import ro.ubbcluj.scs.bdir2463.androidapp.core.TAG
import ro.ubbcluj.scs.bdir2463.androidapp.todo.data.Person

class PersonEditFragment : Fragment() {
    companion object {
        const val PERSON_ID = "PERSON_ID"
    }

    private lateinit var viewModel: PersonEditViewModel
    private var personId: String? = null
    private var person: Person? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
        arguments?.let {
            if (it.containsKey(PERSON_ID)) {
                personId = it.getString(PERSON_ID).toString()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_person_edit, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        setupViewModel()
        fab.setOnClickListener {
            Log.v(TAG, "save person")
            val i = person
            if (i != null) {
                i.nume = person_nume.text.toString()
                i.prenume = person_prenume.text.toString()
                i.telefon = person_telefon.text.toString()
                i.ocupatie = person_ocupatie.text.toString()
                viewModel.saveOrUpdatePerson(i)
            }
        }

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(PersonEditViewModel::class.java)
        viewModel.fetching.observe(viewLifecycleOwner, { fetching ->
            Log.v(TAG, "update fetching")
            progress.visibility = if (fetching) View.VISIBLE else View.GONE
        })
        viewModel.fetchingError.observe(viewLifecycleOwner, { exception ->
            if (exception != null) {
                Log.v(TAG, "update fetching error")
                val message = "Fetching exception ${exception.message}"
                val parentActivity = activity?.parent
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.completed.observe(viewLifecycleOwner, { completed ->
            if (completed) {
                Log.v(TAG, "completed, navigate back")
                findNavController().popBackStack()
            }
        })
        val id = personId
        if (id == null) {
            person = Person("", "", "", "", "")
        } else {
            viewModel.getPersonById(id).observe(viewLifecycleOwner, {
                Log.v(TAG, "update persons")
                if (it != null) {
                    person = it
                    person_nume.setText(it.nume)
                    person_prenume.setText(it.prenume)
                    person_telefon.setText(it.telefon)
                    person_ocupatie.setText(it.ocupatie)
                }
            })
        }
    }
}
