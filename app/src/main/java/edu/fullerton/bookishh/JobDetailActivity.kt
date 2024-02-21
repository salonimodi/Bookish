package edu.fullerton.bookishh

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class JobDetailActivity: AppCompatActivity() {

    private lateinit var applyButton: Button
    private lateinit var compName: TextView
    private lateinit var compDesc: TextView
    private lateinit var jobName: TextView
    private lateinit var jobDesc: TextView
    private lateinit var location: TextView
    private lateinit var salary: TextView
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_detail)
        this.applyButton = findViewById(R.id.apply_button)
        this.compName = findViewById(R.id.company_name)
        this.compDesc = findViewById(R.id.company_description)
        this.jobName = findViewById(R.id.job_title)
        this.jobDesc = findViewById(R.id.job_description)
        this.location = findViewById(R.id.location)
        this.salary = findViewById(R.id.salary)
        sessionManager = SessionManager(this)
        applyButton.setOnClickListener{
            if(sessionManager.getLoggedInStatus()) {
                val intent = Intent(this, UserDetailsActivity::class.java)
                startActivity(intent)
            } else
                Toast.makeText(this, "Unauthorized!, Please Login", Toast.LENGTH_LONG).show()
        }
        this.compName.text = intent.getStringExtra("company_name")
        this.compDesc.text = intent.getStringExtra("company_description")
        this.jobName.text = intent.getStringExtra("job_title")
        this.jobDesc.text = intent.getStringExtra("job_description")
        this.location.text = intent.getStringExtra("location")
        var salary = intent.getIntExtra("salary", 0)
        this.salary.text = salary.toString()
    }
}