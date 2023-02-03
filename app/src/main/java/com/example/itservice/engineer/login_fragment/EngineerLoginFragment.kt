package com.example.itservice.engineer.login_fragment

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
import com.example.itservice.common.utils.Constants
import com.example.itservice.common.utils.ContextExtentions
import com.example.itservice.common.utils.DbInstance
import com.example.itservice.databinding.FragmentEngineerLoginBinding
import com.example.itservice.engineer.dashboard.EngineerDashBoardActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EngineerLoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EngineerLoginFragment : BaseFragment(), TextWatcher {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentEngineerLoginBinding
    private lateinit var viewModel: EngineerLoginViewModel
    private lateinit var tvSignUp: TextView
    private lateinit var btnSignIN: Button
    private var etengineerEmail: AppCompatEditText? = null
    private var etengineerPassword: AppCompatEditText? = null
    private var engineerEmail: String? = null
    private var engineerPassword: String? = null
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
        binding = FragmentEngineerLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as LoginActivity).selectTabAt(1)
        (requireActivity() as LoginActivity).setTitle("Engineer login")
        viewModel = ViewModelProvider(
            this,
            ViewModelProviderFactory()
        ).get(EngineerLoginViewModel::class.java)
        tvSignUp = binding.tvSignUpEngineerLogin
        btnSignIN = binding.btnSignInEngineer
        etengineerEmail = binding.etEmailEngineerLogin
        etengineerPassword = binding.etPasswordEngineerLogin
        progressBar = binding.pbEngineerLogin

        btnSignIN.setOnClickListener {
            ContextExtentions.hideKeyboard(it, requireContext())
            engineerEmail = setErrorMessage(etengineerEmail, "Enter Valid email")
            engineerPassword = setErrorMessage(etengineerPassword, "Valid Password must be more than 7 characters")

            if (engineerEmail != "" && engineerPassword != ""
            ) {
                Log.d(TAG, "onViewCreated: READY for login")
                progressBar?.visibility = View.VISIBLE
                viewModel.signInUserWithEmailPassword(engineerEmail!!, engineerPassword!!)
            }
        }
        viewModel.engineerAuthResult.observe(viewLifecycleOwner){
            if(it.isSuccess){
                progressBar?.visibility = View.GONE
                val uid = it.resultData as String
                DbInstance.setUserUid(requireContext(), uid)
                Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
                requireActivity().startActivity(Intent(requireContext(), EngineerDashBoardActivity::class.java))
                moveWithAnimationToAnotherActivity()
            }else{
                progressBar?.visibility = View.GONE
                Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
        tvSignUp.setOnClickListener {
            try {
                (requireActivity() as LoginActivity).navController.navigate(R.id.action_engineerLoginFragment_to_engineerRegistationFragment)
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
            etengineerEmail?.setText(email)
            etengineerPassword?.setText( password)
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
            etengineerEmail?.text.hashCode() -> cleanErrorMessage(etengineerEmail)
            etengineerPassword?.text.hashCode() -> cleanErrorMessage(etengineerPassword)
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EngineerLoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EngineerLoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}