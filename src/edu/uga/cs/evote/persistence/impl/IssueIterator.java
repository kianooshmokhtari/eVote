package edu.uga.cs.evote.persistence.impl;

import java.sql.ResultSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.*;
import edu.uga.cs.evote.object.ObjectLayer;

public class IssueIterator implements Iterator<Issue>{

	private ResultSet	rs = null;
	private ObjectLayer	objectLayer = null;
	private boolean		more = false;

	public IssueIterator(ResultSet rs, ObjectLayer objectLayer) throws EVException {
		this.rs = rs;
		this.objectLayer = objectLayer;
		try {
			more = rs.next();
		}
		catch( Exception e ) {
			throw new EVException( "IssueIterator: Cannot create Issueiterator; root cause: " + e );
		}
	}

	public boolean hasNext() {
		return more;
	}

	public Issue next() {

		long id;
		int VoteCount;
		Ballot ball = null;
		String question;

		if (more) {

			try {
				id = rs.getLong( 1 );
				VoteCount= rs.getInt( 2 );
				question = rs.getString(3);


				more = rs.next();
			}
			catch( Exception e ){
				throw new NoSuchElementException( "IssueIterator: No next Issue object; root cause: " + e );
			}

			Issue issue = null;
			try {
				issue = objectLayer.createIssue(question, ball);
				issue.setId( id );
			} catch (EVException e) {
				e.printStackTrace();
			}

			return issue;

		}
		else{
			throw new NoSuchElementException( "IssueIterator: No next Issue object" );
		}

	}

	public void remove(){
		throw new UnsupportedOperationException();
	}

}
