package com.example.a18mas.mp3front.UI

import android.support.v4.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.a18mas.mp3front.R

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment(), View.OnClickListener {
    override fun onClick(p0: View?) {
        Log.i("FRAGMENT","CLICK.......................")

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {


        var view = inflater.inflate(R.layout.fragment_main, container, false)
        var btn = view.findViewById(R.id.btn_search) as Button
        btn.setOnClickListener(this)

        return view

    }

}
