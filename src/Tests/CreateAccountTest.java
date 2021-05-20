package tests;
import dataManager.*;
import control.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreateAccountControlTest {

	private DataManager dm;
	private CreateAccountControl control;
	
	@BeforeEach
	void setUp() throws Exception {
		dm = new DataManager();
		control = new CreateAccountControl(dm);
	}
	
	@Test
	void testAddAccount1() {
		boolean result = control.addAccount("admin", "Team1Admin");
		assertEquals(false, result);
	}
	
	@Test
	void testAddAccount2() {
		boolean result = control.addAccount("NewUser", "password");
		assertEquals(true, result);
	}

}
