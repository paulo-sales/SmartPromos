package br.com.smartpromos.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.com.smartpromos.R;
import br.com.smartpromos.ui.activity.DashBoardActivity;
import br.com.smartpromos.ui.activity.LocationActivity;
import br.com.smartpromos.ui.activity.RememberPasswrodActivity;

/**
 * Created by Paulo Vitor on 12/03/2016.
 */
public class DialogUI extends DialogFragment {

    private Button btnClose;
    private TextView txtTitleDialog, descDialog;
    private RelativeLayout containerErrors;

    public DialogUI(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_errors, container, false);
        // Inflate the layout to use as dialog or embedded fragment
        containerErrors = (RelativeLayout) view.findViewById(R.id.containerErrors);

        btnClose = (Button) view.findViewById(R.id.btnClose);
        txtTitleDialog = (TextView) view.findViewById(R.id.txtTitleDialog);
        descDialog = (TextView) view.findViewById(R.id.descDialog);

        txtTitleDialog.setText(getArguments().getString("title"));
        descDialog.setText(getArguments().getString("description"));
        final String extra = getArguments().getString("extra");

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();

                if(extra == "rejected"){
                    getContext().startActivity(new Intent(getActivity(), DashBoardActivity.class));
                    getActivity().finish();
                }

                if(extra == "used"){
                    getContext().startActivity(new Intent(getActivity(), DashBoardActivity.class));
                    getActivity().finish();
                }


            }
        });

        containerErrors.setOnClickListener(new View.OnClickListener() {
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
}
