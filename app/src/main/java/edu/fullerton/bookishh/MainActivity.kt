package edu.fullerton.bookishh


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.provider.WebAuthProvider
import java.util.*


private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private val SHARED_PREFS_FILE = "bookishh_shared_prefs"
    private val LANG_PREFS_FILE = "lang_shared_prefs"
    private lateinit var logoImage: ImageView
    lateinit var loginButton: Button
    private lateinit var menu: Menu
    private lateinit var account: Auth0
    private lateinit var sessionManager: SessionManager
    var darkModeVal : Boolean = false
    var languageCheckboxVal: Boolean = false
    var languageCodeFrenchVal : Boolean = false
    var langModeVal: String = "en"
    private lateinit var jobFilterContainer: FrameLayout
    private lateinit var jobDetailsContainer: FrameLayout
    private lateinit var loginContainer: FrameLayout
    private lateinit var jobFilterFragment: FilterDialogFragment
    private lateinit var jobDetailsFragment: JobDetailsFragment
    private lateinit var loginFragment: LoginFragment

    private val jobPortalViewModel: JobPortalViewModel by lazy {
        MyPreferencesRepo.initialize(this)
        JobPortalViewModel()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
        darkModeVal = sharedPreferences.getBoolean("darkMode", false)
        this.setDarkModeUtility(darkModeVal)

        val langPreferences = getSharedPreferences(LANG_PREFS_FILE, Context.MODE_PRIVATE)
        langModeVal = langPreferences.getString("spanishMode", "en").toString()
        this.setLocaleUtility(langModeVal)

        setContentView(R.layout.activity_main)

        this.loginButton = findViewById<Button>(R.id.loginButton)
        this.jobPortalViewModel.loadInputs(this)

        jobFilterContainer = this.findViewById(R.id.job_filter_container)
        jobDetailsContainer = this.findViewById(R.id.job_fragment_container)
        loginContainer = this.findViewById(R.id.login_fragment_container)

        sessionManager = SessionManager(this)
        account = Auth0("dev-tre6pxtrhb4kvx31.us.auth0.com", "uv71pifU3o7GJza4WSR4ChlPToPyQ2sz")

        // Set click listener for login button
        loginButton.setOnClickListener {
            Log.v(TAG, "Button Clicked")
            var isLoggedIn = sessionManager.getLoggedInStatus()
            Log.v(TAG, "Status: $isLoggedIn")
            if(isLoggedIn) {
                loginButton.text = "Logout"
                setLogoutButton()
            } else
                loadLoginFragment()
        }

        this.logoImage = findViewById(R.id.logoImageView)
        logoImage.setOnClickListener {
            val popup = AboutUsPopup()
            popup.show(supportFragmentManager, "about_us")
        }

        jobDetailsFragment = JobDetailsFragment()
        jobFilterFragment = FilterDialogFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.job_fragment_container, jobDetailsFragment)
            .replace(R.id.job_filter_container, jobFilterFragment)
            .addToBackStack(null)
            .commit()

    }

    private fun loadLoginFragment() {
        jobFilterContainer.visibility = View.GONE
        jobDetailsContainer.visibility = View.GONE
        loginFragment = LoginFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.login_fragment_container, loginFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setLogoutButton() {
        Toast.makeText(this@MainActivity, "Logged out successfully", Toast.LENGTH_SHORT).show()
        loginButton.text = "Login"
        sessionManager.clearSession()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.hamburger_menu, menu)
        this.menu = menu
        this.menu.findItem(R.id.menu_item_language).isChecked = languageCheckboxVal
        this.menu.findItem(R.id.menu_item_dark_mode).isChecked = darkModeVal
        this.menu.findItem(R.id.menu_item_language_fr).isChecked = languageCodeFrenchVal
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_dark_mode -> {
                darkModeVal = !darkModeVal
                jobPortalViewModel.saveInput(darkModeVal.toString(), 1)
                setDarkMode(darkModeVal)
                true
            }

            R.id.menu_item_language -> {
                languageCheckboxVal = !languageCheckboxVal
                val languageCode = if (languageCheckboxVal) "es" else "en"
                jobPortalViewModel.saveInput(languageCheckboxVal.toString(), 2)
                setLocale(languageCode)
                true
            }
            R.id.menu_item_language_fr -> {
                languageCodeFrenchVal = !languageCodeFrenchVal
                val languageCode = if (languageCodeFrenchVal) "fr" else "en"
                jobPortalViewModel.saveInput(languageCodeFrenchVal.toString(), 3)
                setLocale(languageCode)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setDarkMode(isDarkMode: Boolean) {
        this.setDarkModeUtility(isDarkMode)
        // Save the dark mode state in a shared preference
        val editor = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE).edit()
        editor.putBoolean("darkMode", isDarkMode)
        editor.apply()
    }

    private fun setDarkModeUtility(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun setLocale(languageCode: String) {
        this.setLocaleUtility(languageCode)
        recreate()
        val editor = getSharedPreferences(LANG_PREFS_FILE, Context.MODE_PRIVATE).edit()
        editor.putString("spanishMode", languageCode)
        editor.apply()
    }

    private fun setLocaleUtility(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun showJobs() {
        jobFilterContainer.visibility = View.VISIBLE
        jobDetailsContainer.visibility = View.VISIBLE
        loginContainer.visibility = View.GONE
    }

    fun reloadJobs() {
        jobDetailsContainer.visibility = View.VISIBLE
    }

    fun hideJobs() {
        jobDetailsContainer.visibility = View.GONE
    }
}
