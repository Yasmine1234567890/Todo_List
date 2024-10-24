package com.example.todo_listapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todo_listapp.R
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todo_listapp.databinding.FragmentSignUpBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult


class SignUpFragment : Fragment() {

    private lateinit var navControl: NavController
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()
    }

    private fun init(view: View) {
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }

    private fun registerEvents() {
        binding.authTextView.setOnClickListener {
            navControl.navigate(R.id.action_signUpFragment_to_signinFragment2)

        }
        binding.nextBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passEt.text.toString()
            val verifyPass = binding.rePassEt.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty() && verifyPass.isNotEmpty()) {
                if (pass==verifyPass) {

                    binding.progressBar.visibility = View.VISIBLE
                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Registered Successfully " , Toast.LENGTH_LONG).show()
                            navControl.navigate(R.id.action_signUpFragment_to_homeFragment3)
                        } else {
                            // Show error message
                            Toast.makeText(context, "Sign Up Failed: " + task.exception?.message, Toast.LENGTH_LONG).show()
                        }
                        binding.progressBar.visibility = View.GONE
                    })
                }else{
                Toast.makeText(context, "Password Not matching" , Toast.LENGTH_LONG).show()

            }
            }else{
            Toast.makeText(context, "Empty fields not allowed" , Toast.LENGTH_LONG).show()

        }

        }
    }
}




