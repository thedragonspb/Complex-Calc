package thedragonspb.complexcalc.calc;

import java.math.BigDecimal;

/**
 * Created by thedragonspb on 30.06.17.
 */

public abstract class ComplexNumberForm {

    protected BigDecimal X;
    protected BigDecimal Y;

    public ComplexNumberForm(BigDecimal x, BigDecimal y) {
        X = x;
        Y = y;
    }

    public BigDecimal getX() {
        return X;
    }

    public void setX(BigDecimal x) {
        X = x;
    }

    public BigDecimal getY() {
        return Y;
    }

    public void setY(BigDecimal y) {
        Y = y;
    }
}