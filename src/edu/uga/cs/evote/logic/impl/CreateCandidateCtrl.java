package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.object.ObjectLayer;

public class CreateCandidateCtrl {
	
	private ObjectLayer objectLayer = null;
	
	public CreateCandidateCtrl(ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}
	
	public long create(String name, String partyName, String Office) throws EVException{
		
		PoliticalParty party = null;
		
		if(partyName.length() > 1){
			party = objectLayer.createPoliticalParty(partyName);
			List<PoliticalParty> parties = objectLayer.findPoliticalParty(party);
			if(parties.size() == 0){
				throw new EVException("CreateCandidateCtrl: Party not found. " + partyName);
			}
			else{
				party = parties.get(0);
			}
		}
		
		Election election = objectLayer.createElection();
		election.setOffice(Office);
		List<Election> elections = objectLayer.findElection(election);
		if(elections.size() == 0){
			throw new EVException("CreateCandidateCtrl: Election not found");
		}
		else{
			election = elections.get(0);
		}
		
		Candidate candidate = objectLayer.createCandidate(name, party, election);
		List<Candidate> candidates = objectLayer.findCandidate(candidate);
		if(candidates.size() == 0){
			objectLayer.storeCandidate(candidate);
		}
		else{
			throw new EVException("CreateCandidateCtrl: Candidate already exists.");
		}
		
		return candidate.getId();
		
	}

}
