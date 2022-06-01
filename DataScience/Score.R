rm(list = ls()); gc()

install.packages("readxl")
library(readxl)
df_exam <- read_excel("C:/Users/user/OneDrive/¹®¼­/csv_hw.xlsx")
df_exam$dept

install.packages("dplyr")
library(dplyr)

math_average <- df_exam %>%
  group_by(df_exam$dept) %>%
  summarise(math_average = mean(df_exam$math))
math_average

science_average <- df_exam %>%
  group_by(df_exam$dept) %>% 
  summarise(science_average = mean(science))
science_average

english_average <- df_exam %>%
  group_by(df_exam$dept) %>%
  summarise(english_average = mean(english))
english_average

art_average <- df_exam %>%
  group_by(df_exam$dept) %>%
  summarise(art_average = mean(art))
art_average

df_exam %>%
  select(math, english, science, art) %>%
  mutate(average = (math + english + science + art) / 4) %>%
  arrange(desc(average)) %>%
  head(10)

View(df_exam)

df_avg <- df_exam %>%
  mutate(average = (math + english + science + art) / 4) %>%
  select(id, average)

df_avg <- left_join(df_avg, df_exam, by = "id")
View(df_avg)

df_test <- df_avg %>%
  mutate(test = ifelse(average >= 80, "pass", "fail")) %>%
  select(id, average, test)
View(df_test)

df_exam %>%
  filter(dept == "Business") %>%
  group_by(grad) %>%
  summarise(english_average = mean(english))

df_exam %>%
  filter(dept == "Library") %>%
  group_by(grad) %>%
  summarise(english_average = mean(english))

df_exam %>%
  filter(dept == "Chemistry") %>%
  group_by(grad) %>%
  summarise(english_average = mean(english))

df_exam %>%
  filter(dept == "Statistics") %>%
  group_by(grad) %>%
  summarise(english_average = mean(english))

df_exam %>%
  filter(dept == "CS") %>%
  group_by(grad) %>%
  summarise(english_average = mean(english))

df_exam %>%
  filter(dept == "Statistics") %>%
  filter(math >= 50 & english >= 50) %>%
  mutate(avg = (math + english) / 2)

df_exam[c(3, 10, 12), "art"] <- NA
df_exam

art_mean <- mean(df_exam$art, na.rm = T)
art_mean
table(is.na(df_exam$art))

df_exam$art <- ifelse(is.na(df_exam$art), art_mean, df_exam$art)
table(is.na(df_exam$art))
df_exam

boxplot(df_exam$math)$stats

df_exam$math <- ifelse(df_exam$math < 3.0 | df_exam$math > 99.0, NA, df_exam$math)
mean(df_exam$math)
