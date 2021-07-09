package edu.uga.cs.evote.logic.impl;

import java.sql.*;
import java.util.*;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.object.ObjectLayer;

public class DeleteIssueCtrl {

	private ObjectLayer objectLayer = null;
	
	public DeleteIssueCtrl(ObjectLayer objectLayer){
		this.objectLayer = objectLayer;
	}

	public long delete(String ID) throws EVException{
		long id = -1;
		Issue issue = objectLayer.createIssue();
		issue.setId(Long.parseLong(ID));
		List<Issue> issues = objectLayer.findIssue(issue);
		if(issues.size() == 0){
			throw new EVException( "DeleteIssueCtrl: Issue does not exist." );
		}
		else{
			issue = issues.get(0);
			id = issue.getId();
			objectLayer.deleteIssue(issue);
		}
		
		return id;
		
	}
	
}
