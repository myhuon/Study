package mobile.example.dbtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class UpdateContactActivity extends AppCompatActivity {
    private Intent intent;
    private ContactDto item;

    private EditText etName;
    private EditText etPhone;
    private EditText etCategory;

    private ContactDBHelper helper;

    private final static String UPDATE_CODE = "updateItem";
    private final static String TAG = "update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

        helper = new ContactDBHelper(this);

        intent = getIntent();
        item = (ContactDto)intent.getSerializableExtra(UPDATE_CODE);

        etName = (EditText)findViewById(R.id.etUpdateName);
        etPhone = (EditText)findViewById(R.id.etUpdatePhone);
        etCategory = (EditText)findViewById(R.id.etUpdateCategory);

        etName.setText(item.getName());
        etPhone.setText(item.getPhone());
        etCategory.setText(item.getCategory());
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnUpdateSave:
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues row = new ContentValues();

                row.put(ContactDBHelper.COL_NAME, etName.getText().toString());
                row.put(ContactDBHelper.COL_PHONE, etPhone.getText().toString());
                row.put(ContactDBHelper.COL_CAT, etCategory.getText().toString());

                String whereClause = "_id=?";
                String[] whereArgs = new String[]{String.valueOf(item.getId())};

                int result = db.update(ContactDBHelper.TABLE_NAME, row, whereClause, whereArgs);
                if(result > 0){
                    Log.d(TAG, "item update success!!");
                }
                break;
            case R.id.btnUpdateClose:
                break;
        }
        helper.close();
        finish();
    }
}