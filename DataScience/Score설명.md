# 필요 패키지 설치 및 로드
``` R
install.packages("readxl")
library(readxl)

df_exam <- read_excel("C:/Users/user/OneDrive/문서/csv_hw.xlsx")
df_exam$dept

install.packages("dplyr")
library(dplyr)
```

# 1.	csv_hw.csv 데이터를 이용해서 다음 분석 문제를 해결하시오.
## (1) csv_hw 데이터의 dept별 모든 과목의 평균을 구하시오.
``` R
math_average <- df_exam %>%
  group_by(df_exam$dept) %>%
  summarise(math_average = mean(df_exam$math))
math_average
```

 ![image](https://user-images.githubusercontent.com/84562885/171395291-ab89f26b-6b64-4abe-94a7-e94fff0b2a4b.png)

</br>

``` R
science_average <- df_exam %>%
  group_by(df_exam$dept) %>% 
  summarise(science_average = mean(science))
science_average
```
 ![image](https://user-images.githubusercontent.com/84562885/171395318-c9d0c6a8-a36b-481c-b957-39e3cb640222.png)

</br>

``` R
english_average <- df_exam %>%
  group_by(df_exam$dept) %>%
  summarise(english_average = mean(english))
english_average
```
![image](https://user-images.githubusercontent.com/84562885/171395335-0e5eb957-d173-4a6c-9bff-0e5703360990.png)
 
</br>

```R
art_average <- df_exam %>%
  group_by(df_exam$dept) %>%
  summarise(art_average = mean(art))
art_average
``` 
![image](https://user-images.githubusercontent.com/84562885/171395352-a7da45a8-614b-4d36-9ac6-07f502dfc637.png)

</br></br>

## (2) Math, English, science, art 과목의 평균을 구하여 점수가 높은 학생별로 정렬하여 1등부터 10등까지 보이시오. 
``` R
df_exam %>%
  select(math, english, science, art) %>%
  mutate(average = (math + english + science + art) / 4) %>%
  arrange(desc(average)) %>%
head(10)
``` 
![image](https://user-images.githubusercontent.com/84562885/171395418-84165b13-cc7f-46b0-a2f5-8c563d507a0f.png)
 
</br> 

## (3) 학생의 전과목 평균 점수가 80점 이상이면 pass를, avg가 80점 미만이면 fail을 출력하시오. //df_avg와 df_exam을 id로 합쳐서 실제 프레임에 average 열을 만들어 줌
``` R
df_avg <- df_exam %>%
  mutate(average = (math + english + science + art) / 4) %>%
  select(id, average)

df_avg <- left_join(df_avg, df_exam, by = "id")

df_test <- df_avg %>%
  mutate(test = ifelse(average >= 80, "pass", "fail")) %>%
select(id, average, test)
```
![image](https://user-images.githubusercontent.com/84562885/171395442-1d5fdcca-b4f9-42bf-9707-4ecef2d85bf7.png)
![image](https://user-images.githubusercontent.com/84562885/171395462-9116a487-926d-4d06-8e55-4801a3ee76e1.png)

</br>


## (4) 학과별로 자료를 그룹화하고 그 안에서 다시 학년별로 그룹화하여 각 학과별 학년의 영어점수의 평균을 보이시오.
``` R
df_exam %>%
  filter(dept == "Business") %>%
  group_by(grad) %>%
  summarise(english_average = mean(english))
``` 
![image](https://user-images.githubusercontent.com/84562885/171395504-13853abe-661c-458a-ac0b-5a57fde61093.png)

</br>

``` R
df_exam %>%
  filter(dept == "Library") %>%
  group_by(grad) %>%
  summarise(english_average = mean(english))
``` 
 ![image](https://user-images.githubusercontent.com/84562885/171395521-3dc9a4d1-fa66-4544-8333-8cb83dbc8389.png)

</br> 

``` R
df_exam %>%
  filter(dept == "Chemistry") %>%
  group_by(grad) %>%
  summarise(english_average = mean(english))
```
 ![image](https://user-images.githubusercontent.com/84562885/171395546-f9c8f97a-a479-444d-b320-8b1750eaf089.png)

</br>

``` R
df_exam %>%
  filter(dept == "Statistics") %>%
  group_by(grad) %>%
  summarise(english_average = mean(english))
``` 
 ![image](https://user-images.githubusercontent.com/84562885/171395563-ec1b01f8-f99b-4b4c-afa8-f1eae4a28026.png)

 </br>

``` R
df_exam %>%
  filter(dept == "CS") %>%
  group_by(grad) %>%
summarise(english_average = mean(english))
```
![image](https://user-images.githubusercontent.com/84562885/171395577-6e89fd80-078a-459a-903a-9ecc6e8aadf7.png)

</br>

## (5) Statistics학과의 학생들 중 math와 english가 모두 50점 이상인 학생들의 영어 점수 평균과 수학 점수 평균(avg)을 구하시오.
``` R
df_exam %>%
  filter(dept == "Statistics") %>%
  filter(math >= 50 & english >= 50) %>%
  mutate(avg = (math + english) / 2)
``` 
 ![image](https://user-images.githubusercontent.com/84562885/171395599-fb278755-8cfc-426a-b4e3-ca6e8a7fbc9c.png)

</br></br></br>

# 2. csv_hw.csv 데이터를 이용하여 결측치 처리 및 데이터 탐색 단계를 수행하시오.
## (1) 3, 10, 12 행의 art점수에 NA를 할당하시오.
``` R
df_exam[c(3, 10, 12), "art"] <- NA
df_exam
``` 
![image](https://user-images.githubusercontent.com/84562885/171395607-501bd4ec-0bad-46c0-9fa8-3aa1d8aed17c.png)
 
</br> 

## (2) 결측치를 제외하고 art 점수의 평균을 산출하시오.
``` R
art_mean <- mean(df_exam$art, na.rm = T) //제거가 아닌 제외한 후 계산
art_mean        
```
[1] 65.7037 FALSE TRUE 
            27    3

</br>

## (3) ART 점수의 결측치가 있으면 (2)번에서 구한 평균으로 대체하시오.
``` R
df_exam$art <- ifelse(is.na(df_exam$art), art_mean, df_exam$art) //결측치 제거
table(is.na(df_exam$art))
df_exam
``` 
![image](https://user-images.githubusercontent.com/84562885/171395620-d9e4bb78-2c71-4d5f-9822-2bad953268c2.png)
 
 </br>
 
## (4) MATH 점수에 대해 박스플롯을 그리고 이상치의 범위를 구하시오.
``` R
boxplot(df_exam$math)$stats
```  
- X = 이상치 범위
- F(x) = x < 3.0 | x > 99.0

![image](https://user-images.githubusercontent.com/84562885/171395634-2223130f-e4cc-4ae0-8bdb-00e1c50627e8.png)
![image](https://user-images.githubusercontent.com/84562885/171395646-1beb7143-0ce8-4612-a109-6dddea649c60.png)

</br>


## (5) MATH 점수에 이상치가 있다면 NA로 대치하고 MATH 의 평균을 구하시오.
``` R
df_exam$math <- ifelse(df_exam$math < 3.0 | df_exam$math > 99.0, NA, df_exam$math)
mean(df_exam$math)
``` 
[1] 60.3














