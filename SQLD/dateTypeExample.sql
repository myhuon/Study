--Oracle 환경
/*
  YYYY.MM.DD : (네 자릿수 연도).(두 자릿수 월).(두 자릿수 일)
  HH24 : 24시간 표시 시간
  MI : MINUTE
  SS : SECONDS
  
  DATE 타입 + 1은 하루를 더하는 것을 의미.
  1/24/(60/10) : 하루를 24시간으로 나눔 => 1시간을 6으로 나눔 => 10분
  
  결과 : 2022.05.21 10:10:00
*/

SELECT TO_CHAR(TO_DATE('2022.05.21 10', 'YYYY.MM.DD HH24') + 1/24/(60/10), 'YYYY.MM.DD HH24:MI:SS') 
FROM DUAL;

