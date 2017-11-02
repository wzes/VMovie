package com.wzes.vmovie.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wzes.vmovie.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InTheaterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InTheaterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InTheaterFragment extends Fragment {

    public InTheaterFragment() {
        // Required empty public constructor
    }

    public static InTheaterFragment newInstance() {
        InTheaterFragment fragment = new InTheaterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_in_theater, container, false);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}
