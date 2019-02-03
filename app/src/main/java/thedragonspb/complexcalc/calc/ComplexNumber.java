package thedragonspb.complexcalc.calc;

import java.math.BigDecimal;

/**
 * Created by thedragonspb on 30.06.17.
 */
public class ComplexNumber {

    private int id = -1;
    private String name;
    private PolarForm polarForm = null;
    private ExpForm expForm     = null;

    public ComplexNumber(PolarForm pf) {
        polarForm = pf;
        expForm   = toExp(pf);
    }

    public ComplexNumber(ExpForm ef) {
        expForm   = ef;
        polarForm = toPolar(ef);
    }

    public ComplexNumber(ComplexNumber cn) {
        polarForm = cn.getPolarForm();
        expForm   = cn.getExpForm();
    }

    public PolarForm getPolarForm() {
        return polarForm;
    }

    public ExpForm getExpForm() {
        return expForm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private static PolarForm toPolar(ExpForm ef) {
        double X = ef.getX().doubleValue(), Y = ef.getY().doubleValue();

        double x = X * Math.cos(Math.toRadians(Y));
        double y = X * Math.sin(Math.toRadians(Y));

        return new PolarForm(new BigDecimal(x),new BigDecimal(y));
    }

    private static ExpForm toExp(PolarForm pf) {
        double X = pf.getX().doubleValue(), Y = pf.getY().doubleValue();

        double x = Math.sqrt(X * X + Y * Y);
        double y = Math.toDegrees(Math.atan(Y/X));
        //Если комплексное число располагается не в 1-й и не 4-й координатной четверти
        if (X < 0) {
            if (Y > 0) {
                y += 180;
            } else if (Y < 0) {
                y -= 180;
            }
        }
        y = Double.isNaN(y) ? 0 : y;

        return new ExpForm(new BigDecimal(x), new BigDecimal(y));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}