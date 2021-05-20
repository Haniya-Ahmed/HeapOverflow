package tests;
import dataManager.*;
import objects.*;
import control.*;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AddTagControlTest {

	private DataManager dm;
	private CreatePostControl pCtrl;
    private AddTagControl control;
	
    @BeforeEach
   	void setUp() throws Exception {
   		dm = new DataManager();
   		pCtrl = new CreatePostControl(dm);
   		control = new AddTagControl(dm);
   	}
    
    @Test
   	void testGetTags() {
   		ArrayList<TagObject> tags = control.getTags(1);
   		if(tags.size() == 0) {
   			fail("No post created, run test.sql.");
   		}
   		String[] expected = {"C", "SQL", "Java"};
    	String[] returned = {tags.get(0).getTagName(), tags.get(1).getTagName(), tags.get(2).getTagName()};
    	assertArrayEquals(expected, returned);
   	}
    
    @Test
    void testAddTags() {
    	boolean result = false;
    	PostObject post = pCtrl.addPost("AddTagControlTest post", 1, "AddTagControlTest post", "<C>");
    	if(post == null) {
    		fail("No post created.");
    	}
    	result = control.addTags("<MySQL><Postgres>", post.getId());
    	if(result) {
    		ArrayList<TagObject> tags = control.getTags(post.getId());
    		String[] expected = {"C", "MySQL", "Postgres"};
        	String[] returned = {tags.get(0).getTagName(), tags.get(1).getTagName(), tags.get(2).getTagName()};
        	assertArrayEquals(expected, returned);
    	}
    	assertEquals(result, true);
    }

}
