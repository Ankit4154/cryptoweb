package com.crypto.services;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.crypto.constants.BookGenre;
import com.crypto.constants.KidFriendlyStatus;
import com.crypto.constants.MovieGenre;
import com.crypto.dao.BookmarkDao;
import com.crypto.entities.Book;
import com.crypto.entities.Bookmark;
import com.crypto.entities.Movie;
import com.crypto.entities.User;
import com.crypto.entities.UserBookmark;
import com.crypto.entities.WebLink;

public class BookmarkService {
	private static BookmarkService instance = new BookmarkService();
	private static BookmarkDao bookmarkDao = new BookmarkDao();

	private BookmarkService() {
	}

	public static BookmarkService getInstance() {
		return instance;
	}

	public Movie createMovie(long id, String title, String profileUrl, int releaseYear, String[] cast,
			String[] directors, MovieGenre genre, double imdbRating) {

		Movie movie = new Movie();

		movie.setId(id);
		movie.setTitle(title);
		movie.setProfileUrl(profileUrl);
		movie.setReleaseYear(releaseYear);
		movie.setCast(cast);
		movie.setDirectors(directors);
		movie.setGenre(genre);
		movie.setImdbRating(imdbRating);

		return movie;

	}

	public Book createBook(long id, String title, String imageUrl, int publicationYear, String publisher,
			String[] authors, BookGenre genre, double amazonRating) {

		Book book = new Book();
		book.setId(id);
		book.setTitle(title);
		//book.setProfileUrl(profileUrl);
		book.setImageUrl(imageUrl);
		book.setPublicationYear(publicationYear);
		book.setPublisher(publisher);
		book.setAuthors(authors);
		book.setGenre(genre);
		book.setAmazonRating(amazonRating);

		return book;

	}

	public WebLink createWebLink(long id, String title, String url, String host) {

		WebLink weblink = new WebLink();
		weblink.setId(id);
		weblink.setTitle(title);
		weblink.setUrl(url);
		weblink.setHost(host);

		return weblink;

	}

	public List<List<Bookmark>> getBookmarks() {
		return bookmarkDao.getBookmarks();
	}

	public void saveUserBookmark(User user, Bookmark bookmark) {
		UserBookmark userBookmark = new UserBookmark();
		userBookmark.setUser(user);
		userBookmark.setBookmark(bookmark);

		// If weblink, fetch URL and download the webpage.
		/*
		 * Downloading HTML pages with 1 thread. if(bookmark instanceof WebLink) { try {
		 * String url = ((WebLink)bookmark).getUrl(); if(!url.endsWith(".pdf")) { String
		 * webPage = HttpConnect.download(url); if(webPage != null) {
		 * IOUtil.write(webPage, bookmark.getId()); } }
		 * 
		 * } catch (MalformedURLException | URISyntaxException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } }
		 */

		bookmarkDao.saveBookmark(userBookmark);
	}

	public void setKidFriendlyStatus(User user, KidFriendlyStatus kidFriendlyStatus, Bookmark bookmark) {
		bookmark.setKidFriendlyStatus(kidFriendlyStatus);
		bookmark.setKidFriendlyMarkedBy(user);

		bookmarkDao.updateKidFriendlyStatus(bookmark);
		System.out.println(
				"Kid Friendly Status : " + kidFriendlyStatus + ", Marked by : " + user.getEmail() + ", " + bookmark);
	}

	public void share(User user, Bookmark bookmark) {
		bookmark.setSharedBy(user);

		System.out.println("Data to be shared");
		if (bookmark instanceof Book) {
			System.out.println(((Book) bookmark).getItemData());
		} else if (bookmark instanceof WebLink) {
			System.out.println(((WebLink) bookmark).getItemData());
		}

		bookmarkDao.sharedByInfo(bookmark);
	}

	public Collection<Bookmark> getBooks(boolean isBookmarked, long userId) {
		Collection<Bookmark> result = new ArrayList<>();

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=crypto", "sa2",
					"Test@123");
			if (conn != null) {
				DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
				System.out.println("Driver name: " + dm.getDriverName());
				System.out.println("Driver version: " + dm.getDriverVersion());
				System.out.println("Product name: " + dm.getDatabaseProductName());
				System.out.println("Product version: " + dm.getDatabaseProductVersion());
			}
			Statement stmt = conn.createStatement();
			if (stmt == null) {
				System.out.println("Null statement reference");
			}

			String query = "";
			if(!isBookmarked) {
				query = "select b.id, b.title, b.image_url, b.publication_year, p.name, "
						+ "STUFF((SELECT ', ' + a.name FROM Author a join Book_Author ba "
						+ "		   on a.id = ba.author_id and ba.book_id=b.id "
						+ "        FOR XML PATH('')), 1, 2, '') as authors, "
						+ "b.book_genre_id, b.amazon_rating, b.created_date from Book b, Author a, Publisher p, "
						+ "Book_Author ba where b.publisher_id=p.id and b.id = ba.book_id and ba.author_id = a.id "
						+ "and b.id NOT IN (select ub.book_id from Users u, Users_Book ub where u.id = "
						+ userId + " and u.id = ub.users_id) "
						+ "group by b.id, b.title, b.image_url, b.publication_year, p.name, b.book_genre_id, b.amazon_rating, b.created_date";

			} /*else {
				query = "select b.id, b.title, b.publication_year, p.name, "
						+ "STUFF((SELECT ', ' + a.name FROM Author a join Book_Author ba "
						+ "		   on a.id = ba.author_id and ba.book_id=b.id "
						+ "        FOR XML PATH('')), 1, 2, '') as authors, "
						+ "b.book_genre_id, b.amazon_rating, b.created_date from Book b, Author a, Publisher p, "
						+ "Book_Author ba where b.publisher_id=p.id and b.id = ba.book_id and ba.author_id = a.id "
						+ "and b.id IN (select ub.book_id from Users u, Users_Book ub where u.id = "
						+ userId + " and u.id = ub.users_id) "
						+ "group by b.id, b.title, b.publication_year, p.name, b.book_genre_id, b.amazon_rating, b.created_date";

			}*/
			
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {

				long id = rs.getLong("id");
				String title = rs.getString("title");
				String imageUrl = rs.getString("image_url");
				int publicationYear = rs.getInt("publication_year");
				String publisher = rs.getString("name");
				String[] authors = rs.getString("authors").split(",");
				int genreId = rs.getInt("book_genre_id");
				BookGenre genre = BookGenre.values()[genreId];
				double amazonRating = rs.getDouble("amazon_rating");
				

				//Date createdDate = rs.getDate("created_date");
				/*System.out.println("createdDate: " + createdDate);
				*/
				System.out.println("id: " + id + ", title: " + title + ", publication year: " + publicationYear
						+ ", publisher: " + publisher + ", authors: " + String.join(", ", authors) + ", genre: " + genre
						+ ", amazonRating: " + amazonRating);
				
				Bookmark bookmark = BookmarkService.getInstance().createBook(id, title, imageUrl, publicationYear, publisher,
						authors, genre, amazonRating);
				result.add(bookmark);
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
