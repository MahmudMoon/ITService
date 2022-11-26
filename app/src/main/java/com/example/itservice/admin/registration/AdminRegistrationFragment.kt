package com.example.itservice.admin.registration

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.application.TAG
import com.example.itservice.common.LoginActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.utils.ContextExtentions
import com.example.itservice.databinding.FragmentAdminRegistrationBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdminRegistrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminRegistrationFragment : Fragment(), TextWatcher {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentAdminRegistrationBinding
    private lateinit var viewModel: AdminRegistrationViewModel
    private lateinit var tvSignIn: TextView
    private lateinit var btnSignUp: Button
    private var etadminName: AppCompatEditText? = null
    private var etadminEmail: AppCompatEditText? = null
    private var etadminPassword: AppCompatEditText? = null
    private var adminName: String? = null
    private var adminEmail: String? = null
    private var adminPassword: String? = null

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
        binding = FragmentAdminRegistrationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as LoginActivity).selectTabAt(3)
        tvSignIn = binding.tvSignUpAdminRegistation
        btnSignUp = binding.btnSingUpAdminRegistation
        etadminName = binding.etFullnameAdminRegistation
        etadminEmail = binding.etEmailAdminRegistation
        etadminPassword = binding.etPasswordAdminRegistation
        viewModel = ViewModelProvider(
            this,
            ViewModelProviderFactory()
        ).get(AdminRegistrationViewModel::class.java)
        btnSignUp.setOnClickListener {
            ContextExtentions.hideKeyboard(it, requireContext())
            adminName = setErrorMessage(etadminName, "Enter Valid Name")
            adminEmail = setErrorMessage(etadminEmail, "Enter Valid email")
            adminPassword = setErrorMessage(etadminPassword, "Valid Password must be more than 7 characters")

            if (adminName != "" && adminEmail != "" && adminPassword != ""
            ) {
                Log.d(TAG, "onViewCreated: READY for registation")
            }
        }
        tvSignIn.setOnClickListener {
            try {
                (requireActivity() as LoginActivity).navController.navigate(R.id.action_adminRegistrationFragment_to_adminLoginFragment)
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
            etadminName?.text.hashCode() -> cleanErrorMessage(etadminName)
            etadminEmail?.text.hashCode() -> cleanErrorMessage(etadminEmail)
            etadminPassword?.text.hashCode() -> cleanErrorMessage(etadminPassword)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AdminRegistrationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminRegistrationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}