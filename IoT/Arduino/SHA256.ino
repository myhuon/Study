/* KISA SHA256 Implementation for Arduino Platform */

#include "KISA_SHA256.h"

#define   Total_Run         10    // # of test trials
#define   LITTLE_ENDIAN

const UINT SHA256_K[64] = 
{
 0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1,

 0x923f82a4, 0xab1c5ed5, 0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3,

 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174, 0xe49b69c1, 0xefbe4786,

 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,

 0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147,

 0x06ca6351, 0x14292967, 0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13,

 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85, 0xa2bfe8a1, 0xa81a664b,

 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,

 0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a,

 0x5b9cca4f, 0x682e6ff3, 0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208,

 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
};

/**************************          Defined Macro          **************************************/

#define ROTL_ULONG(x, n) ((ULONG)((x) << (n)) | (ULONG)((x) >> (32 - (n))))

#define ROTR_ULONG(x, n) ((ULONG)((x) >> (n)) | (ULONG)((x) << (32 - (n))))



#define ENDIAN_REVERSE_ULONG(dwS)
  ( (ROTL_ULONG((dwS),  8) & 0x00ff00ff) | (ROTL_ULONG((dwS), 24) & 0xff00ff00) )



#define BIG_B2D(B, D)  D = ENDIAN_REVERSE_ULONG(*(ULONG_PTR)(B))

#define BIG_D2B(D, B)
  *(ULONG_PTR)(B) = ENDIAN_REVERSE_ULONG(D)



#define LITTLE_B2D(B, D) D = *(ULONG_PTR)(B)

#define LITTLE_D2B(D, B)
  *(ULONG_PTR)(B) = (ULONG)(D)


#define RR(x, n)
  ROTR_ULONG(x, n)

#define SS(x, n)  (x >> n)



#define Ch(x, y, z)  ((x & y) ^ ((~x) & z))

#define Maj(x, y, z) ((x & y) ^ (x & z) ^ (y & z))

#define Sigma0(x)  (RR(x,  2) ^ RR(x, 13) ^ RR(x, 22))

#define Sigma1(x)  (RR(x,  6) ^ RR(x, 11) ^ RR(x, 25))



#define RHO0(x)   (RR(x,  7) ^ RR(x, 18) ^ SS(x,  3))

#define RHO1(x)   (RR(x, 17) ^ RR(x, 19) ^ SS(x, 10))



#define FF(a, b, c, d, e, f, g, h, j) {          \

  T1 = h + Sigma1(e) + Ch(e, f, g) + SHA256_K[j] + X[j];    \

  d += T1;              \

  h = T1 + Sigma0(a) + Maj(a, b, c);        \

}


#define GetData(x)  ENDIAN_REVERSE_ULONG(x)



/**************************         Defined  Functions         ************************************/

void SHA256_Transform(ULONG_PTR Message, ULONG_PTR ChainVar)

{

 ULONG   a, b, c, d, e, f, g, h, T1, X[64];

 ULONG   j;



 for (j = 0; j < 16; j++)

  X[j] = GetData(Message[j]);



 for (j = 16; j < 64; j++)

  X[j] = RHO1(X[j - 2]) + X[j - 7] + RHO0(X[j - 15]) + X[j - 16];



 a = ChainVar[0];

 b = ChainVar[1];

 c = ChainVar[2];

 d = ChainVar[3];

 e = ChainVar[4];

 f = ChainVar[5];

 g = ChainVar[6];

 h = ChainVar[7];



 for (j = 0; j < 64; j += 8)

 {

  FF(a, b, c, d, e, f, g, h, j + 0);

  FF(h, a, b, c, d, e, f, g, j + 1);

  FF(g, h, a, b, c, d, e, f, j + 2);

  FF(f, g, h, a, b, c, d, e, j + 3);

  FF(e, f, g, h, a, b, c, d, j + 4);

  FF(d, e, f, g, h, a, b, c, j + 5);

  FF(c, d, e, f, g, h, a, b, j + 6);

  FF(b, c, d, e, f, g, h, a, j + 7);

 }



 ChainVar[0] += a;

 ChainVar[1] += b;

 ChainVar[2] += c;

 ChainVar[3] += d;

 ChainVar[4] += e;

 ChainVar[5] += f;

 ChainVar[6] += g;

 ChainVar[7] += h;

}


void SHA256_Init( OUT SHA256_INFO *Info )

{

 Info->uChainVar[0] = 0x6a09e667;

 Info->uChainVar[1] = 0xbb67ae85;

 Info->uChainVar[2] = 0x3c6ef372;

 Info->uChainVar[3] = 0xa54ff53a;

 Info->uChainVar[4] = 0x510e527f;

 Info->uChainVar[5] = 0x9b05688c;

 Info->uChainVar[6] = 0x1f83d9ab;

 Info->uChainVar[7] = 0x5be0cd19;


 Info->uHighLength = Info->uLowLength = 0;

}



void SHA256_Process( OUT SHA256_INFO *Info, IN const BYTE *pszMessage, IN UINT uDataLen )

{

 if ((Info->uLowLength += (uDataLen << 3)) < 0)
  Info->uHighLength++;

 Info->uHighLength += (uDataLen >> 29);

 while (uDataLen >= SHA256_DIGEST_BLOCKLEN)
 {
  memcpy((UCHAR_PTR)Info->szBuffer, pszMessage, (SINT)SHA256_DIGEST_BLOCKLEN);

  SHA256_Transform((ULONG_PTR)Info->szBuffer, (ULONG_PTR)Info->uChainVar);

  pszMessage += SHA256_DIGEST_BLOCKLEN;

  uDataLen -= SHA256_DIGEST_BLOCKLEN;
 }

  memcpy((UCHAR_PTR)Info->szBuffer, pszMessage, uDataLen);
}


void SHA256_Close( OUT SHA256_INFO *Info, IN BYTE *pszDigest )
{
 ULONG i, Index;

 Index = (Info->uLowLength >> 3) % SHA256_DIGEST_BLOCKLEN;
 Info->szBuffer[Index++] = 0x80;

 if (Index > SHA256_DIGEST_BLOCKLEN - 8)
 {
  memset((UCHAR_PTR)Info->szBuffer + Index, 0, (SINT)(SHA256_DIGEST_BLOCKLEN - Index));

  SHA256_Transform((ULONG_PTR)Info->szBuffer, (ULONG_PTR)Info->uChainVar);

  memset((UCHAR_PTR)Info->szBuffer, 0, (SINT)SHA256_DIGEST_BLOCKLEN - 8);
 }
 else
  memset((UCHAR_PTR)Info->szBuffer + Index, 0, (SINT)(SHA256_DIGEST_BLOCKLEN - Index - 8));


#if defined(LITTLE_ENDIAN)

 Info->uLowLength = ENDIAN_REVERSE_ULONG(Info->uLowLength);

 Info->uHighLength = ENDIAN_REVERSE_ULONG(Info->uHighLength);

#endif

  ((ULONG_PTR)Info->szBuffer)[SHA256_DIGEST_BLOCKLEN / 4 - 2] = Info->uHighLength;


  ((ULONG_PTR)Info->szBuffer)[SHA256_DIGEST_BLOCKLEN / 4 - 1] = Info->uLowLength;


  SHA256_Transform((ULONG_PTR)Info->szBuffer, (ULONG_PTR)Info->uChainVar);


 for (i = 0; i < SHA256_DIGEST_VALUELEN; i += 4)
  BIG_D2B((Info->uChainVar)[i / 4], &(pszDigest[i]));
}


void SHA256_Encrpyt( IN const BYTE *pszMessage, IN UINT uPlainTextLen, OUT BYTE *pszDigest )
{
 SHA256_INFO info;


 SHA256_Init( &info );

 SHA256_Process( &info, pszMessage, uPlainTextLen );

 SHA256_Close( &info, pszDigest );
}


/**************************         Arduio Setting          **************************************/

#include <SoftwareSerial.h>

int   loop_num = 0;

byte buffer[1024];

int bufferPosition;

byte data;

// Test Vector HASH Value

u8    t_value[32]={ 0xBA, 0x78, 0x16, 0xBF, 0x8F, 0x01, 0xCF, 0xEA, 0x41, 0x41, 0x40, 0xDE, 0x5D, 0xAE, 0x22, 0x23, 
                    0xB0, 0x03, 0x61, 0xA3, 0x96, 0x17, 0x7A, 0x9C, 0xB4, 0x10, 0xFF, 0x61, 0xF2, 0x00, 0x15, 0xAD};

void  setup() {

  Serial.begin(9600);    // for Debug
  Serial.println(" ");  Serial.println("KISA SHA256 Code Start..");  Serial.println(" ");  

  loop_num = 0;
  bufferPosition = 0;
}

void  loop() {  
      UCHAR   OUT_DST[32] = {0x0,};

      if(Serial.available()){

      while(data != '\n')
     {
            data = Serial.read();

            buffer[bufferPosition++] = data;

            if(data == '\n') break;
        }

      if( loop_num == 0 )    // Display just one time
      {    
            Serial.print("Input Message: ");  Serial.write(buffer, bufferPosition);  

            Serial.print("Input Length: ");  Serial.println( bufferPosition - 1 );

            SHA256_Encrpyt( buffer, bufferPosition - 1, OUT_DST );

            Serial.print("Calculated Digest Value:  ");
    
        for (int i=0; i<32; i++)    
        {   
	Serial.print(OUT_DST[i], HEX);  Serial.print(", ");  
        }   
	Serial.println(" ");
            Serial.print("TestVector Digest Value:  ");

            for (int i=0; i<32; i++) 
            {   
		Serial.print(t_value[i], HEX);  Serial.print(", ");  
	}   

	Serial.println(" ");

            loop_num++;
          }
      }

      delay(100);  // 1 second

}
