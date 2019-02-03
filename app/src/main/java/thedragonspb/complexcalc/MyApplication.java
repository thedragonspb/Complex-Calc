package thedragonspb.complexcalc;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import thedragonspb.complexcalc.calc.ComplexNumber;
import thedragonspb.complexcalc.calc.PolarForm;
import thedragonspb.complexcalc.database.DBConnection;

/**
 * Created by Ivan on 04.08.2016.
 */
public class MyApplication extends Application {

    private static MyApplication INSTANCE;
    private DBConnection db;
    private String locale;

    Resources res;
    DisplayMetrics dm;
    Configuration conf;
    SharedPreferences settings;

    @Override
    public final void onCreate()
    {
        super.onCreate();
        INSTANCE = this;
        db = new DBConnection(this);

        res  = getResources();
        dm   = res.getDisplayMetrics();
        conf = res.getConfiguration();

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        Locale current = getResources().getConfiguration().locale;
        locale = settings.getString("locale", current.getLanguage());
        setLocale(locale);

        int rounding = settings.getInt("rounding", 2);
        Data.getInstance().setRounding(rounding);

        List<ComplexNumber> savedNumbers = db.getCurrency();
        Data.getInstance().setSavedNumbers(savedNumbers);

    }

    public void setLocale(String locale) {
        this.locale = locale;
        Locale.setDefault(new Locale(locale));
        conf.locale = Locale.getDefault();
        settings.edit().putString("locale", locale).apply();
    }

    public String getLocale() {
        return locale;
    }


    public void setRounding(int rounding) {
        Data.getInstance().setRounding(rounding);
        settings.edit().putInt("rounding", rounding).apply();
    }

    public static MyApplication getInstance()
    {
        return INSTANCE;
    }

    public synchronized DBConnection getDBConnection()
    {
        return db;
    }

}