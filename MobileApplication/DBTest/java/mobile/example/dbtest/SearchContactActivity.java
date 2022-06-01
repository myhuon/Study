package mobile.example.dbtest;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchContactActivity extends Activity {

	private TextView tvResult = null;
	private EditText etName;

	private ArrayAdapter<ContactDto> adapter;
	private ContactDBHelper helper;
	private ArrayList<ContactDto> searchResultList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_contact);
		
		helper = new ContactDBHelper(this);
		searchResultList = new ArrayList<ContactDto>();

		etName = (EditText)findViewById(R.id.etSearchName);
		tvResult = (TextView)findViewById(R.id.tvSearchResult);

		//adapter = new ArrayAdapter<ContactDto>(this, android.R.layout.simple_list_item_1, searchResultList);
		//tvResult.setAdapter(adapter);
	}

	public void onClick(View v) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String searchName = etName.getText().toString();

		Cursor cursor = db.rawQuery("select " + ContactDBHelper.COL_PHONE + " from " + ContactDBHelper.TABLE_NAME + " WHERE " +
									ContactDBHelper.COL_NAME + " LIKE '%" + searchName + "%';", null);

		String searchResult = "";
		while(cursor.moveToNext()){
			String name = cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_NAME));
			String phone = cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_PHONE));
			searchResult += name + " : " + phone + "\n";
		}
		tvResult.setText(searchResult);

		cursor.close();
		helper.close();
	}
}
