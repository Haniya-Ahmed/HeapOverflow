package objects;

public class ResponseObject {
	private long id;
	private long userId;
	private String username;
	private long postId;
	private String text;
	private int score;
	
	public ResponseObject(long id, long userId, String username, long postId, String text, int score) {
    	this.id = id;
    	this.userId = userId;
    	this.username = username;
    	this.postId = postId;
    	this.text = text;
    	this.score = score;
    }
	
	public long getId() {
        return this.id;
    }
	
	public long getUserId() {
		return this.userId;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public long getPostId() {
		return this.postId;
	}
	
	public String getText() {
		return this.text;
	}
	
	public int getScore() {
		return this.score;
	}
}
