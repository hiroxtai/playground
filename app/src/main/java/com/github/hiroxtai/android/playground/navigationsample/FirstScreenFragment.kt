package com.github.hiroxtai.android.playground.navigationsample

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FirstScreenFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FirstScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FirstScreenFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_first_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btn_goto_second)
            .setOnClickListener { v ->
                Navigation.findNavController(v)
                    .navigate(R.id.secondScreenFragment)
            }
    }
}
