package mobile.example.dbtest;

import java.util.ArrayList;

import android.app.Activity;
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

public class AllContactsActivity extends Activity {
	
	private ListView lvContacts = null;
	private final static String TAG = "remove";
    private final static String UPDATE_CODE = "updateItem";

	private ArrayAdapter<ContactDto> adapter;
	private ContactDBHelper helper;
	private ArrayList<ContactDto> contactList;

	private Intent intent;
    private ContactDto rmItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_contacts);

		helper = new ContactDBHelper(this);
		contactList = new ArrayList<ContactDto>();

		lvContacts = (ListView)findViewById(R.id.lvContacts);
		adapter = new ArrayAdapter<ContactDto>(this, android.R.layout.simple_list_item_1, contactList);

		lvContacts.setAdapter(adapter);

		// 아이템 롱클릭시 아이템 삭제 여부 다이얼로그 띄운다. 확인 -> 삭제
		lvContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()  {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				rmItem = new ContactDto();
				rmItem = (ContactDto) adapter.getItem(position);

				AlertDialog.Builder builder = new AlertDialog.Builder(AllContactsActivity.this);
				builder.setTitle("삭제")
						.setMessage(rmItem.getName() + " 님을 삭제하시겠습니까?")
						.setPositiveButton("확인", new DialogInterface.OnClickListener() { // 확인 누르면 error
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//삭제
								SQLiteDatabase db = helper.getWritableDatabase();
								String whereClause = "_id=?";
								String[] whereArgs = new String[]{String.valueOf(rmItem.getId())};

								db.delete(ContactDBHelper.TABLE_NAME, whereClause, whereArgs);

								Log.d(TAG, "remove item success");
							}
						})
						.setNegativeButton("취소", null)
						.show();
				return false;
			}
		});

		// 아이템 클릭시 내용 수정 액티비티로 전환
		lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ContactDto upItem = new ContactDto();
				upItem = (ContactDto)adapter.getItem(position);

				intent = new Intent(AllContactsActivity.this, UpdateContactActivity.class);
				intent.putExtra(UPDATE_CODE, upItem);
				startActivity(intent);
			}
		});

		helper.close();
	}


	@Override
	protected void onResume() {
		super.onResume();
		
		SQLiteDatabase db = helper.getReadableDatabase();

		

		Cursor cursor = db.query(ContactDBHelper.TABLE_NAME, null, null, null, null, null, null);
		//Cursor cursor = db.rawQuery("select * from " + ContactDBHelper.TABLE_NAME, null);


		contactList.clear();

		while(cursor.moveToNext()){
			ContactDto dto = new ContactDto();
			dto.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			dto.setName(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_NAME)));
			dto.setPhone(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_PHONE)));
			dto.setCategory(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_CAT)));
			contactList.add(dto);
		}

		adapter.notifyDataSetChanged();

		cursor.close();
		helper.close();
	}

}




