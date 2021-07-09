/**
 * @file LogicLayerImpl.java
 * @author Team 9: Joey Bruce, Jack Fisher, Kianoosh Mokhtari, Will Runge
 */

package edu.uga.cs.evote.logic.impl;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.*;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;


public class LogicLayerImpl implements LogicLayer {

	private ObjectLayer objectLayer = null;

	// Constructor `LogicLayerImpl(Connection)`
	public LogicLayerImpl(Connection conn) {
		this.objectLayer = new ObjectLayerImpl();
		PersistenceLayer persistenceLayer = new PersistenceLayerImpl(conn, objectLayer);
		this.objectLayer.setPersistence(persistenceLayer);
		System.out.println("LogicLayerImpl.LogicLayerImpl(conn): initialized");
	}

	// Constructor `LogicLayerImpl(ObjectLayer)`
	public LogicLayerImpl(ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
		System.out.println("LobicLayerImpl.LogicLayerImpl(objectLayer): initialized");
	}

	// USER STORY METHODS PUT HERE

	// 1 & 30
	public String login(Session session, String userName, String password)
			throws EVException {
		LoginCtrl ctrlVerifyPerson = new LoginCtrl(objectLayer);
		return ctrlVerifyPerson.login(session, userName, password);
	}

	// 2

	// 3 & 32
	public void logout(String ssid) throws EVException {
		SessionManager.logout(ssid);
	}

	// 4
	public long createElectoralDistrict(Session session, String name) throws EVException{

		CreateElectoralDistrictCtrl ctrl = new CreateElectoralDistrictCtrl(objectLayer);
		long id = ctrl.create(session, name);
		return id;

	}

	public long editElectoralDistrict(String oldName, String newName) throws EVException{

		EditElectoralDistrictCtrl ctrl = new EditElectoralDistrictCtrl(objectLayer);
		long id = ctrl.edit(oldName, newName);
		return id;

	}

	// 5
	public long createElection(String office, boolean isPartisan, String ballotID) throws EVException {
		CreateElectionCtrl ctrl = new CreateElectionCtrl(objectLayer);
		long id = ctrl.create(office, isPartisan, ballotID);
		return id;
	}

	public long editElection(String originalOffice,String Office, boolean isPartisan, String ballotID) throws EVException{
		EditElectionCtrl ctrl = new EditElectionCtrl(objectLayer);
		long id = ctrl.edit(originalOffice, Office, isPartisan, ballotID);
		return id;
	}

	// 6
	public long createCandidate(String name, String partyName, String Office) throws EVException{
		CreateCandidateCtrl ctrl = new CreateCandidateCtrl(objectLayer);
		long id = ctrl.create( name, partyName, Office);
		return id;
	}

	public long editCandidate(String oldName,String name, String partyName, String Office) throws EVException{
		EditCandidateCtrl ctrl = new EditCandidateCtrl(objectLayer);
		long id = ctrl.edit(oldName, name, partyName, Office);
		return id;
	}
	// 7
	public long createPoliticalParty(Session session, String name) throws EVException{

		CreatePoliticalPartyCtrl ctrl = new CreatePoliticalPartyCtrl(objectLayer);
		long id = ctrl.create(session, name);
		return id;

	}

	public long editPoliticalParty(String oldName, String newName) throws EVException{

		EditPoliticalPartyCtrl ctrl = new EditPoliticalPartyCtrl(objectLayer);
		long id = ctrl.edit(oldName, newName);
		return id;

	}

	// 8
	public long createIssue(String question, String ballotID) throws EVException {
		CreateIssueCtrl ctrl = new CreateIssueCtrl(objectLayer);
		long id = ctrl .create(question, ballotID);
		return id;
	}
	
	public long editIssue(String ID, String question, String ballotID) throws EVException{
		EditIssueCtrl ctrl = new EditIssueCtrl(objectLayer);
		long id = ctrl.edit(ID, question, ballotID);
		return id;
	}

	// 9
	public long createBallot(Date openDate, Date closeDate, boolean approved, String electoralDistrict) throws EVException {

		CreateBallotCtrl ctrl = new CreateBallotCtrl(objectLayer);
		long id = ctrl.create(openDate, closeDate, approved, electoralDistrict);
		return id;
	}

	// 10
	public List<String> findAllDistricts() throws EVException{

		FindAllDistrictsCtrl  ctrl = new FindAllDistrictsCtrl(objectLayer);
		List<String> districts = ctrl.find();
		return districts;

	}

	// 11
	public List<String> findAllElections() throws EVException{

		FindAllElectionsCtrl ctrl = new FindAllElectionsCtrl(objectLayer);
		List<String> elections = ctrl.find();
		return elections;

	}
	// 12
	public List<String> findAllCandidates() throws EVException{

		FindAllCandidatesCtrl ctrl = new FindAllCandidatesCtrl(objectLayer);
		List<String> candidates = ctrl.find();
		return candidates;

	}

	// 13
	public List<String> findAllParties() throws EVException{

		FindAllPartiesCtrl  ctrl = new FindAllPartiesCtrl(objectLayer);
		List<String> parties = ctrl.find();
		return parties;

	}

	// 14
	public List<String> findAllIssues() throws EVException{

		FindAllIssuesCtrl  ctrl = new FindAllIssuesCtrl(objectLayer);
		List<String> issues = ctrl.find();
		return issues;

	}

	// 15
	public List<String> findAllBallots() throws EVException{

		FindAllBallotsCtrl  ctrl = new FindAllBallotsCtrl(objectLayer);
		List<String> ballots = ctrl.find();
		return ballots;

	}

	// 16
	public long deleteDistrict(String name) throws EVException{

		DeleteDistrictCtrl ctrl = new DeleteDistrictCtrl(objectLayer);
		long id = ctrl.delete(name);
		return id;
	}

	// 17
	public long deleteElection(String ID) throws EVException{
		DeleteElectionCtrl ctrl = new DeleteElectionCtrl(objectLayer);
		long id = ctrl.delete(ID);
		return id;
	}
	// 18
	public long deleteCandidate(String name) throws EVException{
		DeleteCandidateCtrl ctrl = new DeleteCandidateCtrl(objectLayer);
		long id = ctrl.delete(name);
		return id;
	}
	// 19
	public long deleteParty(String name) throws EVException{
		DeletePartyCtrl ctrl = new DeletePartyCtrl(objectLayer);
		long id = ctrl.delete(name);
		return id;
	}
	// 20
	public long deleteIssue(String ID) throws EVException{
		DeleteIssueCtrl ctrl = new DeleteIssueCtrl(objectLayer);
		long id = ctrl.delete(ID);
		return id;
	}
	// 21
	public long deleteBallot(String ID) throws EVException{
		DeleteBallotCtrl ctrl = new DeleteBallotCtrl(objectLayer);
		long id = ctrl.delete(ID);
		return id;
	}

	// 22

	// 23

	// 24

	// 25

	// 26

	// 27

	// 28

	// 29
	public long createVoter(String userName, String password, String email,
			String firstName, String lastName, String address,
			int age, String district) throws EVException {

		CreateVoterCtrl ctrl = new CreateVoterCtrl(objectLayer);
		long id = ctrl.createVoter(userName, password, email, firstName, lastName, address, age ,district);

		return id;
	}

	// 31

	public void resetPassword(String userName, String newPass) throws EVException{

		ResetPasswordCtrl ctrl = new ResetPasswordCtrl(objectLayer);

		ctrl.reset(userName, newPass);

	}

	// 33

	public Voter getProfile(String userName) throws EVException{

		GetProfileCtrl ctrl = new GetProfileCtrl(objectLayer);




		return ctrl.getProfile(userName);
	}

public void editVoterFirstName(String userName,String firstName) throws EVException{

	EditVoterFirstNameCtrl ctrl = new EditVoterFirstNameCtrl(objectLayer);
						ctrl.edit(userName, firstName);



}

	public void editVoterLastName(String userName,String lastName) throws EVException{

		EditVoterLastNameCtrl ctrl = new EditVoterLastNameCtrl(objectLayer);
				ctrl.edit(userName, lastName);

	}

	public void editVoterAge(String userName,String age) throws EVException{

		EditVoterAgeCtrl ctrl = new EditVoterAgeCtrl(objectLayer);
					ctrl.edit(userName, age);

	}

	public void editVoterAddress(String userName,String address) throws EVException{

		EditVoterAddressCtrl ctrl = new EditVoterAddressCtrl(objectLayer);
						ctrl.edit(userName, address);

	}

	public void editVoterEmail(String userName,String email) throws EVException{

		EditVoterEmailCtrl ctrl = new EditVoterEmailCtrl(objectLayer);
				ctrl.edit(userName, email);

	}

	public void editVoterDistrict(String userName,String district) throws EVException{

				EditVoterDistrictCtrl ctrl = new EditVoterDistrictCtrl(objectLayer);
				ctrl.edit(userName, district);

	}

	// 34

	// 35




	public void castIssueVote(Voter voter, String who) throws EVException{

		CastIssueVoteCtrl ctrl = new CastIssueVoteCtrl(objectLayer);
		ctrl.Cast(voter, who);
	}

	public void castElectionVote(Voter voter, String who) throws EVException{

		CastElectionVoteCtrl ctrl = new CastElectionVoteCtrl(objectLayer);

			ctrl.Cast(voter, who);
	}

	public void createVoterRecord(Voter voter, String ballotId) throws EVException{

		CreateVoterRecordCtrl ctrl = new CreateVoterRecordCtrl(objectLayer);

		 ctrl.Create(voter, ballotId);

	}

	public List <VoteRecord> findVoteRecord(Voter voter) throws EVException{
		FindVoteRecordCtrl ctrl = new FindVoteRecordCtrl(objectLayer);
		return ctrl.find(voter);
	}





	// 36

	public void deleteVoterAccount(String userName) throws EVException{

		DeleteVoterAccountCtrl ctrl = new DeleteVoterAccountCtrl(objectLayer);
		ctrl.delete(userName);



	}

	// 37

}
