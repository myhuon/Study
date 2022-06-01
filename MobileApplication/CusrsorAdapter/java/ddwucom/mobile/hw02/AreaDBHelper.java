package ddwucom.mobile.hw02;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AreaDBHelper extends SQLiteOpenHelper {

    private final String TAG = "AreaDBHelper";

    private final static String DB_NAME = "area_db";
    public final static String TABLE_NAME = "area_table";
    public final static String COL_NAME = "name";
    public final static String COL_PHONE = "phone";
    public final static String COL_ADDRESS = "address";

    // db 파일명, 버전 설정
    public AreaDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    // db 테이블 생성
    @Override
    public void onCreate(SQLiteDatabase db) {
        String creatSql = "create table " + TABLE_NAME + " (_id integer primary key autoincrement, "
                + COL_NAME + " TEXT, " + COL_PHONE + " TEXT, " + COL_ADDRESS + " TEXT);";
        Log.d(TAG, creatSql);
        db.execSQL(creatSql);

        db.execSQL("insert into " + TABLE_NAME + " values (null, '서울 골프장', '010-111-222', '서울');");
        db.execSQL("insert into " + TABLE_NAME + " values (null, '구리 골프장', '010-333-444', '구리');");
        db.execSQL("insert into " + TABLE_NAME + " values (null, '부산 골프장', '010-555-666', '부산');");
    }

    // db 업그레이드 시 자동 호출 ( 버전 달라지면 )
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
