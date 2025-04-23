package controller;

import java.io.IOException;
import java.util.List;
import java.util.Calendar;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ModelException;
import model.Post;
import model.User;
import model.dao.DAOFactory;
import model.dao.PostDAO;
import model.dao.UserDAO;

@WebServlet(urlPatterns = {"/posts", "/post/save", "/post/update", "/post/load", "/post/delete"})
public class PostsController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		String action = req.getRequestURI();

		System.out.println(action);

		switch (action) {
			case "/Facebook/posts": {
				loadPosts(req);
				RequestDispatcher rd = req.getRequestDispatcher("posts.jsp");
				rd.forward(req, resp);
				break;
			}
			case "/Facebook/post/save": {
				String postId = req.getParameter("post_id");
				if (postId != null && !postId.isEmpty())
					updatePost(req);
				else
					insertPost(req);

				resp.sendRedirect("/Facebook/posts");
				break;
			}
			case "/Facebook/post/update": {
				loadPost(req);
				RequestDispatcher rd = req.getRequestDispatcher("/form_post.jsp");
				rd.forward(req, resp);
				break;
			}
			case "/Facebook/post/load": {
				loadAllUsers(req);
				RequestDispatcher rd = req.getRequestDispatcher("/form_post.jsp");
				rd.forward(req, resp);
				break;
			}
			case "/Facebook/post/delete": {
				deletePost(req);
				resp.sendRedirect("/Facebook/posts");
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected action: " + action);
		}
	}

	private void loadPosts(HttpServletRequest req) {
		PostDAO dao = DAOFactory.createDAO(PostDAO.class);

		try {
			List<Post> posts = dao.listAll();
			req.setAttribute("posts", posts);
		} catch (ModelException e) {
			e.printStackTrace();
		}
	}

	private void loadAllUsers(HttpServletRequest req) {
	    UserDAO userDao = DAOFactory.createDAO(UserDAO.class);
	    try {
	        req.setAttribute("usuarios", userDao.listAll()); // aqui carrega os usuários
	    } catch (ModelException e) {
	        e.printStackTrace();
	    }
	}
	
	private void loadPost(HttpServletRequest req) {
	    int postId = Integer.parseInt(req.getParameter("postId"));
	    PostDAO postDao = DAOFactory.createDAO(PostDAO.class);
	    UserDAO userDao = DAOFactory.createDAO(UserDAO.class);

	    try {
	        Post post = postDao.findById(postId);
	        if (post == null)
	            throw new ModelException("Post não encontrado");

	        req.setAttribute("post", post);
	        req.setAttribute("usuarios", userDao.listAll()); // aqui carrega os usuários
	    } catch (ModelException e) {
	        e.printStackTrace();
	    }
	}

	private void insertPost(HttpServletRequest req) {
		Post post = createPost(req);

		PostDAO dao = DAOFactory.createDAO(PostDAO.class);

		try {
			dao.save(post);
		} catch (ModelException e) {
			e.printStackTrace();
		}
	}

	private void updatePost(HttpServletRequest req) {
		Post post = createPost(req);

		PostDAO dao = DAOFactory.createDAO(PostDAO.class);

		try {
			dao.update(post);
		} catch (ModelException e) {
			e.printStackTrace();
		}
	}

	private void deletePost(HttpServletRequest req) {
		int postId = Integer.parseInt(req.getParameter("postId"));
		Post post = new Post(postId);

		PostDAO dao = DAOFactory.createDAO(PostDAO.class);

		try {
			dao.delete(post);
		} catch (ModelException e) {
			e.printStackTrace();
		}
	}

	private Post createPost(HttpServletRequest req) {
		String postIdStr = req.getParameter("post_id");
		String content = req.getParameter("post_content");
		String userIdStr = req.getParameter("user_id");

		Post post = postIdStr == null || postIdStr.isEmpty() ? new Post() : new Post(Integer.parseInt(postIdStr));
		post.setContent(content);
		post.setPostDate(Calendar.getInstance().getTime());

		if (userIdStr != null && !userIdStr.isEmpty()) {
			int userId = Integer.parseInt(userIdStr);
			User user = new User(userId);
			post.setUser(user);
		}

		return post;
	}
}
