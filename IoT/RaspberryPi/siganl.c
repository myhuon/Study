#include <signal.h> //Signal 사용 헤더파일
#include <unistd.h>
#include <stdio.h> 
#include <string.h> 
#include <errno.h>
#include <stdlib.h> //exit() 사용 헤더파일
#include <wiringPi.h>

void (*old_handler)();
//void sig_handler(int signo); // 마지막 종료 함수

void aaa()
{
    printf("aaa!\n");
    signal(SIGINT, old_handler);
}

int main (void)
{
	old_handler = signal(SIGINT, SIG_IGN);	//시그널 핸들러 함수
    printf("main process start!\n");
    signal(SIGALRM, aaa);
	alarm(3);
    printf("alarm end!\n");

    while(1){

    }

	return 0 ;
}

void sig_handler(int signo)
{
    printf("ALARM!\n");
	
	signal(SIGINT, SIG_IGN);
    signal(SIGALRM, aaa);
   alarm(3);

    // 인터럽트에서 루프 사용 절대 금지!!
} 




