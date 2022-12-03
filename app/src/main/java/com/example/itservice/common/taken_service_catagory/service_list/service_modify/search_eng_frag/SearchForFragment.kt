package com.example.itservice.common.taken_service_catagory.service_list.service_modify.search_eng_frag

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itservice.R
import com.example.itservice.application.TAG
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.models.Engineer
import com.example.itservice.common.taken_service_catagory.service_list.service_modify.ServiceModifyActivity
import com.example.itservice.databinding.FragmentSearchForBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchForFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentSearchForBinding
    private lateinit var viewModel: SearchForViewModel
    private lateinit var rvEngineerList: RecyclerView
    private lateinit var engAdapter: EngineerListAdapter
    private val engList = ArrayList<Engineer>()
   // private var activityRef: ServiceModifyActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //activityRef = context as ServiceModifyActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchForBinding.inflate(inflater, container, false )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(SearchForViewModel::class.java)
        viewModel.getAllEngineers()

        engAdapter = EngineerListAdapter(requireActivity(), engList)

        rvEngineerList = binding.rvEngList
        rvEngineerList.layoutManager = LinearLayoutManager(requireContext())
        rvEngineerList.adapter = engAdapter

        viewModel.engData.observe(viewLifecycleOwner){ list ->
            Log.d(TAG, "onViewCreated:Eng list:  ${list.size}")
            engList.clear()
            engList.addAll(list)
            engAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchForFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}