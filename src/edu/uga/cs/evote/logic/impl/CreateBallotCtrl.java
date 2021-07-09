package edu.uga.cs.evote.logic.impl;

import java.util.Date;
import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.object.ObjectLayer;

public class CreateBallotCtrl {
	
	private ObjectLayer objectLayer = null;
	
	public CreateBallotCtrl(ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}
	
	public long create(Date openDate, Date closeDate, boolean approved, String electoralDistrict) throws EVException {
		
		ElectoralDistrict district = objectLayer.createElectoralDistrict(electoralDistrict);
		List<ElectoralDistrict> districts = objectLayer.findElectoralDistrict(district);
		if(districts.size() == 0){
			throw new EVException("CreateBallotCtrl: District not found.");
		}
		else{
			district = districts.get(0);
		}
		
		Ballot ballot = objectLayer.createBallot(openDate, closeDate, approved, district);
		
		List<Ballot> ballots = objectLayer.findBallot(ballot);
		if(ballots.size() == 0){
			objectLayer.storeBallot(ballot);
		}
		else{
			throw new EVException("CreateBallotCtrl: Ballot already exists.");
		}
		
		return ballot.getId();
		
	}
	
}
