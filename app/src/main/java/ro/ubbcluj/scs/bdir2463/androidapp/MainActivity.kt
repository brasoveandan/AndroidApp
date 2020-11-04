package ro.ubbcluj.scs.bdir2463.androidapp

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import ro.ubbcluj.scs.bdir2463.androidapp.core.TAG

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        Log.i(TAG, "onCreate")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(person: MenuItem): Boolean {
        return when (person.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(person)
        }
    }
}