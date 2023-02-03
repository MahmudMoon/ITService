package com.example.itservice.enterprise_user.enterprise_user_login

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
import com.example.itservice.R
import com.example.itservice.application.TAG
import com.example.itservice.base.BaseFragment
import com.example.itservice.common.LoginActivity
import com.example.itservice.common.factory.ViewModelProviderFactory
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.ContextExtentions
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.databinding.FragmentEnterpriseUserLoginBinding
import com.example.itservice.user.user_dash_board.UserdashboardActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EnterpriseUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EnterpriseUserFragment : BaseFragment(), TextWatcher {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentEnterpriseUserLoginBinding
    private lateinit var viewModel: EnterpriseUserLoginViewModel
    private lateinit var tvSignUp: TextView
    private lateinit var btnSignIN: Button
    private var etenterpriseuserEmail: AppCompatEditText? = null
    private var etenterpriseuserPassword: AppCompatEditText? = null
    private var enterpriseuserEmail: String? = null
    private var enterpriseuserPassword: String? = null
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
        binding = FragmentEnterpriseUserLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as LoginActivity).selectTabAt(2)
        viewModel = ViewModelProvider(
            this,
            ViewModelProviderFactory()
        ).get(EnterpriseUserLoginViewModel::class.java)
        tvSignUp = binding.tvSignUpEnterpriseUserLogin
        btnSignIN = binding.btnSignInEnterpriseUser
        etenterpriseuserEmail = binding.etEmailEnterpriseUserLogin
        etenterpriseuserPassword = binding.etPasswordEnterpriseUserLogin
        progressBar = binding.pbEnterpriseUserLogin
        (requireActivity() as LoginActivity).setTitle("Enterprise user login")

        btnSignIN.setOnClickListener {
            ContextExtentions.hideKeyboard(it, requireContext())
            enterpriseuserEmail = setErrorMessage(etenterpriseuserEmail, "Enter Valid email")
            enterpriseuserPassword = setErrorMessage(etenterpriseuserPassword, "Valid Password must be more than 7 characters")

            if (enterpriseuserEmail != "" && enterpriseuserPassword != ""
            ) {
                Log.d(TAG, "onViewCreated: READY for login")
                progressBar?.visibility = View.VISIBLE
                viewModel.signInUserWithEmailPassword(enterpriseuserEmail!!, enterpriseuserPassword!!)
            }
        }
        viewModel.enterpriseuserAuthResult.observe(viewLifecycleOwner){
            if(it.isSuccess){
                progressBar?.visibility = View.GONE
                val uid = it.resultData as String
                DbInstance.setUserUid(requireContext(), uid)
                Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
                requireActivity().startActivity(Intent(requireContext(), UserdashboardActivity::class.java))
                moveWithAnimationToAnotherActivity()
            }else{
                progressBar?.visibility = View.GONE
                Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
        tvSignUp.setOnClickListener {
            try {
                (requireActivity() as LoginActivity).navController.navigate(R.id.action_enterpriseUserFragment_to_enterpriseUserRegistationFragment)
            } catch (e: Exception) {
                Log.e(TAG, "onViewCreated: ${e.localizedMessage}")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        try {
            val email = arguments?.getString(Constants.email)
            val password = arguments?.getString(Constants.password)
            etenterpriseuserEmail?.setText(email)
            etenterpriseuserPassword?.setText( password)
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
            etenterpriseuserEmail?.text.hashCode() -> cleanErrorMessage(etenterpriseuserEmail)
            etenterpriseuserPassword?.text.hashCode() -> cleanErrorMessage(etenterpriseuserPassword)
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EnterpriseUserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EnterpriseUserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}