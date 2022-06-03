#include <stdio.h> 
#include <string.h> 
#include <errno.h>
 
#include <wiringPi.h> 
#include <wiringPiSPI.h> 

// If you sue CE0 pin as a SPI CS, then don't need to use a SPI CS explicitly.
// But if you use another pin as a SPI CS, then you need to define SPI CS pin.

// #define CS_MCP3208 10 // Pin 10 in wPi library, which is BCM Pin 8

#define SPI_CHANNEL 0	// There are two soft channels in wPi library, which depend on CE0 or CE1  
#define SPI_SPEED 1000000 //1Mhz

// spi communication with Rpi and get sensor data 

int read_mcp3208_adc(unsigned char adcChannel) 
{
	unsigned char buff[3];
	int adcValue = 0;
	
	buff[0] = 0x06 | ((adcChannel & 0x07) >> 2);
	buff[1] = ((adcChannel & 0x07) << 6);
	buff[2] = 0x00;
	
//	digitalWrite(CS_MCP3208, 0);
	wiringPiSPIDataRW(SPI_CHANNEL, buff, 3);
	
	buff[1] = 0x0f & buff[1];
	adcValue = (buff[1] << 8 ) | buff[2];
	
//	digitalWrite(CS_MCP3208, 1);
	
	return adcValue;
}

int main(void) {

	unsigned char adcChannel_sound = 0;

	int adcValue_sound = 0;

	printf("start");
	
	if(wiringPiSetupGpio() == -1)
	{
		fprintf(stdout, "Unable to start wiringPi :%s\n", strerror(errno));
		return 1;
	}
	
	if(wiringPiSPISetup(SPI_CHANNEL, SPI_SPEED) == -1)
	{
		fprintf(stdout, "wiringPiSPISetup Failed :%s\n", strerror(errno));
		return 1;
	}
	
//	pinMode(CS_MCP3208, OUTPUT);
	
	while(1)
	{
		adcValue_sound = read_mcp3208_adc(adcChannel_sound);
		
		printf("sound = %u\n", adcValue_sound);
		
		delay(100);
	}
	return 0;
}
