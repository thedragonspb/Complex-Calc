package thedragonspb.complexcalc.settings;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import thedragonspb.complexcalc.MyApplication;
import thedragonspb.complexcalc.R;

/**
 * Created by thedragonspb on 17.06.17.
 */
public class LanguageDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        TextView title = new TextView(getActivity());
        title.setText(R.string.language);
        title.setTextColor(getResources().getColor(R.color.colorPrimary));
        title.setTextSize(18);
        title.setPadding(30,20,0,0);

        final String[] locales   = getResources().getStringArray(R.array.locales);
        final String currentLocale = MyApplication.getInstance().getLocale();
        int i;
        for (i = 0; i < locales.length; i ++) {
            if (locales[i].equals(currentLocale)) {
                break;
            }
        }
        builder.setCustomTitle(title).setSingleChoiceItems(R.array.localeNames, i, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!locales[i].equals(currentLocale)) {
                    MyApplication.getInstance().setLocale(locales[i]);
                }
                dialogInterface.cancel();
            }
        });

        return builder.create();
    }
}