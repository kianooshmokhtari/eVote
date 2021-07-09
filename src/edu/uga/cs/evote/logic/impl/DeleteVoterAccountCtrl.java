package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;


public class DeleteVoterAccountCtrl{
	private ObjectLayer objectLayer = null;
	private String		userName = null;
	
	
	
	
	
	public DeleteVoterAccountCtrl(ObjectLayer objectLayer){
		
		this.objectLayer = objectLayer;
		
	}
	
	
	public void delete(String userName) throws EVException{
		
		Voter voterModel = objectLayer.createVoter();
		voterModel.setUserName(userName);
		List <Voter> voters = objectLayer.findVoter(voterModel);
		if(!voters.isEmpty()){
			
			Voter vote = voters.get(0);
			
			objectLayer.deleteVoter(vote);
		
			
			
		}
		else{
			throw new EVException("Could not delete User");
		}
		
		
		
		
		
		
		
		
		
		
	}
}