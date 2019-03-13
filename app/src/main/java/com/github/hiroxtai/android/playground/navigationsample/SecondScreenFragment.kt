package com.github.hiroxtai.android.playground.navigationsample

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SecondScreenFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SecondScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SecondScreenFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_second_screen, container, false)
    }
}
