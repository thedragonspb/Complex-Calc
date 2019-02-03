package thedragonspb.complexcalc.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
;


/**
 * Created by Ivan on 04.08.2016.
 */
public class MySQLOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "dbcomplexcalc";
    private static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "numbers";

    public static final String ID       = "id";
    public static final String NAME     = "name";
    public static final String POLAR_FP = "polar_first_param";
    public static final String POLAR_SP = "polar_second_param";


    private final Context context;

    public MySQLOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase  db) {
        String createCurrencyTable = 
                "CREATE TABLE " + "IF NOT EXISTS " + TABLE_NAME + "(" + ID + " integer primary key " +
                "autoincrement, " + NAME + " char (50), " + POLAR_FP + " double, " + POLAR_SP + " double " + ");";
        db.execSQL(createCurrencyTable);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }
}
