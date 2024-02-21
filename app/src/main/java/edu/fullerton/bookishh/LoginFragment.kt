package edu.fullerton.bookishh

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import edu.fullerton.bookishh.api.LoginExecuter
import edu.fullerton.bookishh.api.LoginResponse
import edu.fullerton.bookishh.api.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "LoginFragment"
class LoginFragment : Fragment() {

    private lateinit var submitButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var sessionManager: SessionManager
    private lateinit var registerTextView: Button
    private lateinit var authLoginButton: Button
    private lateinit var account: Auth0
    private var userIsAuthenticated = false
    private var user: User? = null
    private var fragment1: JobDetailsFragment? = null
    private var fragment2: FilterDialogFragment? = null

    private lateinit var mainActivity: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragment1 = requireActivity().supportFragmentManager.findFragmentById(R.id.job_fragment_container) as? JobDetailsFragment
        fragment2 = requireActivity().supportFragmentManager.findFragmentById(R.id.job_filter_container) as? FilterDialogFragment
        print("Fragment on create")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view =  inflater.inflate(R.layout.login, container, false)
        this.submitButton = view.findViewById<Button>(R.id.button_login)
        this.emailEditText = view.findViewById<EditText>(R.id.edit_text_email)
        this.passwordEditText = view.findViewById<EditText>(R.id.edit_text_password)
        this.authLoginButton = view.findViewById(R.id.button_google_login)
        this.registerTextView = view.findViewById(R.id.text_view_create_account)
        registerTextView.setOnClickListener { goToRegisterFragment() }
        sessionManager = SessionManager(requireContext())
        submitButtonCallback()
        account = Auth0("uv71pifU3o7GJza4WSR4ChlPToPyQ2sz", "dev-tre6pxtrhb4kvx31.us.auth0.com")
        authLoginButton.setOnClickListener { login() }
        return view
    }

    override fun onStart() {
        super.onStart()
        print("Fragment on  start")
    }

    private fun goToRegisterFragment() {
        Log.v(TAG, "Reached Inside")
        val fragment = CreateAccountFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.login_fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    private fun submitButtonCallback() {
        submitButton.setOnClickListener{
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
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
            LoginExecuter.api.login(email, password).enqueue(object: Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val loginResponse= response.body()
                        if(loginResponse!=null) {
                            var authToken  = loginResponse.token
                            Log.v(TAG,authToken)
                            Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_LONG).show()
                            routeToJobDetailPage()
                            sessionManager.saveAuthToken(authToken)
                            sessionManager.setLogggedInStatus(true)
                            mainActivity.loginButton.text = "Logout"
                            mainActivity.showJobs()
                        }
                        else
                            Toast.makeText(requireContext(), "Login failed please register", Toast.LENGTH_LONG).show()
                    } else
                        Toast.makeText(requireContext(), "Login failed please try again", Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG).show()
                    Log.e(TAG, "Error: "+ t.message)
                }

            })
        }
    }

    private fun login() {
        Log.v(TAG, "AM I GETTING CALLED LOGOUT?>>?")
        WebAuthProvider
            .login(account)
            .withScheme("app")
            .start(requireContext(), object : com.auth0.android.callback.Callback<Credentials, AuthenticationException> {
                override fun onFailure(exception: AuthenticationException) {
                    Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_LONG).show()
                }

                override fun onSuccess(credentials: Credentials) {
                    val idToken = credentials.idToken
                    Log.v(TAG, "TOKEN: $idToken")
                    user = User(idToken)
                    userIsAuthenticated = true
                    Toast.makeText(requireContext(), "Login Successful, Welcome ${user!!.name.substringBefore('@')}", Toast.LENGTH_LONG).show()
//                    routeToJobDetailPage()
                    sessionManager.saveAuthToken(idToken)
                    sessionManager.setLogggedInStatus(true)
                    mainActivity.loginButton.text = "Logout"
                    mainActivity.showJobs()
                }
            })
    }

    private fun routeToJobDetailPage() {
        fragment1?.view?.visibility = View.VISIBLE
        fragment2?.view?.visibility = View.VISIBLE
    }
}