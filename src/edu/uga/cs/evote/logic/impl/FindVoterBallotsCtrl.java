package edu.uga.cs.evote.logic.impl;

import java.sql.*;
import java.util.*;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.User;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.*;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.persistence.impl.DbUtils;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;

public class FindVoterBallotsCtrl {

	private ObjectLayer objectLayer = null;

	public FindVoterBallotsCtrl(ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}

	public List<Ballot> find(ElectoralDistrict ED) throws EVException {

		List<Ballot> ballots = ED.getBallots();

		return ballots;




		}
	}
