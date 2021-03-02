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

class IpAddressDialogFragment : DialogFragment() {

    private val model : BasicSetupViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            val layout = inflater.inflate(R.layout.dialog_ipaddress,null)
            builder.setView(layout)
                .setPositiveButton(R.string.edit, DialogInterface.OnClickListener { dialog, id ->
                    val addressEditText = layout.findViewById(R.id.edit_setup_ip_address_text) as EditText



                    val addressString : String = addressEditText.text.toString()

                    model.updateServerIpAddress(addressString)
                    getDialog()?.dismiss()
                })
                .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialog, id -> getDialog()?.dismiss() })
            builder.create()
        }
    }
}