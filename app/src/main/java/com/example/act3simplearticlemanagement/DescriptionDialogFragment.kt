package com.example.act3simplearticlemanagement

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment

class DescriptionDialogFragment : DialogFragment() {
    private var mCallback: FilterDescriptionInterface? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.insert_word_dialog, container, false)
        val btnOk: Button = view.findViewById(R.id.ButtonSubmitDialog)
        val btnCancel: Button = view.findViewById(R.id.ButtonCancelDialog)
        val editTextData: EditText = view.findViewById(R.id.ETDataDialog)

        btnOk.setOnClickListener {
            mCallback?.filter(editTextData.text.toString())
            this.dismiss()
        }

        btnCancel.setOnClickListener {
            this.dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()

        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mCallback = activity as FilterDescriptionInterface
        } catch (e: ClassCastException) {
            Log.e("Dialog", "onAttach: ClassCastException: " + e.message)
        }
    }
}
