package com.example.itservice.base

import androidx.fragment.app.Fragment
import com.example.itservice.R

open class BaseFragment : Fragment() {
    fun moveWithAnimationToAnotherActivity(){
        requireActivity().overridePendingTransition(R.anim.slide_fade_in, R.anim.slide_in_right)
    }
}