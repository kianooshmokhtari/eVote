package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.*;
import edu.uga.cs.evote.object.ObjectLayer;

public class GetProfileCtrl {
	
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
	
	public GetProfileCtrl (ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}
	
	public Voter getProfile(String userName) throws EVException{
		
		int age = -1;
		
		Voter voterModel = objectLayer.createVoter(null, null, userName, null, null, null, age);
		
		List <Voter> voters = objectLayer.findVoter(voterModel);
		
		Voter profile = objectLayer.createVoter();
		
		if(!voters.isEmpty()){
			
			firstName =	 voters.get(0).getFirstName();
			lastName = 	 voters.get(0).getLastName();
			userPass =   voters.get(0).getPassword();
			userName =   voters.get(0).getUserName();
			email = 	 voters.get(0).getEmailAddress();
			address = voters.get(0).getAddress();
			age = 		voters.get(0).getAge();
			voterId = 	voters.get(0).getVoterId();
			id =		voters.get(0).getId();
			
			profile.setFirstName(firstName);
			profile.setLastName(lastName);
			profile.setUserName(userName);
			profile.setPassword(userPass);
			profile.setEmailAddress(email);
			profile.setAddress(address);
			profile.setAge(age);
			profile.setId(id);
			profile.setVoterId(voterId);
			
			
			
			
			
			
		}
		else
			throw new EVException("User Name does not exsist");
		
		
		
		return profile;
		
		
		
		
	}
	/*
	public ElectoralDistrict getDistrict(String id){
		
		Voter voterModel 
		
		ElectoralDistrict ED = objectLayer.
		
		
		
		
	}
	*/
	
}