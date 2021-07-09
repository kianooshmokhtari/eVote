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
import edu.uga.cs.evote.object.ObjectLayer;

class VoteRecordManager {

	private ObjectLayer	objectLayer = null;
	private Connection	conn = null;

	public VoteRecordManager (Connection conn, ObjectLayer objectLayer) {

		this.conn = conn;
		this.objectLayer = objectLayer;
	}

	public void deleteVoteRecord (VoteRecord voteRecord) throws EVException{

		String				deleteVoteRecordSql = "delete from VoteRecords where id = ?";
		PreparedStatement	stmt = null;
		int					inscnt;

		if (!voteRecord.isPersistent())
			return;

		try {
			stmt = (PreparedStatement)conn.prepareStatement(deleteVoteRecordSql);

			stmt.setLong(1, voteRecord.getId());

			inscnt = stmt.executeUpdate();

			if (inscnt == 0) {
				throw new EVException("VoteRecordManager.delete: failed to delete this VoteRecord");
			}
		}
		catch (SQLException e) {
			throw new EVException("VoteRecordManager.delete: failed to delete this VoteRecord: " + e.getMessage() );
		}
	}

	public List<VoteRecord> restoreVoteRecord (VoteRecord voteRecord) throws EVException {

		String		selectVoteRecordSql = "select vr.id, vr.voterID, vr.ballotID, vr.date, u.firstname, u.lastname, u.username, u.userpass, "
										 + "u.email, u.address, u.age, u.voterID, b.openDate, b.closeDate, b.approved "
										 + "from VoteRecords vr, Users u, Ballots b "
										 + "where vr.voterID = u.id and vr.ballotID = b.id";
		Statement	stmt = null;
		StringBuffer query = new StringBuffer(100);
		StringBuffer condition = new StringBuffer(100);
		List<VoteRecord> records = new ArrayList<VoteRecord>();

		/*
		if (voteRecord.getBallot() != null && !voteRecord.getBallot().isPersistent())
			throw new EVException("VoteRecordManager.restore: the argument voteRecord includes a non-persistent Ballot object");
        if (voteRecord.getVoter() != null && !voteRecord.getVoter().isPersistent())
			throw new EVException("VoteRecordManager.restore: the argument voteRecord includes a non-persistent Voter object");
        */

		condition.setLength(0);

		query.append(selectVoteRecordSql);

		if (voteRecord != null) {

			if (voteRecord.isPersistent()) // id is unique
				query.append(" and vr.id = " + voteRecord.getId());

			else {

				if (voteRecord.getBallot() != null)
					condition.append(" and vr.ballotID = " + voteRecord.getBallot().getId());

				if (voteRecord.getVoter() != null)
					condition.append(" and vr.voterID = " + voteRecord.getVoter().getId());

				if (voteRecord.getDate() != null)
					condition.append(" and vr.date = '" + voteRecord.getDate() + "'");

				if (condition.length() > 0)
					query.append(condition);
			}
		}

		try {

			stmt = conn.createStatement();

			if (stmt.execute( query.toString() ) ) {

				ResultSet rs = stmt.getResultSet();

				// voteRecord
				long	vrid;
				Date	date;

				// voter/user
				long	vid;
				String	firstname;
				String	lastname;
				String	username;
				String	password;
				String	email;
				String	address;
				int		age;
				String	voterId;

				// ballot
				long	bid;
				Date	openDate;
				Date	closeDate;
				boolean	approved;

				Voter voter = null;
				Ballot ballot = null;
				VoteRecord nextVoteRecord = null;


				while( rs.next() ) {

					vrid = rs.getLong(1);
					vid = rs.getLong(2);
					bid = rs.getLong(3);
					date = rs.getDate(4);
					firstname = rs.getString(5);
					lastname = rs.getString(6);
					username = rs.getString(7);
					password = rs.getString(8);
					email = rs.getString(9);
					address = rs.getString(10);
					age = rs.getInt(11);
					voterId = rs.getString(12);
					openDate = rs.getDate(13);
					closeDate = rs.getDate(14);
					approved = rs.getBoolean(15);

					voter = objectLayer.createVoter(firstname, lastname, username, password, email, address, age);
					voter.setVoterId(voterId);
					voter.setId(vid);

					ballot = objectLayer.createBallot(openDate, closeDate, approved, null);
					ballot.setId(bid);

					nextVoteRecord = objectLayer.createVoteRecord(ballot, voter, date);
					nextVoteRecord.setId(vrid);

					records.add(nextVoteRecord);
				}

				return records;
			}
		} catch(Exception e) {	// just in case...
			throw new EVException( "VoteRecordManager.restore: Could not restore persistent VoteRecord object; Root cause: " + e );
		}

		// if we get to this point, it's an error
		throw new EVException( "VoteRecordManager.restore: Could not restore persistent VoteRecord objects" );
	}

	public void storeVoteRecord (VoteRecord voteRecord) throws EVException {

		String				insertVoteRecordSql = "insert into VoteRecords (date, voterID, ballotID) "
												+ "values (?, ?, ?)";
		String				updateVoteRecordSql = "update VoteRecords set date = ?, voterID = ?, ballotID = ?, where id = ?";
		PreparedStatement	stmt = null;
		int					inscnt;
		long				voteRecordId;

		try {
			if (!voteRecord.isPersistent())
				stmt = (PreparedStatement)conn.prepareStatement(insertVoteRecordSql);
			else
				stmt = (PreparedStatement)conn.prepareStatement(updateVoteRecordSql);

			// date
			if (voteRecord.getDate() != null) {
				java.util.Date jDate = voteRecord.getDate();
				java.sql.Date sDate = new java.sql.Date(jDate.getTime());
				stmt.setDate(1, sDate);
			} else
				stmt.setNull(1, java.sql.Types.DATE);

			// voterId
			if (voteRecord.getVoter().getId() != 0)
				stmt.setLong(2, voteRecord.getVoter().getId());
			else
				throw new EVException("VoteRecordManager.store: can't store a VoteRecord: voterID undefined");

			// ballotId
			if (voteRecord.getBallot().getId() != 0)
				stmt.setLong(3, voteRecord.getBallot().getId());
			else
				throw new EVException("VoteRecordManager.store: can't store a VoteRecord: ballotID undefined");

			if (voteRecord.isPersistent())
				stmt.setLong(4, voteRecord.getId());

			inscnt = stmt.executeUpdate();

			if (!voteRecord.isPersistent()) {
				// if being stored for first time, establish persistent identifier (primary key)
				if (inscnt == 1) {
					String sql = "select last_insert_id()";
					if (stmt.execute(sql)) {
						// retrieve result if there is one
						ResultSet r = stmt.getResultSet();
						//use only first row
						while(r.next()) {
							voteRecordId = r.getLong(1);
							if (voteRecordId > 0) {
								voteRecord.setId(voteRecordId);
							}
						}
					}
				}
			}
			else {
				if (inscnt < 1)
					throw new EVException("VoteRecordManager.store: failed to store a VoteRecord");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new EVException("VoteRecordManager.store: failed to store a VoteRecord: " + e );
		}
	}
}
