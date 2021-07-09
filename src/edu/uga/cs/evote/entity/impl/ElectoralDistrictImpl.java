/**
 * @file ElectoralDistrictImpl.java
 * @author Team 9: Joey Bruce. Jack Fisher, Kianoosh Mokhtari, Will Runge
 */

package edu.uga.cs.evote.entity.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.persistence.impl.Persistent;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.entity.Ballot;


public class ElectoralDistrictImpl extends Persistent implements ElectoralDistrict {

	// Class attributes
	private String name;

	// Default constructor
	public ElectoralDistrictImpl() {
		super(-1);
		this.name = null;
	}

	// Specified constructor
	public ElectoralDistrictImpl(String name) {
		super(-1);
		this.name = name;
	}

	/** Return the name of this electoral district.
	 * @return the name of this electoral district
	 */
	public String getName() {
		return this.name;
	}

	/** Set the new name of this electoral district.
	 * @param name the new name of this electoral district
	 */
	public void setName(String name) {
		this.name = name;
	}

	/** Return a list of the voters in this electoral district.
	 * @return a list of the voters in this electoral district
	 * @throws EVException in case there is a problem with traversing links to the requested objects
	 */
	public List<Voter> getVoters() throws EVException {
		if (isPersistent()) {
			return getPersistenceLayer().restoreVoterBelongsToElectoralDistrict(this);
		} else throw new EVException("This Electoral District object is not persistent.");
	}

	/** Return a list of the ballots created for this electoral district.
	 * @return a list of the ballots created for this electoral district
	 * @throws EVException in case there is a problem with traversing links to the requested objects
	 */
	public List<Ballot> getBallots() throws EVException {
		if (isPersistent()) {
			return getPersistenceLayer().restoreElectoralDistrictHasBallotBallot(this);
		} else throw new EVException("This Electoral District object is not persistent.");
	}

	/** Add a new ballot for this electoral district.
	 * @param ballot the new ballot for this electoral district
	 * @throws EVException in case there is a problem with creating a link to the requested objects
	 */
	public void addBallot(Ballot ballot) throws EVException {
		if (isPersistent()) {
			getPersistenceLayer().storeElectoralDistrictHasBallotBallot(this, ballot);
		} else throw new EVException("This Electoral District object is not persistent.");
	}

	/** Remove a ballot from this electoral district.
	 * @param ballot the ballot to be removed from this electoral district
	 * @throws EVException in case there is a problem with deleting a link to the requested objects
	 */
	public void deleteBallot( Ballot ballot ) throws EVException {
		if (isPersistent()) {
			getPersistenceLayer().deleteElectoralDistrictHasBallotBallot(this, ballot);
		} else throw new EVException("This Electoral District object is not persistent.");
	}

	public String toString() {
			return "ElectoralDistrict[" + this.getId() + "] " + this.getName();
	}

}
