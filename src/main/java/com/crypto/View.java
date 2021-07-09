package com.crypto;

import java.util.List;

import com.crypto.constants.KidFriendlyStatus;
import com.crypto.constants.UserType;
import com.crypto.controllers.BookmarkController;
import com.crypto.entities.Bookmark;
import com.crypto.entities.User;
import com.crypto.partner.Shareable;

public class View {

	public static void browse(User user, List<List<Bookmark>> bookmarks) {
		System.out.println("\n" + user.getEmail() + " is browsing items.");
		int bookmarkCount = 0;
		for (List<Bookmark> bookmarkList : bookmarks) {
			for (Bookmark bookmark : bookmarkList) {
				// Bookmarking
				//if (bookmarkCount < DataStore.USER_BOOKMARK_LIMIT) {
					boolean isBookmarked = getBookmarkDecision(bookmark);
					if (isBookmarked) {
						bookmarkCount++;

						//BookmarkController.getInstance().saveUserBookmark(user, bookmark);

						System.out.println("new item bookmarked : " + bookmark);
					}

				//}
				// Mark as Kid friendly
				if (user.getUserType().equals(UserType.CHIEF_EDITOR) || user.getUserType().equals(UserType.EDITOR)) {
					if (bookmark.isKidFriendly() && bookmark.getKidFriendlyStatus().equals(KidFriendlyStatus.UNKNOWN)) {
						KidFriendlyStatus kidFriendlyStatus = getKidFriendlyStatusDecision(bookmark);
						if (!kidFriendlyStatus.equals(KidFriendlyStatus.UNKNOWN)) {
							//BookmarkController.getInstance().setKidFriendlyStatus(user, kidFriendlyStatus, bookmark);
						}
					}
					// Sharing
					if (bookmark.getKidFriendlyStatus().equals(KidFriendlyStatus.APPROVED)
							&& (bookmark instanceof Shareable)) {
						boolean isShared = getShareDecision(bookmark);
						if (isShared) {
							//BookmarkController.getInstance().share(user, bookmark);
						}
					}
				}

			}
		}
	}

	// TODO: Below methods simulate user input.
	// After IO we take input from via console.
	private static boolean getShareDecision(Bookmark bookmark) {
		return Math.random() < 0.5 ? true : false;
	}

	private static KidFriendlyStatus getKidFriendlyStatusDecision(Bookmark bookmark) {
		double decision = Math.random();
		return decision < 0.4 ? KidFriendlyStatus.APPROVED
				: (decision >= 0.4 && decision < 0.8) ? KidFriendlyStatus.REJECTED
						: KidFriendlyStatus.UNKNOWN;
	}

	private static boolean getBookmarkDecision(Bookmark bookmark) {
		return Math.random() < 0.5 ? true : false;
	}

	/*
	 * public static void bookmark(User user, Bookmark[][] bookmarks) {
	 * System.out.println("\n" + user.getEmail() + " is bookmarking."); for (int i =
	 * 0; i < DataStore.USER_BOOKMARK_LIMIT; i++) { int typeOffset = (int)
	 * (Math.random() * DataStore.BOOKMARK_TYPE_COUNT); int bookmarkOffset = (int)
	 * (Math.random() * DataStore.BOOKMARK_COUNT_PER_TYPE); Bookmark bookmark =
	 * bookmarks[typeOffset][bookmarkOffset];
	 * 
	 * BookmarkController.getInstance().saveUserBookmark(user, bookmark);
	 * 
	 * System.out.println("View " + bookmark); } }
	 */

}
