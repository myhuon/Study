package ddwucom.mobile.hw02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class InsertAreaActivity extends AppCompatActivity {
    AreaDBHelper helper;
    EditText etAddName;
    EditText etAddPhone;
    EditText etAddAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_area);

        //      DBHelper 생성
        helper = new AreaDBHelper(this);

        etAddName = (EditText)findViewById(R.id.etAddName);
        etAddPhone = (EditText)findViewById(R.id.etAddPhone);
        etAddAddress = (EditText)findViewById(R.id.etAddAddress);
    }

    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnAddAreaSave:
                SQLiteDatabase db = helper.getWritableDatabase();

                ContentValues row = new ContentValues();
                row.put(AreaDBHelper.COL_NAME, etAddName.getText().toString());
                row.put(AreaDBHelper.COL_PHONE, etAddPhone.getText().toString());
                row.put(AreaDBHelper.COL_ADDRESS, etAddAddress.getText().toString());

                long result = db.insert(AreaDBHelper.TABLE_NAME, null, row);

                //		db.execSQL("insert into " + ContactDBHelper.TABLE_NAME + " values ( NULL, '"
                //	 				+ etName.getText().toString() + "', '" + etPhone.getText().toString() + "', '"
                //					+ etCategory.getText().toString() + "');");
                helper.close();
                break;
            case R.id.btnAddAreaClose:
                break;
        }
        finish();
    }

}