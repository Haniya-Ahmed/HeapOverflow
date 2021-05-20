package objects;

public class VoteObject {
	private long id;
	private long userId;
	private long repsonseId;
	private short type;
	
	public VoteObject (long id, long userId, long responseId, short type)
	{
		this.id = id;
		this.userId = userId;
		this.repsonseId = responseId;
		this.type = type;
	}

	public long getId()
	{
		return this.id;
	}
	
	public long getUserId()
	{
		return this.userId;
	}
	
	public long getResponseId()
	{
		return this.repsonseId;
	}
	
	public short getType()
	{
		return this.type;
	}
}
