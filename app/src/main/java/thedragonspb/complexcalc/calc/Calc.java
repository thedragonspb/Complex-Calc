package thedragonspb.complexcalc.calc;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by thedragonspb on 03.05.16.
 */
public class Calc {

    static public ComplexNumber plus(ComplexNumber left, ComplexNumber right){

        BigDecimal lX = left.getPolarForm().getX();
        BigDecimal lY = left.getPolarForm().getY();

        BigDecimal rX = right.getPolarForm().getX();
        BigDecimal rY = right.getPolarForm().getY();

        return new ComplexNumber(new PolarForm(lX.add(rX), lY.add(rY)));
    }

    static public ComplexNumber minus(ComplexNumber left, ComplexNumber right){

        BigDecimal lX = left.getPolarForm().getX();
        BigDecimal lY = left.getPolarForm().getY();

        BigDecimal rX = right.getPolarForm().getX();
        BigDecimal rY = right.getPolarForm().getY();

        return new ComplexNumber(new PolarForm(lX.subtract(rX), lY.subtract(rY)));
    }

    static public ComplexNumber multiplied(ComplexNumber left, ComplexNumber right){
        BigDecimal lX = left.getExpForm().getX();
        BigDecimal lY = left.getExpForm().getY();

        BigDecimal rX = right.getExpForm().getX();
        BigDecimal rY = right.getExpForm().getY();

        return new ComplexNumber(new ExpForm(lX.multiply(rX), lY.add(rY)));
    }

    static public ComplexNumber divided(ComplexNumber left, ComplexNumber right){
        BigDecimal lX = left.getExpForm().getX();
        BigDecimal lY = left.getExpForm().getY();

        BigDecimal rX = right.getExpForm().getX();
        BigDecimal rY = right.getExpForm().getY();

        if (rX.doubleValue() == 0)
            return null;


        return new ComplexNumber(new ExpForm(lX.divide(rX, 10, RoundingMode.HALF_UP), lY.subtract(rY)));
    }
}