package tests;
import dataManager.*;
import objects.*;
import control.*;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchPostControlTest {
	private DataManager dm;
    private SearchPostControl control;

    @BeforeEach
	void setUp() throws Exception {
		dm = new DataManager();
		control = new SearchPostControl(dm);
	}
    
    @Test
	void testSearch1() {
		ArrayList<PostObject> post = control.getPosts("SQL");
		assertEquals(1, post.get(0).getId());
	}
    
    @Test
	void testSearch2() {
		ArrayList<PostObject> post = control.getPosts("NotARealTag");
		assertEquals(0, post.size());
	}
    
    @Test
	void testSearch3() {
		ArrayList<PostObject> post = control.getPosts("SQL Java");
		assertEquals(1, post.get(0).getId());
		assertEquals(2, post.get(1).getId());
	}
}
