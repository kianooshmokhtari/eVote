/**
 * @file ElectionImpl.java
 * @author Team 9: Joey Bruce. Jack Fisher, Kianoosh Mokhtari, Will Runge
 */

package edu.uga.cs.evote.entity.impl;

import java.util.List;
import java.util.ArrayList;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Candidate;


public class ElectionImpl extends BallotItemImpl implements Election {

	// Class attributes
	private String	office;
	private boolean	isPartisan;

	// Default constructor
	public ElectionImpl() {
		super();
		this.office =		null;
		this.isPartisan =	false;
	}

	// Specified constructor
	public ElectionImpl(String office, boolean isPartisan, Ballot ballot) {
		super(ballot);
		this.office =		office;
		this.isPartisan =	isPartisan;
	}

	/** Return the office for which this election is held.
	 * @return the office of this election
	 */
	public String getOffice() {
		return this.office;
	}

	/** Set the new office for which this election is held.
	 * @param office the new office for this election
	 */
	public void setOffice(String office) {
		this.office = office;
	}

	/** Return true if this is a partisan election and false otherwise.
	 * @return partisan status of this election
	 */
	public boolean getIsPartisan() {
		return this.isPartisan;
	}

	/** Set the new partisan status of this election.
	 * @param isPartisan the new partisan status
	 */
	public void setIsPartisan(boolean isPartisan) {
		this.isPartisan = isPartisan;
	}

	/** Return a list of the candidates for this election.
	 * @return the list of the candidates
	 * @throws EVException in case there is a problem with traversing a link to the requested objects
	 */
	public List<Candidate> getCandidates() throws EVException {
		if (isPersistent())
			return getPersistenceLayer().restoreCandidateIsCandidateInElection(this);
		else throw new EVException("This Election object is not persistent.");
	}

	/** Add a candidate for this election.
	 * @param candidate the candidate to be added
	 * @throws EVException in case there is a problem with setting a link to the requested object
	 */
	public void addCandidate(Candidate candidate) throws EVException {
		if (isPersistent())
			getPersistenceLayer().storeCandidateIsCandidateInElection(candidate, this);
		else throw new EVException("This Election object is not persistent.");
	}

	/** Remove the candidate from the candidates for this election.
	 * @param candidate to be removed
	 * @throws EVException in case there is a problem with removing the link to the requested object
	 */
	public void deleteCandidate(Candidate candidate) throws EVException {
		if (isPersistent())
			getPersistenceLayer().deleteCandidateIsCandidateInElection(candidate, this);
		else throw new EVException("This Election object is not persistent.");
	}

	public String toString() {
		return "Election[" + this.getId() + "] " + this.getVoteCount() + " "
				+ " " + this.office + " " + this.isPartisan;
	}

}
