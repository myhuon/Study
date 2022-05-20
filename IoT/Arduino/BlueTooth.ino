#include <SoftwareSerial.h>
SoftwareSerial BTSerial(2,3);
 
byte buffer[1024];
int bufferPosition;
int i;
 
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);	// Serial 통신속도 초기화
  BTSerial.begin(9600);	// SoftwareSerial 통신속도 초기화
  Serial.println("ready");
  bufferPosition = 0;	
}
 
void loop() {
 // put your main code here, to run repeatedly:
  if(Serial.available()){
    getchar();	// 문자열 비우기 getchar();
    byte data = Serial.read();		// 화면 출력 내용 읽기
    
    //Serial.write(data);
     if(data != '\n'){		// 문장의 끝이 아니면
      buffer[bufferPosition++] = data;		// 버퍼에 데이터 계속 넣기
    }
 
    if(data=='\n'){		// 문장의 끝이면
        for(i = bufferPosition; i < 15; i++){	
          buffer[i] = 'X';	// buffer[15]까지 X로 채우기
      }
        buffer[15] = '\0';	// 마지막 index에 '\0'을 넣어서 문자열 만들기
        
         BTSerial.write(buffer, i);	// 블루투스로 연결된 SoftwareSerial(핸드폰)에 buffer과 크기 넣기
         BTSerial.write("\n");
         bufferPosition = 0;		// position 다시 0으로 초기화
    }
  }
}
