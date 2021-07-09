package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.User;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;

public class CreatePoliticalPartyCtrl {

	private ObjectLayer objectLayer = null;

	public CreatePoliticalPartyCtrl(ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}

	public long create(Session session, String name) throws EVException {

		PoliticalParty party = objectLayer.createPoliticalParty(name);

		List<PoliticalParty> parties = objectLayer.findPoliticalParty(party);
		if(parties.size() == 0){
			objectLayer.storePoliticalParty(party);
		}
		else{
			throw new EVException("CreatePoliticalPartyCtrl: Party already exists.");
		}

		return party.getId();

	}

}
