package com.application.places.detail

import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.application.places.R
import com.application.places.databinding.FragmentDetailsBinding
import com.application.places.util.obtainViewModel

/**
 * Fragment which display details about a place
 */
class DetailsActivityFragment : Fragment() {
    private lateinit var bindings: FragmentDetailsBinding
    private var editMenuItem: MenuItem? = null
    private var saveMenuItem: MenuItem? = null

    private val editModeChangedCallback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            updateMenuAccordingToMode((sender as ObservableBoolean).get())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bindings = FragmentDetailsBinding.inflate(inflater, container, false).apply {
            detailsViewModel = (activity as AppCompatActivity).obtainViewModel(DetailsViewModel::class.java)
            detailsViewModel!!.inEditMode.addOnPropertyChangedCallback(editModeChangedCallback)
        }
        setHasOptionsMenu(true)
        return bindings.root
    }

    override fun onDestroyView() {
        bindings.detailsViewModel?.inEditMode?.removeOnPropertyChangedCallback(editModeChangedCallback)
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_details, menu)
        saveMenuItem = menu!!.findItem(R.id.save)
        editMenuItem = menu.findItem(R.id.edit)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        val inEditMode = bindings.detailsViewModel?.inEditMode?.get()
        if (inEditMode != null) {
            updateMenuAccordingToMode(inEditMode)
        }
    }

    fun updateMenuAccordingToMode(inEditMode: Boolean) {
        saveMenuItem?.isVisible = inEditMode
        editMenuItem?.isVisible = !inEditMode
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home ->
                bindings.detailsViewModel?.back()
            R.id.edit -> {
                bindings.detailsViewModel?.enterEditMode()
            }
            R.id.save -> {
                bindings.detailsViewModel?.confirm()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
