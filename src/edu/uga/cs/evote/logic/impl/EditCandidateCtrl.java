package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.object.ObjectLayer;

public class EditCandidateCtrl {
	
	private ObjectLayer objectLayer = null;
	
	public EditCandidateCtrl(ObjectLayer objectLayer){
		this.objectLayer = objectLayer;
	}
	
	public long edit(String oldName,String name, String partyName, String Office) throws EVException{
		
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
		
		Election election = null;
		
		if(Office.length() > 1){
			election = objectLayer.createElection();
			election.setOffice(Office);
			List<Election> elections = objectLayer.findElection(election);
			if(elections.size() == 0){
				throw new EVException("CreateCandidateCtrl: Election not found");
			}
			else{
				election = elections.get(0);
			}
		}
		
		Candidate candidate = objectLayer.createCandidate();
		candidate.setName(oldName);
		List<Candidate> candidates = objectLayer.findCandidate(candidate);
		if(candidates.size() == 0){
			throw new EVException("EditCandidateCtrl: Candidate does not exist.");
		}
		else{
			
			candidate = candidates.get(0);
			
			
			Candidate oldCandidate = objectLayer.createCandidate();
			oldCandidate.setId(candidate.getId());
			oldCandidate.setName(oldName);
			oldCandidate.setElection(candidate.getElection() );
			if(candidate.getPoliticalParty() != null){
				oldCandidate.setPoliticalParty(candidate.getPoliticalParty());
			}
			oldCandidate.setVoteCount(candidate.getVoteCount());
			
			
			
			if(party != null){
				candidate.setPoliticalParty(party);
			}
			
			if(election != null){
				candidate.setElection(election);
			}
			
			if(name.length() > 1){
				candidate.setName(name);
			}
			
			//objectLayer.deleteCandidate(oldCandidate);
			objectLayer.storeCandidate(candidate);
			
		}
		
		return candidate.getId();
		
	}

}
