package thedragonspb.complexcalc;

import java.util.List;

import thedragonspb.complexcalc.calc.ComplexNumber;

/**
 * Created by thedragonspb on 25.06.17.
 */

public class Data {
    private static final Data ourInstance = new Data();

    public static Data getInstance() {
        return ourInstance;
    }

    ComplexNumber buffer = null;
    ComplexNumber toSave = null;
    List<ComplexNumber> savedNumbers;

    int rounding;

    private Data() {
    }

    public void setBuffer(ComplexNumber buffer) {
        this.buffer = new ComplexNumber(buffer);
    }

    public ComplexNumber getToSave() {
        return toSave;
    }

    public void setToSave(ComplexNumber toSave) {
        this.toSave = new ComplexNumber(toSave);
    }

    public List<ComplexNumber> getSavedNumbers() {
        return savedNumbers;
    }

    public ComplexNumber getBuffer() {
        return buffer;
    }

    public void setSavedNumbers(List<ComplexNumber> savedNumbers) {
        this.savedNumbers = savedNumbers;
    }

    public int getRounding() {
        return rounding;
    }

    public void setRounding(int rounding) {
        this.rounding = rounding;
    }

    public void saveNumber(ComplexNumber complexNumber) {
        savedNumbers.add(complexNumber);
        MyApplication.getInstance().getDBConnection().addNumber(complexNumber);
    }

    public void deleteNumber(ComplexNumber complexNumber) {
        MyApplication.getInstance().getDBConnection().deleteNumber(complexNumber);
        savedNumbers.remove(complexNumber);
    }
}
