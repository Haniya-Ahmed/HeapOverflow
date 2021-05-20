package objects;

public class AccountObject {

    private long id;
    private String username;
    private String password;
    private boolean moderator;
    
    public AccountObject(long id, String username, String password, boolean moderator) {
    	this.id = id;
    	this.username = username;
    	this.password = password;
    	this.moderator = moderator;
    }

    public long getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String password() {
        return this.password;
    }

    public boolean getModerator() {
        return this.moderator;
    }
}
