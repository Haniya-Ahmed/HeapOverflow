package tests;
import dataManager.*;
import objects.*;
import control.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreatePostControlTest {
	private DataManager dm;
    private CreatePostControl control;
    
    @BeforeEach
	void setUp() throws Exception {
		dm = new DataManager();
		control = new CreatePostControl(dm);
	}
    
    @Test
    void testAddPost1() {
    	PostObject post = control.addPost("TestPost1", 1, "TestPost1Title", "");
    	if(post == null) {
    		fail("No post created.");
    	}
    	String[] expected = {"TestPost1", "TestPost1Title"};
    	String[] returned = {post.getBody(), post.getTitle()};
    	assertArrayEquals(expected, returned);
    	assertEquals(0, post.getTags().size());
    }
    
    @Test
    void testAddPost2() {
    	PostObject post = control.addPost("TestPost1", 1, "TestPost1Title", "<C><Java>");
    	if(post == null) {
    		fail("No post created.");
    	}
    	String[] expected = {"TestPost1", "TestPost1Title", "C", "Java"};
    	String[] returned = {post.getBody(), post.getTitle(), post.getTags().get(0).getTagName(), post.getTags().get(1).getTagName()};
    	assertArrayEquals(expected, returned);
    }
}
