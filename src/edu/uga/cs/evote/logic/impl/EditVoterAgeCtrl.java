package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;

public class EditVoterAgeCtrl{
	
	private ObjectLayer objectLayer = null;
	String firstName = null;
	String lastName = null;
	String userPass = null;
	String userName = null;
	String email = null;
	String address = null;
	int age;
	String voterId = null;
	long id;
	
	public EditVoterAgeCtrl (ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}
	
	public void edit(String userName, String age) throws EVException{
		
		Voter voterModel = objectLayer.createVoter();
		voterModel.setUserName(userName);
		Voter voter;
		List <Voter> voters = objectLayer.findVoter(voterModel);
		
		if(!voters.isEmpty()){
			
			voter = voters.get(0);
			voter.setAge(Integer.parseInt(age));
			objectLayer.storeVoter(voter);
			
			
			
		}
		
		else
			throw new EVException("Username does not exsist");
		
			
		
		
	}
}
