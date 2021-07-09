package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.object.ObjectLayer;

public class EditVoterDistrictCtrl{
	
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
	
	public EditVoterDistrictCtrl (ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}
	
	public void edit(String userName, String newDistrict) throws EVException{
		ElectoralDistrict ED;
		ElectoralDistrict EDparam = objectLayer.createElectoralDistrict();
		long id;
		Voter voterModel = objectLayer.createVoter();
		voterModel.setUserName(userName);
		ElectoralDistrict EDModel = objectLayer.createElectoralDistrict(newDistrict);
		List <ElectoralDistrict> electoralDistricts = objectLayer.findElectoralDistrict(EDModel);
		
		
		if(!electoralDistricts.isEmpty()){
			
			 ED = electoralDistricts.get(0);
			 id = ED.getId();
			 EDparam.setId(id);
			
		}
		else
			throw new EVException("Electoral District Does not Exsist");
		
		Voter voter;
		List <Voter> voters = objectLayer.findVoter(voterModel);
		
		if(!voters.isEmpty()){
			
			voter = voters.get(0);
			voter.setElectoralDistrict(EDparam);
			
			
			
		}
		else
			throw new EVException("Username does not exsist");
		
			
		
		
	}
}
