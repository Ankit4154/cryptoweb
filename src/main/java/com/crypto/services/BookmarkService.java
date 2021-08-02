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

	public void removeUserBookmark(User user, Bookmark bookmark) {
		UserBookmark userBookmark = new UserBookmark();
		userBookmark.setUser(user);
		userBookmark.setBookmark(bookmark);

		bookmarkDao.removeUserBookmark(userBookmark);
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
		return bookmarkDao.getBooks(isBookmarked, userId);
	}

	public Bookmark getBook(long bookId) {
		return bookmarkDao.getBook(bookId);
	}
}
