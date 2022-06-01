package ddwu.mobile.week6.basicfiletest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

    final static String IN_FILE_NAME = "infile.txt";
    final static String EXT_FILE_NAME = "extfile.txt";
    final static String CACHE_FILE_NAME = "cachefile.txt";

    EditText etText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etText = findViewById(R.id.etText);
    }

    public void onInClick(View v) { // 내부 저장소
        switch(v.getId()) {
            case R.id.btnInWrite:
                String data = etText.getText().toString();
                FileOutputStream fos = null;
                // 1. 파일 객체 획득 후 stream 접근하기
                File saveFile = new File(getFilesDir(), IN_FILE_NAME);
                try {
                    // 문자열 파일에 기록
                    fos = new FileOutputStream(saveFile);
                    fos.write(data.getBytes());
                    fos.flush();
                    // 비트맵 이미지 파일에 기록, 비트맵 크기가 클 경우 quality 조정
                    // bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                    // 2. stream에 바로 접근하기
                   /* fos = openFileOutput(IN_FILE_NAME, MODE_PRIVATE); // 덮어쓰기, MODE_APPEND : 이어쓰기
                    fos.write(data.getBytes());*/

                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnInRead:
                String path = getFilesDir().getPath() + "/" + IN_FILE_NAME;

                // 문자열일 경우 파일 읽기
                File readFile = new File(path);
                FileReader fileReader = null;
                try {
                    fileReader = new FileReader(readFile);
                    BufferedReader br = new BufferedReader(fileReader);
                    String line = "";
                    while((line = br.readLine()) != null){
                        Log.i(TAG, line);
                    }
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 비트맵일 경우 파일 읽기
                // Bitmap bitmap = BitmapFactory.decodeFile(path);
               break;
            case R.id.btnInDelete:
                File file = new File(getFilesDir() + "/" + IN_FILE_NAME); // .path() 불필요?
                if(file.delete()){ // boolean 타입으로 결과 반환
                    Toast.makeText(this, "내부 파일 삭제 성공!!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void onExtClick(View v) { // 외부 저장소 (manifests에 퍼미션 추가)
        switch (v.getId()) {
            case R.id.btnExtWrite:
                if(isExternalStorageWritable()){ //외부 저장소 사용 가능 상태 확인
                    // 외부 앱 전용 위치에 저장
                    // 루트/mnt/sdcard/Android/data/패키지명/files/pictures/myalbum
                    //File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum");
                    File file = new File("/sdcard/Pictures", "myalbum");
                    if(!file.mkdirs()){
                        Log.d(TAG, "directory not created");
                        Toast.makeText(this, "외부 파일 생성 실패", Toast.LENGTH_SHORT).show();
                    }

                    // Steam 바로 접근 방식으로도 외부 저장소 파일 만들기 가능
                    String data = etText.getText().toString();
                    //File savefile = new File(file.getPath(), EXT_FILE_NAME);
                    File savefile = new File("/sdcard/Pictures", EXT_FILE_NAME);
                    try{
                        FileOutputStream fos = new FileOutputStream(savefile);
                        fos.write(data.getBytes());
                        fos.close();
                    }catch(IOException e){ e.printStackTrace();}
                }
                break;
            case R.id.btnExtRead:
                if(isExternalStorageReadalbe()){
                    // getExternalFilesDir()로 디렉토리 객체 가져온 후, 그 객체의 경로를 얻는 작업
                    String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + "/myalbum/" + EXT_FILE_NAME;
                    // 내부 저장소의 파일 읽기 방법과 동일
                    File readFile = new File(path);
                    try{
                        FileReader fileReader = new FileReader(readFile);
                        BufferedReader br = new BufferedReader(fileReader);
                        String line = "";
                        String result = "";
                        while((line = br.readLine()) != null){
                            result += line;
                        }
                        etText.setText(result);
                        br.close();
                    }catch (IOException e){ e.printStackTrace(); }
                }
                break;
            case R.id.btnExtDelete:
                String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + "/myalbum/" + EXT_FILE_NAME;
                File file = new File(path);
                if(file.delete()){ // boolean 타입으로 결과 반환
                    Toast.makeText(this, "외부 파일 삭제 성공!!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void onCacheClick(View v) { // 캐시
        switch(v.getId()) {
            case R.id.btnCacheWrite:
                String data = etText.getText().toString();
                FileOutputStream fos = null;
                // 1. 파일 객체 획득 후 stream 접근하기
                File saveFile = new File(getCacheDir(), CACHE_FILE_NAME);
                try {
                    // 문자열 파일에 기록
                    fos = new FileOutputStream(saveFile);
                    fos.write(data.getBytes());
                    fos.flush();
                    // 비트맵 이미지 파일에 기록, 비트맵 크기가 클 경우 quality 조정
                    // bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                    // 2. stream에 바로 접근하기
                   /* fos = openFileOutput(CACHE_FILE_NAME, MODE_PRIVATE); // 덮어쓰기, MODE_APPEND : 이어쓰기
                    fos.write(data.getBytes());*/

                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnCacheRead:
                String path = getCacheDir().getPath() + "/" + CACHE_FILE_NAME;

                // 문자열일 경우 파일 읽기
                File readFile = new File(path);
                FileReader fileReader = null;
                try {
                    fileReader = new FileReader(readFile);
                    BufferedReader br = new BufferedReader(fileReader);
                    String line = "";
                    while((line = br.readLine()) != null){
                        Log.i(TAG, line);
                    }
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 비트맵일 경우 파일 읽기
                // Bitmap bitmap = BitmapFactory.decodeFile(path);
                break;
            case R.id.btnCacheDelete:
                File file = new File(getCacheDir().getPath() + "/" + CACHE_FILE_NAME);
                if(file.delete()){ // boolean 타입으로 결과 반환
                    Toast.makeText(this, "내부 파일 삭제 성공!!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    // 외부 저장소 사용 가능 확인
    private boolean isExternalStorageWritable(){ //쓰기 가능 상태
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private boolean isExternalStorageReadalbe(){ // 읽기 전용 상태
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ||
                Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }

}