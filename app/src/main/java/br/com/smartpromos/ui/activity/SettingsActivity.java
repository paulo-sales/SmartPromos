package br.com.smartpromos.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import br.com.smartpromos.R;

public class SettingsActivity extends AppCompatActivity {

    private TextView txtTitle;
    private ImageButton imgToolbar;

    private ToggleButton tgBtnConectado;

    private ToggleButton tgBtnRecebeOfertaTodas;
    private ToggleButton tgBtnRecebeOfertaSolicitadas;
    private ToggleButton tgBtnRecebeOfertaComprei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(getResources().getString(R.string.txt_settings));

        imgToolbar = (ImageButton) findViewById(R.id.imgToolbar);
        imgToolbar.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back_white_48dp));

        imgToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tgBtnConectado = (ToggleButton) findViewById(R.id.tgBtnConectado);

        tgBtnRecebeOfertaTodas = (ToggleButton) findViewById(R.id.tgBtnRecebeOfertaTodas);
        tgBtnRecebeOfertaSolicitadas = (ToggleButton) findViewById(R.id.tgBtnRecebeOfertaSolicitadas);
        tgBtnRecebeOfertaComprei = (ToggleButton) findViewById(R.id.tgBtnRecebeOfertaComprei);

        tgBtnRecebeOfertaTodas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tgBtnRecebeOfertaSolicitadas.setChecked(false);
                    tgBtnRecebeOfertaComprei.setChecked(false);
                }
            }
        });

        tgBtnRecebeOfertaSolicitadas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tgBtnRecebeOfertaTodas.setChecked(false);
                    tgBtnRecebeOfertaComprei.setChecked(false);
                }
            }
        });

        tgBtnRecebeOfertaComprei.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tgBtnRecebeOfertaTodas.setChecked(false);
                    tgBtnRecebeOfertaSolicitadas.setChecked(false);
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
