package objects;

public class TagObject {
	private long id;
	private String tagName;
	
	public TagObject (long id, String tagName)
	{
		this.id = id;
		this.tagName = tagName;
	}
	
	public long getId()
	{
		return this.id;
	}
	
	public String getTagName()
	{
		return this.tagName;
	}
}
