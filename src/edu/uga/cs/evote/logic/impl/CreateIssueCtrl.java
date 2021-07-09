package edu.uga.cs.evote.logic.impl;

import java.util.Date;
import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.object.ObjectLayer;

public class CreateIssueCtrl {
	
	private ObjectLayer objectLayer = null;
	
	public CreateIssueCtrl(ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}
	
	public long create(String question, String ballotID) throws EVException {
		
		Ballot ballot = objectLayer.createBallot();
		ballot.setId(Long.parseLong(ballotID));
		List<Ballot> ballots = objectLayer.findBallot(ballot);
		if(ballots.size() == 0){
			throw new EVException("CreateIssueCtrl: Ballot not found.");
		}
		else{
			ballot = ballots.get(0);
		}
		
		Issue issue = objectLayer.createIssue(question, ballot);
		List<Issue> issues = objectLayer.findIssue(issue);
		if(issues.size() == 0){
			objectLayer.storeIssue(issue);
		}
		else{
			throw new EVException("CreateIssueCtrl: Issue already exists.");
		}
		
		return issue.getId();
	}
}
