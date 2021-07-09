/**
 * @file IssueImpl.java
 * @author Team 9: Joey Bruce. Jack Fisher, Kianoosh Mokhtari, Will Runge
 */

package edu.uga.cs.evote.entity.impl;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.entity.Ballot;

public class IssueImpl extends BallotItemImpl implements Issue {

	// Class attributes
	private String question;
	private int yesCount;

	// Default constructor
	public IssueImpl() {
		super();
		this.question =		null;
		this.yesCount =		0;
	}

	// Specified constructor
	public IssueImpl(String question, Ballot ballot) {
		super(ballot);
		this.question = question;
		this.yesCount = 0;
	}

	/** Return the question of this issue.
	 * @return the question of this issue
	 */
	public String getQuestion() {
		return this.question;
	}

	/** Set the new question of this issue.
	 * @param question the new question of this issue
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/** Return the number of yes votes for this issue.
	 * @return the number of yes votes for this issue
	 */
	public int getYesCount() {
		return this.yesCount;
	}

	/** Set the new number of yes votes for this issue.
	 * @param yesCount the new number of yes votes for this issue
	 * @throws EVException in case the new yes vote count is negative
	 */
	public void setYesCount(int yesCount) throws EVException {
		if (yesCount < 0) throw new EVException("Yes vote count cannot be negative.");
		else this.yesCount = yesCount;
	}

	/** Return the number of no votes for this issue.
	 * @return the number of no votes for this issue
	 */
	public int getNoCount() {
		return this.getVoteCount() - this.yesCount;
	}

	/** Add one YES vote (increment by one) to the yes votes cast for this Issue.
	 */
	public void addYesVote() {
		this.addVote();
		this.yesCount++;
	}

	/** Add one NO vote (increment by one) to the votes cast for this Issue.
	 */
	public void addNoVote() {
		this.addVote();
	}

	public String toString() {
		return "Issue[" + this.getId() + "] " + this.getVoteCount() + " "
				+ this.getYesCount() + " " + this.getNoCount() + " "
				+ this.getQuestion();
	}

}
