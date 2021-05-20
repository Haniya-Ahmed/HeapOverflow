package tests;
import dataManager.*;
import objects.*;
import control.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EditPostControlTest {
	private DataManager dm;
    private EditPostControl control;

    @BeforeEach
	void setUp() throws Exception {
		dm = new DataManager();
		control = new EditPostControl(dm);
	}
    
    @Test
   	void testVerifyEdit1() {
       	boolean result = false;
       	PostObject post = dm.addPostByText("SDIABSDBxvo;dfbsafodHOSAISD", 1, "TestTitle", "<SQL>");
       	if(post != null) {
       		result = control.editPost("JUnitTest", post.getId());
       		if(result) {
       			post = dm.getPostById(post.getId());
       			String expected = "JUnitTest";
       			String returned = post.getBody();
       			assertEquals(expected, returned);
       		}
       	}
   		assertEquals(true, result);
   	}
    
    @Test
	void testVerifyEdit2() {
		boolean result = control.editPost("JUnitTest", 50049);
		assertEquals(false, result);
	}

}
