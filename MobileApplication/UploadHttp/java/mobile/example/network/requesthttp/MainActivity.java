package mobile.example.network.requesthttp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

    private final String KEY = "key";
    private final String TARGET_DT = "targetDt";
    private final String ITEM_PER_PAGE = "itemPerPage"; // 결과 row 개수 지정 (default : 10, max : 10)
    private final String ADDRESS = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.xml";

    EditText etDate;
    TextView tvResult;
    URL url;

    HashMap<String, String> hashMap = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDate = findViewById(R.id.etUrl);
        tvResult = findViewById(R.id.tvResult);

        StrictMode.ThreadPolicy pol // 임시 방편 -> Thread/ AsyncTask로 변환 필요
                = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(pol);

        hashMap.put(KEY, "64e86a739c6949e26ad8d5c4fbe9bd22"); // 자신의 키 값값
        hashMap.put(ITEM_PER_PAGE, "5"); // 5개 row로 지정
   }

    public void onClick(View v) throws IOException {

        if (!isOnline()) {
            Toast.makeText(this, "Network is not available!", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (v.getId()) {
            case R.id.btn_request:
                hashMap.put(TARGET_DT,etDate.getText().toString()); // 날짜 설정

                String query = "?"; // 쿼리 설정
                // HashMap에 들어있는 데이터 꺼내서 URL에 이어붙힌다. ( GET 방식에서 사용하는 방법 )
                Iterator<Map.Entry<String, String>> iter = hashMap.entrySet().iterator();
                while(true){
                    Map.Entry<String, String> item = iter.next();
                    query += item.getKey() + "=" + item.getValue(); // 해쉬 맵에 저장된 키와 값 가져오기
                    if(iter.hasNext())
                        query += "&"; // 다음 값이 있으면 &로 연결
                    else
                        break;
                }
                url = new URL(ADDRESS + query);
                tvResult.setText(downloadUrl(url));
                //이미지
                //ivResult.setImageBitmap(downLoadUrl(url));
                break;
        }
    }

    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected()); // 사용 가능한 네트워크 모듈이 있고, 네트워크가 연결되어있다.
    }

    private String downloadUrl(URL url) throws IOException{
        InputStream stream = null;
        HttpURLConnection conn = null;
        String result = null;

        try {
            // GET 방식이니까 URL에 요청 정보 들어있음. 그래서 openConnection() 하고, connect()하면 전송됨.
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(3000);
            conn.setConnectTimeout(3000);
            conn.setRequestMethod("GET"); // default : GET
            conn.setDoInput(true);
            // POST 방식
            // conn.setDoOutput(true);
            // conn.setRequestMethod("POST");
            // conn.setRequestProperty("content-type", "application/x-www-form-urlencoded;charcharset=EUC-KR");


            conn.connect(); // POST 방식일 때 이 부분 대신 전송 값 및 출력스트림 준비 과정 넣으면 됨.
            // 데이터 전송 방식
            // GET : url에 키=값 넣기, POST: 따로 바디에서 아래처럼 데이터 작성
            /*
            * OutputStreamWriter outStream = new OutputStreamWriter(conn.getOutputStream(), "EUC-KR");
            * BufferedWriter writer = new BufferedWriter(outStream);
            * writer.write("키=값");
            * writer.flush();
            * */


            int responseCode = conn.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) { // 정상 처리가 아니라면
                throw new IOException("HTTP error code : " + responseCode);
            }

            stream = conn.getInputStream(); // 서버에서 받아온 결과

            if (stream != null) {
                result = readStream(stream);
                // 이미지
                // result = readImageStream(stream);
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (NetworkOnMainThreadException e){
            e.printStackTrace();
        }finally{
            if(stream != null){
                try{ stream.close(); }
                catch(IOException e) { e.printStackTrace(); }
            }
            if(conn != null) conn.disconnect();
        }
        return result;
    }

    private String readStream(InputStream stream){
        StringBuilder result = new StringBuilder();

        try{
            InputStreamReader inputStreamReader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String readLine = bufferedReader.readLine();
            while(readLine != null){
                result.append(readLine + "\n");
                readLine = bufferedReader.readLine();
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    private Bitmap readImageStream(InputStream stream){
        return BitmapFactory.decodeStream(stream);
    }
}
