package edu.uga.cs.evote.logic.impl;

import java.sql.*;
import java.util.*;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.BallotItem;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.object.ObjectLayer;

public class DeleteBallotCtrl {

	private ObjectLayer objectLayer = null;
	
	public DeleteBallotCtrl(ObjectLayer objectLayer){
		this.objectLayer = objectLayer;
	}

	public long delete(String bId) throws EVException{
		
		long id = Long.parseLong(bId);
		Ballot ballot = objectLayer.createBallot();
		ballot.setId(id);
		List<Ballot> ballots = objectLayer.findBallot(ballot);
		if(ballots.size() == 0){
			throw new EVException("DeleteBallotCtrl: Ballot does not exist.");
		}
		else{
			List<BallotItem> items = ballot.getBallotItems();
			if(items.size() > 0){
				for(int i = 0; i < items.size(); i++){
					BallotItem item = items.get(i);
					if( item instanceof Election){
						objectLayer.deleteElection( (Election) item);
					}
					else{
						objectLayer.deleteIssue( (Issue) item);
					}
					
				}
			}
			
			objectLayer.deleteBallot(ballot);
		}
		return id;
	}
	
}
