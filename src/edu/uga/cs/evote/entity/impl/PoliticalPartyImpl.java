/**
 * @file PoliticalPartyImpl.java
 * @author Team 9: Joey Bruce. Jack Fisher, Kianoosh Mokhtari, Will Runge
 */

package edu.uga.cs.evote.entity.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.persistence.impl.Persistent;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.entity.Candidate;


public class PoliticalPartyImpl extends Persistent implements PoliticalParty {

	// Class attributes
	private String name;

	// Default constructor
	public PoliticalPartyImpl() {
		super(-1);
		this.name = null;
	}

	// Specified constructor
	public PoliticalPartyImpl(String name) {
		super(-1);
		this.name = name;
	}

	/** Return the name of this political party.
	 * @return the name of the political party
	 */
	public String getName() {
		return this.name;
	}

	/** Set the new name of this political party.
	 * @param name the new name of the political party
	 */
	public void setName(String name) {
		this.name = name;
	}

	/** Return a list of the candidates belonging to this political party.
	 * @return a list of this political party's candidates
	 * @throws EVException in case there is a problem with traversing links to the requested objects
	 */
	public List<Candidate> getCandidates() throws EVException {
		if (isPersistent()) {
			return getPersistenceLayer().restoreCandidateIsMemberOfPoliticalParty(this);
		} else throw new EVException("This Political Party object is not persistent.");
	}

	public String toString() {
		return "PoliticalParty[" + this.getId() + "] " + this.getName();
	}

}
