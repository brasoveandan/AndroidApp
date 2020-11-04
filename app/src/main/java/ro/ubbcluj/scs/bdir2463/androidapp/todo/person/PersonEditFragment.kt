package ro.ubbcluj.scs.bdir2463.androidapp.todo.person

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_person_edit.*
import ro.ubbcluj.scs.bdir2463.androidapp.R
import ro.ubbcluj.scs.bdir2463.androidapp.core.TAG

class PersonEditFragment : Fragment() {
    companion object {
        const val PERSON_ID = "PERSON_ID"
    }

    private lateinit var viewModel: PersonEditViewModel
    private var personId: String? = null

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v(TAG, "onViewCreated")
        person_nume.setText(personId)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        setupViewModel()
        fab.setOnClickListener {
            Log.v(TAG, "save person")
            viewModel.saveOrUpdatePerson(
                person_nume.text.toString(),
                person_prenume.text.toString(),
                person_telefon.text.toString(),
                person_ocupatie.text.toString()
            )
        }

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(PersonEditViewModel::class.java)
        viewModel.person.observe(viewLifecycleOwner, { person ->
            Log.v(TAG, "update persons")
            person_nume.setText(person.nume)
            person_prenume.setText(person.prenume)
            person_telefon.setText(person.telefon)
            person_ocupatie.setText(person.ocupatie)
        })
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
        viewModel.completed.observe(viewLifecycleOwner, Observer { completed ->
            if (completed) {
                Log.v(TAG, "completed, navigate back")
                findNavController().navigateUp()
            }
        })
        val id = personId
        if (id != null) {
            viewModel.loadPerson(id)
        }
    }
}
