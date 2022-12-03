package com.example.itservice.common.parts_pack.display_parts.available_parts_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.models.Parts
import com.example.itservice.databinding.FragmentPartsBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class PartsFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentPartsBinding
    private lateinit var viewModel: PartsViewModel
    private lateinit var rvDisplayAvailableParts: RecyclerView
    private lateinit var adapterDisplayAvailbleParts: PartsAvailableAdapter
    private var partsAvailable = ArrayList<Parts>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPartsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelProviderFactory())[PartsViewModel::class.java]
        rvDisplayAvailableParts = binding.rvDisplayPartsAvailable
        rvDisplayAvailableParts.layoutManager = LinearLayoutManager(requireContext())
        adapterDisplayAvailbleParts = PartsAvailableAdapter(requireActivity(), partsAvailable)
        rvDisplayAvailableParts.adapter = adapterDisplayAvailbleParts

        viewModel.getAllParts()
        viewModel.partData.observe(viewLifecycleOwner){list ->
            partsAvailable.clear()
            partsAvailable.addAll(list)
            adapterDisplayAvailbleParts.notifyDataSetChanged()
        }
    }


    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PartsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}