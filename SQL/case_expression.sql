/*
  CASE(시작)
    WHEN 조건식 THEN (조건식 true일 때수행문) // simplify => column WHEN 조건 THEN true수행문
    ESLSE (조건식 false일 때 수행문)
  END(종료)
*/

--SEARCHED_CASE_EXPRESSION
SELECT LOC
  CASE WHEN LOC = 'NEW YORK' THEN 'EAST'
       ELSE 'ETC'
  END as AREA
FROM DEPT;

--SIMPLE_CASE_EXPRESSION
SELECT LOC
  CASE LOC WHEN 'NEW YORK' THEN 'EAST'
       ELSE 'ETC'
  END as AREA
FROM DEPT;
