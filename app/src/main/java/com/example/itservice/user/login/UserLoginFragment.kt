package com.example.itservice.user.login

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
import com.example.itservice.databinding.FragmentUserLoginBinding
import com.example.itservice.user.user_dash_board.UserdashboardActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserLoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

interface SetData{
    fun setEmailPassword(email: String, password: String)
}

class UserLoginFragment : BaseFragment(), TextWatcher, SetData {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentUserLoginBinding
    private lateinit var tvMoveToRegistaion: TextView
    private lateinit var viewModel: UserLoginViewModel
    private lateinit var btnSignIN: Button
    private var etuserEmail: AppCompatEditText? = null
    private var etuserPassword: AppCompatEditText? = null
    private var userEmail: String? = null
    private var userPassword: String? = null
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
        binding = FragmentUserLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as LoginActivity).selectTabAt(0)
        viewModel =
            ViewModelProvider(this, ViewModelProviderFactory()).get(UserLoginViewModel::class.java)
        tvMoveToRegistaion = binding.tvSignUpUserLogin
        btnSignIN = binding.btnSignInuser
        etuserEmail = binding.etEmailUserLogin
        etuserPassword = binding.etPasswordUserLogin
        progressBar = binding.pbUserLogin
        (requireActivity() as LoginActivity).setTitle("User login")

        btnSignIN.setOnClickListener {
            ContextExtentions.hideKeyboard(it, requireContext())
            userEmail = setErrorMessage(etuserEmail, "Enter Valid email")
            userPassword = setErrorMessage(etuserPassword, "Valid Password must be more than 7 characters")

            if (userEmail != "" && userPassword != ""
            ) {
                Log.d(TAG, "onViewCreated: READY for login")
                progressBar?.visibility = View.VISIBLE
                viewModel.signInUserWithEmailPassword(userEmail!!, userPassword!!)
            }
        }

        viewModel.userAuthResult.observe(viewLifecycleOwner){
            if(it.isSuccess){
                val uid = it.resultData as String
                Log.d(TAG, "onViewCreated: uid: ${uid}")
                DbInstance.setUserUid(requireContext(), uid)
                viewModel.getUserData(uid)
            }else{
                progressBar?.visibility = View.GONE
                Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.userData.observe(viewLifecycleOwner){
            if(it!=null){
                DbInstance.setUserName(requireContext() ,it.fullName!!)
                progressBar?.visibility = View.GONE
                Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
                requireActivity().startActivity(Intent(requireContext(), UserdashboardActivity::class.java))
                moveWithAnimationToAnotherActivity()
            }else{
                progressBar?.visibility = View.GONE
                Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }

        tvMoveToRegistaion.setOnClickListener {
            try {
                (requireActivity() as LoginActivity).navController.navigate(R.id.action_userLoginFragment_to_userRegistationFragment)
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
            etuserEmail?.setText(email)
            etuserPassword?.setText( password)
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
            etuserEmail?.text.hashCode() -> cleanErrorMessage(etuserEmail)
            etuserPassword?.text.hashCode() -> cleanErrorMessage(etuserPassword)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserLoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserLoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun setEmailPassword(email: String, password: String) {
        etuserEmail?.setText(email)
        etuserPassword?.setText(password)
    }
}