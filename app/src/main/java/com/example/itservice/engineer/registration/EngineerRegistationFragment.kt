package com.example.itservice.engineer.registration

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.admin.admin_dashboard.AdminDashBoardActivity
import com.example.itservice.application.TAG
import com.example.itservice.base.BaseFragment
import com.example.itservice.common.LoginActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.utils.ContextExtentions
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.databinding.FragmentEngineerRegistationBinding
import com.example.itservice.engineer.dashboard.EngineerDashBoardActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EngineerRegistationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EngineerRegistationFragment : BaseFragment(), TextWatcher {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentEngineerRegistationBinding
    private lateinit var viewModel: EngineerRegistrationViewModel
    private lateinit var tvSingIn: TextView
    private lateinit var btnSignUp: Button
    private var etengineerName: AppCompatEditText? = null
    private var etengineerEmail: AppCompatEditText? = null
    private var etengineerPassword: AppCompatEditText? = null
    private var etengineerCompanyName: AppCompatEditText? = null
    private var etengineerEmployeeID: AppCompatEditText? = null
    private var etengineerNID: AppCompatEditText? = null
    private var etengineerContactNumber: AppCompatEditText? = null
    private var engineerName: String? = null
    private var engineerEmail: String? = null
    private var engineerPassword: String? = null
    private var engineerCompanyName: String? = null
    private var engineerEmployeeID: String? = null
    private var engineerContactNumber: String? = null
    private var engineerNID: String? = null
    private var progressBar: ProgressBar? = null


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
        binding = FragmentEngineerRegistationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProviderFactory()
        ).get(EngineerRegistrationViewModel::class.java)
        (requireActivity() as LoginActivity).selectTabAt(1)
        tvSingIn = binding.tvSignUpEngineerRegistation
        btnSignUp = binding.btnSingUpengineerRegistation
        etengineerName = binding.etFullnameEngineerRegistation
        etengineerEmail = binding.etEmailEngineerRegistation
        etengineerPassword = binding.etPasswordEngineerRegistation
        etengineerCompanyName = binding.etCompanyNameEngineerRegistation
        etengineerEmployeeID = binding.etEmployeeIdEngineerRegistation
        etengineerContactNumber = binding.etContactNumberEngineerRegistation
        etengineerNID = binding.etNidEngineerRegistation
        progressBar = binding.pbEngineerRegistration

        btnSignUp.setOnClickListener {
            ContextExtentions.hideKeyboard(it, requireContext())
            engineerName = setErrorMessage(etengineerName, "Enter Valid Name")
            engineerEmail = setErrorMessage(etengineerEmail, "Enter Valid email")
            engineerPassword = setErrorMessage(etengineerPassword, "Valid Password must be more than 7 characters")
            engineerCompanyName = setErrorMessage(etengineerCompanyName, "Enter valid company Name")
            engineerEmployeeID = setErrorMessage(etengineerEmployeeID, "Enter valid employee id")
            engineerContactNumber = setErrorMessage(etengineerContactNumber, "Enter valid contact number")
            engineerNID = setErrorMessage(etengineerNID, "Enter valid NID")

            if (engineerName != "" && engineerEmail != ""
                && engineerPassword != "" && engineerCompanyName != "" && engineerEmployeeID != ""
                && engineerContactNumber != "" && engineerNID != ""
            ) {
                Log.d(TAG, "onViewCreated: READY for registation")
                progressBar?.visibility = View.VISIBLE
                viewModel.registerUserWithEmailPassword(engineerName!!, engineerEmail!!,
                    engineerPassword!! , engineerContactNumber!! , engineerCompanyName!!, engineerEmployeeID!!, engineerNID!! )

            }
        }

        viewModel.engineerAuthResult.observe(viewLifecycleOwner){
            if(it.isSuccess){
                val uid = it.resultData as String
                DbInstance.setUserUid(requireContext(), uid)
                progressBar?.visibility = View.GONE
                Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show()
                requireActivity().startActivity(Intent(requireContext(), EngineerDashBoardActivity::class.java))
                moveWithAnimationToAnotherActivity()
            }else{
                Toast.makeText(requireContext(), "Registration failed", Toast.LENGTH_SHORT).show()
                progressBar?.visibility = View.GONE
            }
        }
        
        tvSingIn.setOnClickListener {
            try {
                (requireActivity() as LoginActivity).navController.navigate(R.id.action_engineerRegistationFragment_to_engineerLoginFragment)
            } catch (e: Exception) {
                Log.e(TAG, "onViewCreated: ${e.localizedMessage}")
            }
        }
    }

    fun setErrorMessage(view: AppCompatEditText?, message: String): String? {
        try {
            val data = view?.text.toString()
            if (data == "" ) {
                view?.error = message
                return data
            }else if(message == "Valid Password must be more than 7 characters" && data.length<=7){
                view?.error = message
                return ""
            }
            return data
        } catch (exception: Exception) {
            view?.error = message
        }
        return null
    }

    fun cleanErrorMessage(view: AppCompatEditText?) {
        try {
            view?.error = null
        } catch (exception: Exception) {
            view?.error = null
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(p0: Editable?) {
        when (p0.hashCode()) {
            etengineerName?.text.hashCode() -> cleanErrorMessage(etengineerName)
            etengineerEmail?.text.hashCode() -> cleanErrorMessage(etengineerEmail)
            etengineerPassword?.text.hashCode() -> cleanErrorMessage(etengineerPassword)
            etengineerCompanyName?.text.hashCode() -> cleanErrorMessage(etengineerCompanyName)
            etengineerEmployeeID?.text.hashCode() -> cleanErrorMessage(etengineerEmployeeID)
            etengineerContactNumber?.text.hashCode() -> cleanErrorMessage(etengineerContactNumber)
            etengineerNID?.text.hashCode() -> cleanErrorMessage(etengineerNID)
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EngineerRegistationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EngineerRegistationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}