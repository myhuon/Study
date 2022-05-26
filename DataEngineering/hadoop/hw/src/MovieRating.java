

	public class MovieRating {
		public String userId;
		public String movieId;
		public double rating;
		public String title;

		public MovieRating(String _userId, String _movieId, double _rating, String _title) 
		{
			this.userId = _userId;
			this.movieId = _movieId;
			this.rating = _rating;
			this.title = _title;
		}

		public MovieRating(String title, double rating)
		{
			this.rating = rating;
			this.title = title;
		}


		public String getString()	// use in map output
		{
			return title + " " + Double.toString(rating);
		}
	}
