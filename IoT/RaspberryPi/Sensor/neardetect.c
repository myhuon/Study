#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <stdlib.h>
#include <wiringPi.h>


// Use GPIO Pin 3, which is BCM Pin 22

#define COLLISION 3  // J14 connect


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
	
	pinMode(COLLISION, INPUT);

	// display counter value every second.
	while ( 1 ) 
	{
		//printf( "%d\n", eventCounter );
		//eventCounter = 0;
		
		if(digitalRead(COLLISION) == 0)
			printf("Carefull~~~~ oops \n");
		if(digitalRead(COLLISION) == 1)
			printf("Not Collioson... \n");		
	
		delay( 200 ); // wait 1 second
	}

	return 0;
}
