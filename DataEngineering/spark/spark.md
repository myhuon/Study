##  ๐ก ์์ค ์ฝ๋ ์ปดํ์ผ 
###  ant
**bulid.xml์ด ์กด์ฌํ๋ ํด๋**์์ src ํด๋์ ์ฌ๋ ค์ง ์์ค ํ์ผ์ ant ํ๋ค.

</br>

## ๐ก Spark ์คํ 
### spark-submit --class className [full Path/build/spark-project.jar] inputFileName outputFileName
!! ์ฌ๊ธฐ์ ํ๋ผ๋ฏธํฐ๋ก input output์ ๋ฐ์๋ค๋ฉด, ์ด input, output๋ค์ local ํ์ผ๋ช์ด๋ค.

</br>

## ๐ก ํ๋ก dfs์์ ํ์ผ ์ฌ๋ฆฌ๊ธฐ
### copyFromLocal
1. ํ๋ก ํด๋๋ก ์ด๋
2. hdfs dfs -copyFromLocal [local ํ์ผ๋ช ์ ์ฒด ๊ฒฝ๋ก] [ํ๋ก์ ๋ณต์ฌํ  ํ์ผ๋ช]
