package ddwucom.mobile.hw02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.btnOpenAllArea:
                intent = new Intent(this, AllAreaActivity.class);
                break;
            case R.id.btnAddNewArea:
                intent = new Intent(this, InsertAreaActivity.class);
                break;
        }

        if (intent != null) startActivity(intent);
    }
}