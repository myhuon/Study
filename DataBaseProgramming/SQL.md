# emp, dept, salgrade 테이블을 이용하여 SQL 질의 작성



## 1. 20번 부서나 30번 부서에 속하고 이름에 ‘AR’이 포함된 사원들의 이름과 부서번호, 업무(job)를구하시오. (where절, OR 또는 IN, LIKE 연산자 이용) </br>
![No 1](https://user-images.githubusercontent.com/84562885/169504862-618ff69f-fead-408c-b021-e7e84c5dceec.JPG) </br></br>

## 2. RESEARCH 부서에서 일하는 사원들 중 급여가 2,000이하인 사원들의 수를 구하시오. </br>
(1) join을 이용하는 질의</br> 
![No 2](https://user-images.githubusercontent.com/84562885/169504864-59e201b6-21d3-46e7-9923-62d98b44b17e.JPG)</br></br>

(2) subquery를 이용하는 질의 </br>
![No 2_2](https://user-images.githubusercontent.com/84562885/169504868-5758fa95-79ca-48f6-a7ba-6117a7084f48.JPG)</br></br>
    
## 3. 사원들 중 급여등급이 5에 속하면서 수당(comm)이 500미만인 사원들의 이름과 급여, 수당을구하고 “연봉”을 계산하시오. 단, 수당을 모를 경우 0으로 간주함  // 연봉 = sal + comm * 12</br>
![NO 3](https://user-images.githubusercontent.com/84562885/169504871-d63402e8-e59e-407c-a070-2183dbf753ff.JPG)</br></br>


## 4. 모든 사원들의 이름, 입사일, 재직년수(만으로 계산), “퇴사예정일”(재직한지 30년 되는 수년간의 서비스날)을 구하시오. 입사일은 “OOOO년 OO월 OO일” 형식으로 출력하고, 입사일이 빠른 사원부터 순서대로 출력할 것 (SYSDATE, date 타입 연산/함수, 타입변환 함수, 버림 함수 이용) 재직년수 = 현재시간 - 입사일 </br>
![No 4](https://user-images.githubusercontent.com/84562885/169504872-8857f162-d4b3-48ad-bb74-7cbab8572235.JPG)</br></br>

## 5. 각 부서에서 같은 업무를 수행하는 사원들을 분류하여 부서번호, 업무명, 인원수, 평균 급여를출력하시오. (group by절, 집계 함수 이용)</br>
![No 5](https://user-images.githubusercontent.com/84562885/169504877-83ff1a2e-de41-4883-8872-c6dd5ef83641.JPG)</br></br>

## 6. 사원이 2명 이상 있는 부서들에 대해 부서명과 평균 급여를 각각 구하시오. </br>

1)having절 이용 </br>
![No 6](https://user-images.githubusercontent.com/84562885/169504880-b54710a7-083c-41ca-ab6e-c0f5b6d5ef0d.JPG)</br></br>

2) having절을 사용하지 않고 derived relation(subquery)을 사용</br>
![No 6_2](https://user-images.githubusercontent.com/84562885/169504850-1c481fb6-3027-4fef-83a1-b741653c029f.JPG)</br></br>

	

## 7. 모든 부서에 대해 부서명과 사원수, 최대급여를 각각 구하시오. 단, 사원이 한 명도 없는 부서도 결과에 포함되어야 함 (outer-join 이용)</br>
![No 7](https://user-images.githubusercontent.com/84562885/169504852-d381bf42-a8df-49e8-a8e4-ea95cd7ba132.JPG)</br></br>


## 8. 각 사원에 대해 같은 부서에 근무하는 동료 사원들의 이름을 구하시오. (self-join 이용) (참고: 위 질의의 결과는 두 사원의 이름 쌍들의 집합임. Oracle에서 제공하는 LISTAGG 함수를사용하면 각 사원에 대해 모든 동료 사원들의 이름 리스트를 하나의 행에 출력할 수 있음) // LISTAGG는 오라클 버전 11이상 사용 가능 함수 </br>

1) LISTAGG 함수 이용</br>

SELECT e.ename, LISTAGG(m.ename, ', ') WITHIN GROUP(ORDER BY ename) AS colleague
FROM emp e JOIN emp m ON e.deptno = m.deptno

2) LISTAGG 함수 이용 X </br>
![No 8](https://user-images.githubusercontent.com/84562885/169504854-f014af8a-870f-47af-868d-46127c3b8d0f.JPG)</br></br>


## 9. 각 사원의 이름, 급여, “공제액”을 출력하되 급여가 가장 많은 사원부터 순서대로 출력하시오. 단, 공제액은 급여가 1000미만인 경우 급여의 1%, 급여가 1000이상 2000미만인 경우 1.5%, 급여가 2000이상인 경우는 2%에 해당하는 금액으로 계산함 (CASE WHEN, 정렬 이용) </br>
![No 9](https://user-images.githubusercontent.com/84562885/169504859-ef7583dc-de89-44da-b61e-5a179e2ea692.JPG)</br></br>



