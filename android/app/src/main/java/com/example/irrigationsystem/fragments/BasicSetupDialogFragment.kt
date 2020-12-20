package com.example.irrigationsystem.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.irrigationsystem.R
import com.example.irrigationsystem.viewmodels.BasicSetupViewModel
import java.lang.ClassCastException

class BasicSetupDialogFragment : DialogFragment() {

    private val model : BasicSetupViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            val layout = inflater.inflate(R.layout.dialog_setup,null)
             builder.setView(layout)
                .setPositiveButton(R.string.edit_plan,DialogInterface.OnClickListener { dialog, id ->
                    val addressEditText = layout.findViewById(R.id.edit_setup_address_text) as EditText
                    val cityEditText = layout.findViewById(R.id.edit_setup_city_text) as EditText
                    val tempMinEditText = layout.findViewById(R.id.edit_setup_tempMin_text) as EditText
                    val tempMaxEditText = layout.findViewById(R.id.edit_setup_tempMax_text) as EditText
                    val hummMinEditText = layout.findViewById(R.id.edit_setup_hummMin_text) as EditText
                    val hummMaxEditText = layout.findViewById(R.id.edit_setup_hummMax_text) as EditText


                    val addressString : String = addressEditText.text.toString()
                    val cityString : String = cityEditText.text.toString()
                    val tempMinValue = tempMinEditText.text.toString().toDouble()
                    val tempMaxValue = tempMaxEditText.text.toString().toDouble()
                    val hummMinValue = hummMinEditText.text.toString().toDouble()
                    val hummMaxValue = hummMaxEditText.text.toString().toDouble()
                    model.updateSetupInfo(addressString,cityString,tempMinValue,tempMaxValue,hummMinValue,hummMaxValue)
                    getDialog()?.dismiss()
                })
                .setNegativeButton(R.string.cancel,DialogInterface.OnClickListener { dialog, id -> getDialog()?.dismiss() })
            builder.create()
        }
    }
}