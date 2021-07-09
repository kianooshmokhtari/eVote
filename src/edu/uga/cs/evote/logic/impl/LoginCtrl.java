/**
 * @file LogicLayerImpl.java
 */

package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.User;
import edu.uga.cs.evote.entity.ElectionsOfficer;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;


public class LoginCtrl {

	private ObjectLayer objectLayer = null;

	public LoginCtrl(ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}

	public String login(Session session, String userName, String password)
															throws EVException {
		String ssid = null;

		ElectionsOfficer modelElectionsOfficer = objectLayer.createElectionsOfficer();
		modelElectionsOfficer.setUserName(userName);
		modelElectionsOfficer.setPassword(password);
		List<ElectionsOfficer> officers = objectLayer.findElectionsOfficer(modelElectionsOfficer);
		Voter modelVoter = objectLayer.createVoter();
		modelVoter.setUserName(userName);
		modelVoter.setPassword(password);
		List<Voter> voters = objectLayer.findVoter(modelVoter);
		if (officers.size() > 0) {
			ElectionsOfficer officer = officers.get(0);
			session.setUser(officer);
			ssid = SessionManager.storeSession(session);
		} else if (voters.size() > 0) {
			Voter voter = voters.get(0);
			session.setUser(voter);
			ssid = SessionManager.storeSession(session);
		} else throw new EVException("SessionManager.login: Invalid User Name or Password");

		return ssid;
	}

}
