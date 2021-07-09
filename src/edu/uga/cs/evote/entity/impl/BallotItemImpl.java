/**
 * @file BallotItemImpl.java
 * @author Team 9: Joey Bruce. Jack Fisher, Kianoosh Mokhtari, Will Runge
 */

package edu.uga.cs.evote.entity.impl;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.persistence.impl.Persistent;
import edu.uga.cs.evote.entity.BallotItem;
import edu.uga.cs.evote.entity.Ballot;


public abstract class BallotItemImpl extends Persistent implements BallotItem {

	// Class attributes
	private int		voteCount;
	private Ballot	ballot;

	// Default constructor
	public BallotItemImpl() {
		super(-1);
		this.voteCount =	0;
		this.ballot =		null;
	}

	// Specified constructor
	public BallotItemImpl(Ballot ballot) {
		super(-1);
		this.voteCount =	0;
		this.ballot =		ballot;
	}

	/** Return the vote count of this BallotItem.
	 * @return the vote count of this BallotItem
	 */
	public int getVoteCount() {
		return this.voteCount;
	}

	/** Set the new vote count of this BallotItem.
	 * @param voteCount the new vote count;  it must be non-negative
	 * @throws EVException in case the new vote is negative
	 */
	public void setVoteCount( int voteCount ) throws EVException {
		if (voteCount < 0) throw new EVException("Vote count cannot be negative.");
		else this.voteCount = voteCount;
	}

	/** Add one vote (increment by one) to the vote count of this BallotItem.
	 */
	public void addVote() {
		this.voteCount++;
	}

	/** Return the Ballot on which this BallotItem is listed.
	 * @return the Ballot of this BallotItem
	 * @throws EVException in case there is a problem with traversing a link to the requested object
	 */
	public Ballot getBallot() throws EVException {
		if (this.ballot == null) {
			if (isPersistent())
				this.ballot = getPersistenceLayer().restoreBallotIncludesBallotItem(this);
			else throw new EVException("This Ballot Item object is not persistent.");
		}
		return this.ballot;
	}

	/** Set the new Ballot on which this BallotItem is listed.
	 * @param ballot the new Ballot
	 * @throws EVException in case there is a problem with setting a link to the requested object
	 */
	public void setBallot(Ballot ballot) throws EVException {
		this.ballot = ballot;
		if (isPersistent())
			getPersistenceLayer().storeBallotIncludesBallotItem(ballot, this);
		else throw new EVException("This Ballot Item object is not persistent.");
	}

}
