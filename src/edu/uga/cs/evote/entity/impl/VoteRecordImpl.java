/**
 * @file VoteRecordImpl.java
 * @author Team 9: Joey Bruce. Jack Fisher, Kianoosh Mokhtari, Will Runge
 */

package edu.uga.cs.evote.entity.impl;

import java.util.Date;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.persistence.impl.Persistent;
import edu.uga.cs.evote.entity.VoteRecord;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.entity.Ballot;


public class VoteRecordImpl extends Persistent implements VoteRecord {

	// Class attributes
	private Date	date;
	private Voter	voter;
	private Ballot	ballot;

	// Default constructor
	public VoteRecordImpl() {
		super(-1);
		this.ballot =	null;
		this.voter =	null;
		this.date =		null;
	}

	// Specified constructor
	public VoteRecordImpl(Ballot ballot, Voter voter, Date date) {
		super(-1);
		this.ballot =	ballot;
		this.voter =	voter;
		this.date =		date;
	}

	/** Return the date the vote has been cast.
	 * @return the date of the cast vote
	 */
	public Date getDate() {
		return date;
	}

	/** Set the date the vote has been cast.
	 * @param date the new date of the cast vote
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/** Return the Voter who cast the vote.
	 * @return the voter who cast the vote
	 * @throws EVException in case there is a problem with the retrieval of the requested object
	 */
	public Voter getVoter() throws EVException {
		return this.voter;
	}

	/** Set the Voter who cast the vote.
	 * @param voter the new voter who cast the vote
	 * @throws EVException in case there is a problem with setting a link to the argument object
	 */
	public void setVoter(Voter voter) throws EVException {
		this.voter = voter;
	}

	/** Return the Ballot on which the vote has been cast.
	 * @return the ballot on which the vote has been cast
	 * @throws EVException in case there is a problem with the retrieval of the requested object
	 */
	public Ballot getBallot() throws EVException {
		return this.ballot;
	}

	/** Set the new Ballot on which the vote has been cast.
	 * @param ballot the new ballot on which the vote has been cast
	 * @throws EVException in case there is a problem with setting a link to the argument object
	 */
	public void setBallot(Ballot ballot) throws EVException {
		this.ballot = ballot;
	}

	public String toString() {
		return "VoteRecord[" + this.getId() + "] " + this.getDate();
	}

}
