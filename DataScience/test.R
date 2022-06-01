sales <- data.frame(fruit = c("사과", "딸기", "수박"),
                    price = c(1800, 1500, 3000),
                    volume = c(24, 38, 13))
sales
mean(sales$price)            
mean(sales$volume)

df_csv_exam <- read.csv("csv_exam.csv")
df_csv_exam

install.packages("readxl")
library(readxl)
                 