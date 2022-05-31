
class Book {
	String title, author;
	
	public void setTitle(String title) {
		this.title = title;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getAuthor() {
		return author;
	}
	public String toString() {
		return "제목 : "+title+",  저자 : "+author;
	}
}
