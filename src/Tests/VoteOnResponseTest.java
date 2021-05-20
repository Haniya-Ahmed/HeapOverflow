package tests;
import dataManager.*;
import objects.*;
import control.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VoteOnResponseControlTest {
	private DataManager dm;
    private VoteOnResponseControl control;

    @BeforeEach
	void setUp() throws Exception {
		dm = new DataManager();
		control = new VoteOnResponseControl(dm);
	}
    
    @Test
    void addVoteTest()  {
    	ResponseObject response = dm.addResponseByPostId(1, "VoteOnResponseTest", 2);
    	control.addVote(response.getId(), (long)1, (short)1);
    	response = dm.getResponseById(response.getId());
    	assertEquals(1, response.getScore());
    }
    
    @Test
    void verifyVoteTest()  {
    	ResponseObject response = dm.addResponseByPostId(1, "VoteOnResponseTest2", 2);
    	control.addVote(response.getId(), (long)1, (short)1);
    	boolean result = control.addVote(response.getId(), (long)1, (short)1);
    	assertEquals(result, false);
    	response = dm.getResponseById(response.getId());
    	assertEquals(1, response.getScore());
    }
    
    @Test
    void editVoteTest()  {
    	ResponseObject response = dm.addResponseByPostId(1, "VoteOnResponseTest3", 2);
    	control.addVote(response.getId(), (long)1, (short)-1);
    	response = dm.getResponseById(response.getId());
    	assertEquals(-1, response.getScore());
    	VoteObject vote = dm.getVotesByUserAndResponse(1, response.getId());
    	control.editVote((short)1, vote.getId());
    	response = dm.getResponseById(response.getId());
    	assertEquals(1, response.getScore());
    }
	
}
