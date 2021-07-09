package edu.uga.cs.evote.persistence.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.*;
import edu.uga.cs.evote.object.ObjectLayer;

class VoterManager {

	private ObjectLayer objectLayer = null;
	private Connection 	conn = null;

	public VoterManager (Connection conn, ObjectLayer objectLayer) {

		this.conn = conn;
		this.objectLayer = objectLayer;
	}

	public void deleteVoter (Voter voter) throws EVException{

		String				deleteVoterSql = "delete from Users where id = ?";
		PreparedStatement	stmt = null;
		int					inscnt;

		if (!voter.isPersistent())
			return;

		try {
			stmt = (PreparedStatement)conn.prepareStatement(deleteVoterSql);

			stmt.setLong(1, voter.getId());

			inscnt = stmt.executeUpdate();

			if (inscnt == 0) {
				throw new EVException("Voter.delete: failed to delete this Voter");
			}
		}
		catch (SQLException e) {
			throw new EVException("VoterManager.delete: failed to delete this Voter: " + e.getMessage() );
		}
	}

	public void deleteVoterBelongsToElectoralDistrict (Voter voter, ElectoralDistrict electoralDistrict) throws EVException {

		String              deleteBelongsToSql = "delete from BelongsTo where ";
        PreparedStatement   stmt = null;
        int                	inscnt;
        StringBuffer 		query = new StringBuffer(100);

        if (!voter.isPersistent() || !electoralDistrict.isPersistent()) {
        	return;
        }

        query.append(deleteBelongsToSql);

        if (voter != null && electoralDistrict != null ){
        	deleteBelongsToSql += "voterID = " + voter.getId()
        					+ " and districtID = " + electoralDistrict.getId();
        }

        try {
            stmt = (PreparedStatement)conn.prepareStatement(deleteBelongsToSql);
            inscnt = stmt.executeUpdate();
            if (inscnt == 1) {
                return;
            }
            else
                throw new EVException("VoterManager.deleteVoterBelongsToElectoralDistrict: failed to delete a BelongsTo relationship");
        }
        catch (SQLException e) {
            throw new EVException("BallotManager.delete: failed to delete an Includes relationship; root cause: " + e);
        }
	}

	public List<Voter> restoreVoter (Voter voter) throws EVException {

		String			selectVoterSql = "select id, firstname, lastname, username, userpass, email, address, age, voterID from Users where type = 'voter'";
		Statement		stmt = null;
		StringBuffer	query = new StringBuffer(100);
		StringBuffer 	condition = new StringBuffer(100);
		List<Voter> 	voters = new ArrayList<Voter>();

		condition.setLength(0);

		//query is dependent on given Voter object instance
		query.append(selectVoterSql);

		if (voter != null) {

			// id
			if (voter.getId() >= 0)
				query.append(" and id = " + voter.getId());

			// username
			else if (voter.getUserName() != null)
				query.append(" and username = '" + voter.getUserName() + "'");

			else {
				// password/userpass
				if (voter.getPassword() != null)
					if (condition.length() > 0)
                        condition.append(" and");
					condition.append(" userpass = '" + voter.getPassword() + "'");

				// firstName
                if (voter.getFirstName() != null) {
                    if (condition.length() > 0)
                        condition.append(" and");
                    condition.append(" firstname = '" + voter.getFirstName() + "'");
                }

                // lastName
                if (voter.getLastName() != null) {
                    if (condition.length() > 0)
                        condition.append(" and");
                    condition.append(" lastname = '" + voter.getLastName() + "'");
                }

				// emailAddress
				if (voter.getEmailAddress() != null) {
                    if (condition.length() > 0)
                        condition.append(" and");
                    condition.append(" email = '" + voter.getEmailAddress() + "'");
                }

				// address
                if (voter.getAddress() != null) {
                    if (condition.length() > 0)
                        condition.append(" and");
                    condition.append(" address = '" + voter.getAddress() + "'");
                }

                // age
                if (voter.getAge() != 0 && voter.getAge()!= -1) {
                    if (condition.length() > 0)
                        condition.append(" and");
                    condition.append(" age = " + voter.getAge());
                }

                // voterId
                if (voter.getVoterId() != null) {
                    if (condition.length() > 0)
                        condition.append(" and");
                    condition.append(" voterID = '" + voter.getVoterId() + "'");
                }

                if (condition.length() > 0) {
                    query.append(" where ");
                    query.append(condition);
                }
			}
		}

		try {

            stmt = conn.createStatement();

            if (stmt.execute(query.toString())) {

                ResultSet rs = stmt.getResultSet();

                long   	id;
                String 	firstName;
                String 	lastName;
                String 	userName;
                String	password;
                String 	email;
                String 	address;
                int		age;
                String	voterId;

                while (rs.next()) {

                    id = rs.getLong(1);
                    firstName = rs.getString(2);
                    lastName = rs.getString(3);
                    userName = rs.getString(4);
                    password = rs.getString(5);
                    email = rs.getString(6);
                    address = rs.getString(7);
                    age = rs.getInt(8);
                    voterId = rs.getString(9);

                    Voter nextVoter = objectLayer.createVoter(firstName, lastName, userName, password, email, address, age);
                    nextVoter.setVoterId(voterId);
                    nextVoter.setId(id);

                    voters.add(nextVoter);
                }

                return voters;
            }
        }
        catch (Exception e) {
            throw new EVException("VoterManager.restore: Could not restore persistent Voter object; Root cause: " + e );
        }

        // if we get to this point, it's an error
        throw new EVException( "VoterManager.restore: Could not restore persistent Voter objects" );
	}

	// move this to EDManager
	public List<Voter> restoreVoterBelongsToElectoralDistrict (ElectoralDistrict electoralDistrict) throws EVException {

		String			selectVoteRecordSql = "select u.id, u.firstname, u.lastname, u.username, u.userpass, u.email, u.address, u.age, u.voterID "
											+ "from Users u, Districts d, BelongsTo b where b.voterID = u.id and b.districtID = d.id";
		Statement 		stmt = null;
		StringBuffer	query = new StringBuffer(100);
		StringBuffer	condition = new StringBuffer(100);
		List<Voter>		voters = new ArrayList<Voter>();

		condition.setLength(0);

		query.append(selectVoteRecordSql);

		if (electoralDistrict != null) {

			if (electoralDistrict.getId() >= 0)
				query.append(" and d.id = " + electoralDistrict.getId());

			else if (electoralDistrict.getName() != null)
				query.append(" and d.name = " + electoralDistrict.getName());
		}

		try {

			stmt = conn.createStatement();

			if (stmt.execute(query.toString())) {

				long 	id;
				String	firstname;
				String	lastname;
				String	username;
				String	userpass;
				String	email;
				String	address;
				int		age;
				String	voterId;

				Voter 	nextVoter = null;

				ResultSet rs = stmt.getResultSet();

				while(rs.next()) {

					id = rs.getLong(1);
					firstname = rs.getString(2);
					lastname = rs.getString(3);
					username = rs.getString(4);
					userpass = rs.getString(5);
					email = rs.getString(6);
					address = rs.getString(7);
					age = rs.getInt(8);
					voterId = rs.getString(9);

					nextVoter = objectLayer.createVoter();
					nextVoter.setId(id);
					nextVoter.setFirstName(firstname);
					nextVoter.setLastName(lastname);
					nextVoter.setUserName(username);
					nextVoter.setPassword(userpass);
					nextVoter.setEmailAddress(email);
					nextVoter.setAddress(address);
					nextVoter.setAge(age);
					nextVoter.setVoterId(voterId);

					voters.add(nextVoter);
				}
				return voters;
			}
		}
		catch (Exception e) {
			throw new EVException("VoterManager.restoreVoterBelongsToElectoralDistrict: Could not restore persistent Voter objects: Root cause: " + e);
		}

		throw new EVException("VoterManager.restoreVoterBelongsToElectoralDistrict: Could not restore persistent Voter objects");
	}

	public ElectoralDistrict restoreVoterBelongsToElectoralDistrict (Voter voter) throws EVException {

		String			selectElectoralDistrict = "select d.id, d.name from Users u, Districts d, BelongsTo b where b.voterID = u.id and b.districtID = d.id";
		Statement		stmt = null;
		StringBuffer	query = new StringBuffer(100);
		StringBuffer	condition = new StringBuffer(100);

		condition.setLength(0);

		query.append(selectElectoralDistrict);

		if (voter != null) {

			if (voter.getId() >= 0)
				query.append(" and u.id = " + voter.getId());

			else if (voter.getUserName() != null)
				query.append(" and u.username = '" + voter.getUserName() + "'");

			else {

				if (voter.getFirstName() != null)
					query.append(" and u.firstname = '" + voter.getFirstName() + "'");

				if (voter.getLastName() != null)
					query.append(" and u.lastname = '" + voter.getLastName() + "'");

				if (voter.getPassword() != null)
					query.append(" and u.password = '" + voter.getPassword() + "'");

				if (voter.getEmailAddress() != null)
					query.append(" and u.email = '" + voter.getEmailAddress() + "'");

				if (voter.getAddress() != null)
					query.append(" and u.address = '" + voter.getAddress() + "'");

				if (voter.getAge() != 0)
					query.append(" and u.age = '" + voter.getAge() + "'");

				if (condition.length() > 0)
					query.append(condition);
			}
		}

		try {
			stmt = conn.createStatement();

			if (stmt.execute(query.toString())) {

				ResultSet rs = stmt.getResultSet();

				long				id;
				String				name;
				ElectoralDistrict	electoralDistrict = null;

				while (rs.next()) {

					id = rs.getLong(1);
					name = rs.getString(2);

					electoralDistrict = objectLayer.createElectoralDistrict(name);
					electoralDistrict.setId(id);
				}
				return electoralDistrict;
			}
			else
				return null;
		}
		catch (Exception e) {
			throw new EVException( "VoterManager.restoreVoterBelongsToElectoralDistrict: Could not restore persistent ElectoralDistrict object; Root cause: " + e );
		}
	}

	public void storeVoter (Voter voter) throws EVException {
		String				insertVoterSql = "insert into Users (type, firstname, lastname, username, userpass, email, address, age, voterID) "
										   + "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		String				updateVoterSql = "update Users set type = ?, firstname = ?, lastname = ?, username = ?, userpass = ?, email = ?, address = ?, age = ?, voterID = ? where id = ?";
		java.sql.PreparedStatement	stmt = null;
		int					inscnt;
		long				id;

		try {
			if (!voter.isPersistent())
				stmt = (PreparedStatement)conn.prepareStatement(insertVoterSql);
			else{
				stmt = (PreparedStatement)conn.prepareStatement(updateVoterSql);
			if(voter.getId() > 0)
				stmt.setLong(10, voter.getId());
			}

			// type
			stmt.setString(1, "voter");

			// firstName
			if (voter.getFirstName() != null)
				stmt.setString(2, voter.getFirstName());
			else
				throw new EVException("VoterManager.store: can't store a Voter: firstname undefined");

			// lastName
			if (voter.getLastName() != null)
				stmt.setString(3, voter.getLastName());
			else
				throw new EVException("VoterManager.store: can't store a Voter: lastname undefined");

			// userName
			if (voter.getUserName() != null)
				stmt.setString(4, voter.getUserName());
			else
				throw new EVException("VoterManager.store: can't store a Voter: username undefined");

			// password
			if (voter.getPassword() != null)
				stmt.setString(5, voter.getPassword());
			else
				throw new EVException("VoterManager.store: can't store a Voter: password undefined");

			// emailAddress
			if (voter.getEmailAddress() != null)
				stmt.setString(6, voter.getEmailAddress());
			else
				throw new EVException("VoterManager.store: can't store a Voter: email undefined");

			// address
			if (voter.getAddress() != null)
				stmt.setString(7, voter.getAddress());
			else
				throw new EVException("VoterManager.store: can't store a Voter: address undefined");

			// age
			if (voter.getAge() != 0 || voter.getAge() != -1)
				stmt.setInt(8, voter.getAge());
			else
				stmt.setNull(8, java.sql.Types.INTEGER);

			// voterId
			if (voter.getVoterId() != null)
				stmt.setString(9, voter.getVoterId());
			else
				stmt.setNull(9, java.sql.Types.VARCHAR);

			inscnt = stmt.executeUpdate();

			if (!voter.isPersistent()) {
				// if being stored for first time, establish persistent identifier (primary key)
				if (inscnt == 1) {
					String sql = "select last_insert_id()";
					if (stmt.execute(sql)) {
						// retrieve result if there is one
						ResultSet r = stmt.getResultSet();
						//use only first row
						while(r.next()) {
							id = r.getLong(1);
							if (id > 0) {
								voter.setId(id);
							}
						}
					}
				}
			}
			else {
				if (inscnt < 1)
					throw new EVException("VoterManager.store: failed to store a Voter " + inscnt + " " + stmt.toString());
			}
		}
		catch(SQLException e) {
            e.printStackTrace();
            throw new EVException("VoterManager.store: sql new failed to store a Voter: " + e );
        }


	}

	public void storeVoterBelongsToElectoralDistrict (Voter voter, ElectoralDistrict electoralDistrict) throws EVException {

		String				insertVoterSql = "insert into BelongsTo (voterID, districtID) values (?, ?)";
		String				updateVoterSql = "update BelongsTo set districtID = ? where voterID = ?";
		java.sql.PreparedStatement	stmt = null;
		int					inscnt;
		long				voterId = voter.getId();
		long				districtId = electoralDistrict.getId();

		try {
			if (voter.isPersistent() && electoralDistrict.isPersistent() && electoralDistrict.getName()!= null) {
				stmt = (PreparedStatement)conn.prepareStatement(insertVoterSql);
				stmt.setLong(1 , voterId);
				stmt.setLong(2 , districtId);
			}
			else{
				stmt = (PreparedStatement)conn.prepareStatement(updateVoterSql);

				stmt.setLong(1, districtId);
				stmt.setLong(2, voterId);


			}

			inscnt = stmt.executeUpdate();
		}
		catch (SQLException e) {
			throw new EVException("VoterManager.storeVoterBelongsToElectoralDistrict: failed to store a link: " + e);
		}
	}
}
