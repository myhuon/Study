package mobile.example.dbtest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactDBHelper extends SQLiteOpenHelper {

	private final String TAG = "ContactDBHelper";

	private final static String DB_NAME = "contact_db"; 
	public final static String TABLE_NAME = "contact_table";
	public final static String COL_NAME = "name";
	public final static String COL_PHONE = "phone";
	public final static String COL_CAT = "category";

	// db 파일명, 버전 설정
	public ContactDBHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	// db 테이블 생성
	@Override
	public void onCreate(SQLiteDatabase db) {
		String creatSql = "create table " + TABLE_NAME + " (_id integer primary key autoincrement, "
						+ COL_NAME + " TEXT, " + COL_PHONE + " TEXT, " + COL_CAT + " TEXT);";
		Log.d(TAG, creatSql);
		db.execSQL(creatSql);
	}

	// db 업그레이드 시 자동 호출 ( 버전 달라지면 )
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
}


