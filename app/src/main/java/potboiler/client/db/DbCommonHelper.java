package potboiler.client.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbCommonHelper extends SQLiteOpenHelper {
	private Context mContext;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "potboiler.db";

    public static final String USERS_TABLE_NAME = "users";
    private static final String USERS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + USERS_TABLE_NAME + " ("
                    + " _id integer primary key AUTOINCREMENT NOT NULL, "
                    + " place TEXT, "
                    + " password TEXT,"
                    + " phone TEXT);";

	public static final String VOTERS_TABLE_NAME = "voters";
	private static final String VOTERS_TABLE_CREATE =
			"CREATE TABLE IF NOT EXISTS " + VOTERS_TABLE_NAME + " ("
					+ " _id integer primary key AUTOINCREMENT NOT NULL, "
                    + " owner_id integer default 0, "
                    + " server_id integer default 0, "
					+ " name TEXT, "
					+ " address TEXT,"
                    + " name_lower TEXT, "
                    + " address_lower TEXT,"
                    + " qrcode TEXT,"
                    + " status  integer default 0,"
                    + " lat real default 0,"
                    + " lon real default 0,"
                    + " from_qrcode integer default 0,"
                    + " location_fallback integer default 0,"
                    + " date integer default 0, "
                    + " unique(owner_id, server_id) ON CONFLICT IGNORE);";
    
    private static SQLiteDatabase mDb;
    private static SQLiteDatabase mDbReadable;

    public DbCommonHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mContext = context;
    }
    public void onCreate(SQLiteDatabase db) { 
        db.execSQL(VOTERS_TABLE_CREATE);
        db.execSQL(USERS_TABLE_CREATE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
        db.execSQL("drop table  IF EXISTS " + VOTERS_TABLE_NAME);
        db.execSQL("drop table  IF EXISTS " + USERS_TABLE_NAME);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    
    public SQLiteDatabase open(){
    	if(mDb==null)
    		mDb = getWritableDatabase();
    	return mDb; 
    }

    public SQLiteDatabase openReadable(){
        if(mDbReadable==null)
            mDbReadable = getReadableDatabase();
        return mDbReadable;
    }

    public void close(){

    }
}
