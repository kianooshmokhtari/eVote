package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.object.ObjectLayer;

public class EditIssueCtrl {
	
	private ObjectLayer objectLayer = null;
	
	public EditIssueCtrl( ObjectLayer objectLayer){
		this.objectLayer = objectLayer;
	}
	
	public long edit(String ID, String question, String ballotID) throws EVException{
		long id = -1;
		Ballot ballot = objectLayer.createBallot();
		ballot.setId(Long.parseLong(ballotID));
		List<Ballot> ballots = objectLayer.findBallot(ballot);
		if(ballots.size() == 0){
			throw new EVException("EditIssueCtrl: Ballot not found.");
		}
		else{
			ballot = ballots.get(0);
		}
		
		Issue issue = objectLayer.createIssue();
		issue.setId(Long.parseLong(ID));
		List<Issue> issues = objectLayer.findIssue(issue);
		if(!issues.isEmpty()){
			issue = objectLayer.createIssue(question, ballot);
			id = issue.getId();
			objectLayer.storeIssue(issue);
		}
		
		return id;
	}
	
}
