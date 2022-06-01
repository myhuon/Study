package ddwucom.mobile.hw02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class UpdateContactActivity extends AppCompatActivity {
    private Intent intent;
    private long itemId;

    private EditText etName;
    private EditText etPhone;
    private EditText etAddress;

    private AreaDBHelper helper;

    private final static String UPDATE_CODE = "updateItem";
    private final static String TAG = "update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

        helper = new AreaDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        intent = new Intent(this.getIntent());
        itemId = intent.getLongExtra(UPDATE_CODE, 1);

        etName = (EditText)findViewById(R.id.etUpdateName);
        etPhone = (EditText)findViewById(R.id.etUpdatePhone);
        etAddress = (EditText)findViewById(R.id.etUpdateAddress);

        Cursor cursor = db.rawQuery("SELECT _id, " + AreaDBHelper.COL_NAME + ", " + AreaDBHelper.COL_PHONE
                + ", " + AreaDBHelper.COL_ADDRESS + " FROM " + AreaDBHelper.TABLE_NAME + " WHERE _id = " + itemId + ";", null);

        while(cursor.moveToNext()) {
            etName.setText(cursor.getString(cursor.getColumnIndex(AreaDBHelper.COL_NAME)));
            etPhone.setText(cursor.getString(cursor.getColumnIndex(AreaDBHelper.COL_PHONE)));
            etAddress.setText(cursor.getString(cursor.getColumnIndex(AreaDBHelper.COL_ADDRESS)));
        }
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnUpdateSave:
                SQLiteDatabase db = helper.getWritableDatabase();

                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();
                String address = etAddress.getText().toString();

                // UPDATE tableName SET COLNAME = "name", COLPHONE = "phone", ... ; 인 형태임
                db.execSQL("UPDATE " + AreaDBHelper.TABLE_NAME + " SET " + AreaDBHelper.COL_NAME + " = \"" + name + "\", " +
                        AreaDBHelper.COL_PHONE + " = \"" + phone + "\", " + AreaDBHelper.COL_ADDRESS + " = \"" + address +
                        "\" WHERE _id = " + itemId + ";");
                break;
            case R.id.btnUpdateClose:
                break;
        }
        helper.close();
        finish();
    }
}