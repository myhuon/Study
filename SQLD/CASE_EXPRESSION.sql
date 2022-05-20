[SEARCHED_CASE_EXPRESSION]
SELECT LOC
  (CASE WHEN LOC = 'NEW YORK' THEN 'EAST'
       ELSE 'ETC'
  END) as AREA
FROM DEPT;

[SIMPLE_CASE_EXPRESSION]
SELECT LOC
  (CASE LOC WHEN 'NEW YORK' THEN 'EAST'
       ELSE 'ETC'
  END) as AREA
FROM DEPT;
