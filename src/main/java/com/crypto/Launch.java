package com.crypto;

import java.util.List;

import com.crypto.entities.Bookmark;
import com.crypto.entities.User;
import com.crypto.jobs.WebPageDownloaderTask;
import com.crypto.services.BookmarkService;
import com.crypto.services.UserService;

public class Launch {

	private static List<User> users;
	private static List<List<Bookmark>> bookmarks;

	private static void loadData() {
		System.out.println("1. Loading Data ...");
		//DataStore.loadData();
		DataStore.loadDataFromDB();

		users = UserService.getInstance().getUsers();
		bookmarks = BookmarkService.getInstance().getBookmarks();

		/*
		System.out.println("2. Printing Data ...");
		printUserData();
		printBookmarkData();
		*/
	}

	private static void printBookmarkData() {
		for (List<Bookmark> bookmarkList : bookmarks) {
			for (Bookmark bookmark : bookmarkList) {
				System.out.println(bookmark);
			}
		}
	}

	private static void printUserData() {
		for (User user : users) {
			System.out.println(user);
		}
	}

	private static void start() {
		//System.out.println("\n 2. Bookmarking ...");
		for (User user : users) {
			View.browse(user, bookmarks);
			//View.bookmark(user, bookmarks);
		}
	}

	public static void main(String args[]) {
		loadData();
		start();
		
		// run background jobs
		//runDownloaderJob();
	}

	private static void runDownloaderJob() {
		WebPageDownloaderTask task = new WebPageDownloaderTask(true);
		(new Thread(task)).start();
		
	}

}
