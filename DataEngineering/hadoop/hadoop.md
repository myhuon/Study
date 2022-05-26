##  💡 소스 코드 컴파일 
###  ant
**bulid.xml이 존재하는 폴더**에서 src 폴더에 올려진 소스 파일을 ant 한다.

</br>

## 💡 하둡 실행 
### hadoop jar [jar파일 경로] 클래스명 parameters
!! 여기서 파라미터로 input output을 받았다면, 이 input, output들은 hadoop dfs상에 저장된 input 파일명과 저장될 output 파일명이다.

</br>

## 💡 하둡 dfs상에 파일 올리기
### copyFromLocal
1. 하둡 폴더로 이동
2. hdfs dfs -copyFromLocal [local 파일명 전체 경로] [하둡에 복사할 파일명]
