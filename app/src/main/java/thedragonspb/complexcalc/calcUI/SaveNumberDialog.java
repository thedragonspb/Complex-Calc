package thedragonspb.complexcalc.calcUI;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import thedragonspb.complexcalc.Data;
import thedragonspb.complexcalc.R;
import thedragonspb.complexcalc.calc.ComplexNumber;

/**
 * Created by thedragonspb on 01.07.17.
 */
public class SaveNumberDialog extends DialogFragment {

    EditText nameField;
    TextInputLayout textInputLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_save_number, null);
        nameField = (EditText) v.findViewById(R.id.save_number_field);
        textInputLayout = (TextInputLayout) v.findViewById(R.id.input_layout);

        builder.setView(v);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (submitForm()) {
                            ComplexNumber number = Data.getInstance().getToSave();
                            number.setName(nameField.getText().toString());
                            Data.getInstance().saveNumber(number);
                            Toast.makeText(getActivity(), R.string.saved, Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                            hideKeyboard();
                        }
                    }
                });
            }
        });

        return dialog;
    }

    private boolean submitForm() {
        if (!validateMsg()) {
            return false;
        }
        return  true;
    }

    private boolean validateMsg() {
        if (nameField.getText().toString().trim().isEmpty()) {
            textInputLayout.setError(getString(R.string.emptyFields));
            requestFocus(nameField);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
