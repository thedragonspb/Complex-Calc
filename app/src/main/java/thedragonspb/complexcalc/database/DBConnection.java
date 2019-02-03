package thedragonspb.complexcalc.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;

import thedragonspb.complexcalc.R;
import thedragonspb.complexcalc.calc.ComplexNumber;
import thedragonspb.complexcalc.calc.PolarForm;


/**
 * Created by Ivan on 04.08.2016.
 */
public class DBConnection {

    private SQLiteDatabase db;//взаимодействие с БД
    private final MySQLOpenHelper dbHelper;//создание БД
    private Cursor cursor;
    private final Context context;

    public DBConnection(Context context) {
        this.context = context;
        dbHelper = new MySQLOpenHelper(context);
    }

    public void deleteNumber(ComplexNumber complexNumber) {
        int id = complexNumber.getId();
        db = dbHelper.getWritableDatabase();
        String sqlQuery = "delete from " + dbHelper.TABLE_NAME + " WHERE " + dbHelper.ID + " = " + id + ";";
        Log.d("DELETE_QUERY", sqlQuery);
        db.execSQL(sqlQuery);
    }

    public void addNumber(ComplexNumber number) {
        db = dbHelper.getWritableDatabase();
        String sqlQuery = "INSERT INTO " + dbHelper.TABLE_NAME + " (" + dbHelper.NAME + ", " + dbHelper.POLAR_FP + ", " + dbHelper.POLAR_SP + ") " +
                "VALUES ( '" + number.getName() + "', " + number.getPolarForm().getX() + ", " + number.getPolarForm().getY() + ");";
        db.execSQL(sqlQuery);
        Cursor cursor = db.query(dbHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToLast()) {
            int idColumnInd = cursor.getColumnIndex(dbHelper.ID);
            number.setId(cursor.getInt(idColumnInd));
        }
    }

    public ArrayList<ComplexNumber> getCurrency()
    {
        ArrayList<ComplexNumber> numbers = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(dbHelper.TABLE_NAME, null, null, null, null, null, null);
        int id;
        String name;
        double polarFP;
        double polarSP;

        if(cursor.moveToFirst())
        {
            int idIndex = cursor.getColumnIndex(dbHelper.ID);
            int nameIndex = cursor.getColumnIndex(dbHelper.NAME);
            int fpIndex = cursor.getColumnIndex(dbHelper.POLAR_FP);
            int spIndex = cursor.getColumnIndex(dbHelper.POLAR_SP);

            do {
                id = cursor.getInt(idIndex);
                name = cursor.getString(nameIndex);
                polarFP = cursor.getDouble(fpIndex);
                polarSP = cursor.getDouble(spIndex);
                ComplexNumber number = new ComplexNumber(new PolarForm(new BigDecimal(polarFP), new BigDecimal(polarSP)));
                number.setName(name);
                number.setId(id);
                numbers.add(number);
            } while (cursor.moveToNext());

        } else {
        }

        cursor.close();
        return numbers;
    }

}
