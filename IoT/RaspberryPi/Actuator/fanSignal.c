/*
	1. 근접 센서 발생 시 (입력=1)
		(1) 인터럽트 함수 실행, FAN 동작, ctrl+c 무시
		(2) 인터럽트 함수 실행 6초 뒤에 FAN 정지, ctrl+c 입력 시 정상 종료
		
	2. 근접 센서 발생하지 않는 경우 (입력=0)
		(1) 3초마다 "monitoring..." 메세지 출력, ctrl+c 입력 시 정상 종료
*/
#include <signal.h> //Signal 사용 헤더파일
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>
#include <stdlib.h> //exit() 사용 헤더파일

#include <wiringPi.h>
#include <softPwm.h>

#define FAN  22	//GPIO 22 - Motor
#define LIGHTSEN_OUT2 3		// 근접 센서

void (*old_handler)();

void myISR(); // SIGINT 핸들러 함수
void myARM();

int main (void)
{
	if (wiringPiSetup () == -1)
	{
		fprintf(stdout, "Unable to start wiringPi: %s\n", strerror(errno));
		exit(1) ;
	}
		
    	pinMode(FAN, OUTPUT);
	pinMode(LIGHTSEN_OUT2, INPUT);

    	if( wiringPiISR(LIGHTSEN_OUT2, INT_EDGE_RISING, myISR ) < 0 ){
    	    printf("ERROR!\n");
     	    return 1;
  	  }

	int delaytime = 3000;
	while(1)
	{	
		printf("monitoring.. \n");	// 인터럽트 발생하지 않으면 print됨.
		delay(delaytime);	
	}	
  
 	 return 0 ;
}

void myARM()
{
	printf("process func2\n");
    	signal(SIGINT, old_handler);	// ctrl+c 키보드 입력 시 실행되는 기본 함수 적용 (= signal(SIGINT_SIG_DFL))
	digitalWrite(FAN, 0);	// FAN 중지
}

void myISR() 
{
   	printf("process sig_handler\n"); 	
	digitalWrite(FAN, 1);	// FAN 가동

    	old_handler = signal(SIGINT, SIG_IGN);
    	signal(SIGALRM, myARM);
    	alarm(6);
}
