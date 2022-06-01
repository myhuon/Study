mpg <- as.data.frame(ggplot2::mpg)
mpg[c(10, 14, 50, 93), "drv"] <- "k"
mpg[c(29, 43, 129, 203) ,"cty"] <-c(3, 4, 359, 42)
table(is.na(mpg$drv))
drv_nomiss <- mpg$drv %in% NA
table(drv_nomiss)

boxplot(mpg$cty)$stats
mpg$cty <- ifelse(mpg$cty < 9 | mpg$cty > 26, NA, mpg$cty)
boxplot(mpg$cty)$stats

library(dplyr)
mpg %>% group_by(drv) %>% summarise(mean_cty = mean(mpg$cty, na.rm = T))
