package com.example.irrigationsystem.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.irrigationsystem.R
import com.example.irrigationsystem.viewmodels.BasicSetupViewModel

class ParamsSetupDialogFragment : DialogFragment() {

    private val model : BasicSetupViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            val layout = inflater.inflate(R.layout.main_dialog_limit_values_setup,null)
            builder.setView(layout)
                .setPositiveButton(R.string.edit_plan, DialogInterface.OnClickListener { dialog, id ->
                    val hummMinEditText = layout.findViewById(R.id.edit_setup_hummMin_text) as EditText
                    val hummMaxEditText = layout.findViewById(R.id.edit_setup_hummMax_text) as EditText
                    val tempMinEditText = layout.findViewById(R.id.edit_setup_tempMin_text) as EditText
                    val tempMaxEditText = layout.findViewById(R.id.edit_setup_tempMax_text) as EditText



                    val hummMin  = hummMinEditText.text.toString().toDouble()
                    val hummMax  = hummMaxEditText.text.toString().toDouble()
                    val tempMin  = tempMinEditText.text.toString().toDouble()
                    val tempMax  = tempMaxEditText.text.toString().toDouble()

                    model.updateParameterValues(tempMin,tempMax,hummMin,hummMax)
                    dialog.dismiss()
                })
                .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialog, id -> dialog.dismiss() })
            builder.create()
        }
    }
}