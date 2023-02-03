package com.example.itservice.enterprise_user.registation

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.itservice.R
import com.example.itservice.application.TAG
import com.example.itservice.base.BaseFragment
import com.example.itservice.common.LoginActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.ContextExtentions
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.databinding.FragmentEnterpriseUserRegistationBinding
import com.example.itservice.user.user_dash_board.UserdashboardActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EnterpriseenterpriseUserRegistationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EnterpriseenterpriseUserRegistationFragment : BaseFragment(), TextWatcher {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentEnterpriseUserRegistationBinding
    private lateinit var viewModel: EnterpriseUserRegistrationViewModel
    private lateinit var tvSignIn: TextView
    private lateinit var btnSignUp: Button
    private var etenterpriseUserEmail: AppCompatEditText? = null
    private var etenterpriseUserPassword: AppCompatEditText? = null
    private var etenterpriseUserCompanyName: AppCompatEditText? = null
    private var etenterpriseUserCompanyAddress: AppCompatEditText? = null
    private var etenterpriseUserTIN: AppCompatEditText? = null
    private var etenterpriseUserContactNumber: AppCompatEditText? = null
    private var enterpriseUserEmail: String? = null
    private var enterpriseUserPassword: String? = null
    private var enterpriseUserCompanyName: String? = null
    private var enterpriseUserCompanyAddress: String? = null
    private var enterpriseUserTIN: String? = null
    private var enterpriseUserContactNumber: String? = null
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
        binding = FragmentEnterpriseUserRegistationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as LoginActivity).selectTabAt(2)
        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(
            EnterpriseUserRegistrationViewModel::class.java
        )
        tvSignIn = binding.tvSignUpEnterpriseUserRegistation
        btnSignUp = binding.btnSingUpenterpriseUserRegistation
        etenterpriseUserEmail = binding.etEmailEnterpriseUserRegistation
        etenterpriseUserPassword = binding.etPasswordEnterpriseUserRegistation
        etenterpriseUserCompanyName = binding.etCompanyNameEnterpriseUserRegistation
        etenterpriseUserCompanyAddress = binding.etCompanyAddressEnterpriseUserRegistation
        etenterpriseUserTIN = binding.etTinEnterpriseUserRegistation
        etenterpriseUserContactNumber = binding.etContactNumberEnterpriseUserRegistation
        progressBar = binding.pbEnterpriseUserRegistration
        (requireActivity() as LoginActivity).setTitle("Enterprise user registration")

        btnSignUp.setOnClickListener {
            ContextExtentions.hideKeyboard(it, requireContext())
            enterpriseUserEmail = setErrorMessage(etenterpriseUserEmail, "Enter Valid email")
            enterpriseUserPassword = setErrorMessage(etenterpriseUserPassword, "Valid Password must be more than 7 characters")
            enterpriseUserCompanyName = setErrorMessage(etenterpriseUserCompanyName, "Enter valid company Name")
            enterpriseUserCompanyAddress = setErrorMessage(etenterpriseUserCompanyAddress, "Enter valid company Address")
            enterpriseUserTIN = setErrorMessage(etenterpriseUserTIN, "Enter valid TIN")
            enterpriseUserContactNumber = setErrorMessage(etenterpriseUserContactNumber, "Enter valid contact number")

            if (enterpriseUserEmail != ""
                && enterpriseUserPassword != "" && enterpriseUserCompanyName != "" && enterpriseUserCompanyAddress != null && enterpriseUserTIN != ""
                && enterpriseUserContactNumber != ""
            ) {
                progressBar?.visibility = View.VISIBLE
                viewModel.registerUserWithEmailPassword(
                    enterpriseUserEmail!!,
                    enterpriseUserPassword!!,
                    enterpriseUserTIN!!,
                    enterpriseUserCompanyName!!,
                    enterpriseUserCompanyAddress!!,
                    enterpriseUserContactNumber!!)
            }
        }

        viewModel.enterprise_userAuthResult.observe(viewLifecycleOwner){
            if(it.isSuccess){
                progressBar?.visibility = View.GONE
                val uid = it.resultData as String
                DbInstance.setUserUid(requireContext(), uid)
                Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show()
                requireActivity().startActivity(Intent(requireContext(), LoginActivity::class.java)
                    .putExtra(Constants.email, enterpriseUserEmail)
                    .putExtra(Constants.password, enterpriseUserPassword)
                    .putExtra(Constants.tabSelection, 2))
                moveWithAnimationToAnotherActivity()
            }else{
                progressBar?.visibility = View.GONE
                Toast.makeText(requireContext(), "Registration failed", Toast.LENGTH_SHORT).show()
            }
        }

        tvSignIn.setOnClickListener {
            try {
                (requireActivity() as LoginActivity).navController.navigate(R.id.action_enterpriseUserRegistationFragment_to_enterpriseUserFragment)
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
            etenterpriseUserEmail?.text.hashCode() -> cleanErrorMessage(etenterpriseUserEmail)
            etenterpriseUserPassword?.text.hashCode() -> cleanErrorMessage(etenterpriseUserPassword)
            etenterpriseUserCompanyName?.text.hashCode() -> cleanErrorMessage(etenterpriseUserCompanyName)
            etenterpriseUserCompanyAddress?.text.hashCode() -> cleanErrorMessage(etenterpriseUserCompanyAddress)
            etenterpriseUserTIN?.text.hashCode() -> cleanErrorMessage(etenterpriseUserTIN)
            etenterpriseUserContactNumber?.text.hashCode() -> cleanErrorMessage(etenterpriseUserContactNumber)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EnterpriseenterpriseUserRegistationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EnterpriseenterpriseUserRegistationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}