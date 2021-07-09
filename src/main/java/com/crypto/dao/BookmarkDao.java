package com.crypto.dao;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.crypto.DataStore;
import com.crypto.LocalConnection;
import com.crypto.entities.Book;
import com.crypto.entities.Bookmark;
import com.crypto.entities.Movie;
import com.crypto.entities.UserBookmark;
import com.crypto.entities.WebLink;

public class BookmarkDao {
	
	private static Statement stmt;

	public List<List<Bookmark>> getBookmarks() {
		return DataStore.getBookmarks();
	}
	
	public BookmarkDao() {
		stmt = LocalConnection.getLocalConnection();
	}

	public void saveBookmark(UserBookmark userBookmark) {

		// Add data to DB
		try {
			if (userBookmark.getBookmark() instanceof Book) {
				saveUserBook(userBookmark, stmt);
			} else if (userBookmark.getBookmark() instanceof Movie) {
				saveUserMovie(userBookmark, stmt);
			} else {
				saveUserWebLink(userBookmark, stmt);
			}
			//LocalConnection.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Add data to runtime object.
		// DataStore.add(userBookmark);
	}

	private void saveUserWebLink(UserBookmark userBookmark, Statement stmt) throws SQLException {
		String query = "insert into Users_WebLink(users_id, weblink_id) values (" + userBookmark.getUser().getId()
				+ ", " + userBookmark.getBookmark().getId() + ")";
		System.out.println("Rows inserted : " + stmt.executeUpdate(query) + " Users_WebLink table");
	}

	private void saveUserMovie(UserBookmark userBookmark, Statement stmt) throws SQLException {
		String query = "insert into Users_Movie(users_id, movie_id) values (" + userBookmark.getUser().getId() + ", "
				+ userBookmark.getBookmark().getId() + ")";
		System.out.println("Rows inserted : " + stmt.executeUpdate(query) + " Users_Movie table");
	}

	private void saveUserBook(UserBookmark userBookmark, Statement stmt) throws SQLException {
		String query = "insert into Users_Book(users_id, book_id) values (" + userBookmark.getUser().getId() + ", "
				+ userBookmark.getBookmark().getId() + ")";
		System.out.println("Rows inserted : " + stmt.executeUpdate(query) + " Users_Book table");
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

			//LocalConnection.closeConnection();
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
			
			//LocalConnection.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
