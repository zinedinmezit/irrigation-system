package com.example.irrigationsystem.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import com.example.irrigationsystem.R
import com.example.irrigationsystem.viewmodels.MainViewModel

class MainFragment : Fragment() {

    private val model: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.i("FragmentTest","onCreateView")
        model.runWebSocket()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("FragmentTest","onViewCreated")
        val button = view.findViewById<Button>(R.id.button)
        button?.setOnClickListener {
            model.signalCode=1
            model.runWebSocket()
        }
    }
    
}