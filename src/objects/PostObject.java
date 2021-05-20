package objects;

import java.util.*;

public class PostObject {
	private long id;
	private long userId;
	private String username;
	private String title;
	private String body;
	private Date date;
	private ArrayList<ResponseObject> responses;
	private ArrayList<TagObject> tags;
	//private ArrayList<String> tags;
	
	public PostObject(long id, long userId, String username, String title, String body, Date date 
			/*,ArrayList<ResponseObject> responses, ArrayList<TagObject> tags*/)
	{
		this.id = id;
		this.userId = userId;
		this.username = username;
		this.title = title;
		this.body = body;
		this.date = date;
		this.responses = new ArrayList<ResponseObject>();
		this.tags = new ArrayList<TagObject>();
		//this.tags = new ArrayList<String>();
	}
	 public long getId()
	 {
		 return this.id;
	 }
	 
	 public long getUserId()
	 {
		 return this.userId;
	 }
	 
	 public String getUsername() {
			return this.username;
		}
	 
	 public String getTitle()
	 {
		 return this.title;
	 }
	 
	 public String getBody()
	 {
		 return this.body;
	 }
	 
	 public Date getDate()
	 {
		 return this.date;
	 }
	 
	 public ArrayList<ResponseObject> getResponses()
	 {
		 return this.responses;
	 }

	 public ArrayList<TagObject> getTags()
	 {
		 return this.tags;
	 }

	 /* Should we have setters? */
	 public void setResponses(ArrayList<ResponseObject> responses)
	 {
		 this.responses = responses;
	 }
	 
	 public void setTags(ArrayList<TagObject> tags)
	 {
		 this.tags = tags;
	 }
	 
	 @Override
	 public boolean equals(Object other) {
		 PostObject otherPost = (PostObject)other;
		 if(this.getId() == otherPost.getId()) {
			 return true;
		 }
		 return false;
	 }
}
