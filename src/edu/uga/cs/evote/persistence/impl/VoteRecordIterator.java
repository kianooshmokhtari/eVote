package edu.uga.cs.evote.persistence.impl;

import java.sql.ResultSet;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.uga.cs.evote.entity.*;
import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.object.ObjectLayer;

public class VoteRecordIterator implements Iterator<VoteRecord> {

	private ResultSet		rs = null;
	private ObjectLayer		objectLayer = null;
	private boolean			more = false;

	public VoteRecordIterator (ResultSet rs, ObjectLayer objectLayer) throws EVException {

		this.rs = rs;
		this.objectLayer = objectLayer;
		try {
			more = rs.next();
		}
		catch (Exception e) {
			throw new EVException ("VoteRecordIterator: Cannot create VoteRecord iterator; root cause: " + e);
		}
	}

	public boolean hasNext() {

		return more;
	}


	public VoteRecord next() {

		// voteRecord
		long	vrid = 0;
		Date	date;
		String	voterID;
		int		ballotID;

		// voter/user
		Long	uid = null;
		String	firstname;
		String	lastname;
		String	username;
		String	password;
		String	email;
		String	address;
		int		age;
		String	voterId;

		// ballot
		long	bid;
		Date	openDate;
		Date	closeDate;
		boolean	approved;

		Voter voter = null;
		Ballot ballot = null;
		VoteRecord nextVoteRecord = null;

		if (more) {

			try {
				voterID = rs.getString(1);
				ballotID = rs.getInt(2);
				date = rs.getDate(3);
				firstname = rs.getString(4);
				lastname = rs.getString(5);
				username = rs.getString(6);
				password = rs.getString(7);
				email = rs.getString(8);
				address = rs.getString(9);
				age = rs.getInt(10);
				voterId = rs.getString(11);
				openDate = rs.getDate(12);
				closeDate = rs.getDate(13);
				approved = rs.getBoolean(14);

				more = rs.next();
			} catch (Exception e) {      // just in case...
				throw new NoSuchElementException( "VoteRecordIterator: No next VoteRecord object; root cause: " + e );
			}
			try {
				voter = objectLayer.createVoter(firstname, lastname, username, password, email, address, age);
				voter.setId(uid);

				ballot = objectLayer.createBallot(openDate, closeDate, approved, null);

				nextVoteRecord = objectLayer.createVoteRecord(ballot, voter, date);
				nextVoteRecord.setId(vrid);
			} catch (EVException ev) {
				ev.printStackTrace();
				System.out.println(ev);
			}
			return nextVoteRecord;
		} else {
			throw new NoSuchElementException( "VoteRecordIterator: No next VoteRecord object" );
		}
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
