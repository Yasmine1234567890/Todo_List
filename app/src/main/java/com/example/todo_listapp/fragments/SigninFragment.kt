package com.example.todo_listapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todo_listapp.R

import com.example.todo_listapp.databinding.FragmentSigninBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class SigninFragment : Fragment() {

    private lateinit var navControl: NavController
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSigninBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSigninBinding.inflate(inflater, container, false)
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
            navControl.navigate(R.id.action_signinFragment_to_signUpFragment)

        }
        binding.nextBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passEt.text.toString()


            if (email.isNotEmpty() && pass.isNotEmpty() ) {
                binding.progressBar3.visibility=View.VISIBLE

                    auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(
                        OnCompleteListener<AuthResult> { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Login Successfully " , Toast.LENGTH_LONG).show()
                            navControl.navigate(R.id.action_signinFragment_to_homeFragment)
                        } else {
                            // Show error message
                            Toast.makeText(context, "Login Failed: " + task.exception?.message, Toast.LENGTH_LONG).show()
                        }
                            binding.progressBar3.visibility=View.GONE
                    })
                }else{
                Toast.makeText(context, "Empty fields not allowed" , Toast.LENGTH_LONG).show()

            }
            }

        }
    }







