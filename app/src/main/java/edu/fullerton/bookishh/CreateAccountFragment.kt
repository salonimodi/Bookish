package edu.fullerton.bookishh

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import edu.fullerton.bookishh.api.RegisterExecuter
import edu.fullerton.bookishh.api.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "RegisterFragment"
class CreateAccountFragment : Fragment() {

    lateinit var submitButton: Button
    lateinit var emailEditText: EditText
    lateinit var nameEditText: EditText
    lateinit var passwordEditText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view =  inflater.inflate(R.layout.createaccount, container, false)
        this.submitButton = view.findViewById(R.id.buttonSubmit)
        this.nameEditText = view.findViewById(R.id.editTextName)
        this.emailEditText = view.findViewById(R.id.editTextEmail)
        this.passwordEditText = view.findViewById(R.id.editTextPassword)
        submitButtonCallback()
        return view
    }

    private fun submitButtonCallback() {
        submitButton.setOnClickListener {
            var name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            if(name.isEmpty()) {
                nameEditText.error = "Name Required"
                nameEditText.requestFocus()
                return@setOnClickListener
            }
            if(email.isEmpty()) {
                emailEditText.error = "Email Required"
                emailEditText.requestFocus()
                return@setOnClickListener
            }
            if(password.isEmpty()) {
                passwordEditText.error = "Password Required"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }
            RegisterExecuter.api.register(name, email, password, 0).enqueue(object: Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                        if (response.isSuccessful) {
                            val registerResponse = response.body()
                            if (registerResponse?.successMessage != null) {
                                Toast.makeText(
                                    requireContext(),
                                    registerResponse.successMessage,
                                    Toast.LENGTH_LONG
                                ).show()
                                val fragment =
                                    LoginFragment()
                                val fragmentManager = requireActivity().supportFragmentManager
                                val fragmentTransaction = fragmentManager.beginTransaction()
                                fragmentTransaction.replace(
                                    R.id.login_fragment_container,
                                    fragment
                                )
                                fragmentTransaction.commit()
                            } else
                                Toast.makeText(
                                    requireContext(),
                                    registerResponse?.errorMessage,
                                    Toast.LENGTH_LONG
                                ).show()
                        } else
                            Toast.makeText(
                                requireContext(),
                                "Registration failed",
                                Toast.LENGTH_LONG
                            ).show()

                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG).show()
                    Log.e(TAG, "Error: "+ t.message)
                }

            })
        }
    }

    override fun onStart() {
        super.onStart()
    }

}