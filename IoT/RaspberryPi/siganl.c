/*
	1번째 알람 이전 : ctrl+c default 함수 동작
	1번째 알람 이후, 2번째 알람 이전 : ctrl+c 무시
	2번째 알람 이후 : ctrl+c default 함수로 복귀하여 동작
*/

#include <signal.h> //Signal 사용 헤더파일
#include <stdio.h> 
#include <string.h> 
#include <stdlib.h> //exit() 사용 헤더파일

void (*old_handler)();
void first_alarm();
void second_alarm(int signo); // 마지막 종료 함수

int main (void)
{
    	printf("main process start!\n");
    	
	signal(SIGALRM, first_alarm);
	alarm(3);
    
    	printf("alarm end!\n");

    	while(1){

    	}

	return 0 ;
}

void first_alarm()
{
	printf("first_alarm start!\n");
    	old_handler = signal(SIGINT, SIG_IGN);
	
	signal(SIGALRM, second_alaram);
	alarm(3);
}

void second_alarm()
{
    	printf("second_alarm start!\n");
	signal(SIGINT, old_handler);

    	// 인터럽트에서 루프 사용 절대 금지!!
} 




