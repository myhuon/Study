#include <signal.h> //Signal 사용 헤더파일
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>
#include <stdlib.h> //exit() 사용 헤더파일

#include <wiringPi.h>
#include <softPwm.h>

#define FAN  22	//GPIO 22 - Motor
#define LIGHTSEN_OUT2 3

void (*old_handler)();

void myISR(); // SIGINT 핸들러 함수
int wiringPicheck(void);

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
		printf("monitoring.. \n");	
		delay(delaytime);	
	}	
  
  return 0 ;
}


void myARM()
{
	printf("process func2\n");
    signal(SIGINT, old_handler);
	digitalWrite(FAN, 0);
}

void myISR() // ctrl-c 로 종료시 실행되는 함수
{
    printf("process sig_handler\n"); 	
	digitalWrite(FAN, 1);

    old_handler = signal(SIGINT, SIG_IGN);
    signal(SIGALRM, myARM);
    alarm(6);
}