

package edu.uga.cs.evote.logic.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Date;


import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.entity.VoteRecord;





public class CreateVoterRecordCtrl
{
    
    private ObjectLayer objectLayer = null;
    
   
    
    
    public CreateVoterRecordCtrl( ObjectLayer objectModel )
    {
        this.objectLayer = objectModel;
    }
    
    public void Create( Voter voter, String ballotId )
    throws EVException
    {
    	
    	long five = 5;
    	
    	//throw new EVException("WWWWWTFFFF");
    	
    	Ballot ballotModel = objectLayer.createBallot();
    	ballotModel.setId(Long.parseLong(ballotId));
    	List<Ballot> ballots = objectLayer.findBallot(ballotModel);
    	if(!ballots.isEmpty()){
    		
    		Ballot ballot = ballots.get(0);
    		
    		VoteRecord VR = objectLayer.createVoteRecord();
    		VR.setBallot(ballot);
    		VR.setVoter(voter);
    		objectLayer.storeVoteRecord(VR);
    		
    		//throw new EVException("we have stored voterRecord");
    		
    		
    		
    		
    	}
    	else
    		throw new EVException("could not restore ballots");
    	
    	
    	
    	
    	  
       }
    	

        
    
}


