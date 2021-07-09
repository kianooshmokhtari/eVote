package edu.uga.cs.evote.logic.impl;

import java.sql.*;
import java.util.*;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.User;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.persistence.impl.DbUtils;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;

public class FindAllPartiesCtrl {

	private ObjectLayer objectLayer = null;

	public FindAllPartiesCtrl(ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}

	public List<String> find() throws EVException {

		List<PoliticalParty> parties = objectLayer.findPoliticalParty(null);
		List<String> result = new ArrayList<String>();

		for (int i = 0; i < parties.size(); i++) {
			result.add( parties.get(i).toString() );
		}

		return result;

	}

}
