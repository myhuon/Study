#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <errno.h>
#include "iotmakers.h"
#include <stdint.h>
#include <unistd.h>
#include <string.h> 
#include <wiringPi.h>

#define LED1 5 //BCM_GPIO 24
#define TC1 0 //BCM_GPIO 17 - 조도 센서 digital GPIO : 0, analog channel : 1
#define MAXTIMINGS 85
#define DHTPIN 7
#define FAN 22

int dht11_dat[5] = {0,0,0,0};

static int local_loop = (0);

static void SigHandler(int sig)
{
	switch(sig)
	{
		case SIGTERM :
		case SIGINT :
			printf("accept signal SIGINT[%d]\n", sig);
			im_stop_service();
			local_loop = (0);
			break;
		default :
			;
	};
	return;
} 
static void set_SigHandler()
{
	signal(SIGINT,   SigHandler);	
	signal(SIGTERM,  SigHandler);	
}

static void mycb_numdata_handler(char *tagid, double numval)
{
	// !!! USER CODE HERE
	
	
	if(!strcmp(tagid, "Light"))
	{
			if(numval == 1)
			{
				printf("tagid=[%s], val=[%f]\n", tagid, numval);
			//	digitalWrite(LED1, 1); //ON
				digitalWrite (TC1, 1) ; // On
			}
			else if(numval == 0)
			{
				printf("tagid=[%s], val=[%s]\n", tagid, numval);
			//	digitalWrite(LED1, 0); //ON
				digitalWrite (TC1, 0) ; // Off
			}
	}
}

static void mycb_strdata_handler(char *tagid, char *strval)
{
	// LED on-off
	pinMode(LED1, OUTPUT);
	
	if(!strcmp(tagid, "LED"))
	{
			if(!strcmp(strval, "ON"))
			{
				printf("tagid=[%s], val=[%s]\n", tagid, strval);
				digitalWrite(LED1, 1); //ON
			}
			else if(!strcmp(strval, "OFF"))
			{
				printf("tagid=[%s], val=[%s]\n", tagid, strval);
				digitalWrite(LED1, 0); //ON			
			}
	}
}

/* ============================
main_sample1.c

- Sending the collection data
	im_send_numdata();
	im_send_strdata();
============================ */
int main()
{
	int i;
	int rc;
	double val = 0.0;

	set_SigHandler();

	printf("im_init()\n");
	rc = im_init_with_config_file("./config.txt");
	if ( rc < 0  )	{
		printf("fail im_init()\n");
		return -1;
	}

	im_set_loglevel(LOG_LEVEL_DEBUG);
	im_set_numdata_handler(mycb_numdata_handler);
	im_set_strdata_handler(mycb_strdata_handler);

	printf("im_start_service()...\n");
	rc = im_start_service();
	if ( rc < 0  )	{
		printf("fail im_start_service()\n");
		im_release();
		return -1;
	}

	// Raspberry Pi wiringPiSetup
	printf("dht11 Raspberry pi\n");
	if(wiringPiSetup() == -1) exit(1);
	
	local_loop = (1);
	while(local_loop == (1))
	{
		//read read dht11 data/config setup
		uint8_t laststate = HIGH;
		uint8_t counter = 0;
		uint8_t j = 0, i;
		float f;
		
		dht11_dat[0] = dht11_dat[1] = dht11_dat[2] = dht11_dat[3] = dht11_dat[4] = 0;
			
		//pull pin down for 18 millisecond
		pinMode(DHTPIN, OUTPUT);
		digitalWrite(DHTPIN, LOW);
		delay(2000);
		digitalWrite(DHTPIN, HIGH);
		delayMicroseconds(40);
		pinMode(DHTPIN, INPUT);
		
		for(i = 0; i < MAXTIMINGS; i++)
		{
				counter = 0;
				while(digitalRead(DHTPIN) == laststate)
				{
						counter++;
						delayMicroseconds(1);
						if(counter==255) break;
				}
				
				laststate = digitalRead(DHTPIN);
				if(counter==255) break;
				
				if((i>=4) && (i%2==0))
				{
						dht11_dat[j/8] <<= 1;
						if(counter > 16) dht11_dat[j/8] |= 1;
						j++; 
				}
		}
		
		if((j>=40) && (dht11_dat[4]==((dht11_dat[0] + dht11_dat[1] + dht11_dat[2] + dht11_dat[3]) & 0xff)))
		{
			printf("Humidity = %d.%d %% Temperature = %d.%d * C \n", dht11_dat[0], dht11_dat[1], dht11_dat[2], dht11_dat[3]);
			
			double dData2 = (double) dht11_dat[2];
			double dData3 = (double) dht11_dat[3];
			double dOut = dData2 + dData3;
			
			im_send_numdata("Temp", dOut, 0);	
		}
		
		delay(1000);
		
		pinMode(TC1, INPUT);
		if(digitalRead(TC1)==0)
		{	
				im_send_numdata("Light", 0, 0);	
		}
		else
		{
				im_send_numdata("Light", 1, 0);
				
		}
	}

	printf("im_stop_service()\n");
	im_stop_service();

	printf("im_release()\n");
	im_release();

	return 0;
}



