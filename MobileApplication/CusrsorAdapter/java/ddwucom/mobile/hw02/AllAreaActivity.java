package ddwucom.mobile.hw02;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AllAreaActivity extends AppCompatActivity {

    private ListView lvAreas = null;
    private final static String TAG = "remove";
    private final static String UPDATE_CODE = "updateItem";

    private MyCursorAdapter adapter;
    private AreaDBHelper helper;

    private Intent intent;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_area);

        helper = new AreaDBHelper(this);
        //areaList = new ArrayList<AreaDto>();

        lvAreas = (ListView)findViewById(R.id.lvAreas);
        // cursor null로 초기화
        adapter = new MyCursorAdapter(this, R.layout.custom_adpater_list_view, null);
        lvAreas.setAdapter(adapter);

        // 아이템 롱클릭시 아이템 삭제 여부 다이얼로그 띄운다. 확인 -> 삭제
        lvAreas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AllAreaActivity.this);
                builder.setTitle("삭제")
                        .setMessage("삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //삭제
                                SQLiteDatabase db = helper.getWritableDatabase();
                                db.execSQL("DELETE FROM " + AreaDBHelper.TABLE_NAME + " WHERE _id = " + id + ";" );
                                Log.d(TAG, "remove item success");
                                onResume();
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();

                return true;
            }
        });

        // 아이템 클릭시 내용 수정 액티비티로 전환
        lvAreas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(AllAreaActivity.this, UpdateContactActivity.class);
                intent.putExtra(UPDATE_CODE, id);
                startActivity(intent);
            }
        });

        helper.close();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //DB에서 데이터를 읽어와 Adapter에 설정
        SQLiteDatabase db = helper.getReadableDatabase();

        //Cursor cursor = db.query(ContactDBHelper.TABLE_NAME, null, null, null, null, null, null);
        cursor = db.rawQuery("select * from " + AreaDBHelper.TABLE_NAME, null);
        // cursor 바꿔주기
        adapter.changeCursor(cursor);

        helper.close();
    }

    protected void onDestroy() {
        super.onDestroy();
        // cursor 사용 종료 ( 실행 중에 커서 닫으면 db 사용 할 수 없기 때문에 앱이 닫히는 onDestroy()에서 커서 close 해주는 것이다. )
        if(cursor != null) cursor.close();
    }

}