package tests;
import dataManager.*;
import objects.*;
import control.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeleteResponseControlTest {
	private DataManager dm;
    private DeleteResponseControl control;
	
    @BeforeEach
	void setUp() throws Exception {
		dm = new DataManager();
		control = new DeleteResponseControl(dm);
	}
    
    @Test
	void testVerifyDelete1() {
    	boolean result = false;
    	ResponseObject response = dm.addResponseByPostId(1, "SDIABSDBxvo;dfbsafodHOSAISD", 1);
    	if(response != null) {
    		result = control.deleteResponse(response.getId());
    		if(result) {
    			response = dm.getResponseById(response.getId());
    			assertNull(response);
    		}
    	}
		assertEquals(true, result);
	}
    
    @Test
	void testVerifyDelete2() {
		boolean result = control.deleteResponse(50049);
		assertEquals(false, result);
	}

}
