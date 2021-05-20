package tests;
import dataManager.*;
import objects.*;
import control.*;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BrowsePostControlTest {
	private DataManager dm;
	private CreatePostControl aCtrl;
    private BrowsePostControl control;

	 @BeforeEach
		void setUp() throws Exception {
			dm = new DataManager();
			aCtrl = new CreatePostControl(dm);
			control = new BrowsePostControl(dm);
		}
	
	@Test
	void browsePostTest1() {
		ArrayList<PostObject> posts = control.getHome(0);
		assertEquals(posts.size(), 0);
	}
	
	@Test
	void browsePostTest2() {
		aCtrl.addPost("BrowseControlTest post", 1, "BrowseControlTest post", "<Java>");
		ArrayList<PostObject> posts = control.getHome(1);
		assertEquals(posts.size(), 1);
		String expected = "BrowseControlTest post";
		String returned = posts.get(0).getBody();
		assertEquals(expected, returned);
	}

}
