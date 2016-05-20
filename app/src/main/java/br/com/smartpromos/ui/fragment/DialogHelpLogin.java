package br.com.smartpromos.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import br.com.smartpromos.R;
import br.com.smartpromos.ui.activity.LocationActivity;
import br.com.smartpromos.ui.activity.RegisterActivity;
import br.com.smartpromos.ui.activity.RememberPasswrodActivity;

/**
 * Created by Paulo Vitor on 12/03/2016.
 */
public class DialogHelpLogin extends DialogFragment {

    private RelativeLayout containerHelp;

    private ImageButton btnRegister;
    private ImageButton btnRemember;

    private TextView txtTitleRegister;
    private TextView txtTitleRemember;

    private TextView txtSubRegister;
    private TextView txtSubRemember;

    public DialogHelpLogin(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_help_login, container, false);
        // Inflate the layout to use as dialog or embedded fragment

        containerHelp = (RelativeLayout) view.findViewById(R.id.containerHelp);

        btnRegister = (ImageButton) view.findViewById(R.id.btnRegister);
        btnRemember = (ImageButton) view.findViewById(R.id.btnRemember);

        txtTitleRegister = (TextView) view.findViewById(R.id.txtTitleRegister);
        txtTitleRemember = (TextView) view.findViewById(R.id.txtTitleRemember);

        txtSubRegister = (TextView) view.findViewById(R.id.txtSubRegister);
        txtSubRemember = (TextView) view.findViewById(R.id.txtSubRemember);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerActivity();
            }
        });

        btnRemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rememberActivity();
            }
        });

        txtTitleRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerActivity();
            }
        });

        txtTitleRemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rememberActivity();
            }
        });

        txtSubRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerActivity();
            }
        });

        txtSubRemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rememberActivity();
            }
        });

        containerHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    public void registerActivity(){
        getActivity().startActivity(new Intent(getContext(), RegisterActivity.class));
    }


    public void rememberActivity(){
        getActivity().startActivity(new Intent(getContext(), RememberPasswrodActivity.class));
    }

}
