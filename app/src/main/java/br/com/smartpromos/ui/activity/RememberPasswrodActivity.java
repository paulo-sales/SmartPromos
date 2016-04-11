package br.com.smartpromos.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import br.com.smartpromos.R;

public class RememberPasswrodActivity extends AppCompatActivity {

    private TextView txtTitle;
    private ImageButton imgToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remember_passwrod);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(getResources().getString(R.string.txt_recuperar_senha));

        imgToolbar = (ImageButton) findViewById(R.id.imgToolbar);
        imgToolbar.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back_white_48dp));

        imgToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
