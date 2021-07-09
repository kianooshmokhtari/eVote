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
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.BallotItem;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.object.ObjectLayer;

class BallotManager {
	private ObjectLayer objectLayer = null;
    private Connection   conn = null;

    public BallotManager( Connection conn, ObjectLayer objectLayer ){
    	this.conn = conn;
    	this.objectLayer = objectLayer;
    } // constructor

    //store methods

    //Store a given Ballot object in the persistent data store.
    public void storeBallot( Ballot ballot ) throws EVException, SQLException{
    	 String               insertBallotSql = "insert into Ballots ( openDate, closeDate, approved ) values ( ?, ?, ? )";
         String               updateBallotSql = "update Ballots set openDate = ?, closeDate = ?, approved = ? where id = ?";
         PreparedStatement    stmt = null;
         int                  inscnt;
         long                 ballotId = ballot.getId();

         try {
        	 if( !ballot.isPersistent() ){
        		 stmt = (PreparedStatement) conn.prepareStatement( insertBallotSql );
        	 }
        	 else{
        		 stmt = (PreparedStatement) conn.prepareStatement( updateBallotSql );
        	 }

        	 // Open date

        	 if( ballot.getOpenDate() != null ){
        		 java.util.Date jDate = ballot.getOpenDate();
        		 java.sql.Date sDate = new java.sql.Date( jDate.getTime() );
                 stmt.setDate( 1, sDate );
        	 }
             else{
            	 stmt.setNull(1, java.sql.Types.DATE);
             }

        	 // close date

        	 if( ballot.getCloseDate() != null ){
        		 java.util.Date jDate = ballot.getCloseDate();
        		 java.sql.Date sDate = new java.sql.Date( jDate.getTime() );
                 stmt.setDate( 2, sDate );
        	 }
             else{
            	 stmt.setNull(2, java.sql.Types.DATE);
             }

        	 // approved

        	 stmt.setBoolean(3, ballot.getApproved());

        	 if( ballot.isPersistent() ){
        		 stmt.setLong( 4, ballotId );
        	 }

        	 inscnt = stmt.executeUpdate();

        	 if ( !ballot.isPersistent() ) {
        		 if ( inscnt >= 1) {
        			 String sql = "select last_insert_id()";
                     if( stmt.execute( sql ) ) { // statement returned a result

                         // retrieve the result
                         ResultSet r = stmt.getResultSet();

                         // we will use only the first row!
                         //
                         while( r.next() ) {

                             // retrieve the last insert auto_increment value
                             ballotId = r.getLong( 1 );
                             if( ballotId > 0 )
                                 ballot.setId( ballotId ); // set this person's db id (proxy object)
                         }
                     }
        		 }
        		 else{
        			 throw new EVException( "BallotManager.store: failed to save a Ballot" );
        		 }
        	 }
        	 else{
        		 if( inscnt < 1 )
                     throw new EVException( "BallotManager.store: failed to save a Ballot" );
        	 }


         }
         catch ( SQLException e ){
        	 e.printStackTrace();
        	 throw new EVException ("BallotManager.store: failed to store a Ballot: " + e );
         }
    }

    // Store a link between a Ballot and a BallotItem included on the Ballot.
    public void storeBallotIncludesBallotItem(Ballot ballot, BallotItem ballotItem) throws EVException{
    	String               insertBallotSql = "insert into Includes ( ballotID, itemID ) values ( ?, ? )";
    	String       		 selectBallotSql = "select id, ballotID, itemID from Includes"
    										 + " where ballotID = " + ballot.getId() + " and itemID = " + ballotItem.getId();
        String               updateBallotSql = "update Includes set ballotID = ?, itemID = ? where id = ?";
        Statement			 stmt;
        PreparedStatement    pstmt = null;
        int                  inscnt;
        long                 ballotId = ballot.getId();
        long				 ballotItemId = ballotItem.getId();
        StringBuffer 		 query = new StringBuffer( 100 );

        try {
       	 	if( ballot.isPersistent() && ballotItem.isPersistent() ){

       	 		query.append( selectBallotSql );
       	 		stmt = conn.createStatement();
       	 		if( stmt.execute( query.toString() ) ) { // statement returned a result

       	 			ResultSet rs = stmt.getResultSet();

       	 			if(!rs.isBeforeFirst() ) {
       	 				pstmt = (PreparedStatement) conn.prepareStatement( insertBallotSql );
       	 				pstmt.setLong( 1 , ballotId);
       	 				pstmt.setLong( 2 , ballotItemId);

       	 				inscnt = pstmt.executeUpdate();
       	 			}
       	 		}
       	 	}
       	 	else{
       	 	throw new EVException("BallotManager.storeBallotIncludesBallotItem: failed to store a link" );
       	 	}

        }
        catch( SQLException e) {
        	e.printStackTrace();
        	throw new EVException("BallotManager.storeBallotIncludesBallotItem: failed to store a link: " + e);
        }

    } // store methods

    // restore methods

    //Restore all Ballot objects that match attributes of the model Ballot.
    public List<Ballot> restoreBallot( Ballot modelBallot ) throws EVException{
    	String       selectBallotSql = "select id, openDate, closeDate, approved from Ballots";
        Statement    stmt = null;
        StringBuffer query = new StringBuffer( 100 );
        StringBuffer condition = new StringBuffer( 100 );
        List<Ballot> ballots = new ArrayList<Ballot>();
        java.sql.Date sDate1, sDate2;

        condition.setLength( 0 );

     // form the query based on the given Ballot object instance
        query.append( selectBallotSql );

        if( modelBallot != null ){
        	// if unique id
        	if( modelBallot.getId() >= 0 ) {
        		query.append( " where id = " + modelBallot.getId() );
        	}
        	// else if openDate, closeDate, approved
        	else{

        		if (modelBallot.getOpenDate() != null){
        			java.util.Date jDate = modelBallot.getOpenDate();
        	   		sDate1 = new java.sql.Date( jDate.getTime() );
        			condition.append(" where openDate = '" + sDate1 + "'" );
        		}
        		if (modelBallot.getCloseDate() != null){
        			if( condition.length() > 0 )
        				condition.append( " and" );
        			else
        				condition.append( " where" );
        			java.util.Date jDate = modelBallot.getCloseDate();
        	   		sDate2 = new java.sql.Date( jDate.getTime() );
        			condition.append(" closeDate = '" + sDate2 + "'" );
        		}
        		if( condition.length() > 0 )
        			condition.append( " and" );
        		else
        			condition.append( " where" );
        		condition.append( " approved = " + modelBallot.getApproved() );
        	}
        	
        	query.append( condition );
        }

        try{
        	stmt = conn.createStatement();

        	// retrieve the persistent Ballot Objects

        	if( stmt.execute( query.toString() ) ) {

        		long id;
        		Date openDate;
        		Date closeDate;
        		boolean approved;
        		Ballot nextBallot = null;

        		ResultSet rs = stmt.getResultSet();

        		//retrieve the retrieved Ballots
        		while( rs.next() ) {

        			id = rs.getLong( 1 );
        			openDate = rs.getDate( 2 );
        			closeDate = rs.getDate( 3 );
        			approved = rs.getBoolean( 4 );

        			nextBallot = objectLayer.createBallot();
        			nextBallot.setId( id );
        			nextBallot.setOpenDate(openDate);
        			nextBallot.setCloseDate(closeDate);
        			nextBallot.setApproved(approved);
        			//nextBallot.setElectoralDistrict(modelBallot.getElectoralDistrict());

        			ballots.add( nextBallot );
        		}

        		return ballots;
        	}
        }
        catch( Exception e ){
        	throw new EVException ( " BallotManager.restore: could not restore persistent Ballot objects; Root cause: " + e );
        }

        throw new EVException ( " BallotManager.restore: could not restore persistent Ballot objects." );
    }

    //Return all Ballots which a given ElectoralDistrict has.
    public List<Ballot> restoreElectoralDistrictHasBallotBallot(ElectoralDistrict electoralDistrict) throws EVException{
    	String       selectBallotSql = "select b.id, b.openDate, b.closeDate, b.approved"
									+ " from HasBallot y, Ballots b"
									+ " where y.ballotID = b.id and y.districtID = ";
    	Statement    stmt = null;
        StringBuffer query = new StringBuffer( 100 );
        List<Ballot> ballots = new ArrayList<Ballot>();

        query.append( selectBallotSql );

        if( electoralDistrict != null ){
			query.append( electoralDistrict.getId() );
		}

        try {

            stmt = conn.createStatement();

            // retrieve the Election

            if( stmt.execute( query.toString() ) ) { // statement returned a result
                ResultSet rs = stmt.getResultSet();

                long   id;
                Date openDate;
                Date closeDate;
                boolean approved;

                Ballot nextBallot = null;

                while( rs.next() ) {

                    id = rs.getLong( 1 );
                    openDate = rs.getDate( 2 );
                    closeDate = rs.getDate( 3 );
                    approved = rs.getBoolean( 4 );

                    nextBallot = objectLayer.createBallot(openDate, closeDate, approved, electoralDistrict);
                    nextBallot.setId( id );
                    ballots.add( nextBallot );
                }

                return ballots;
            }
        }
        catch( Exception e ){
        	throw new EVException( "BallotManager.restoreElectoralDistrictHasBallotBallot could not restore persistent Election; root cause: " + e);
        }

        throw new EVException( "BallotManager.restoreElectoralDistrictHasBallotBallot could not restore persistent Election." );
    }

    //Return BallotItems included on a given Ballot.
    public List<BallotItem> restoreBallotIncludesBallotItem(Ballot ballot) throws EVException{
    	String       selectBallotItemSql = "select x.id, x.type, x.votecount, x.question, x.yesCount, x.office, x.isPartisan"
    									+ " from Items x, Includes y"
    									+ " where y.itemID = x.id and";
        Statement    stmt = null;
        StringBuffer query = new StringBuffer( 100 );
        List<BallotItem> ballotItems = new ArrayList<BallotItem>();

        //form query
        query.append( selectBallotItemSql );

        if(ballot != null ){
        	query.append( " y.ballotID = " + ballot.getId() );
        }

        try {

            stmt = conn.createStatement();

            // retrieve the Ballot Items

            if( stmt.execute( query.toString() ) ) { // statement returned a result
                ResultSet rs = stmt.getResultSet();

                long   id;
                String type;
                int voteCount;
                String question;
                int yesCount;
                String office;
                boolean isPartisan;

                BallotItem nextItem = null;

                while( rs.next() ) {

                    id = rs.getLong( 1 );
                    //System.out.println("ID: " + id);
                    type = rs.getString( 2 );
                    //System.out.println(type);
                    voteCount = rs.getInt( 3 );
                    //System.out.println("a");
                    question = rs.getString( 4 );
                    //System.out.println("b");
                    yesCount = rs.getInt( 5 );
                    //System.out.println("c");
                    office = rs.getString( 6 );
                    //System.out.println("d");
                    isPartisan = rs.getBoolean( 7 );
                    //System.out.println("e");

                    if( type.equals("issue") ){
                    	//System.out.println("f");
                    	nextItem = objectLayer.createIssue(question, ballot);
                    	//nextItem.setYesCount(yesCount);
                    }
                    else if( type.equals("election") ){
                    	//System.out.println("f");
                    	nextItem = objectLayer.createElection(office, isPartisan, ballot);
                    }
                    //System.out.println("g");
                    //id = 1;
                    nextItem.setId( id );
                    //System.out.println("h");
                    nextItem.setVoteCount(voteCount);

                    //System.out.println("i");
                    ballotItems.add( nextItem );
                }

                return ballotItems;
            }
        }
        catch( Exception e ){
        	throw new EVException( "BallotManager.restoreBallotIncludesBallotItem could not restore persistent Ballot Items; root cause: " + e);
        }

        throw new EVException( "BallotManager.restoreBallotIncludesBallotItem could not restore persistent Ballot Items." );
    }

    //Return the Ballot which includes a given BallotItem.
    public Ballot restoreBallotIncludesBallotItem(BallotItem ballotItem) throws EVException{

    	String       selectBallotSql = "select b.id, b.openDate, b.closeDate, b.approved, d.id, d.name"
				+ " from Includes y, Ballots b, Districts d, HasBallot x"
				+ " where y.ballotID = b.id and y.itemID = " + ballotItem.getId() + " and x.districtID = d.id and x.ballotID = b.id";
    	Statement    stmt = null;
    	StringBuffer query = new StringBuffer( 100 );

    	query.append( selectBallotSql );


    	try {

            stmt = conn.createStatement();

            // retrieve the persistent Club objects
            //
            if( stmt.execute( query.toString() ) ) { // statement returned a result

            	long   id = 0;
                Date   openDate;
                Date	closeDate;
                boolean approved;
                //district
                long districtId = 0;
                String name;

                Ballot   ballot = null;
                ElectoralDistrict district = null;

                ResultSet rs = stmt.getResultSet();

                // retrieve the retrieved clubs
                while( rs.next() ) {

                    id = rs.getLong( 1 );
                    openDate = rs.getDate( 2 );
                    closeDate = rs.getDate( 3 );
                    approved = rs.getBoolean( 4 );
                    districtId = rs.getLong( 5 );
                    name = rs.getString( 6 );

                    district = objectLayer.createElectoralDistrict(name);
                    district.setId( districtId );

                    ballot = objectLayer.createBallot(openDate, closeDate, approved, district);
                    ballot.setId( id );
                }

                return ballot;
            }
        }
        catch( Exception e ) {      // just in case...
            throw new EVException( "BallotManager.restoreBallotIncludesBallotItem: Could not restore persistent Ballot; Root cause: " + e );
        }

    	throw new EVException( "BallotMangaer.restoreBallotIncludesBallotItem could not restore persistent Ballot Items." );

    } // restore methods

    // delete methods

    //Delete a given Ballot object from the persistent data store.
    public void deleteBallot( Ballot ballot ) throws EVException{
    	String               deleteBallotSql = "delete from Ballots where id = ?";
        PreparedStatement    stmt = null;
        int                  inscnt;

        if( !ballot.isPersistent() )
        	return;

        try{
        	stmt = (PreparedStatement) conn.prepareStatement( deleteBallotSql );
            stmt.setLong( 1, ballot.getId() );
            inscnt = stmt.executeUpdate();
            if( inscnt == 1 ) {
                return;
            }
            else
                throw new EVException( "BallotManager.delete: failed to delete a Club" );
        }
        catch( SQLException e ){
        	e.printStackTrace();
        	throw new EVException( "BallotManager.delete: failed to delete a Club: " + e );
        }
    }

    //Delete a link between a Ballot and a BallotItem included on the Ballot.
    public void deleteBallotIncludesBallotItem(Ballot ballot, BallotItem ballotItem) throws EVException{
    	String               deleteIncludesSql = "delete from Includes where";
        PreparedStatement    stmt = null;
        int                  inscnt;
        StringBuffer query = new StringBuffer( 100 );

        if( !ballot.isPersistent() || !ballotItem.isPersistent() ){
        	return;
        }

        query.append( deleteIncludesSql );

        if( ballot != null && ballotItem != null ){
        	deleteIncludesSql += " ballotID = " + ballot.getId()
        					+ " and itemID = " + ballotItem.getId();
        }

        try {
            stmt = (PreparedStatement) conn.prepareStatement( deleteIncludesSql );
            inscnt = stmt.executeUpdate();
            if( inscnt == 1 ) {
                return;
            }
            else
                throw new EVException( "BallotManager.deleteBallotIncludesBallotItem: failed to delete an Includes relationship" );
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new EVException( "BallotManager.deleteBallotIncludesBallotItem: failed to delete an Includes relationship; root cause: " + e );
        }

    } // delete methods
}
