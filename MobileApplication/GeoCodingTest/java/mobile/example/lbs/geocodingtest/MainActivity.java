package mobile.example.lbs.geocodingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AddressResultReceiver addressResultReceiver;
    private LatLngResultReceiver latLngResultReceiver;

    /*UI*/
    EditText etLat;
    EditText etLng;
    EditText etAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etLat = findViewById(R.id.etLat);
        etLng = findViewById(R.id.etLon);
        etAddress = findViewById(R.id.etAddress);

//        IntentService가 생성하는 결과 수신용 ResultReceiver
        addressResultReceiver = new AddressResultReceiver(new Handler()); // 결과가 주소일 때
        latLngResultReceiver = new LatLngResultReceiver(new Handler()); // 결과가 위도, 경도일 때

    }


    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnFindAddress:
                if (etLat.getText().toString().equals("")) etLat.setText(etLat.getHint());
                if (etLng.getText().toString().equals("")) etLng.setText(etLng.getHint());
                startAddressService();
                break;
            case R.id.btnLatLon:
                if (etAddress.getText().toString().equals("")) etAddress.setText(etAddress.getHint());
                startLatLngService();
                break;
            case R.id.btnClear:
                etAddress.setText("");
                etLat.setText("");
                etLng.setText("");
        }
    }


    /* 위도/경도 → 주소 변환 IntentService 실행 */
    private void startAddressService() {
        // 넘어가는 intent class에 extends IntentServie 구현 해야함
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        String latitude = etLat.getText().toString();
        String longitude = etLng.getText().toString();

        // 주소 결과 받는 리시버, 위도, 경도 extra에 추가
        intent.putExtra(Constants.RECEIVER, addressResultReceiver);
        intent.putExtra(Constants.LAT_DATA_EXTRA, Double.valueOf(latitude));
        intent.putExtra(Constants.LNG_DATA_EXTRA, Double.valueOf(longitude));

        // startService임 (startActivity 아님!!)
        startService(intent);
    }



    /* 주소 → 위도/경도 변환 IntentService 실행 */
    private void startLatLngService() {
        String address = etAddress.getText().toString();
        // 넘어가는 intent에 extends IntentService 구현 해야함
        Intent intent = new Intent(this, FetchLatLngIntentService.class);

        // 리시버, 주소 extra에 추가
        intent.putExtra(Constants.RECEIVER, latLngResultReceiver);
        intent.putExtra(Constants.ADDRESS_DATA_EXTRA, address);

        // startService() 실행
        startService(intent);
    }



    /* 위도/경도 → 주소 변환 ResultReceiver */
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            String addressOutput = null;

            if (resultCode == Constants.SUCCESS_RESULT) {
                if (resultData == null) return;
                addressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
                if (addressOutput == null) addressOutput = "";
                etAddress.setText(addressOutput);
            }
        }
    }


    /* 주소 → 위도/경도 변환 ResultReceiver */
    class LatLngResultReceiver extends ResultReceiver {
        public LatLngResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            String lat;
            String lng;
            ArrayList<LatLng> latLngList = null;

            if (resultCode == Constants.SUCCESS_RESULT) {
                if (resultData == null) return;
                latLngList = (ArrayList<LatLng>) resultData.getSerializable(Constants.RESULT_DATA_KEY);
                if (latLngList == null) {
                    lat = (String) etLat.getHint();
                    lng = (String) etLng.getHint();
                } else {
                    LatLng latlng = latLngList.get(0);
                    lat = String.valueOf(latlng.latitude);
                    lng = String.valueOf(latlng.longitude);
                }

                etLat.setText(lat);
                etLng.setText(lng);
            } else {
                etLat.setText(getString(R.string.no_address_found));
                etLng.setText(getString(R.string.no_address_found));
            }
        }
    }


}
