public class YouTubeRating{
                public String category;
                public double rating;

                public YouTubeRating(String category, double rating)
                {
                        this.rating = rating;
                        this.category = category;
                }

		public String getCategory()
		{
			return category;
		}

		public double getRating()
		{
			return rating;
		}


                public String getString()       // use in map output
                {
                        return category + "|" + Double.toString(rating);
                }
}
