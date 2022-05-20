#include <Servo.h>

Servo myservo;  // create servo object to control a servo
int pos = 0;    // variable to store the servo position

void setup() {	// 최초 1회 실행되는 함수 ( 초기화 및 설정 )
  myservo.attach(9);  // attaches the servo on pin 9 to the servo object
  Serial.begin(9600);		// Serial.begin(비트단위 통신속도)
}

void loop() {
  if(Serial.available()){		// Serial.available() > 0 이면 사용 가능 상태
    delay(1000);	// delay(miliseconds);

    String szTemp;
    while(Serial.available() > 0){		// 화면에 입력한 문자열 한 글자 씩 읽어서 (Serial.read()) String에 저장
      char cRead = Serial.read();		// 화면에 입력한 문자 한 글자씩 읽기 Serial.read()
      szTemp += cRead;
    }

    if(szTemp){
      pos = szTemp.toInt();	// toInt()로 형 변환 필요함
    }

    myservo.write(pos);              // tell servo to go to position in variable 'pos'

    variable 'pos'    

	delay(15);  
   position		// 만약 변수 pos이면 delay 해라
  }
}
