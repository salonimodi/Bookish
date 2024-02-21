package edu.fullerton.bookishh

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.fullerton.bookishh.api.Job
import edu.fullerton.bookishh.api.JobResponse
import edu.fullerton.bookishh.api.JobsExecuter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "JobDetails"
class JobDetailsFragment : Fragment() {
    private lateinit var jobRecyclerView: RecyclerView
    private var adapter: JobAdapter? = null
    private lateinit var mainActivity: MainActivity
    private lateinit var jobList: List<Job>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.joblist_fragment, container, false)
        // Initialize RecyclerView and Adapter
        jobList = emptyList()
        jobRecyclerView = view.findViewById(R.id.job_recycler_view) as RecyclerView
        jobRecyclerView.layoutManager = LinearLayoutManager(context)
        updateUI()
        return view
    }

    private fun updateUI()
    {
        val columnName = arguments?.getString("columnName")?.takeUnless { it == "null" }
        val value = arguments?.getString("value")?.takeUnless { it == "null" }
        val columnValueMap = if (columnName != null && value != null) {
            mapOf("column_name" to columnName, "value" to value)
        } else {
            emptyMap()
        }
            val call = JobsExecuter.api.getJobs(columnValueMap)
            call.enqueue(object : Callback<JobResponse> {
                override fun onResponse(call: Call<JobResponse>, response: Response<JobResponse>) {
                    jobList = response.body()?.jobs ?: emptyList()
                    Log.v(TAG, "HERE ?? $jobList")
                    if (jobList != null) {
                        adapter = JobAdapter(jobList)
                        mainActivity.reloadJobs()
                        jobRecyclerView.adapter = adapter
                    } else {
                        Log.v(TAG, "Job list is null")
                    }
                }

                override fun onFailure(call: Call<JobResponse>, t: Throwable) {
                    Log.e(TAG, "Something went wrong $t")
                    mainActivity.reloadJobs()
                }

            })

    }

    private inner class JobHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        private lateinit var job: Job
        private val companyName: TextView = this.itemView.findViewById(R.id.company_name)
        private val jobRole: TextView = this.itemView.findViewById(R.id.job_role)
        private val location: TextView = this.itemView.findViewById(R.id.location)

        fun bind(job: Job)
        {
            this.job = job
            this.companyName.text = this.job.companyName
            this.jobRole.text = this.job.role
            this.location.text = this.job.location
        }
    }

    private inner class JobAdapter (var job: List<Job>)
        : RecyclerView.Adapter<JobHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobHolder
        {
            val view = layoutInflater.inflate(R.layout.list_job, parent, false)
            return JobHolder(view)
        }

        override fun onBindViewHolder(holder: JobHolder, position: Int)
        {
            val jobDetails = this.job[position]
            holder.bind(jobDetails)
            holder.itemView.setOnClickListener {
                val intent = Intent(context, JobDetailActivity::class.java)
                intent.putExtra("company_name", jobDetails.companyName)
                intent.putExtra("company_description", jobDetails.companyDescription)
                intent.putExtra("job_title", jobDetails.role)
                intent.putExtra("job_description", jobDetails.desc)
                intent.putExtra("location", jobDetails.location)
                intent.putExtra("salary", jobDetails.salary)
                context?.startActivity(intent)
            }
        }

        override fun getItemCount() = job.size
    }

    companion object {
        fun newInstance(): JobDetailsFragment {
            return JobDetailsFragment()
        }
    }

}
