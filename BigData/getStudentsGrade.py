def calc_grade(sheet_ranges, stu_dict, stu_num):
  a_num = math.floor(stu_num * 0.3) # 학생수이기 때문에 내림함수 사용
  b_num = math.floor(stu_num * 0.7)
  
  aplus_num = a_num
  bplus_num = (b_num - a_num)
  cplus_num = (stu_num - b_num)
  
  cnt = 1
  for total in sorted(stu_dict.values(), reverse=True):
    grade = 'F'
    for key in stu_dict.keys(): # 학생 id로 row_num 구하기
      if stu_dict[key] == total:
        row_num = int(str(key)[4:8]) + 1
      if not (total < 40):
        if cnt <= a_num:
          if cnt <= aplus_num:
            grade = 'A+'
          else:
            grade = 'A0'
        elif cnt <= b_num:
          if cnt <= bplus_num:
            grade = 'B+'
          else:
            grade = 'B0'
        else:
          if cnt <= cplus_num:
            grade = 'C+'
          else:
            grade = 'C0'
     
     sheet_ranges.cell( row = row_num, column = 8, value = grade )
     cnt += 1
