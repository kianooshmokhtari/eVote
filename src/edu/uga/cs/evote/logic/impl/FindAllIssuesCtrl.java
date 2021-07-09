package edu.uga.cs.evote.logic.impl;

import java.sql.*;
import java.util.*;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.User;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.persistence.impl.DbUtils;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;

public class FindAllIssuesCtrl {

	private ObjectLayer objectLayer = null;

	public FindAllIssuesCtrl(ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}

	public List<String> find() throws EVException {

		List<Issue> issues = objectLayer.findIssue(null);
		List<String> result = new ArrayList<String>();

		for (int i = 0; i < issues.size(); i++) {
			result.add( issues.get(i).toString() );
		}

		return result;

	}

}
