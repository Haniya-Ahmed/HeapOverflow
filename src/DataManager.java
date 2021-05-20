package dataManager;
import objects.*;

import java.util.*;

import java.sql.*;
public class DataManager {
	Connection connection = null;
	
	public DataManager() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		String url = "jdbc:mysql://cs2043.cs.unb.ca:3306/cs2043team1";
		try {
			connection = DriverManager.getConnection(url, "cs2043team1", "RTr88QQG");
		} catch (SQLException e) {
			System.err.println("Database connection error.");
		}
	}

	public ArrayList<PostObject> getPostsByDate(int numrow){
		ArrayList<PostObject> PostList = new ArrayList<PostObject>();
		try {
			Statement st = connection.createStatement();
			
			String sqlQuery = "CALL get_posts_by_date_posted(" + numrow + ");";
			
			ResultSet rs = st.executeQuery(sqlQuery);
			while(rs.next())
			{
				PostObject post = new PostObject(rs.getLong(1), rs.getLong(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getDate(6));
				PostList.add(post);
				if(rs.getString(7) != null) {
					post.getTags().add(new TagObject(rs.getLong(7), rs.getString(8)));
				}
			}
		} catch (SQLException e) {
			System.err.println("SQL error: getPostsByDate");
		}
		
		return PostList;
	}//getPostsbyDate.
	
	public ArrayList<PostObject> getPostsByKeywords(String keywords)
	{
		ArrayList<PostObject> PostList = new ArrayList<PostObject>();
		String[] split = keywords.split(" ");
		for(int i = 0; i < split.length; i++) {
			try {
				Statement st = connection.createStatement();
		
				//create query string
				String sqlQuery = "CALL get_posts_by_tag('" + split[i] + "');";
				
				//execute SQL query
				ResultSet rs = st.executeQuery(sqlQuery);
				while (rs.next()) {
					PostObject post = new PostObject(rs.getLong(1), rs.getLong(2), rs.getString(3), rs.getString(4),
							rs.getString(5), rs.getDate(6));
					if(!PostList.contains(post)) {
						PostList.add(post);
					}
						
				}
			} catch (SQLException e) {
				System.err.println("SQL error: getPostsByKeywords.");
			}
		}
		return PostList;
	}//getPostsByKeywords.
	
	public PostObject getPostById(long id)
	{
		PostObject post = null;
		try {
			Statement st = connection.createStatement();
			String sqlQuery = "CALL get_posts_by_id(" + id + ");";
			ResultSet rs = st.executeQuery(sqlQuery);
			
			while(rs.next())
			{
				post = new PostObject(rs.getLong(1), rs.getLong(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getDate(6));
				if(rs.getString(7) != null) {
					post.getTags().add(new TagObject(rs.getLong(7), rs.getString(8)));
				}
			}
			
		} catch (SQLException e) {
			System.err.println("SQL error: getPostsById.");
		}
		return post;
	}//getPostById
	
	public boolean deletePostById(long id)
	{
		boolean r = true;
		try {
			//create query string
			String sqlQuery = "CALL delete_post(" + id + ");";
			
			Statement st = connection.createStatement();
			
			//execute SQL query
		    int row_affected = st.executeUpdate(sqlQuery);
			if(row_affected == 0)
			{
				r = false;
			}
			
		} catch (SQLException e) {
			System.err.println("SQL error: deletePostById.");
		}
		return r;
	}//deletePostById.
	
	public boolean deleteResponseById(long id)
	{
		boolean r = true;
		try {
			Statement st = connection.createStatement();
			String sqlQuery = "CALL delete_response(" + id + ");";
			int result = st.executeUpdate(sqlQuery);

			if(result < 1)
			{
				r = false;
			}
			
		} catch (SQLException e) {
			System.err.println("SQL error: deleteResponseById.");
		}
		return r;
	}//deleteResponseById
	
	public AccountObject getAccountByUserAndPass(String name, String pass)
	{
		AccountObject account = null;
		try {
			Statement st = connection.createStatement();
			String sqlQuery = "CALL user_login('" + name + "', '" + pass +  "');";
			
			ResultSet rs = st.executeQuery(sqlQuery);
			if(rs.next()) {
				account = new AccountObject(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getBoolean(4));
			}
			
		} catch (SQLException e) {
			System.err.println("SQL error: getAccountByUserandPass.");
		}
		return account;
	}//getAccountByUserandPass
	
	public boolean checkUserAlreadyExists(String Username)
	{
		boolean r = false;
		try {
			Statement st = connection.createStatement();
			
			String sqlQuery = "CALL check_account_exists('" + Username + "');";
			
			//execute SQL query
			ResultSet rs = st.executeQuery(sqlQuery);
			if(rs.next()) {
				r = rs.getBoolean(1);
			} else {
				r = false;
			}
			
			
		} catch (SQLException e) {
			System.err.println("SQL error: checkUserAlreadyExists.");
		}
		return r;
	}//checkUserAlreadyExists.
	
	public boolean addAccount(String name, String pass)
	{
		boolean r = false;
		try {
			String sqlQuery = "CALL add_account('" + name + "', '" + pass +  "')";
			Statement st = connection.createStatement();
			
			//execute SQL query
		    int row_affected  = st.executeUpdate(sqlQuery);
			if(row_affected == 1)
			{
				r = true;
			}
			
		} catch (SQLException e) {
			System.err.println("SQL error: addAccount.");
		}
		return r;
	}//addAccount.
	
	public PostObject addPostByText(String text, long userid, String title, String tags)
	{
		PostObject post = null;
		try {
			String sqlQuery = "CALL add_post(" + userid + ", '"+ title + "', '"+  text + "', '" + tags + "');";
			Statement st = connection.createStatement();

			//execute SQL query
			ResultSet rs = st.executeQuery(sqlQuery);
			if(rs.next()) {
				post = new PostObject(rs.getLong(1), rs.getLong(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getDate(6));
				if(rs.getString(7) != null) {
					post.getTags().add(new TagObject(rs.getLong(7), rs.getString(8)));
				}
				
			}
			while(rs.next()) {
				post.getTags().add(new TagObject(rs.getLong(7), rs.getString(8)));
			}
			
		} catch (SQLException e) {
			System.err.println("SQL error: addPostByText.");
		}
		return post;		
	}//addPostByText.
	
	public boolean editPostById(String text, long id)
	{
		try {
			String sqlQuery = "CALL edit_post_body('" + text + "', " + id + ");";
			Statement st = connection.createStatement();
			
			//execute SQL query
			int result = st.executeUpdate(sqlQuery);
			if(result == 1) {
				return true;
			}
							
		} catch (SQLException e) {
			System.err.println("SQL error: editPostById.");
		}
		return false;
		
	}//editPostById
	
	public ArrayList<ResponseObject> getResponsesByPost(long postId)
	{
		ArrayList<ResponseObject> responseList = new ArrayList<ResponseObject>();
		try {
			String sqlQuery = "CALL get_responses_by_parent(" + postId + ");";
			Statement st = connection.createStatement();
			
			//execute SQL query
			ResultSet rs = st.executeQuery(sqlQuery);
			
			//convert retrieved rows to ResponsesObject[]
			while (rs.next()) {
				ResponseObject response = new ResponseObject(rs.getLong(1), rs.getLong(2), rs.getString(3), rs.getLong(4), rs.getString(5), rs.getInt(6));
				responseList.add(response);
			}
			
		} catch (SQLException e) {
			System.err.println("SQL error: getResponseByPost");
		}
		
		return responseList;
	}//getResponsesByPost
	
	public ResponseObject addResponseByPostId(long postId, String text, long userId)
	{
		ResponseObject response = null;
		try {
			Statement st = connection.createStatement();
			String sqlQuery = "CALL add_response(" + userId + ", " + postId +  ", '" + text + "');";
			boolean result = st.execute(sqlQuery);
			if(result) {
				ResultSet rs = st.getResultSet();
				if(rs.next()) {
					response = new ResponseObject(rs.getLong(1), rs.getLong(2), rs.getString(3), rs.getLong(4), rs.getString(5), rs.getInt(6));
				} else {
					return null;
				}
				
			}		
			
		} catch (SQLException e) {
			System.err.println("SQL error: addResponseByPostId.");
		}
		return response;
	}//addResponseByPostId

	public ResponseObject getResponseById(long id) {
		ResponseObject response = null;
		try {
			Statement st = connection.createStatement();
			String sqlQuery = "CALL get_responses_by_id(" + id + ");";
			ResultSet rs = st.executeQuery(sqlQuery);
			if(rs.next()) {
				response = new ResponseObject(rs.getLong(1), rs.getLong(2), rs.getString(3), rs.getLong(4), rs.getString(5), rs.getInt(6));
			} else {
				return null;
			}
			
		} catch (SQLException e) {
			System.err.println("SQL error: addResponseByPostId.");
		}
		return response;
	}
	
	public boolean editResponseById(long responseId, String text)
	{
		try {
			Statement st = connection.createStatement();
			String sqlQuery = "CALL edit_response_body('" + text + "', " + responseId +  ");";
			int result = st.executeUpdate(sqlQuery);
			if(result == 1) {
				return true;
			}
			
		} catch (SQLException e) {
			System.err.println("SQL error: editResponseById.");
		}
		return false;
	}//editResponseById
	
	public ArrayList<TagObject> getTagsByPostId(long postId)
	{
		ArrayList<TagObject> TagList = new ArrayList<TagObject>();
		try {
			String sqlQuery = "CALL get_tags_by_post(" + postId +");";
			Statement st = connection.createStatement();
		
			//execute SQL query
			ResultSet rs = st.executeQuery(sqlQuery);
			
			//convert retrieved rows to TagObject[]
			while (rs.next()) {
				TagObject Tag = new TagObject(rs.getLong(1), rs.getString(2));
				TagList.add(Tag);
			}
			
		} catch (SQLException e) {
			System.err.println("SQL error: getTagsByPostId");
		}
		
		return TagList;
	}//getTagsByPostId
	

	public boolean addTag(String Text)
	{
		try {
			Statement st = connection.createStatement();
	
			//create query string
			String sqlQuery = "CALL add_tag(' "+ Text +" ');";
			
			//execute SQL query
			int row_affected  = st.executeUpdate(sqlQuery);
			if(row_affected == 1)
			{
				return true;
			}
		} catch (SQLException e) {
			System.err.println("SQL error: addTag.");
		}
		return false;
	}//addTag

	public boolean addTagsByPostId(String tags, long postId) {
		boolean result = false;
		
		try {
			String sqlQuery = "CALL add_tags_by_postId('" + tags + "', '" + postId +  "')";
			Statement st = connection.createStatement();
			
			//execute SQL query
		    int row_affected  = st.executeUpdate(sqlQuery);
			if(row_affected != 0) {
				result = true;
			}
			
		} catch (SQLException e) {
			System.err.println("SQL error: addTagsByPostId.");
		}
		
		return result;
	}
	
	public boolean deleteTagById(long postid, String tagName)
	{
		boolean r = true;
		try {
			String sqlQuery = "CALL delete_tag(" + postid + ", '" + tagName + "');";
			Statement st = connection.createStatement();
			
			//execute SQL query
		    int row_affected  = st.executeUpdate(sqlQuery);
			if(row_affected == 0)
			{
				r = false;
			}
			
		} catch (SQLException e) {
			System.err.println("SQL error: deleteTagById.");
		}
		return r;
	}//deleteTagById
	
	public VoteObject getVotesByUserAndResponse(long userId, long responseId)
	{
		VoteObject vote = null;
		try {
			String sqlQuery = "CALL get_votes_by_user_and_response("+ userId + ", "+ responseId +" );";
			Statement st = connection.createStatement();

			
			//execute SQL query
			ResultSet rs = st.executeQuery(sqlQuery);
			
			//convert retrieved rows to VotesObject
			 if(rs.next()) {
				vote = new VoteObject(rs.getLong(1), rs.getLong(2), rs.getLong(3), rs.getShort(4));
			}
			
		} catch (SQLException e) {
			System.err.println("SQL error: getVotesByUserAndResponse");
		}
		
		return vote;
	}//getVotesByUserAndReponse
	
	public boolean addVotesByResponse(long responseId, long userId, short type)
	{
		boolean result = false;
		try {
			String sqlQuery = "CALL add_vote(" + userId + ", " + responseId + ", " + type + ");";
			Statement st = connection.createStatement();

			//execute SQL query
			int row_affected = st.executeUpdate(sqlQuery);
			
			if(row_affected == 0) {
				result = false;
			}
			
		} catch (SQLException e) {
			System.err.println("SQL error: addVotesByResponse");
		}		
		return result;
	}//addVotesByResponse
	
	public boolean editVoteByVoteId(short newvote, long voteId)
	{
		boolean r = true;
		try {
			String sqlQuery = "CALL edit_vote("+ newvote +", " + voteId +");";
			Statement st = connection.createStatement();
			
			
			int row_affected = st.executeUpdate(sqlQuery);
			if(row_affected == 0) {
				r = false;
			}
		}catch (SQLException e) {
			System.err.println("SQL error: editVoteByVoteId");
		}
		return r;
	}//editVoteByVoteId
	
}
