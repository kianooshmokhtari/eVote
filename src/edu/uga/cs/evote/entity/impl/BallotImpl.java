/**
 * @file BallotImpl.java
 * @author Team 9: Joey Bruce. Jack Fisher, Kianoosh Mokhtari, Will Runge
 */

package edu.uga.cs.evote.entity.impl;

import java.util.Date;
import java.util.List;
import java.util.ArrayList; // lel

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.persistence.impl.Persistent;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.BallotItem;
import edu.uga.cs.evote.entity.VoteRecord;


public class BallotImpl extends Persistent implements Ballot {

	// Class attributes
	private Date				openDate;
	private Date				closeDate;
	private boolean				approved;
	private ElectoralDistrict	district;

	// Default constructor
	public BallotImpl() {
		super(-1);
		this.openDate =		null;
		this.closeDate =	null;
		this.approved =		false;
		this.district =		null;
	}

	// Specified constructor
		public BallotImpl(	Date openDate, Date closeDate, boolean approved,
							ElectoralDistrict district) {
		super(-1);
		this.openDate =		openDate;
		this.closeDate =	closeDate;
		this.approved = 	approved;
		this.district =		district;
	}

	/** Return the opening date for this Ballot
	 * @return the opening date for this Ballot
	 */
	public Date getOpenDate() {
		return this.openDate;
	}

	/** Set the new opening date for this Ballot
	 * @param openDate the new opening date
	 */
	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	/** Return the closing date for this Ballot
	 * @return the closing date for this Ballot
	 */
	public Date getCloseDate() {
		return this.closeDate;
	}

	/** Set the new closing date for this Ballot
	 * @param closeDate the new closing date
	 */
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	/** Return a boolean value indicating if the voting results on this Ballot
	 * have been approved.
	 * @return a boolean value indicating if the voting results on this Ballot
	 * have been approved
	 */
	public boolean getApproved() {
		return this.approved;
	}

	/** Set the new boolean value indicating if the voting results on this
	 * Ballot have been approved.
	 * @param approved a boolean value indicating if the voting results on this
	 * Ballot have been approved
	 */
	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	/** Return the ElectoralDistrict of this Ballot
	 * @return the ElectoralDistrict of this Ballot
	 * @throws EVException in case there is a problem with the retrieval of the
	 * requested object
	 */
	public ElectoralDistrict getElectoralDistrict() throws EVException {
		if (this.district == null) {
			if (isPersistent()) {
				this.district = getPersistenceLayer().restoreElectoralDistrictHasBallotBallot(this);
			} else throw new EVException("This Ballot object is not persistent.");
		}
		return this.district;
	}

	/** Set the new ElectoralDistrict of this Ballot
	 * @param electoralDistrict the new ElectoralDistrict
	 * @throws EVException in case there is a problem with setting a link to the
	 * argument object
	 */
	public void setElectoralDistrict(ElectoralDistrict electoralDistrict) throws EVException {
		this.district = electoralDistrict;
		if (isPersistent()) {
			getPersistenceLayer().storeElectoralDistrictHasBallotBallot(electoralDistrict, this);
		} else throw new EVException("This Ballot object is not persistent.");
	}

	/** Return a list of BallotItems on this Ballot.
	 * @return a list of BallotItems on this Ballot
	 * @throws EVException in case there is a problem with the retrieval of the
	 * requested objects
	 */
	public List<BallotItem> getBallotItems() throws EVException {
		if (isPersistent()) {
			return getPersistenceLayer().restoreBallotIncludesBallotItem(this);
		} else throw new EVException("This Ballot object is not persistent.");
	}

	/** Add a new BallotItem to the end of this Ballot.
	 * @param ballotItem the new BallotItem
	 * @throws EVException in case there is a problem with adding a link to the
	 * argument object
	 */
	public void addBallotItem(BallotItem ballotItem) throws EVException {
		if (isPersistent()) {
			getPersistenceLayer().storeBallotIncludesBallotItem(this, ballotItem);
		} else throw new EVException("This Ballot object is not persistent.");
	}

	/** Delete a BallotItem from this Ballot.
	 * @param ballotItem the BallotItem to be removed
	 * @throws EVException in case there is a problem with deleting a link to
	 * the argument object
	 */
	public void deleteBallotItem(BallotItem ballotItem) throws EVException {
		if (isPersistent()) {
			getPersistenceLayer().deleteBallotIncludesBallotItem(this, ballotItem);
		} else throw new EVException("This Ballot object is not persistent.");
	}

	/** Return a list of VoteRecords for this Ballot.
	 * @return a List of VoteRecords recorded for this Ballot
	 * @throws EVException in case there is a problem with traversing links to
	 * the requested objects
	 */
	public List<VoteRecord> getVoterVoteRecords() throws EVException {
		if (isPersistent()) {
			// TODO: Implement
			return new ArrayList<VoteRecord> (); // lel
		} else throw new EVException("This Ballot object is not persistent.");
	}

	public String toString() {
		return "Ballot[" + this.getId() + "] " + this.getOpenDate() + " "
				+ this.getCloseDate() + " " + this.getApproved();
	}

}
