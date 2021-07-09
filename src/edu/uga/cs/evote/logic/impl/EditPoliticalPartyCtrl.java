package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.object.ObjectLayer;

public class EditPoliticalPartyCtrl {

	private ObjectLayer objectLayer = null;

	public EditPoliticalPartyCtrl (ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}

	public long edit(String oldName, String newName) throws EVException{
		long id = -1;

		PoliticalParty party = objectLayer.createPoliticalParty(oldName);
		List<PoliticalParty> parties = objectLayer.findPoliticalParty(party);
		if(!parties.isEmpty()){
			id = parties.get(0).getId();
			party = objectLayer.createPoliticalParty(newName);
			party.setId(id);
			objectLayer.storePoliticalParty(party);
		}
		else{
			throw new EVException("EditPoliticalPartyCtrl: Party does not exist.");
		}

		return id;
	}

}
