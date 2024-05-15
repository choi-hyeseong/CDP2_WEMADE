package com.home.cdp2app.view.fragment.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.home.cdp2app.databinding.AuthSelectBinding
import com.home.cdp2app.view.callback.AuthCallback

class AuthSelectFragment : Fragment() {

    private var callback : AuthCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as AuthCallback?
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bind = AuthSelectBinding.inflate(inflater, container, false)
        bind.signin.setOnClickListener {
            callback?.navigateToLogin()
        }
        bind.signup.setOnClickListener {
            callback?.navigateToRegister()
        }
        return bind.root
    }
}