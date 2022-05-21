# HW2 Description
## 🎬 IMDB
<span>
  [ movies.txt ]  </br>
   movieId::title </br></br>
   
  [ rating.txt ]  </br>
  movieId::userId::rating::etc  </br>

  [ result ]  </br>
  topK movie titles and rating </br>
  
  ✨ my solution </br>
  1. 첫번째 MapReduce : movies.txt와 rating.txt를 movieId로 ReduceSideJoin을 한다. </br>
  2. 두번째 MapReduce : 첫번째 MapReduce 결과를 가지고 상위 k개를 골라낸다.
</span> </br></br>

## 🎬 YouTube
<span>
  [ youtube.dat ]  </br>
   x|x|x|category|x|x|rating </br></br>

  [ result ]  </br>
  topK movie category and rating </br>
  
  ✨ my solution </br>
  1. 첫번째 MapReduce : youtube.dat의 category별 평균 rating을 구한다. </br>
  2. 두번째 MapReduce : 첫번째 MapReduce 결과를 가지고 상위 k개를 골라낸다.
</span> </br></br>
