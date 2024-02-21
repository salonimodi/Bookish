package edu.fullerton.bookishh

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment

private const val TAG = "FilterFrag"
class FilterDialogFragment : DialogFragment() {

    private lateinit var columnSpinner: Spinner
    private lateinit var valueEditText: EditText
    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_filter_dialog, container, false)
        columnSpinner = view.findViewById(R.id.columnSpinner)
        valueEditText = view.findViewById(R.id.valueEditText)
        submitButton = view.findViewById<Button>(R.id.submitButton)

        val displayToVariableMap = mapOf(
            "Apply column" to null,
            "Job Title" to "title",
            "Company Name" to "company_name",
            "Location" to "location"
        )

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.filter_columns,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        columnSpinner.adapter = adapter

        columnSpinner.setSelection(0)

        submitButton.setOnClickListener {
            val selectedDisplay = columnSpinner.selectedItem.toString()
            val selectedVariable = displayToVariableMap[selectedDisplay]
            if (selectedVariable != null) {
                submit(selectedVariable, valueEditText.text.toString())
            }
        }

        return view
    }

    private fun submit(columnName: String, value: String) {
        val jobFrag = JobDetailsFragment()
        val args = Bundle()
        args.putString("columnName", columnName)
        args.putString("value", value)
        jobFrag.arguments = args
        fragmentManager?.beginTransaction()?.add(R.id.job_fragment_container, jobFrag)?.commit()
    }
}
