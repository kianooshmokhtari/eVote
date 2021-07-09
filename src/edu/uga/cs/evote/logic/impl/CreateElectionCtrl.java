package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.object.ObjectLayer;

public class CreateElectionCtrl {
	private ObjectLayer objectLayer = null;
	
	public CreateElectionCtrl(ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}
	
	public long create(String office, boolean isPartisan, String ballotID) throws EVException {
		
		Ballot ballot = objectLayer.createBallot();
		ballot.setId(Long.parseLong(ballotID));
		List<Ballot> ballots = objectLayer.findBallot(ballot);
		if(ballots.size() == 0){
			throw new EVException("CreateElectionCtrl: Ballot not found.");
		}
		else{
			ballot = ballots.get(0);
		}
		
		Election election = objectLayer.createElection(office, isPartisan, ballot);
		List<Election> elections = objectLayer.findElection(election);
		if(elections.size() == 0){
			objectLayer.storeElection(election);
		}
		else{
			throw new EVException("CreateElectionCtrl: Election already exists.");
		}
		
		return election.getId();
	}
}
