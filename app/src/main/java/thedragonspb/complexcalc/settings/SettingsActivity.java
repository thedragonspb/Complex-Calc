package thedragonspb.complexcalc.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import thedragonspb.complexcalc.Data;
import thedragonspb.complexcalc.MyApplication;
import thedragonspb.complexcalc.R;

/**
 * Created by thedragonspb on 17.06.17.
 */
public class SettingsActivity extends AppCompatActivity {

    private static int SEEK_BAR_MAX = 5;
    private static int SEEK_BAR_MIN = 1;
    private static int SEEK_BAR_STEP = 1;

    LinearLayout language;
    TextView languageSummary;

    TextView roundingTV;
    AppCompatSeekBar seekBar;

    TextView feedbackTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        languageSummary = (TextView) findViewById(R.id.languageSummary);
        languageSummary.setText(getCurrentLangage());


        language = (LinearLayout) findViewById(R.id.language);
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LanguageDialog().show(getFragmentManager(), "LANGUAGE DIALOG");
            }
        });


        roundingTV = (TextView) findViewById(R.id.rounding);
        roundingTV.setText("" + Data.getInstance().getRounding());

        seekBar = (AppCompatSeekBar) findViewById(R.id.seekBar);
        seekBar.setMax((SEEK_BAR_MAX - SEEK_BAR_MIN) / SEEK_BAR_STEP);
        seekBar.setProgress(Data.getInstance().getRounding() - 1);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int value = SEEK_BAR_MIN + (i * SEEK_BAR_STEP);
                roundingTV.setText(String.valueOf(value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int value = SEEK_BAR_MIN + (seekBar.getProgress() * SEEK_BAR_STEP);
                MyApplication.getInstance().setRounding(value);
            }
        });

        feedbackTV = (TextView) findViewById(R.id.feedbackTV);
        feedbackTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
    }

    public String getCurrentLangage() {
        String[] languages = getResources().getStringArray(R.array.localeNames);
        String[] locales = getResources().getStringArray(R.array.locales);
        String locale = MyApplication.getInstance().getLocale();
        for (int i = 0; i < languages.length; i++) {
            if (locales[i].equals(locale)) {
                return languages[i];
            }
        }
        return "undefined";

    }
}