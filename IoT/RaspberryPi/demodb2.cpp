#include <stdio.h>
#include <string.h>
#include <errno.h>

#include <wiringPi.h>
#include <wiringPiSPI.h>

// #include <mysql/mysql.h>
#include <mariadb/mysql.h>

#include <stdlib.h>
#include <stdint.h>
#include <sys/types.h>
#include <unistd.h>

#include <time.h>
#include <math.h>

#define SPI_CHANNEL 0	// There are two soft channels in wPi library, which depend on CE0 or CE1  
#define SPI_SPEED 1000000 //1Mhz

// #define CS_MCP3208  8        // BCM_GPIO_8
#define COLLISION 3  // J14 connect - nearDetect

int get_sounddetect_sensor();
int get_neardetect_sensor();

#define DBHOST "localhost"
#define DBUSER "root"
#define DBPASS "root"
#define DBNAME "lab17"

MYSQL *connector;
MYSQL_RES *result;
MYSQL_ROW row;

int read_mcp3208_adc(unsigned char adcChannel)
{
  unsigned char buff[3];
  int adcValue = 0;

  buff[0] = 0x06 | ((adcChannel & 0x07) >> 2);
  buff[1] = ((adcChannel & 0x07) << 6);
  buff[2] = 0x00;

//  digitalWrite(CS_MCP3208, 0);  // Low : CS Active

  wiringPiSPIDataRW(SPI_CHANNEL, buff, 3);

  buff[1] = 0x0F & buff[1];
  adcValue = ( buff[1] << 8) | buff[2];

//  digitalWrite(CS_MCP3208, 1);  // High : CS Inactive

  return adcValue;
}

int main (void)
{
  int adcValue[2] = {0};

  if (wiringPiSetup() == -1)  exit(EXIT_FAILURE) ;
	
  if (setuid(getuid()) < 0)
  {
    perror("Dropping privileges failed\n");
    exit(EXIT_FAILURE);
  }

  if(wiringPiSPISetup(SPI_CHANNEL, SPI_SPEED) == -1)
  {
    fprintf (stdout, "wiringPiSPISetup Failed: %s\n", strerror(errno));
    return 1 ;
  }

   pinMode(COLLISION, INPUT);   // 근접센서 값 읽어와야 하니까 input mode로 설정

  // MySQL connection
  connector = mysql_init(NULL);
  if (!mysql_real_connect(connector, DBHOST, DBUSER, DBPASS, DBNAME, 3306, NULL, 0))  // mysql db 연결
  { 
    fprintf(stderr, "%s\n", mysql_error(connector));
    return 0;
  }

  printf("MySQL(rpidb) opened.\n");
  
  while(1)
  {
      char query[1024];
      adcValue[0] = get_neardetect_sensor();
      adcValue[1] = get_sounddetect_sensor();

      sprintf(query,"insert into thl values (now(),%d,%d)", adcValue[0], adcValue[1]);

      if(mysql_query(connector, query)) // mysql query 전송
      {
        fprintf(stderr, "%s\n", mysql_error(connector));
        printf("Write DB error\n");
      }

      delay(3000); //1sec delay
  }

  mysql_close(connector); // mysql db close

  return 0;
}

int get_neardetect_sensor()	// 근접 센서 Digital read
{
    int result = -1;

	  // display counter value every second.
  	if(digitalRead(COLLISION) == 0){
      		result = 0;
    }
	  else if(digitalRead(COLLISION) == 1){
      		result = 1;
    }

    printf("neardetect: %d\n", result);
    return result;
}

int get_sounddetect_sensor()  // 사운드 센서 Analog read 
{
  	unsigned char adcChannel_sound = 0;   // J3 connect
	  int adcValue_sound = 0;

	  printf("soundDetect start");
	
	  adcValue_sound = read_mcp3208_adc(adcChannel_sound);
	  printf("sound = %u\n", adcValue_sound);
		
	  return adcValue_sound;
}

int wiringPicheck(void)
{
	if (wiringPiSetup () == -1)
	{
		fprintf(stdout, "Unable to start wiringPi: %s\n", strerror(errno));
		return 1 ;
	}
  	return 0;
}

