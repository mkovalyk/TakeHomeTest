package com.application.places.place

import androidx.lifecycle.Observer
import android.content.Context
import android.content.DialogInterface
import android.location.Location
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.application.data.Place
import com.application.places.databinding.FragmentAddPlaceDialogBinding
import com.application.places.util.distanceToLocalized
import com.application.places.util.obtainViewModel

/**
 *
 * A bottom sheet dialog fragment that either adds new place or show information about the existing one.
 */
class AddPlaceDialogFragment : BottomSheetDialogFragment() {
    private val place by lazy { arguments?.getParcelable<Place>(ARGUMENT_PLACE)!! }
    private val inEditMode by lazy { arguments?.getBoolean(ARGUMENT_EDIT) ?: false }
    private val location by lazy { arguments?.getParcelable<Location>(ARGUMENT_LOCATION) }
    private var addPlaceListener: AddPlaceListener? = null
    private lateinit var dataBinding: FragmentAddPlaceDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val model = (activity as AppCompatActivity).obtainViewModel(AddPlaceViewModel::class.java)
        dataBinding = FragmentAddPlaceDialogBinding.inflate(inflater, container, false).apply {
            viewModel = model
            distanceTo = place.distanceToLocalized(location, root.context)
        }
        model.initWith(this.place, this.inEditMode, this.location)
        model.cancelClicked.observe(this, Observer {
            addPlaceListener?.onCancel()
            dismiss()
        })
        model.saveClicked.observe(this, Observer {
            addPlaceListener?.onSaveClicked(place)
            dismiss()
        })
        model.detailsClicked.observe(this, Observer {
            addPlaceListener?.onDetailsClicked(place)
            dismiss()
        })

        return dataBinding.root
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        addPlaceListener = if (targetFragment != null) {
            targetFragment as AddPlaceListener
        } else {
            activity as AddPlaceListener
        }
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        addPlaceListener?.onCancel()
    }

    override fun onDetach() {
        addPlaceListener = null
        super.onDetach()
    }

    interface AddPlaceListener {
        fun onSaveClicked(place: Place)

        fun onDetailsClicked(place: Place)

        fun onCancel()
    }

    companion object {
        const val TAG = "AddPlaceDialogFragment"
        private const val ARGUMENT_PLACE = "Place"
        private const val ARGUMENT_EDIT = "Edit"
        private const val ARGUMENT_LOCATION = "Location"

        fun newInstance(place: Place, edit: Boolean, location: Location?) = AddPlaceDialogFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARGUMENT_PLACE, place)
                putBoolean(ARGUMENT_EDIT, edit)
                putParcelable(ARGUMENT_LOCATION, location)
            }
        }
    }
}
