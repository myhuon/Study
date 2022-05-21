# HW2 Description
## 🎬 IMDB
<span>
  [ movies.txt ]  </br>
   movieId::title </br></br>
   
  [ rating.txt ]  </br>
  movieId::userId::rating::etc  </br></br>

  [ result ]  </br>
  topK movie titles and rating </br></br>
  
  ✨ my solution </br>
  첫번째 MapReduce : movies.txt와 rating.txt를 movieId로 ReduceSideJoin을 한다. </br>
  두번째 MapReduce : 첫번째 MapReduce 결과를 가지고 상위 k개를 골라낸다.
 </span>
