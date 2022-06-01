install.packages("dplyr")
install.packages("ggplot2")

library(dplyr)
library(ggplot2)

exam <- read.csv("csv_exam.csv")  # 데이터 불러오기
exam
exam[c(3, 8, 15), "math"] <- NA             # 3, 8, 15행의 math에 NA 할당
exam


exam %>% summarise(mean_math = mean(math))             # 평균 산출
exam %>% summarise(mean_math = mean(math, na.rm = T))  # 결측치 제외하고 평균 산출

mean(exam$math, na.rm = T)  # 결측치 제외하고 math 평균 산출

exam$math <- ifelse(is.na(exam$math), 55, exam$math) # math가 NA면 55로 대체
exam
table(is.na(exam$math))                               # 결측치 빈도표 생성
mpg <- as.data.frame(ggplot2::mpg)           # mpg 데이터 불러오기
View(mpg)
mpg[c(65, 124, 131, 153, 212), "hwy"] <- NA  # NA 할당하기


#Q1.`drv`(구동방식)별로 `hwy`(고속도로 연비) 평균이 어떻게 다른지 알아보려고 합니다. 분석을 하기 전에 우선 두 변수에 결측치가 있는지 확인해야 합니다. `drv` 변수와 `hwy` 변수에 결측치가 몇 개 있는지 알아보세요.

table(is.na(mpg$drv))  # drv 결측치 빈도표 출력
table(is.na(mpg$hwy))  # hwy 결측치 빈도표 출력

#Q2. `filter()`를 이용해 `hwy` 변수의 결측치를 제외하고, 어떤 구동방식의 `hwy` 평균이 높은지 알아보세요. 하나의 `dplyr` 구문으로 만들어야 합니다.

mpg %>%
  filter(!is.na(hwy)) %>%          # 결측치 제외
  group_by(drv) %>%                # drv별 분리
  summarise(mean_hwy = mean(hwy))  # hwy 평균 구하기

mpg <- as.data.frame(ggplot2::mpg)
boxplot(mpg$hwy)

boxplot(mpg$hwy)$stats  # 상자그림 통계치 출력

mpg$hwy <- ifelse(mpg$hwy < 12 | mpg$hwy > 37, NA, mpg$hwy)
table(is.na(mpg$hwy))

mpg %>%
  group_by(drv) %>%
  summarise(mean_hwy = mean(hwy, na.rm = T))

mpg <- as.data.frame(ggplot2::mpg)                  # mpg 데이터 불러오기
mpg[c(10, 14, 58, 93), "drv"] <- "k"                # drv 이상치 할당
mpg[c(29, 43, 129, 203), "cty"] <- c(3, 4, 39, 42)  # cty 이상치 할당

# 이상치 확인
table(mpg$drv)

# drv가 4, f, r이면 기존 값 유지, 그 외 NA할당
mpg$drv <- ifelse(mpg$drv %in% c("4", "f", "r"), mpg$drv, NA)

# 이상치 확인
table(mpg$drv)

boxplot(mpg$cty)$stats


# 9~26 벗어나면 NA 할당
mpg$cty <- ifelse(mpg$cty < 9 | mpg$cty > 26, NA, mpg$cty)

# 상자 그림 생성
boxplot(mpg$cty)

mpg %>%
  filter(!is.na(drv) & !is.na(cty)) %>%  # 결측치 제외
  group_by(drv) %>%                      # drv별 분리
  summarise(mean_hwy = mean(cty))        # cty 평균 구하기
