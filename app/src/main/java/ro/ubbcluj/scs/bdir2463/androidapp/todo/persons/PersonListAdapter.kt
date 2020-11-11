package ro.ubbcluj.scs.bdir2463.androidapp.todo.persons

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_person.view.*
import ro.ubbcluj.scs.bdir2463.androidapp.R
import ro.ubbcluj.scs.bdir2463.androidapp.core.TAG
import ro.ubbcluj.scs.bdir2463.androidapp.todo.data.Person
import ro.ubbcluj.scs.bdir2463.androidapp.todo.person.PersonEditFragment

class PersonListAdapter(
    private val fragment: Fragment
) : RecyclerView.Adapter<PersonListAdapter.ViewHolder>() {

    var persons = emptyList<Person>()
        set(value) {
            field = value
            notifyDataSetChanged();
        }

    private var onPersonClick: View.OnClickListener;

    init {
        onPersonClick = View.OnClickListener { view ->
            val person = view.tag as Person
            fragment.findNavController().navigate(R.id.PersonEditFragment, Bundle().apply {
                putString(PersonEditFragment.PERSON_ID, person._id)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_person, parent, false)
        Log.v(TAG, "onCreateViewHolder")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder $position")
        val person = persons[position]
        holder.itemView.tag = person
        holder.textView.text = person.nume + " " + person.prenume
        holder.itemView.setOnClickListener(onPersonClick)
    }

    override fun getItemCount() = persons.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.nume
    }
}
