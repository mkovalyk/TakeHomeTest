package com.application.places.detail

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.application.data.Place
import com.application.places.R
import com.application.places.util.obtainViewModel
import kotlinx.android.synthetic.main.activity_details.*

/**
 * Activity which displays information about place and allows to edit it.
 */
class DetailsActivity : AppCompatActivity() {
    private val detailsViewModel: DetailsViewModel by lazy { obtainViewModel(DetailsViewModel::class.java) }
    private val place: Place by lazy { intent.getParcelableExtra<Place>(EXTRA_PLACE) }

    private val editModeChangeCallback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            val inEditMode = (sender as ObservableBoolean).get()
            val drawable = if (inEditMode) R.drawable.ic_arrow_back_white_24dp else R.drawable.ic_close_white_24dp
            supportActionBar?.setHomeAsUpIndicator(drawable)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        detailsViewModel.inEditMode.removeOnPropertyChangedCallback(editModeChangeCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val inEditMode = intent.getBooleanExtra(EXTRA_EDIT, false)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailsViewModel.error.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
        detailsViewModel.inEditMode.addOnPropertyChangedCallback(editModeChangeCallback)
        detailsViewModel.exit.observe(this, Observer {
            finish()
        })
        detailsViewModel.message.observe(this, Observer {
            Toast.makeText(this, it!!, Toast.LENGTH_LONG).show()
        })
        if (savedInstanceState == null) {
            detailsViewModel.initWith(place, inEditMode)
        }
    }

    companion object {
        private const val EXTRA_PLACE = "EXTRA_PLACE"
        private const val EXTRA_EDIT = "EXTRA_EDIT"

        fun create(context: Context, place: Place, edit: Boolean): Intent {
            return Intent(context, DetailsActivity::class.java).run {
                putExtra(EXTRA_PLACE, place)
                putExtra(EXTRA_EDIT, edit)
            }
        }
    }
}
