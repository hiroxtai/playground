package com.github.hiroxtai.android.playground.fragmentsample;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements
        FugaFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FugaFragment fragment = FugaFragment.newInstance("Param1 Text", "Param2 Text");
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.contents, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onFragmentInteraction(String param1, String param2) {
        String message = String.format("FugaFragment Action(%s, %s)", param1, param2);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
