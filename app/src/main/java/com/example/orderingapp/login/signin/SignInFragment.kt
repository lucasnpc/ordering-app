package com.example.orderingapp.login.signin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.orderingapp.R
import com.example.orderingapp.databinding.SignInFragmentBinding
import com.example.orderingapp.main.presentation.MainActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.android.material.snackbar.Snackbar

class SignInFragment : Fragment() {
    private val binding: SignInFragmentBinding by lazy {
        SignInFragmentBinding.inflate(layoutInflater)
    }
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            Snackbar.make(
                requireView(),
                getString(R.string.something_went_wrong),
                Snackbar.LENGTH_SHORT
            ).show()
            return@registerForActivityResult
        }
        requireActivity().startActivity(Intent(requireContext(), MainActivity::class.java))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.emailButton.setOnClickListener {
            signInLauncher.launch(AuthUI.getInstance().createSignInIntentBuilder().build())
        }
    }
}