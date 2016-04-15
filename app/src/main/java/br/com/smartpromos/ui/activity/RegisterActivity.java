package br.com.smartpromos.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import br.com.smartpromos.R;
import br.com.smartpromos.adapter.GenderAdapter;
import br.com.smartpromos.api.general.request.LocalizacaoRequest;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener  {

    private TextView txtTitle;
    private ImageButton imgToolbar;
    private Spinner spinnerGender;
    private String[] gender = new String[]{"Gênero", "Masculino", "Feminino"};

    private Button edtDataNasc;

    private LocalizacaoRequest localizacaoRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent i = getIntent();

        if( i != null) {
            String locale = i.getStringExtra("localizacao");
            localizacaoRequest = new Gson().fromJson(locale, LocalizacaoRequest.class);
            Log.i("Locale_json", locale);
            Log.i("Check_locale", localizacaoRequest.getAddress());
        }

        edtDataNasc = (Button) findViewById(R.id.edtDataNasc);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(getResources().getString(R.string.txt_cadastro));

        imgToolbar = (ImageButton) findViewById(R.id.imgToolbar);
        imgToolbar.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back_white_48dp));

        imgToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        spinnerGender.setLayoutParams(lp1);
        spinnerGender.setAdapter(adapter1);
        spinnerGender.setAdapter(new GenderAdapter(this, gender));

        edtDataNasc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        RegisterActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setThemeDark(false);
                dpd.vibrate(true);
                dpd.dismissOnPause(true);
                dpd.setAccentColor(Color.parseColor("#303F9F"));
                dpd.show(getFragmentManager(), "Datepickerdialog");

            }

        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        edtDataNasc.setText(date);
    }
}
