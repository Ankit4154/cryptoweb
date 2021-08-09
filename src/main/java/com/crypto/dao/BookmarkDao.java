package com.crypto.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.crypto.DataStore;
import com.crypto.LocalConnection;
import com.crypto.constants.BookGenre;
import com.crypto.entities.Book;
import com.crypto.entities.Bookmark;
import com.crypto.entities.Movie;
import com.crypto.entities.UserBookmark;
import com.crypto.entities.WebLink;
import com.crypto.services.BookmarkService;

public class BookmarkDao {

	private static Statement stmt;

	public List<List<Bookmark>> getBookmarks() {
		return DataStore.getBookmarks();
	}

	public BookmarkDao() {
		stmt = LocalConnection.getLocalConnection();
	}

	public void saveBookmark(UserBookmark userBookmark) {

		if (userBookmark == null) {
			System.out.println("User-Bookmark reference null");
			return;
		}
		// Add data to DB
		try {
			if (userBookmark.getBookmark() instanceof Book) {
				saveUserBook(userBookmark, stmt);
			} else if (userBookmark.getBookmark() instanceof Movie) {
				saveUserMovie(userBookmark, stmt);
			} else {
				saveUserWebLink(userBookmark, stmt);
			}
			// LocalConnection.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Add data to runtime object.
		// DataStore.add(userBookmark);
	}

	private void saveUserWebLink(UserBookmark userBookmark, Statement stmt) throws SQLException {
		String query = "insert into Users_WebLink(users_id, weblink_id) values (" + userBookmark.getUser().getId()
				+ ", " + userBookmark.getBookmark().getId() + ")";
		System.out.println("Rows inserted : " + stmt.executeUpdate(query) + " in Users_WebLink table");
	}

	private void saveUserMovie(UserBookmark userBookmark, Statement stmt) throws SQLException {
		String query = "insert into Users_Movie(users_id, movie_id) values (" + userBookmark.getUser().getId() + ", "
				+ userBookmark.getBookmark().getId() + ")";
		System.out.println("Rows inserted : " + stmt.executeUpdate(query) + " in Users_Movie table");
	}

	private void saveUserBook(UserBookmark userBookmark, Statement stmt) throws SQLException {
		String query = "insert into Users_Book(users_id, book_id) values (" + userBookmark.getUser().getId() + ", "
				+ userBookmark.getBookmark().getId() + ")";
		System.out.println("Rows inserted : " + stmt.executeUpdate(query) + " in Users_Book table");
	}

	public List<WebLink> getAllWebLinks() {
		List<WebLink> result = new ArrayList<>();
		List<List<Bookmark>> bookmarks = DataStore.getBookmarks();
		List<Bookmark> allWebLinks = bookmarks.get(0);
		for (Bookmark bookmark : allWebLinks) {
			result.add((WebLink) bookmark);
		}
		return result;
	}

	// Get weblink with particular download status value
	public List<WebLink> getWebLink(WebLink.DownloadStatus downloadStatus) {
		List<WebLink> result = new ArrayList<>();
		List<WebLink> allWebLinks = getAllWebLinks();

		for (WebLink weblink : allWebLinks) {
			if (weblink.getDownloadStatus().equals(downloadStatus)) {
				result.add(weblink);
			}
		}

		return result;
	}

	public void updateKidFriendlyStatus(Bookmark bookmark) {
		int kidFriendlyStatus = bookmark.getKidFriendlyStatus().ordinal();
		long userId = bookmark.getKidFriendlyMarkedBy().getId();

		String tableToUpdate = "Book";
		if (bookmark instanceof Movie) {
			tableToUpdate = "Movie";
		} else if (bookmark instanceof WebLink) {
			tableToUpdate = "WebLink";
		}

		try {

			String query = "update " + tableToUpdate + " set kid_friendly_status = " + kidFriendlyStatus
					+ ", kid_friendly_marked_by = " + userId + " where id = " + bookmark.getId();

			System.out.println("Updated rows : " + stmt.executeUpdate(query));

			// LocalConnection.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void sharedByInfo(Bookmark bookmark) {
		long userId = bookmark.getSharedBy().getId();
		String tableToUpdate = "Book";
		if (bookmark instanceof WebLink) {
			tableToUpdate = "WebLink";
		}
		try {

			String query = "update " + tableToUpdate + " set shared_by = " + userId + " where id = " + bookmark.getId();

			System.out.println("Updated rows : " + stmt.executeUpdate(query));

			// LocalConnection.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public Bookmark getBook(long bookId) {
		return DataStore.getBook(bookId);
	}

	public void removeUserBookmark(UserBookmark userBookmark) {
		if (userBookmark == null) {
			System.out.println("User-Bookmark reference null");
			return;
		}
		// Delete data from DB
		try {
			if (userBookmark.getBookmark() instanceof Book) {
				removeUserBook(userBookmark);
			} else {
				System.out.println("Bookmark reference is not an instance of Book");
			}
			// LocalConnection.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void removeUserBook(UserBookmark userBookmark) throws SQLException {
		String query = "delete from Users_Book where users_id = " + userBookmark.getUser().getId() + " and book_id = "
				+ userBookmark.getBookmark().getId();
		System.out.println("Rows deleted : " + stmt.executeUpdate(query) + " from Users_Book table");
	}

	public Collection<Bookmark> getBooks(boolean isBookmarked, long userId) {
		Collection<Bookmark> result = new ArrayList<>();
		try {
			if (stmt == null) {
				System.out.println("Null statement reference");
			}

			String query = "";
			if (!isBookmarked) {
				query = "select b.id, b.title, b.image_url, b.publication_year, p.name, "
						+ "STUFF((SELECT ', ' + a.name FROM Author a join Book_Author ba "
						+ "		   on a.id = ba.author_id and ba.book_id=b.id "
						+ "        FOR XML PATH('')), 1, 2, '') as authors, "
						+ "b.book_genre_id, b.amazon_rating, b.created_date from Book b, Author a, Publisher p, "
						+ "Book_Author ba where b.publisher_id=p.id and b.id = ba.book_id and ba.author_id = a.id "
						+ "and b.id NOT IN (select ub.book_id from Users u, Users_Book ub where u.id = " + userId
						+ " and u.id = ub.users_id) "
						+ "group by b.id, b.title, b.image_url, b.publication_year, p.name, b.book_genre_id, b.amazon_rating, b.created_date";

			} else {
				query = "select b.id, b.title, b.image_url, b.publication_year, p.name, "
						+ "STUFF((SELECT ', ' + a.name FROM Author a join Book_Author ba "
						+ "		   on a.id = ba.author_id and ba.book_id=b.id "
						+ "        FOR XML PATH('')), 1, 2, '') as authors, "
						+ "b.book_genre_id, b.amazon_rating, b.created_date from Book b, Author a, Publisher p, "
						+ "Book_Author ba where b.publisher_id=p.id and b.id = ba.book_id and ba.author_id = a.id "
						+ "and b.id IN (select ub.book_id from Users u, Users_Book ub where u.id = " + userId
						+ " and u.id = ub.users_id) "
						+ "group by b.id, b.title, b.image_url, b.publication_year, p.name, b.book_genre_id, b.amazon_rating, b.created_date";

			}

			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {

				long id = rs.getLong("id");
				String title = rs.getString("title");
				String imageUrl = rs.getString("image_url");
				int publicationYear = rs.getInt("publication_year");
				String publisher = rs.getString("name");
				String[] authors = rs.getString("authors").split(",");
				int genreId = rs.getInt("book_genre_id");
				BookGenre genre = BookGenre.values()[genreId];
				double amazonRating = rs.getDouble("amazon_rating");

				// Date createdDate = rs.getDate("created_date");
				/*
				 * System.out.println("createdDate: " + createdDate);
				 */
				/*
				 * System.out.println("id: " + id + ", title: " + title + ", publication year: "
				 * + publicationYear + ", publisher: " + publisher + ", authors: " +
				 * String.join(", ", authors) + ", genre: " + genre + ", amazonRating: " +
				 * amazonRating);
				 */

				Bookmark bookmark = BookmarkService.getInstance().createBook(id, title, imageUrl, publicationYear,
						publisher, authors, genre, amazonRating);
				result.add(bookmark);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
