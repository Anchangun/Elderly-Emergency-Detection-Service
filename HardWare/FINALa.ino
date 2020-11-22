#include<Wire.h>
#include <SoftwareSerial.h>    
int vib =2;
int shock =0;
int shockdata[50];
int i=0;
int j=0;
const int MPU=0x68;//MPU6050 I2C주소
int AcX[50],AcY[50],AcZ[50];
int numx[50],numy[50],numz[50];
int cnt=0;
SoftwareSerial SW_serial(6, 5);   
//심박
volatile int BPM;                   // 심박수 저장
volatile int Signal;                // 심장박동센서에서 측정되는 값 저장
volatile int IBI = 600;             // 심박수 측정 시 사용되는 시간 변수(심장이 몇초마다 뛰는지 측정)
volatile boolean Pulse = false;     // 유저의 심박수가 측정되면 True, 아무것도 측정되지 않으면 False
 //블루투스
 SoftwareSerial BTSerial(2, 3); // SoftwareSerial(RX, TX)
  

int get6050();
void setup() {
 Wire.begin();
 Wire.beginTransmission(MPU);
 Wire.write(0x6B);
 Wire.write(0);//MPU6050 을 동작 대기 모드로 변경
 Wire.endTransmission(true);
 interruptSetup();                 // 센서의 신호를 2ms마다 읽어 심박수로 변환하는 함수
 
 BTSerial.begin(9600);
    Serial.begin(9600);    
    pinMode(2,INPUT);
    delay(1500);                   // Pause 1.5 seconds  
}

void loop() {
    long measurement = Shock_Judge();
     shockdata[j] = measurement;
      get6050();//센서값 갱신


    if(shockdata[j-2]>5000&&shockdata[j-1]==0&&shockdata[j]==0)
    {
    shock = 1;  
    }
    else 
       shock = 0;

     if(cnt == 0 && shock == 1)
  {
    
    Serial.println("@@@warning@@@");
  }
  if(BPM>60 && BPM<140)
  {
 BTSerial.print("BPM:");
 BTSerial.println(BPM);

 Serial.print("BPM:");
 Serial.println(BPM); 
 }

 
 Serial.print("measurement:");
 Serial.println(measurement);

    j++;
}
long Shock_Judge() //충격측정판단함수
{
  delay(1000);
  long measurement=pulseIn (vib, HIGH); //충격측정값 변수


  return measurement;
}
int get6050(){

  Wire.beginTransmission(MPU);//MPU6050 호출
  Wire.write(0x3B);//AcX 레지스터 위치 요청
  Wire.endTransmission(false);
  Wire.requestFrom(MPU,14,true);//14byte의 데이터를 요청
  AcX[i]=Wire.read()<<8|Wire.read();//두개의 나뉘어진 바이트를 하나로 이어붙입니다.
  AcY[i]=Wire.read()<<8|Wire.read();
  AcZ[i]=Wire.read()<<8|Wire.read();
  numx[i]= AcX[i];
  numy[i]= AcY[i];
  numz[i]= AcZ[i];
  if((abs(abs(numx[i])-abs(numx[i-1])) <=1000 && abs(abs(numx[i]) - abs(numx[i -2])) <=1000 )
    && (abs(abs(numy[i]) - abs(numy[i -1])) <=1000 && abs(abs(numy[i]) - abs(numy[i -2])) <=1000)
    &&(abs(abs(numz[i]) - abs(numz[i -1])) <=1000 && abs(abs(numz[i]) - abs(numz[i -2])) <=1000))
    {
      cnt =1;// 0 이면 가만히
     }
    else
       cnt=0; // 1 이면 움직임
   
 i++;

return cnt;
}


