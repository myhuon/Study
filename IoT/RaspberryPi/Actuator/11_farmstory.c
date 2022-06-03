#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <stdint.h>
#include <wiringPi.h>

#include <sys/types.h>
#include <unistd.h>
#include <time.h>
#include <math.h>

//#include <mysql/mysql.h>
#include <mariadb/mysql.h>
#include <signal.h>

#define LIGHTSEN_OUT 	0  
#define PUMP		21 
#define FAN		22 
#define DCMOTOR 	23 
#define LEDPOWER  	24 

#define MAXTIMINGS	85

int ret_temp, ret_humid;

static int DHTPIN = 11;
static int dht22_dat[5] = {0,0,0,0,0};

void 		wiringPicheck();
void 		Modeset();
void 		sighandler();

void 		get_DHT_sensor();
int 		get_light_sensor();
int 		read_dht22_dat_temp();
static uint8_t 	sizecvt(const int);

#define DBHOST "localhost"
#define DBUSER "root"
#define DBPASS "root"
#define DBNAME "iotfarm"
#define TABLE  "tfarmsensorvalue"

MYSQL *connector;
MYSQL_RES *result;
MYSQL_ROW row;

int main(void)
{
    int light_val, temp_val, humid_val;

    wiringPicheck();
    Modeset();
    signal( SIGINT, (void *)sighandler );

    //iot_connect_to_db(DBNAME);

	//MySQL connection
	connector = mysql_init(NULL);
	if (!mysql_real_connect(connector, DBHOST, DBUSER, DBPASS, DBNAME, 3306, NULL, 0))
	{
		fprintf(stderr, "%s\n", mysql_error(connector));
		return 0;
	}
	printf("MySQL(rpidb) opened.\n");
  
    while(1)
    {
    	char query[1024];

	get_DHT_sensor();
    	temp_val = ret_temp;
    	humid_val = ret_humid;
    	light_val = get_light_sensor(); 

    	sprintf(query,"insert into %s values (null, now(), now(), %d, %d, %d)", TABLE, temp_val, humid_val, light_val);

	//iot_send_query( query );
	
	//MySQL send query
	if(mysql_query(connector, query))
    {
      fprintf(stderr, "%s\n", mysql_error(connector));
      printf("Write DB error\n");
    }


	if( light_val == 0 )
	{
		digitalWrite( LEDPOWER, 1 );
		printf(" Light On.. \n");
	}
	else
	{
		digitalWrite( LEDPOWER, 0 );
		printf(" Light Off.. \n");
	}

	if( temp_val > 26 )
	{
		digitalWrite( PUMP, 1 );
		printf(" Pump On.. \n");
		delay( 2000 );
		digitalWrite( PUMP, 0 );
		printf(" Pump Off.. \n");
	}

	if( humid_val < 50 )
	{
		digitalWrite( FAN, 1 );
		printf(" FAN On.. \n");
		delay( 2000 );
		digitalWrite( FAN, 0 );
		printf(" FAN Off.. \n");
	}

    	delay(1000); 
    }

    //iot_disconnect_from_db();
	//MySQL close
	mysql_close(connector);
    return 0;
}

void wiringPicheck()
{
	if( wiringPiSetup () == -1 )
	{
		fprintf(stdout, "Unable to start wiringPi: %s\n", strerror(errno));
		exit( 0 );
	}
}

void Modeset()
{
	pinMode( LIGHTSEN_OUT, INPUT );

	pinMode( PUMP, OUTPUT );
	pinMode( FAN, OUTPUT );
	pinMode( DCMOTOR, OUTPUT );
	pinMode( LEDPOWER, OUTPUT );
}

void sighandler( )
{
	digitalWrite( PUMP, 0 );
	digitalWrite( FAN, 0 );
	digitalWrite( DCMOTOR, 0 );
	digitalWrite( LEDPOWER, 0 );
	printf(" Program Stop.. \n");
	exit( 0 );
}

int get_light_sensor(void)
{
	if( digitalRead(LIGHTSEN_OUT) )	//day
		return 1;
	else //night
		return 0;
}


void get_DHT_sensor()
{
	int received_temp;

	received_temp = read_dht22_dat_temp();
	while( received_temp == 0 )
	{
		delay(500); 
		received_temp = read_dht22_dat_temp();
	}
}

int read_dht22_dat_temp()
{
  	uint8_t laststate = HIGH;
  	uint8_t counter = 0;
  	uint8_t j = 0, i;

  	dht22_dat[0] = dht22_dat[1] = dht22_dat[2] = dht22_dat[3] = dht22_dat[4] = 0;

  	pinMode(DHTPIN, OUTPUT);
  	digitalWrite(DHTPIN, HIGH);
  	delay(10);
  	digitalWrite(DHTPIN, LOW);
  	delay(18);
  
  	digitalWrite(DHTPIN, HIGH);
  	delayMicroseconds(40); 
  	pinMode(DHTPIN, INPUT);

  	for ( i=0; i< MAXTIMINGS; i++) 
	{
    		counter = 0;
    		while( sizecvt(digitalRead(DHTPIN)) == laststate ) {
      			counter++;
      			delayMicroseconds(1);
      			if (counter == 255) break;
    		}

    		laststate = sizecvt( digitalRead(DHTPIN) );
    		if (counter == 255) break;

    		if ((i >= 4) && (i%2 == 0)) {
      			dht22_dat[j/8] <<= 1;
      			if (counter > 60) 	dht22_dat[j/8] |= 1;
   			j++;
    		}
  	}

  	if( (j >= 40) && (dht22_dat[4] == ((dht22_dat[0] + dht22_dat[1] + dht22_dat[2] + dht22_dat[3]) & 0xFF)) ) 
	{
        	float t, h;
		
        	h = (float)dht22_dat[0] * 256 + (float)dht22_dat[1];
        	h /= 10;
        	t = (float)(dht22_dat[2] & 0x7F)* 256 + (float)dht22_dat[3];
        	t /= 10.0;

        	if ((dht22_dat[2] & 0x80) != 0)  	t *= -1;
		
		ret_humid = (int)h;
		ret_temp = (int)t;
		
    		return ret_temp;
  	}
  	else
  	{
    		printf("Data not good, skip\n");
    		return 0;
  	}
}

static uint8_t sizecvt(const int read)
{
    	if (read > 255 || read < 0)
    	{
    		printf("Invalid data from wiringPi library\n");
    		exit(0);
    	}
    	return (uint8_t)read;
}


