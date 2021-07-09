package edu.uga.cs.evote.persistence.impl;

import java.sql.ResultSet;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.uga.cs.evote.entity.*;
import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.object.ObjectLayer;

public class VoterIterator implements Iterator<Voter> {

	private ResultSet		rs = null;
	private ObjectLayer		objectLayer = null;
	private boolean			more = false;

	public VoterIterator (ResultSet rs, ObjectLayer objectModel) throws EVException {

		this.rs = rs;
		this.objectLayer = objectModel;
		try {
			more = rs.next();
		}
		catch (Exception e) {
			throw new EVException ("VoterIterator: Cannot create Voter iterator; root cause: " + e);
		}
	}

	public boolean hasNext () {

		return more;
	}


	public Voter next () {

		long				id;
		String				type;
		String				firstName;
		String				lastName;
		String				userName;
		String				password;
		String				emailAddress;
		String				address;
		String				voterId;
		int					age;

		Voter 				voter = null;
		ElectoralDistrict 	electoralDistrict = null;

		long				edid;
		String				edname;

		if (more) {

			try {
				id = rs.getLong(1);
				type = rs.getString(2);
				firstName = rs.getString(3);
				lastName = rs.getString(4);
				userName = rs.getString(5);
				password = rs.getString(6);
				emailAddress = rs.getString(7);
				address = rs.getString(8);
				voterId = rs.getString(9);
				age = rs.getInt(10);
				edid = rs.getLong(11);
				edname = rs.getString(12);

				more = rs.next();
			}
			catch (Exception e) {
				throw new NoSuchElementException("VoterIterator: No next Voter Object; root cause: " + e);
			}

			try {
				electoralDistrict = objectLayer.createElectoralDistrict(edname);
			}
			catch (EVException e) {
				e.printStackTrace();
			}
			electoralDistrict.setId(edid);

			try {
				voter = objectLayer.createVoter(firstName, lastName, userName, password, emailAddress, address, age);
			} catch (EVException eve) {
				// safe to ignore: we explicitly set the persistent id of the founder Person object above!
			}

			return voter;
		}
		else {
			throw new NoSuchElementException("VoterIterator: No next Voter object");
		}
	}

	public void remove () {

		throw new UnsupportedOperationException();
	}
}
