/**
 * @file VoterImpl.java
 * @author Team 9: Joey Bruce. Jack Fisher, Kianoosh Mokhtari, Will Runge
 */

package edu.uga.cs.evote.entity.impl;

import java.util.List;
import java.util.ArrayList; // lel

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.VoteRecord;


/**
 * This class represents information about a registered voter in the eVote
 * system. A voter is a user who additionally has an age and a voter ID.
 */
public class VoterImpl extends UserImpl implements Voter {

	// Class attributes
	private String	voterId;
	private int 	age;

	// Default constructor
	public VoterImpl() {
		super();
		this.voterId =	null;
		this.age = 		-1;
	}

	// Constructor with specified values
	public VoterImpl(
			String firstName, String lastName, String username, String password,
			String email, String address, int age) {
		super(firstName, lastName, username, password, email, address);
		this.voterId =	null;
		this.age =		age;
	}


	/** Return the voter id for this voter.
	 * @return the String representing the id of the voter
	 */
	public String getVoterId() {
		return this.voterId;
	}

	/** Set the new voter id for this voter.
	 * @param voterId the new voter id of this voter
	 */
	public void setVoterId(String voterId) {
		this.voterId = voterId;
	}

	/** Return the age of this voter.
	 * @return the age of this voter
	 */
	public int getAge() {
		return this.age;
	}

	/** Set the new age for this voter
	 * @param age the new age of this voter
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/** Return the ElectoralDistrict of this voter
	 * @return the ElectoralDistrict of this voter
	 * @throws EVException in case there is a problem with traversing a link to the requested object
	 */
	public ElectoralDistrict getElectoralDistrict() throws EVException {
		if (isPersistent()) {
			return getPersistenceLayer().restoreVoterBelongsToElectoralDistrict(this);
		} else throw new EVException("This Voter object is not persistent.");
	}

	/** Set the new ElectoralDistrict of this voter
	 * @param electoralDistrict the new ElectoralDistrict
	 * @throws EVException in case there is a problem with setting a link to the requested object
	 */
	public void setElectoralDistrict(ElectoralDistrict electoralDistrict) throws EVException {
		if (isPersistent()) {
			getPersistenceLayer().storeVoterBelongsToElectoralDistrict(this, electoralDistrict);
		} else throw new EVException("This Voter object is not persistent.");
	}

	/** Return a list of VoteRecords for this Voter.
	 * @return a List of VoteRecords recorded for this Voter
	 * @throws EVException in case there is a problem with traversing links to the requested objects
	 */
	public List<VoteRecord> getBallotVoteRecords() throws EVException {
		if (isPersistent()) {
			// TODO: Implement
			return new ArrayList<VoteRecord> (); // lel
		} else throw new EVException("This Voter object is not persistent.");
	}

	public String toString() {
		return "Voter[" + this.getId() + "] " + this.getFirstName() + " "
				+ this.getLastName() + " " + this.getUserName() + " "
				+ this.getPassword() + " " + this.getEmailAddress() + " "
				+ this.getAddress() + " " + this.getAge() + " "
				+ this.getVoterId();
	}

}
