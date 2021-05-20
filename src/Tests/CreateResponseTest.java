package tests;
import dataManager.*;
import objects.*;
import control.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreateResponseControlTest {
	private DataManager dm;
    private CreateResponseControl control;
    
    @BeforeEach
	void setUp() throws Exception {
		dm = new DataManager();
		control = new CreateResponseControl(dm);
	}
    
    @Test
    void testAddResponse1() {
    	ResponseObject response = control.addResponse(1, "TestResponse1", 2);
    	if(response == null) {
    		fail("No response created.");
    	}
    	String expected = "TestResponse1";
    	String returned = response.getText();
    	assertEquals(expected, returned);
    }

}
