package edu.uga.cs.evote.logic.impl;

import java.sql.*;
import java.util.*;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.User;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.persistence.impl.DbUtils;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;

public class FindAllCandidatesCtrl {
	
	private ObjectLayer objectLayer = null;
	
	public FindAllCandidatesCtrl(ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}

	public List<String> find() throws EVException {
		
		List<Candidate> candidates = objectLayer.findCandidate(null);
		List<String> result = new ArrayList<String>();
		
		for (int i = 0; i < candidates.size(); i++) {
			result.add( candidates.get(i).toString() );
		}
		
		return result;
		
	}
	
}
