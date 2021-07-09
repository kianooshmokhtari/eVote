package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.object.ObjectLayer;

public class EditElectionCtrl {
	
	private ObjectLayer objectLayer = null;
	
	public EditElectionCtrl( ObjectLayer objectLayer){
		this.objectLayer = objectLayer;
	}
	
	public long edit(String originalOffice, String office, boolean isPartisan, String ballotID) throws EVException{
		long id = -1;
		Ballot ballot = objectLayer.createBallot();
		ballot.setId(Long.parseLong(ballotID));
		List<Ballot> ballots = objectLayer.findBallot(ballot);
		if(ballots.size() == 0){
			throw new EVException("EditElectionCtrl: Ballot not found.");
		}
		else{
			ballot = ballots.get(0);
		}
		
		Election election = objectLayer.createElection(originalOffice, isPartisan, ballot);
		List<Election> elections = objectLayer.findElection(election);
		if(!elections.isEmpty()){
			objectLayer.deleteElection(election);
			id = elections.get(0).getId();
			election = objectLayer.createElection(office, isPartisan, ballot);
			election.setId(id);
			objectLayer.storeElection(election);
		}
		else{
			throw new EVException("EditElectionCtrl: Election doesn't exist.");
		}
		return id;
	}

}
