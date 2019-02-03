package thedragonspb.complexcalc.calc;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * Created by thedragonspb on 30.06.17.
 */
public class ExpForm extends ComplexNumberForm {

    public ExpForm(BigDecimal x, BigDecimal y) {
        super(x, y);
    }

    public String toString(int accuracy) {
        X = X.setScale(accuracy, RoundingMode.HALF_UP);
        Y = Y.setScale(accuracy, BigDecimal.ROUND_HALF_UP);
        return X.toString() + " ∠" + Y.toString() + "°";
    }
}
