#include <signal.h> //Signal 사용 헤더파일
#include <unistd.h>
#include <stdio.h> 
#include <string.h> 
#include <errno.h>
#include <stdlib.h> //exit() 사용 헤더파일

#include <wiringPi.h>

#define PUMP	21 // BCM_GPIO 5

void sig_handler(int signo); // 마지막 종료 함수

void aaa(){
    printf("aaa!\n");
    exit(0);
}

int main (void)
{

	signal(SIGALRM, (void *)sig_handler);	//시그널 핸들러 함수
    printf("main process start!\n");
	alarm(3);
    printf("alarm end!\n");

while(1){

	}

	return 0 ;
}

void sig_handler(int signo)
{
    printf("ALARM!\n");
	digitalWrite (PUMP, 0) ; // Off
	signal(SIGINT, aaa);
	//exit(0);
} 




