package com.example.itservice.admin.login

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
import com.example.itservice.databinding.FragmentAdminLoginBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdminLoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminLoginFragment : Fragment(), TextWatcher {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentAdminLoginBinding
    private lateinit var viewModel: AdminLoginViewModel
    private lateinit var tvSignUp: TextView
    private lateinit var btnSignIN: Button
    private var etadminEmail: AppCompatEditText? = null
    private var etadminPassword: AppCompatEditText? = null
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
        binding = FragmentAdminLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as LoginActivity).selectTabAt(3)
        tvSignUp = binding.tvSignUpAdminLogin
        btnSignIN = binding.btnSignInAdmin
        etadminEmail = binding.etEmailAdminLogin
        etadminPassword = binding.etPasswordAdminLogin
        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(AdminLoginViewModel::class.java)
        btnSignIN.setOnClickListener {
            ContextExtentions.hideKeyboard(it, requireContext())
            adminEmail = setErrorMessage(etadminEmail, "Enter Valid email")
            adminPassword = setErrorMessage(etadminPassword, "Valid Password must be more than 7 characters")

            if (adminEmail != "" && adminPassword != ""
            ) {
                Log.d(TAG, "onViewCreated: READY for login")
            }
        }
        tvSignUp.setOnClickListener {
            try {
                (requireActivity() as LoginActivity).navController.navigate(R.id.action_adminLoginFragment_to_adminRegistrationFragment)
            }catch (e: Exception){
                Log.e(TAG, "onViewCreated: ${e.localizedMessage}" )
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
         * @return A new instance of fragment AdminLoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminLoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}