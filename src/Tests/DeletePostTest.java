package tests;
import dataManager.*;
import objects.*;
import control.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeletePostControlTest {
	private DataManager dm;
	private CreatePostControl cCtrl;
    private DeletePostControl control;
    
    @BeforeEach
	void setUp() throws Exception {
		dm = new DataManager();
		cCtrl = new CreatePostControl(dm);
		control = new DeletePostControl(dm);
	}

    @Test
	void testVerifyDelete1() {
    	boolean result = false;
    	PostObject post = cCtrl.addPost("DeletePostTest", 1, "DeletePostTest", "<C>");
    	if(post != null) {
    		result = control.deletePost(post.getId());
    		if(result) {
    			post = dm.getPostById(post.getId());
    			assertNull(post);
    		}
    	}
		assertEquals(true, result);
	}
    
    @Test
	void testVerifyDelete2() {
		boolean result = control.deletePost(50049);
		assertEquals(false, result);
	}

}
