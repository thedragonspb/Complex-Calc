package thedragonspb.complexcalc.calcUI;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;

import thedragonspb.complexcalc.Data;
import thedragonspb.complexcalc.R;
import thedragonspb.complexcalc.calc.ComplexNumber;
import thedragonspb.complexcalc.calc.Calc;
import thedragonspb.complexcalc.calc.ExpForm;
import thedragonspb.complexcalc.calc.PolarForm;

/**
 * Created by thedragonspb on 29.01.17.
 */
public class CalcFragment extends Fragment {

    private Button bPlus, bMinus, bMulti, bDivision;
    private Button bCurr;
    private Button bClr;

    private Button saveBtn, copyBtn;

    private ImageButton paste1, paste2;

    private Switch swLeft;
    private Switch swRight;

    private TextView t1, t2;
    private TextView resultPolar, resultExp;

    private EditText left1, left2;
    private EditText right1, right2;
    private EditText[] editTexts;

    private boolean leftForm  = false;
    private boolean rightForm = false;

    private ComplexNumber left, right;
    private ComplexNumber result;

    private Toast toastDivisionByZero;

    private int rounding = 4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.calc_fragment, container, false);

        toastDivisionByZero = Toast.makeText(getActivity().getApplicationContext(), R.string.divisionByZero, Toast.LENGTH_SHORT);
        toastDivisionByZero.setGravity(Gravity.CENTER, 0, 0);

        bPlus     = (Button) v.findViewById(R.id.bPlus);
        bMinus    = (Button) v.findViewById(R.id.bMinus);
        bMulti    = (Button) v.findViewById(R.id.bMulti);
        bDivision = (Button) v.findViewById(R.id.bDivision);
        bCurr     = bPlus;
        bClr      = (Button) v.findViewById(R.id.bClr);

        swLeft  = (Switch) v.findViewById(R.id.swLeft);
        swRight = (Switch) v.findViewById(R.id.swRight);

        t1 = (TextView) v.findViewById(R.id.t1);
        t2 = (TextView) v.findViewById(R.id.t2);

        resultPolar = (TextView) v.findViewById(R.id.resultPolar);
        resultExp   = (TextView) v.findViewById(R.id.resultExp);

        left1  = (EditText) v.findViewById(R.id.left1);
        left2  = (EditText) v.findViewById(R.id.left2);
        right1 = (EditText) v.findViewById(R.id.right1);
        right2 = (EditText) v.findViewById(R.id.right2);
        editTexts = new EditText[]{left1, left2, right1, right2};

        swLeft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    t1.setText("∠");
                    left1.setHint("A");
                    left2.setHint("φ°");
                    leftForm = true;
                    swLeft.setText("Exp form     ");
                } else {
                    t1.setText("+i ");
                    left1.setHint("a");
                    left2.setHint("b");
                    leftForm = false;
                    swLeft.setText("Polar form   ");
                }
            }
        });

        swRight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    t2.setText("∠");
                    right1.setHint("A");
                    right2.setHint("φ°");
                    rightForm = true;
                    swRight.setText("Exp form     ");
                } else {
                    t2.setText("+i ");
                    right1.setHint("a");
                    right2.setHint("b");
                    rightForm = false;
                    swRight.setText("Polar form   ");
                }
            }
        });

        View.OnClickListener oclBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate(v);
            }
        };

        bPlus.setOnClickListener(oclBtn);
        bMinus.setOnClickListener(oclBtn);
        bMulti.setOnClickListener(oclBtn);
        bDivision.setOnClickListener(oclBtn);

        paste1 = (ImageButton) v.findViewById(R.id.paste1);
        paste2 = (ImageButton) v.findViewById(R.id.paste2);

        paste1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paste(swLeft, left1, left2);
            }
        });
        paste2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               paste(swRight, right1, right2);
            }
        });

        bClr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                left1.getText().clear();
                left2.getText().clear();
                right1.getText().clear();
                right2.getText().clear();
            }
        });

        copyBtn = (Button) v.findViewById(R.id.copyBtn);
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!resultPolar.getText().equals("\0") && result != null) {
                    Data.getInstance().setBuffer(result);
                    Toast.makeText(getActivity(), R.string.copied, Toast.LENGTH_SHORT).show();
                }
            }
        });

        saveBtn = (Button) v.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!resultPolar.getText().equals("\0") && result != null) {
                    new SaveNumberDialog().show(getFragmentManager(), "SAVE NUMBER DIALOG");
                    Data.getInstance().setToSave(result);
                    hideKeyboard();
                }
            }
        });

        return v;
    }

    private void calculate(View v) {
        bCurr.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        prepareData();

        switch (v.getId()) {
            case R.id.bPlus     : plus();       break;
            case R.id.bMinus    : minus();      break;
            case R.id.bMulti    : multiplied(); break;
            case R.id.bDivision : division();   break;
        }

        hideKeyboard();
    }

    private void prepareData() {
        rounding = Data.getInstance().getRounding();

        for (EditText editText : editTexts) {
            if (editText.getText().toString().length() == 0) {
                editText.setText("0");
            }
        }

        if (!leftForm) {
            left = new ComplexNumber(new PolarForm(
                    new BigDecimal(left1.getText().toString()), new BigDecimal(left2.getText().toString())));
        } else {
            left = new ComplexNumber(new ExpForm(
                    new BigDecimal(left1.getText().toString()), new BigDecimal(left2.getText().toString())));
        }

        if (!rightForm) {
            right = new ComplexNumber(new PolarForm(
                    new BigDecimal(right1.getText().toString()), new BigDecimal(right2.getText().toString())));
        } else {
            right = new ComplexNumber(new ExpForm(
                    new BigDecimal(right1.getText().toString()), new BigDecimal(right2.getText().toString())));
        }
    }


    private void paste(Switch sw, EditText et1, EditText et2) {
        ComplexNumber number = Data.getInstance().getBuffer();
        if (number == null)
            return;
        BigDecimal x, y;
        if (!sw.isChecked()) {
            x = number.getPolarForm().getX().setScale(rounding, RoundingMode.HALF_UP);
            y = number.getPolarForm().getY().setScale(rounding, RoundingMode.HALF_UP);
        } else {
            x = number.getExpForm().getX().setScale(rounding, RoundingMode.HALF_UP);
            y = number.getExpForm().getY().setScale(rounding, RoundingMode.HALF_UP);
        }
        et1.setText(x.toString());
        et2.setText(y.toString());
    }

    private void plus() {
        result = Calc.plus(left, right);
        setResult(bPlus);
    }

    private void minus() {
        result = Calc.minus(left, right);
        setResult(bMinus);
    }

    private void multiplied() {
        result = Calc.multiplied(left, right);
        setResult(bMulti);
    }

    private void division() {
        if ((result = Calc.divided(left, right)) != null){
            setResult(bDivision);
        } else {
            resultPolar.setText("");
            resultExp.setText("");
            toastDivisionByZero.show();
        }
    }

    private void setResult(Button curBtn) {
        resultPolar.setText((CharSequence) result.getPolarForm().toString(rounding));
        resultExp.setText((CharSequence) result.getExpForm().toString(rounding));
        bCurr = curBtn;
        bCurr.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }

    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}