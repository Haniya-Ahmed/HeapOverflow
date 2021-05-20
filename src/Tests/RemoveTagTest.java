package tests;
import dataManager.*;
import objects.*;
import control.*;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RemoveTagControlTest {
	private DataManager dm;
	private CreatePostControl pCtrl;
    private RemoveTagControl control;
	
    @BeforeEach
   	void setUp() throws Exception {
   		dm = new DataManager();
   		pCtrl = new CreatePostControl(dm);
   		control = new RemoveTagControl(dm);
   	}

    @Test
    void testRemoveTag1() {
    	boolean result = false;
    	PostObject post = pCtrl.addPost("RemoveTagControlTest post", 1, "RemoveTagControlTest post", "<C><Postgres>");
    	if(post == null) {
    		fail("No post created.");
    	}
    	result = control.deleteTag(post.getId(), "<Postgres>");
    	if(result) {
    		ArrayList<TagObject> tags = dm.getTagsByPostId(post.getId());
    		String[] expected = {"C"};
        	String[] returned = {tags.get(0).getTagName()};
        	assertArrayEquals(expected, returned);
    	}
    	System.out.println(result);
    	assertEquals(result, true);
    }
    
    @Test
    void testRemoveTag2() {
    	boolean result = false;
    	PostObject post = pCtrl.addPost("RemoveTagControlTest post2", 1, "RemoveTagControlTest post2", "<C><Postgres><MySQL>");
    	if(post == null) {
    		fail("No post created.");
    	}
    	result = control.deleteTag(post.getId(), "<Postgres><C>");
    	if(result) {
    		ArrayList<TagObject> tags = dm.getTagsByPostId(post.getId());
    		String[] expected = {"MySQL"};
        	String[] returned = {tags.get(0).getTagName()};
        	assertArrayEquals(expected, returned);
    	}
    	assertEquals(result, true);
    }

}
