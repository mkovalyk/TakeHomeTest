package com.application.places.places

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.application.places.databinding.FragmentPlaceListBinding
import com.application.places.util.obtainViewModel
import kotlinx.android.synthetic.main.fragment_place_list.*

/**
 * Fragment that displays list of places
 **/
class PlaceListDialogFragment : Fragment() {
    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var dataBinding: FragmentPlaceListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dataBinding = FragmentPlaceListBinding.inflate(inflater, container, false).apply {
            viewModel = (activity as AppCompatActivity).obtainViewModel(PlacesViewModel::class.java)
        }
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        list.layoutManager = LinearLayoutManager(context)
        placeAdapter = PlaceAdapter(dataBinding.viewModel!!)
        list.adapter = placeAdapter
    }

    companion object {
        fun newInstance(): PlaceListDialogFragment = PlaceListDialogFragment()
    }
}
