package com.example.itservice.admin.login

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
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.R
import com.example.itservice.admin.admin_dashboard.AdminDashBoardActivity
import com.example.itservice.application.TAG
import com.example.itservice.base.BaseFragment
import com.example.itservice.common.LoginActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.ContextExtentions
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.databinding.FragmentAdminLoginBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AdminLoginFragment : BaseFragment(), TextWatcher {
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
        binding = FragmentAdminLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as LoginActivity).selectTabAt(3)
        (requireActivity() as LoginActivity).setTitle("Admin Login")
        tvSignUp = binding.tvSignUpAdminLogin
        btnSignIN = binding.btnSignInAdmin
        etadminEmail = binding.etEmailAdminLogin
        etadminPassword = binding.etPasswordAdminLogin
        progressBar = binding.pbAdminLogin

        viewModel = ViewModelProvider(this, ViewModelProviderFactory()).get(AdminLoginViewModel::class.java)
        btnSignIN.setOnClickListener {
            ContextExtentions.hideKeyboard(it, requireContext())
            adminEmail = setErrorMessage(etadminEmail, "Enter Valid email")
            adminPassword = setErrorMessage(etadminPassword, "Valid Password must be more than 7 characters")

            if (adminEmail != "" && adminPassword != ""
            ) {
                DbInstance.setAdminLastEmail(requireContext() ,adminEmail)
                DbInstance.setAdminLastPassword(requireContext(), adminPassword)
                progressBar?.visibility = View.VISIBLE
                Log.d(TAG, "onViewCreated: READY for login")
                viewModel.signInUserWithEmailPassword(adminEmail!!, adminPassword!!)
            }
        }
        viewModel.adminAuthResult.observe(viewLifecycleOwner){
            if(it.isSuccess){
                val uid = it.resultData as String
                DbInstance.setUserUid(requireContext(), uid)
                viewModel.isAdminExists(uid)
            }else{
                progressBar?.visibility = View.GONE
                Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isAdminExist.observe(viewLifecycleOwner){
            progressBar?.visibility = View.GONE
            if(it){
                Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
                requireActivity().startActivity(Intent(requireContext(), AdminDashBoardActivity::class.java))
                moveWithAnimationToAnotherActivity()
            }else{
                Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT).show()
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

    override fun onStart() {
        super.onStart()
        try {
            val email = arguments?.getString(Constants.email)
            val password = arguments?.getString(Constants.password)
            etadminEmail?.setText(email)
            etadminPassword?.setText( password)
        }catch (e: Exception){
            Log.e(TAG, "onStart: "+e.localizedMessage)
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