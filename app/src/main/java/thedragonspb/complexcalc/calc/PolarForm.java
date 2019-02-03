package thedragonspb.complexcalc.calc;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by thedragonspb on 30.06.17.
 */
public class PolarForm extends ComplexNumberForm {

    public PolarForm(BigDecimal x, BigDecimal y) {
        super(x, y);
    }

    public String toString(int accuracy) {
        X = X.setScale(accuracy, RoundingMode.HALF_UP);
        Y = Y.setScale(accuracy, BigDecimal.ROUND_HALF_UP);

        if (Y.doubleValue() < 0) {
            return X.toString() + " - i" + Double.toString(-1*Y.doubleValue());
        }
        return X.toString() + " + i" + Y.toString();
    }
}
