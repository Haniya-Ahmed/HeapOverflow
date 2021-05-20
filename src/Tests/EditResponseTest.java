package tests;
import dataManager.*;
import objects.*;
import control.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EditResponseControlTest {
	private DataManager dm;
    private EditResponseControl control;
    
    @BeforeEach
	void setUp() throws Exception {
		dm = new DataManager();
		control = new EditResponseControl(dm);
	}
	
    @Test
	void testVerifyEdit1() {
    	boolean result = false;
    	ResponseObject response = dm.addResponseByPostId(1, "SDIABSDBxvo;dfbsafodHOSAISD", 1);
    	if(response != null) {
    		result = control.editResponse(response.getId(), "JUnitTest");
    		if(result) {
    			response = dm.getResponseById(response.getId());
    			String expected = "JUnitTest";
    			String returned = response.getText();
    			assertEquals(expected, returned);
    		}
    	}
		assertEquals(true, result);
	}
    
    //May not need this one, shouldn't be possible under normal circumstances to get false
    @Test
	void testVerifyEdit2() {
		boolean result = control.editResponse(50049, "JUnitTest");
		assertEquals(false, result);
	}

}
