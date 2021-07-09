package edu.uga.cs.evote.logic.impl;

import java.sql.*;
import java.util.*;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.object.ObjectLayer;

public class DeletePartyCtrl {

	private ObjectLayer objectLayer = null;

	public DeletePartyCtrl(ObjectLayer objectLayer){
		this.objectLayer = objectLayer;
	}

	public long delete(String name) throws EVException{
		long id = -1;
		PoliticalParty party = objectLayer.createPoliticalParty(name);
		List<PoliticalParty> parties = objectLayer.findPoliticalParty(party);
		if(parties.size() == 0){
			throw new EVException( "DeletePartyCtrl: Party does not exist." );
		}
		else{
			party = parties.get(0);
			id = party.getId();
			List<Candidate> candidates = party.getCandidates();
			if(candidates.size() == 0){
				objectLayer.deletePoliticalParty(party);
			}
			else{
				throw new EVException( "DeletePartyCtrl: Party has linked candidates." );
			}

		}
		return id;
	}

}
