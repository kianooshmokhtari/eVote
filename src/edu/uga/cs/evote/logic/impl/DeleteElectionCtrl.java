package edu.uga.cs.evote.logic.impl;

import java.sql.*;
import java.util.*;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.object.ObjectLayer;

public class DeleteElectionCtrl {
	
	private ObjectLayer objectLayer = null;
	
	public DeleteElectionCtrl(ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}
	
	public long delete(String ID) throws EVException{
		
		Election election = objectLayer.createElection();
		long id = Long.parseLong(ID);
		election.setId(id);
		List<Election> elections = objectLayer.findElection(election);
		if(elections.size() == 0){
			throw new EVException("DeleteElectionCtrl: Election does not exist.");
		}
		else{
			election = elections.get(0);
			List<Candidate> candidates = election.getCandidates();
			
			if(candidates.size() > 0){
				throw new EVException("DeleteDistrictCtrl: District has linked candidates.");
			}
			else{
				objectLayer.deleteElection(election);
			}
		}
		
		return election.getId();
	}

}
