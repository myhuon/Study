#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <stdlib.h>
#include <wiringPi.h>


// Use GPIO Pin 0, which is BCM Pin 17

#define LIGHTSEN_OUT 0  //  J12 connect
#define LIGHTSEN_OUT2 3

//int alert_flag = 0;
void myInterrupt(void)
{
	printf( "interrupted! \n" );
	//alert_flag = 1;
	
	delay(4000);
}

void myInterrupt2(void)
{
	printf( "interrupted2! \n" );
	//alert_flag = 1;
	
	delay(4000);
}
// -------------------------------------------------------------------------
// main
int main(void) 
{
	// sets up the wiringPi library
	if (wiringPiSetup () < 0) 
	{
		fprintf (stderr, "Unable to setup wiringPi: %s\n", strerror (errno));
		return 1;
	}
	
	pinMode(LIGHTSEN_OUT, INPUT);

	if (wiringPiISR(LIGHTSEN_OUT, INT_EDGE_RISING, &myInterrupt) < 0)
	{
		fprintf (stderr, "Unable to wiringPiISR wiringPi: %s\n", strerror (errno));
		return 1;
	}

	if (wiringPiISR(LIGHTSEN_OUT2, INT_EDGE_RISING, &myInterrupt2) < 0)
	{
		fprintf (stderr, "Unable to wiringPiISR wiringPi: %s\n", strerror (errno));
		return 1;
	}

	// display counter value every second.
	while ( 1 ) 
	{
		//printf( "%d\n", alert_flag );
		//eventCounter = 0;

		//delay( 2000 ); // wait 1 second
		delay(4000);
	}
	
	return 0;
}
