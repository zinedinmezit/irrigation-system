package com.example.irrigationsystem.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.irrigationsystem.R
import com.example.irrigationsystem.viewmodels.BasicSetupViewModel

class BasicSetupDialogFragment : DialogFragment() {

    private val model : BasicSetupViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            val layout = inflater.inflate(R.layout.dialog_setup,null)
             builder.setView(layout)
                .setPositiveButton(R.string.edit) { dialog, _ ->
                    val cityEditText = layout.findViewById(R.id.edit_setup_city_text) as EditText


                    val cityString: String = cityEditText.text.toString()

                    model.updateCity(cityString)
                    dialog.dismiss()
                }
                 .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            builder.create()
        }
    }
}