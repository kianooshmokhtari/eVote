package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;

public class ResetPasswordCtrl {
	
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
	
	public ResetPasswordCtrl (ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}
	
	public void reset(String userName, String newPass) throws EVException{
		int age = -1;
		
		Voter voterModel = objectLayer.createVoter(null, null, userName, null, null, null, age);
		List<Voter> voters = objectLayer.findVoter(voterModel);
		if(!voters.isEmpty()){
			firstName =	 voters.get(0).getFirstName();
			lastName = 	 voters.get(0).getLastName();
			userPass =   newPass;
			userName =   voters.get(0).getUserName();
			email = 	 voters.get(0).getEmailAddress();
			address = voters.get(0).getAddress();
			age = 		voters.get(0).getAge();
			voterId = 	voters.get(0).getVoterId();
			id =		voters.get(0).getId();
			Voter voter = objectLayer.createVoter(firstName, lastName, userName, userPass, email, address, age );
			voter.setId(id);
			voter.setVoterId(null);
			/*
			boolean say = true;
			if(say)
				throw new EVException("this " + " " + firstName + " " + lastName + " "
						+ userPass + " " + userName + " " + email + " " + address + " "
						+ age + " " + id);
			*/
			 objectLayer.storeVoter(voter);
			}
		else{
			throw new EVException("Username Entered Does not exsist");
		}
		
		
	}
	
}