package br.com.smartpromos.ui.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.smartpromos.R;
import br.com.smartpromos.util.Util;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SyncFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SyncFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private View view;
    public SyncFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sync, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!Util.isNetworkAvailable()){
            Util.showNetworkInfo(view, getContext());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
