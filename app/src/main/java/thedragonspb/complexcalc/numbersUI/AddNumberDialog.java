package thedragonspb.complexcalc.numbersUI;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

import thedragonspb.complexcalc.Data;
import thedragonspb.complexcalc.R;
import thedragonspb.complexcalc.calc.ComplexNumber;
import thedragonspb.complexcalc.calc.ExpForm;
import thedragonspb.complexcalc.calc.PolarForm;

import static android.R.string.cancel;

/**
 * Created by thedragonspb on 25.06.17.
 */
public class AddNumberDialog extends DialogFragment {

    Switch   typeSwitch;
    TextView type;
    EditText firstParam, secondParam;
    EditText nameField;
    TextInputLayout textInputLayout;

    boolean form = false;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_add_number, null);
        builder.setView(v);

        typeSwitch  = (Switch)   v.findViewById(R.id.typeSW);
        type        = (TextView) v.findViewById(R.id.typeTV);
        firstParam  = (EditText) v.findViewById(R.id.firstParam);
        secondParam = (EditText) v.findViewById(R.id.secondParam);
        nameField   = (EditText) v.findViewById(R.id.name_field);
        textInputLayout = (TextInputLayout) v.findViewById(R.id.input_layout);

        typeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type.setText("∠");
                    firstParam.setHint("A");
                    secondParam.setHint("φ°");
                    form = true;
                    typeSwitch.setText("Exp form     ");
                } else {
                    type.setText("+i ");
                    firstParam.setHint("a");
                    secondParam.setHint("b");
                    form = false;
                    typeSwitch.setText("Polar form   ");
                }
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
                            if (firstParam.getText().toString().length() == 0 || secondParam.getText().toString().length() == 0) {
                                Toast.makeText(getActivity(), R.string.emptyFields, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            ComplexNumber number;
                            if (!form) {
                                number = new ComplexNumber(new PolarForm(
                                        new BigDecimal(firstParam.getText().toString()), new BigDecimal(secondParam.getText().toString())));
                            } else {
                                number = new ComplexNumber(new ExpForm(
                                        new BigDecimal(firstParam.getText().toString()), new BigDecimal(secondParam.getText().toString())));
                            }
                            number.setName(nameField.getText().toString());
                            Data.getInstance().saveNumber(number);
                            Toast.makeText(getActivity(), R.string.uploaded, Toast.LENGTH_SHORT).show();
                            dialog.cancel();
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
}