package edu.uga.cs.evote.persistence.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.*;
import edu.uga.cs.evote.object.*;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;

public class IssueManager {

	private ObjectLayer objectlayer = null;
	private Connection conn = null;

	public IssueManager(Connection conn, ObjectLayer object){

		this.conn = conn;
		objectlayer = object;
	}

	public void store(Issue issue) throws EVException{

		String 				insertIssueSql = "insert into Items (type, votecount, question, yesCount, office, isPartisan) "
											+ "values ('issue', ?, ?, ?, null, null)";
		String 				updateIssueSql = "update Items set type = 'issue', votecount = ?, question = ?, yesCount = ?, office = null, isPartisan = null where id = " + issue.getId();

		PreparedStatement 	stmt;

		int 				inscnt;
		long				issueId;

		try{

			if(!issue.isPersistent())
				stmt = (PreparedStatement) conn.prepareStatement(insertIssueSql);
			else
				stmt = (PreparedStatement) conn.prepareStatement(updateIssueSql);

			//store votecount
			if(issue.getVoteCount() >= 0)
				stmt.setLong(1, issue.getVoteCount());
			else
				throw new EVException("Could not store votecount; vote = 0");

			//store Question
			if(issue.getQuestion()!= null)
				stmt.setString(2, issue.getQuestion());
			else
				stmt.setNull(2, java.sql.Types.VARCHAR);

			//store yesCount
			if(issue.getYesCount() >= 0)
				stmt.setLong(3, issue.getYesCount());
			else
				stmt.setNull(3,  java.sql.Types.VARCHAR);

			inscnt = stmt.executeUpdate();

			if(!issue.isPersistent()){

				if(inscnt > 0){

					String sql = "select last_insert_id()";
					if(stmt.execute(sql)){
						ResultSet r = stmt.getResultSet();

						while(r.next()){
							issueId = r.getLong(1);
							if(issueId > 0)
								issue.setId(issueId);
						}
					}
				}
			}
			else{
				if(inscnt < 1)
					throw new EVException("did not store issue");
			}
		}
		catch(SQLException e){
			e.printStackTrace();
			throw new EVException("did not store issue " + e.getMessage());
		}
	}

	public List<Issue> restore(Issue issue) throws EVException{

		String			selectIssueSql = "select id, type, votecount, question, yesCount from Items";
		Statement		stmt = null;
		StringBuffer	query = new StringBuffer( 100 );
		StringBuffer	condition = new StringBuffer( 100 );
		List<Issue>		issues = new ArrayList<Issue>();

		condition.setLength( 0 );

		//append selectIssueSal
		query.append(selectIssueSql);

		if(issue != null){
			if(issue.isPersistent())
				query.append(" where id = " + issue.getId());
			else if(issue.getQuestion() != null)
				query.append(" where question = '" + issue.getQuestion() + "'" );
			else {
				condition.append(" where type = 'issue'");

				if(issue.getQuestion() != null){
					if(condition.length() > 0)
						condition.append(" and");
					condition.append(" question = '" + issue.getQuestion() + "'");
				}

				//restore only object that have office and isPartisan set to null
				if(condition.length()>0)
					condition.append(" and");
				condition.append(" office = null and isPartisan = null" );

				query.append(condition);
			}
		} else {
			query.append(" where type = 'issue'");
		}

		try {

			stmt = conn.createStatement();

			if(stmt.execute(query.toString())){

				ResultSet r = stmt.getResultSet();

				int id;
				String question;
				int voteCount;
				int yesVote;


				//while to get all object attributes and assign to new object to be added to the list
				while(r.next()){

					id = r.getInt(1);
					question = r.getString(4);
					voteCount = r.getInt(3);
					yesVote = r.getInt(5);

					Issue nextIssue =objectlayer.createIssue();
					nextIssue.setId(id);
					nextIssue.setQuestion(question);
					nextIssue.setVoteCount((int) voteCount);
					nextIssue.setYesCount(yesVote);

					issues.add(nextIssue);
				}

				return issues;
			}
		}
		catch(Exception e){
			throw new EVException("IssuesManager.restore failed" + e.getMessage());
		}
		throw new EVException("IssueManager.restore failed");
	}

	public void delete(Issue issue) throws EVException{

		String				deleteIssueSql = "delete from Items where id = ?";
		PreparedStatement 	stmt = null;
		int					inscnt;

		if(!issue.isPersistent())
			return;

		try{
			stmt = (PreparedStatement) conn.prepareStatement(deleteIssueSql);

			stmt.setLong(1, issue.getId());

			inscnt = stmt.executeUpdate();

			if(inscnt < 1)
				throw new EVException("IssueManager.delete did not work");

		}
		catch(SQLException e){
			throw new EVException("IssueManager.delete did not work : " + e.getMessage());
		}
	}
}
