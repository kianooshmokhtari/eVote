

package edu.uga.cs.evote.logic.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Date;


import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.entity.VoteRecord;





public class FindVoteRecordCtrl
{
    
    private ObjectLayer objectLayer = null;
    
    boolean yesB = false;
    boolean noB = false;
    
    
    public FindVoteRecordCtrl( ObjectLayer objectModel )
    {
        this.objectLayer = objectModel;
    }
    
    public List <VoteRecord> find( Voter voter)
    throws EVException
    {
    	
    	VoteRecord VRmodel = objectLayer.createVoteRecord();
    	VRmodel.setVoter(voter);
    	
    	List<VoteRecord> VR = objectLayer.findVoteRecord(VRmodel);
    	
    	return VR;
    	
    	  
       }
    	

        
    
}


