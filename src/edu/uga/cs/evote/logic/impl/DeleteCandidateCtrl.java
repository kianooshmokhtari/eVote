package edu.uga.cs.evote.logic.impl;

import java.sql.*;
import java.util.*;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.object.ObjectLayer;

public class DeleteCandidateCtrl {

	private ObjectLayer objectLayer = null;

	public DeleteCandidateCtrl(ObjectLayer objectLayer){
		this.objectLayer = objectLayer;
	}

	public long delete(String name) throws EVException{
		long id = -1;
		Candidate candidate = objectLayer.createCandidate();
		candidate.setName(name);
		List<Candidate> candidates = objectLayer.findCandidate(candidate);
		if(candidates.size() == 0){
			throw new EVException( "DeleteCandidateCtrl: Candidate does not exist." );
		}
		else{
			candidate = candidates.get(0);
			id = candidate.getId();
			objectLayer.deleteCandidate(candidate);
		}

		return id;

	}

}
