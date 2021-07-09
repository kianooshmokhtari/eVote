/**
 * @file CandidateImpl.java
 * @author Team 9: Joey Bruce. Jack Fisher, Kianoosh Mokhtari, Will Runge
 */

package edu.uga.cs.evote.entity.impl;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.persistence.impl.Persistent;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.PoliticalParty;


public class CandidateImpl extends Persistent implements Candidate {

	// Class attributes
	private String			name;
	private PoliticalParty	party;
	private Election		election;
	private int				voteCount;

	// Default constructor
	public CandidateImpl() {
		super(-1);
		this.name =			null;
		this.party = 		null;
		this.election =		null;
		this.voteCount= 	0;
	}

	// Specified constructor
	public CandidateImpl(String name, PoliticalParty party, Election election) {
		super(-1);
		this.name =			name;
		this.party = 		party;
		this.election =		election;
		this.voteCount= 	0;
	}

	/** Return the name of this candidate
	 * @return the name of this candidate
	 */
	public String getName() {
		return this.name;
	}

	/** Set the new name of this candidate
	 * @param name the new name of this candidate
	 */
	public void setName(String name) {
		this.name = name;
	}

	/** Return the vote count for this candidate
	 * @return the vote count for this candidate
	 */
	public int getVoteCount() {
		return this.voteCount;
	}

	/** Set the vote count for this candidate
	 * @param voteCount the new vote count for this candidate
	 * @throws EVException in case the new vote is negative
	 */
	public void setVoteCount(int voteCount) throws EVException {
		if (voteCount < 0) throw new EVException("Vote count cannot be negative.");
		else this.voteCount = voteCount;
	}

	/** Add one vote (increment by one) to the votes cast for this Candidate.
	 */
	public void addVote() {
		this.voteCount++;
	}

	/** Return the Election for which this candidate is running.
	 * @return the Election for this candidate
	 * @throws EVException in case there is a problem with traversing a link to the requested object
	 */
	public Election getElection() throws EVException {
		if (this.election == null) {
			if (isPersistent()) {
				this.election = getPersistenceLayer().restoreCandidateIsCandidateInElection(this);
			} else throw new EVException("This Candidate object is not persistent.");
		}
		return this.election;
	}

	/** Set the Election for which this candidate is running.
	 * @param election the new election for this candidate
	 * @throws EVException in case there is a problem with setting a link to the requested object
	 */
	public void setElection(Election election) throws EVException {
		this.election = election;
		if (isPersistent()) {
			getPersistenceLayer().storeCandidateIsCandidateInElection(this, election);
		} else throw new EVException("This Candidate object is not persistent.");
	}

	/** Return the political party of this candidate.
	 * @return the PoliticalParty of this candidate
	 * @throws EVException in case there is a problem with traversing a link to the requested object
	 */
	public PoliticalParty getPoliticalParty() throws EVException {
		if (this.party == null) {
			if (isPersistent()) {
				this.party = getPersistenceLayer().restoreCandidateIsMemberOfPoliticalParty(this);
			} else throw new EVException("This Candidate object is not persistent.");
		}
		return this.party;
	}

	/** Set the political party of this candidate.
	 * @param politicalParty the new PoiticalParty
	 * @throws EVException in case there is a problem with setting a link to the requested object
	 */
	public void setPoliticalParty(PoliticalParty politicalParty) throws EVException {
		this.party = politicalParty;
		if (isPersistent()) {
			getPersistenceLayer().storeCandidateIsMemberOfPoliticalParty(this, politicalParty);
		} else throw new EVException("This Candidate object is not persistent.");
	}

	public String toString() {
		return "Candidate[" + this.getId() + "] " + this.getName() + " "
				+ this.getVoteCount();
	}

}
