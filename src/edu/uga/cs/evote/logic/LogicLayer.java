/**
 * @file LogicLayer.java
 * @author Team 9: Joey Bruce, Jack Fisher, Kianoosh Mokhtari, Will Runge
 */

package edu.uga.cs.evote.logic;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.*;
import edu.uga.cs.evote.logic.impl.CreateElectoralDistrictCtrl;
import edu.uga.cs.evote.logic.impl.CreateVoterCtrl;
import edu.uga.cs.evote.logic.impl.EditElectoralDistrictCtrl;
import edu.uga.cs.evote.logic.impl.FindAllDistrictsCtrl;
import edu.uga.cs.evote.logic.impl.LoginCtrl;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;


public interface LogicLayer {

	// PUT MORE METHOD SIGNATURES HERE

	// 1 & 30
	public String login(Session session, String userName, String password) throws EVException;

	// 2

	// 3 & 32
	public void logout(String ssid) throws EVException;

	// 4
	public long createElectoralDistrict(Session session, String name) throws EVException;
	public long editElectoralDistrict(String oldName, String newName) throws EVException;

	// 5
	public long createElection(String office, boolean isPartisan, String ballotID) throws EVException;
	public long editElection(String originalOffice, String Office, boolean isPartisan, String ballotID) throws EVException;
	// 6
	public long createCandidate(String name, String partyName, String Office) throws EVException;
	public long editCandidate(String oldName,String name, String partyName, String Office) throws EVException;
	// 7
	public long createPoliticalParty(Session session, String name) throws EVException;
	public long editPoliticalParty(String oldName, String newName) throws EVException;

	// 8
	public long createIssue(String question, String ballotID) throws EVException;
	public long editIssue(String ID, String question, String ballotID) throws EVException;
	// 9
	public long createBallot(Date openDate, Date closeDate, boolean approved, String electoralDistrict) throws EVException;
	// 10
	public List<String> findAllDistricts() throws EVException;

	// 11
	public List<String> findAllElections() throws EVException;
	// 12
	public List<String> findAllCandidates() throws EVException;

	// 13
	public List<String> findAllParties() throws EVException;

	// 14
	public List<String> findAllIssues() throws EVException;

	// 15
	public List<String> findAllBallots() throws EVException;

	// 16
	public long deleteDistrict(String name) throws EVException;
	// 17
	public long deleteElection(String ID) throws EVException;
	// 18
	public long deleteCandidate(String name) throws EVException;
	// 19
	public long deleteParty(String name) throws EVException;
	// 20
	public long deleteIssue(String ID) throws EVException;
	// 21
	public long deleteBallot(String ID) throws EVException;

	// 22

	// 23

	// 24

	// 25

	// 26

	// 27

	// 28

	// 29
	public long createVoter(String firstName, String lastName, String userName,
			String password, String email, String address,
			int age, String district) throws EVException;

	// 31
	public void resetPassword(String userName, String newPass) throws EVException;

	// 33

	public Voter getProfile(String userName) throws EVException;

	//public ElectoralDistrict getUserDistrict(String id );

	public void editVoterFirstName(String userName, String firstName) throws EVException;

	public void editVoterLastName(String userName, String lastName) throws EVException;

	public void editVoterAge(String userName,String age) throws EVException;

	public void editVoterAddress(String userName,String address) throws EVException;

	public void editVoterEmail(String userName,String email) throws EVException;

	public void editVoterDistrict(String userName,String district) throws EVException;





	// 34

	// 35

	public void castElectionVote(Voter voter, String who) throws EVException;

	public void castIssueVote(Voter voter, String who) throws EVException;

	public void createVoterRecord(Voter voter, String ballotId) throws EVException;

	public List <VoteRecord> findVoteRecord(Voter voter) throws EVException;

	//public List <Ballot> findBallots(ElectoralDistrict ED) throws EVException;

	// 36

	public void deleteVoterAccount(String userName) throws EVException;

	// 37

}
