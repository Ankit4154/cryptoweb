package com.crypto.controllers;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.crypto.constants.KidFriendlyStatus;
import com.crypto.entities.Bookmark;
import com.crypto.entities.User;
import com.crypto.services.BookmarkService;
import com.crypto.services.UserService;

@WebServlet(urlPatterns = { "/bookmark", "/bookmark/save", "/bookmark/markedbooks", "/bookmark/remove" })
public class BookmarkController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public BookmarkController() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		RequestDispatcher dispatcher = null;

		if (request.getSession().getAttribute("userId") != null) {
			long userId = (long) request.getSession().getAttribute("userId");
			if (request.getServletPath().contains("save")) {
				// save
				dispatcher = request.getRequestDispatcher("/savedbooks.jsp");
				String bookId = request.getParameter("bid");
				User user = UserService.getInstance().getUser(userId);
				Bookmark bookmark = BookmarkService.getInstance().getBook(Long.parseLong(bookId));
				BookmarkService.getInstance().saveUserBookmark(user, bookmark);
				Collection<Bookmark> list = BookmarkService.getInstance().getBooks(true, userId);
				request.setAttribute("books", list);
			} else if (request.getServletPath().contains("markedbooks")) {
				// saved books
				dispatcher = request.getRequestDispatcher("/savedbooks.jsp");
				Collection<Bookmark> list = BookmarkService.getInstance().getBooks(true, userId);
				request.setAttribute("books", list);
			} else if (request.getServletPath().contains("remove")) {
				// saved books
				dispatcher = request.getRequestDispatcher("/savedbooks.jsp");
				String bookId = request.getParameter("bid");
				User user = UserService.getInstance().getUser(userId);
				Bookmark bookmark = BookmarkService.getInstance().getBook(Long.parseLong(bookId));
				BookmarkService.getInstance().removeUserBookmark(user, bookmark);
				Collection<Bookmark> list = BookmarkService.getInstance().getBooks(true, userId);
				request.setAttribute("books", list);
			} else {
				// 1. Get data from the Model
				Collection<Bookmark> list = BookmarkService.getInstance().getBooks(false, userId);
				request.setAttribute("books", list);
				System.out.println(list);

				// 2. Forwarding to View
				dispatcher = request.getRequestDispatcher("/browse.jsp");
			}

		} else {
			dispatcher = request.getRequestDispatcher("/login.jsp");
		}
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	public void saveUserBookmark(User user, Bookmark bookmark) {
		BookmarkService.getInstance().saveUserBookmark(user, bookmark);
	}

	public void setKidFriendlyStatus(User user, KidFriendlyStatus kidFriendlyStatus, Bookmark bookmark) {
		BookmarkService.getInstance().setKidFriendlyStatus(user, kidFriendlyStatus, bookmark);
	}

	public void share(User user, Bookmark bookmark) {
		BookmarkService.getInstance().share(user, bookmark);
	}

}
