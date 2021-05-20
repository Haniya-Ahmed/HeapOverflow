package tests;
import dataManager.*;
import control.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//import jdk.jfr.Timestamp;

public class LoginControlTest {
    private DataManager dm;
    private LoginControl control;

    @BeforeEach
	void setUp() throws Exception {
		dm = new DataManager();
		control = new LoginControl(dm);
	}

    @Test
	void testLogin1() {
		boolean result = control.login("admin", "Team1Admin");
		assertEquals(true, result);
        String expected = "admin";
        String returned = control.verifyAccount().getUsername();
        assertEquals(expected, returned);
	}

    @Test
	void testLogin2() {
		boolean result = control.login("DoesNotExist", "FakePass");
		assertEquals(false, result);
		assertNull(control.verifyAccount());
	}
}
