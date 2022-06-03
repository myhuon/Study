#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <stdlib.h>
#include <wiringPi.h>


// Use GPIO Pin 2, which is BCM Pin 27

#define MOTION_IN 2  // J13 Connector


// the event counter 
volatile int eventCounter = 0;
unsigned char humandetect = 0;

// -------------------------------------------------------------------------
// myInterrupt:  called every time an event occurs
void myInterrupt(void) {
   eventCounter++;
   humandetect = 1;

}


// -------------------------------------------------------------------------
// main
int main(void) {
  // sets up the wiringPi library
  if (wiringPiSetup () < 0) {
      fprintf (stderr, "Unable to setup wiringPi: %s\n", strerror (errno));
      return 1;
  }

  // set Pin 17/0 generate an interrupt on high-to-low transitions
  // and attach myInterrupt() to the interrupt
  if ( wiringPiISR (MOTION_IN, INT_EDGE_RISING, &myInterrupt) < 0 ) {
      fprintf (stderr, "Unable to setup ISR: %s\n", strerror (errno));
      return 1;
  }

  // display counter value every second.
  while ( 1 ) {
	  if(humandetect == 1)
    	{
		printf("Detect %d\n", eventCounter );
		humandetect = 0;
		while(digitalRead(MOTION_IN))
       		{
            		printf("high\n");
           		delay(250);
        	}
	} else {
		printf(" No detect\n");
	}		
    //eventCounter = 0;
    delay( 500 ); // wait 1 second
  }

  return 0;
}
