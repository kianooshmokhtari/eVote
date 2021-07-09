/**
 * @file PersistenceLayerImpl.java
 * @author Team 9: Joey Bruce. Jack Fisher, Kianoosh Mokhtari, Will Runge
 */

package edu.uga.cs.evote.persistence.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.BallotItem;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.ElectionsOfficer;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.entity.VoteRecord;
import edu.uga.cs.evote.entity.Voter;


public class PersistenceLayerImpl implements PersistenceLayer {

	private BallotManager ballotManager = null;
	private CandidateManager candidateManager = null;
	private ElectionManager electionManager = null;
	private ElectionsOfficerManager electionsOfficerManager = null;
	private ElectoralDistrictManager electoralDistrictManager = null;
	private IssueManager issueManager = null;
	private PoliticalPartyManager politicalPartyManager= null;
	private VoterManager voterManager = null;
	private VoteRecordManager voteRecordManager = null;

	public PersistenceLayerImpl(Connection conn, ObjectLayer objectLayer) {
		ballotManager = new BallotManager(conn, objectLayer);
		candidateManager = new CandidateManager(conn, objectLayer);
		electionManager = new ElectionManager(conn, objectLayer);
		electionsOfficerManager = new ElectionsOfficerManager(conn, objectLayer);
		electoralDistrictManager = new ElectoralDistrictManager(conn, objectLayer);
		issueManager = new IssueManager(conn, objectLayer);
		politicalPartyManager = new PoliticalPartyManager(conn, objectLayer);
		voterManager = new VoterManager(conn, objectLayer);
		voteRecordManager = new VoteRecordManager(conn, objectLayer);
		System.out.println( "PersistenceLayerImpl.PersistenceLayerImpl(conn,objectLayer): initialized" );
	}

	/**
	 * Restore all ElectionsOfficer objects that match attributes of the model ElectionsOfficer.
	 * @param modelElectionsOfficer the model ElectionsOfficer; if null is provided, all ElectionsOfficer objects will be returned
	 * @return a List of the located ElectionsOfficer objects
	 * @throws EVException in case an error occurred during the restore operation
	 */
	public List<ElectionsOfficer> restoreElectionsOfficer(ElectionsOfficer modelElectionsOfficer) throws EVException {
		return electionsOfficerManager.restore(modelElectionsOfficer);
	}

	/**
	 * Store a given ElectionsOfficer object in the persistent data store.
	 * If the ElectionsOfficer object to be stored is already persistent, the persistent
	 * object in the data store is updated.
	 * @param electionsOfficer the ElectionsOfficer to be stored
	 * @throws EVException in case an error occurred during the store operation
	 */
	public void storeElectionsOfficer(ElectionsOfficer electionsOfficer) throws EVException {
		electionsOfficerManager.store(electionsOfficer);
	}

	/**
	 * Delete a given ElectionsOfficer object from the persistent data store.
	 * @param electionsOfficer the ElectionsOfficer to be deleted
	 * @throws EVException in case an error occurred during the delete operation
	 */
	public void deleteElectionsOfficer(ElectionsOfficer electionsOfficer) throws EVException {
		electionsOfficerManager.delete(electionsOfficer);
	}

	/**
	 * Restore all Voter objects that match attributes of the model Voter.
	 * @param modelVoter the model Voter; if null is provided, all Voter objects will be returned
	 * @return a List of the located Voter objects
	 * @throws EVException in case an error occurred during the restore operation
	 */
	public List<Voter> restoreVoter(Voter modelVoter) throws EVException {
		return voterManager.restoreVoter(modelVoter);
	}

	/**
	 * Store a given Voter object in the persistent data store.
	 * If the Voter object to be stored is already persistent, the persistent
	 * object in the data store is updated.
	 * @param voter the Voter to be stored
	 * @throws EVException in case an error occurred during the store operation
	 */
	public void storeVoter(Voter voter) throws EVException {
		voterManager.storeVoter(voter);
	}

	/**
	 * Delete a given Voter object from the persistent data store.
	 * @param voter the Voter to be deleted
	 * @throws EVException in case an error occurred during the delete operation
	 */
	public void deleteVoter(Voter voter) throws EVException {
		voterManager.deleteVoter(voter);
	}

	/**
	 * Restore all Ballot objects that match attributes of the model Ballot.
	 * @param modelBallot the model Ballot; if null is provided, all Ballot objects will be returned
	 * @return a List of the located Ballot objects
	 * @throws EVException in case an error occurred during the restore operation
	 */
	public List<Ballot> restoreBallot(Ballot modelBallot) throws EVException {
		return ballotManager.restoreBallot(modelBallot);
	}

	/**
	 * Store a given Ballot object in the persistent data store.
	 * If the Ballot object to be stored is already persistent, the persistent
	 * object in the data store is updated.
	 * @param ballot the Ballot to be stored
	 * @throws EVException in case an error occurred during the store operation
	 */
	public void storeBallot(Ballot ballot) throws EVException {
		// wanted me to surround with try-catch
		try {
			ballotManager.storeBallot(ballot);
			storeElectoralDistrictHasBallotBallot(ballot.getElectoralDistrict(), ballot);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete a given Ballot object from the persistent data store.
	 * @param ballot the Ballot to be deleted
	 * @throws EVException in case an error occurred during the delete operation
	 */
	public void deleteBallot(Ballot ballot) throws EVException {
		ballotManager.deleteBallot(ballot);
	}

	/**
	 * Restore all Candidate objects that match attributes of the model Candidate.
	 * @param modelCandidate the model Candidate; if null is provided, all Candidate objects will be returned
	 * @return a List of the located Candidate objects
	 * @throws EVException in case an error occurred during the restore operation
	 */
	public List<Candidate> restoreCandidate(Candidate modelCandidate) throws EVException {
		return candidateManager.restoreCandidate(modelCandidate);
	}

	/**
	 * Store a given Candidate object in the persistent data store.
	 * If the Candidate object to be stored is already persistent, the persistent
	 * object in the data store is updated.
	 * @param candidate the Candidate to be stored
	 * @throws EVException in case an error occurred during the store operation
	 */
	public void storeCandidate(Candidate candidate) throws EVException {
		candidateManager.storeCandidate(candidate);
		candidateManager.storeCandidateIsCandidateInElection(candidate, candidate.getElection());
		if(candidate.getPoliticalParty() != null){
			politicalPartyManager.storeCandidateIsMemberOfPoliticalParty(candidate, candidate.getPoliticalParty());
		}
	}

	/**
	 * Delete a given Candidate object from the persistent data store.
	 * @param candidate the Candidate to be deleted
	 * @throws EVException in case an error occurred during the delete operation
	 */
	public void deleteCandidate(Candidate candidate) throws EVException {
		candidateManager.deleteCandidateIsCandidateInElection(candidate, candidate.getElection() );
		if( candidate.getPoliticalParty() != null){
			candidateManager.deleteCandidateIsMemberOfElection(candidate, candidate.getPoliticalParty());
		}
		candidateManager.deleteCandidate(candidate);
	}

	/**
	 * Restore all Election objects that match attributes of the model Election.
	 * @param modelElection the model Election; if null is provided, all Election objects will be returned
	 * @return a List of the located Election objects
	 * @throws EVException in case an error occurred during the restore operation
	 */
	public List<Election> restoreElection(Election modelElection) throws EVException {
		return electionManager.restoreElection(modelElection);
	}

	/**
	 * Store a given Election object in the persistent data store.
	 * If the Election object to be stored is already persistent, the persistent
	 * object in the data store is updated.
	 * @param election the Election to be stored
	 * @throws EVException in case an error occurred during the store operation
	 */
	public void storeElection(Election election) throws EVException {
		electionManager.storeElection(election);
		ballotManager.storeBallotIncludesBallotItem(election.getBallot(), election);
	}

	/**
	 * Delete a given Election object from the persistent data store.
	 * @param election the Election to be deleted
	 * @throws EVException in case an error occurred during the delete operation
	 */
	public void deleteElection(Election election) throws EVException {
		ballotManager.deleteBallotIncludesBallotItem(election.getBallot(), election);
		electionManager.deleteElection(election);
	}

	/**
	 * Restore all ElectoralDistrict objects that match attributes of the model ElectoralDistrict.
	 * @param modelElectoralDistrict the model ElectoralDistrict; if null is provided, all ElectoralDistrict objects will be returned
	 * @return a List of the located ElectoralDistrict objects
	 * @throws EVException in case an error occurred during the restore operation
	 */
	public List<ElectoralDistrict> restoreElectoralDistrict(ElectoralDistrict modelElectoralDistrict) throws EVException {
		return electoralDistrictManager.restore(modelElectoralDistrict);
	}

	/**
	 * Store a given ElectoralDistrict object in the persistent data store.
	 * If the ElectoralDistrict object to be stored is already persistent, the persistent
	 * object in the data store is updated.
	 * @param electoralDistrict the ElectoralDistrict to be stored
	 * @throws EVException in case an error occurred during the store operation
	 */
	public void storeElectoralDistrict(ElectoralDistrict electoralDistrict) throws EVException {
		electoralDistrictManager.store(electoralDistrict);
	}

	/**
	 * Delete a given ElectoralDistrict object from the persistent data store.
	 * @param electoralDistrict the ElectoralDistrict to be deleted
	 * @throws EVException in case an error occurred during the delete operation
	 */
	public void deleteElectoralDistrict(ElectoralDistrict electoralDistrict) throws EVException {
		electoralDistrictManager.delete(electoralDistrict);
	}

	/**
	 * Restore all Issue objects that match attributes of the model Issue.
	 * @param modelIssue the model Issue; if null is provided, all Issue objects will be returned
	 * @return a List of the located Issue objects
	 * @throws EVException in case an error occurred during the restore operation
	 */
	public List<Issue> restoreIssue(Issue modelIssue) throws EVException {
		return issueManager.restore(modelIssue);
	}

	/**
	 * Store a given Issue object in the persistent data store.
	 * If the Issue object to be stored is already persistent, the persistent
	 * object in the data store is updated.
	 * @param issue the Issue to be stored
	 * @throws EVException in case an error occurred during the store operation
	 */
	public void storeIssue(Issue issue) throws EVException {
		issueManager.store(issue);
		ballotManager.storeBallotIncludesBallotItem(issue.getBallot(), issue);
	}

	/**
	 * Delete a given Issue object from the persistent data store.
	 * @param issue the Issue to be deleted
	 * @throws EVException in case an error occurred during the delete operation
	 */
	public void deleteIssue(Issue issue) throws EVException {
		ballotManager.deleteBallotIncludesBallotItem(issue.getBallot(), issue);
		issueManager.delete(issue);
	}

	/**
	 * Restore all PoliticalParty objects that match attributes of the model PoliticalParty.
	 * @param modelPoliticalParty the model PoliticalParty; if null is provided, all PoliticalParty objects will be returned
	 * @return a List of the located PoliticalParty objects
	 * @throws EVException in case an error occurred during the restore operation
	 */
	public List<PoliticalParty> restorePoliticalParty(PoliticalParty modelPoliticalParty) throws EVException {
		return politicalPartyManager.restore(modelPoliticalParty);
	}

	/**
	 * Store a given PoliticalParty object in the persistent data store.
	 * If the PoliticalParty object to be stored is already persistent, the persistent
	 * object in the data store is updated.
	 * @param politicalParty the PoliticalParty to be stored
	 * @throws EVException in case an error occurred during the store operation
	 */
	public void storePoliticalParty(PoliticalParty politicalParty) throws EVException {
		politicalPartyManager.store(politicalParty);
	}

	/**
	 * Delete a given PoliticalParty object from the persistent data store.
	 * @param politicalParty the PoliticalParty to be deleted
	 * @throws EVException in case an error occurred during the delete operation
	 */
	public void deletePoliticalParty(PoliticalParty politicalParty) throws EVException {
		politicalPartyManager.delete(politicalParty);
	}

	/**
	 * Restore all VoteRecord objects that match attributes of the model VoteRecord.
	 * @param modelVoteRecord the model VoteRecord; if null is provided, all VoteRecord objects will be returned
	 * @return a List of the located VoteRecord objects
	 * @throws EVException in case an error occurred during the restore operation
	 */
	public List<VoteRecord> restoreVoteRecord(VoteRecord modelVoteRecord) throws EVException {
		return voteRecordManager.restoreVoteRecord(modelVoteRecord);
	}

	/**
	 * Store a given VoteRecord object in the persistent data store.
	 * If the VoteRecord object to be stored is already persistent, the persistent
	 * object in the data store is updated.
	 * @param voteRecord the VoteRecord to be stored
	 * @throws EVException in case an error occurred during the store operation
	 */
	public void storeVoteRecord(VoteRecord voteRecord) throws EVException {
		voteRecordManager.storeVoteRecord(voteRecord);
	}

	/**
	 * Delete a given VoteRecord object from the persistent data store.
	 * @param voteRecord the VoteRecord to be deleted
	 * @throws EVException in case an error occurred during the delete operation
	 */
	public void deleteVoteRecord(VoteRecord voteRecord) throws EVException {
		voteRecordManager.deleteVoteRecord(voteRecord);
	}



	// Associations
	//
	// Ballot--includes-->BallotItem;   multiplicity:   1  -  *
	// Candidate--isCandidateIn-->Election;   multiplicity:   1..*  -  1
	// ElectoralDistrict--hasBallot-->Ballot;   multiplicity:   1  -  *
	// Candidate--isMemberOf-->PoliticalParty;   multiplicity:   1..*  -  1
	// Voter--belongsTo-->ElectoralDistrict;   multiplicity:   1..*  -  1
	// Voter--VoteRecord-->Ballot;  multiplicity:   *  -  *
	//          it is an association class, so no traversals


	// Ballot--includes-->BallotItem;   multiplicity: 1 - *
	//
	/**
	 * Store a link between a Ballot and a BallotItem included on the Ballot.
	 * @param ballot the Ballot to be linked
	 * @param ballotItem the BallotItem to be linked
	 * @throws EVException in case an error occurred during the store operation
	 */
	public void storeBallotIncludesBallotItem(Ballot ballot, BallotItem ballotItem) throws EVException {
		ballotManager.storeBallotIncludesBallotItem(ballot, ballotItem);
	}

	/**
	 * Return the Ballot which includes a given BallotItem.
	 * @param ballotItem the BallotItem
	 * @return the Ballot which includes the BallotItem
	 * @throws EVException in case an error occurred during the restore operation
	 */
	public Ballot restoreBallotIncludesBallotItem(BallotItem ballotItem) throws EVException {
		return ballotManager.restoreBallotIncludesBallotItem(ballotItem);
	}

	/**
	 * Return BallotItems included on a given Ballot.
	 * @param ballot the Ballot
	 * @return a List with all BallotItems included on the Ballot
	 * @throws EVException in case an error occurred during the restore operation
	 */
	public List<BallotItem> restoreBallotIncludesBallotItem(Ballot ballot) throws EVException {
		return ballotManager.restoreBallotIncludesBallotItem(ballot);
	}

	/**
	 * Delete a link between a Ballot and a BallotItem included on the Ballot.
	 * @param ballot the Ballot
	 * @param ballotItem the BallotItem
	 * @throws EVException in case an error occurred during the delete operation
	 */
	public void deleteBallotIncludesBallotItem(Ballot ballot, BallotItem ballotItem) throws EVException {
		ballotManager.deleteBallotIncludesBallotItem(ballot, ballotItem);
	}

	// Candidate--isCandidateIn-->Election;   multiplicity: 1..* - 1
	//
	/**
	 * Store a link between a Election and a Candidate included on the Election.
	 * @param election the Election to be linked
	 * @param candidate the Candidate to be linked
	 * @throws EVException in case an error occurred during the store operation
	 */
	public void storeCandidateIsCandidateInElection(Candidate candidate, Election election) throws EVException {
		candidateManager.storeCandidateIsCandidateInElection(candidate, election);
	}

	/**
	 * Return the Election in which a given Candidate runs.
	 * @param candidate the Candidate
	 * @return the Election in which the Candidate runs
	 * @throws EVException in case an error occurred during the restore operation
	 */
	public Election restoreCandidateIsCandidateInElection(Candidate candidate) throws EVException {
		return candidateManager.restoreCandidateIsCandidateInElection(candidate);
	}

	/**
	 * Return Candidates running in a given Election.
	 * @param election the Election
	 * @return a List with all Candidates running in the Election
	 * @throws EVException in case an error occurred during the restore operation
	 */
	public List<Candidate> restoreCandidateIsCandidateInElection(Election election) throws EVException {
		return candidateManager.restoreCandidateIsCandidateInElection(election);
	}

	/**
	 * Delete a link between a Election and a Candidate included on the Election.
	 * @param election the Election
	 * @param candidate the Candidate
	 * @throws EVException in case an error occurred during the delete operation
	 */
	public void deleteCandidateIsCandidateInElection(Candidate candidate, Election election) throws EVException {
		candidateManager.deleteCandidateIsCandidateInElection(candidate, election);
	}

	// ElectoralDistrict--hasBallot-->Ballot;   multiplicity:   1  -  *
	//
	/**
	 * Store a link between an ElectoralDistrict and a Ballot which is voted on by voters in the ElectoralDistrict.
	 * @param electoralDistrict the ElectoralDistrict to be linked
	 * @param ballot the Ballot to be linked
	 * @throws EVException in case an error occurred during the store operation
	 */
	public void storeElectoralDistrictHasBallotBallot(ElectoralDistrict electoralDistrict, Ballot ballot) throws EVException {
		electoralDistrictManager.storeElectoralDistrictHasBallotBallot(electoralDistrict, ballot);
	}

	/**
	 * Return the ElectoralDistrict which has a given Ballot.
	 * @param ballot the Ballot
	 * @return the ElectoralDistrict which includes the Ballot
	 * @throws EVException in case an error occurred during the restore operation
	 */
	public ElectoralDistrict restoreElectoralDistrictHasBallotBallot(Ballot ballot) throws EVException {
		return electoralDistrictManager.restoreElectoralDistrictHasBallotBallot(ballot);
	}

	/**
	 * Return alll Ballots which a given ElectoralDistrict has.
	 * @param electoralDistrict the ElectoralDistrict
	 * @return a List with all Ballots maintained by the ElectoralDistrict
	 * @throws EVException in case an error occurred during the restore operation
	 */
	public List<Ballot> restoreElectoralDistrictHasBallotBallot(ElectoralDistrict electoralDistrict) throws EVException {
		return ballotManager.restoreElectoralDistrictHasBallotBallot(electoralDistrict);
	}

	/**
	 * Delete a link between an ElectoralDistrict and a Ballot which is voted on by voters in the ElectoralDistrict.
	 * @param electoralDistrict the ElectoralDistrict
	 * @param ballot the Ballot
	 * @throws EVException in case an error occurred during the delete operation
	 */
	public void deleteElectoralDistrictHasBallotBallot(ElectoralDistrict electoralDistrict, Ballot ballot) throws EVException {
		electoralDistrictManager.deleteElectoralDistrictHasBallotBallot(electoralDistrict, ballot);
	}

	// Candidate--isMemberOf-->PoliticalParty;   multiplicity:   1..*  -  1
	/**
	 * Store a link between a Candidate and a PoliticalParty to which the Candidate belongs.
	 * @param candidate the Candidate to be linked
	 * @param politicalParty the PoliticalParty to be linked
	 * @throws EVException in case an error occurred during the store operation
	 */
	public void storeCandidateIsMemberOfPoliticalParty(Candidate candidate, PoliticalParty politicalParty) throws EVException {
		politicalPartyManager.storeCandidateIsMemberOfPoliticalParty(candidate, politicalParty);
	}

	/**
	 * Return the PoliticalParty of a given Candidate.
	 * @param candidate the Candidate
	 * @return the PoliticalParty of which the Candidate is a member
	 * @throws EVException in case an error occurred during the restore operation
	 */
	public PoliticalParty restoreCandidateIsMemberOfPoliticalParty(Candidate candidate) throws EVException {
		return candidateManager.restoreCandidateIsMemberOfPoliticalParty(candidate);
	}

	/**
	 * Return Candidates who are members of a given PoliticalParty.
	 * @param politicalParty the PoliticalParty
	 * @return a List with all Candidates who are members of the PoliticalParty
	 * @throws EVException in case an error occurred during the restore operation
	 */
	public List<Candidate> restoreCandidateIsMemberOfPoliticalParty(PoliticalParty politicalParty) throws EVException {
		return candidateManager.restoreCandidateIsMemberOfPoliticalParty(politicalParty);
	}

	/**
	 * Delete a link between a Candidate and a PoliticalParty to which the Candidate belongs.
	 * @param candidate the Candidate
	 * @param politicalParty the PoliticalParty
	 * @throws EVException in case an error occurred during the delete operation
	 */
	public void deleteCandidateIsMemberOfElection(Candidate candidate, PoliticalParty politicalParty) throws EVException {
		candidateManager.deleteCandidateIsMemberOfElection(candidate, politicalParty);
	}

	/**
	 * Store a link between a Voter and a ElectoralDistrict to which the Voter belongs.
	 * @param voter the Voter to be linked
	 * @param electoralDistrict the ElectoralDistrict to be linked
	 * @throws EVException in case an error occurred during the store operation
	 */
	public void storeVoterBelongsToElectoralDistrict(Voter voter, ElectoralDistrict electoralDistrict) throws EVException {
		voterManager.storeVoterBelongsToElectoralDistrict(voter, electoralDistrict);
	}

	/**
	 * Return the ElectoralDistrict of a given Voter.
	 * @param voter the Voter
	 * @return the ElectoralDistrict to which the Voter belongs
	 * @throws EVException in case an error occurred during the restore operation
	 */
	public ElectoralDistrict restoreVoterBelongsToElectoralDistrict(Voter voter) throws EVException {
		return voterManager.restoreVoterBelongsToElectoralDistrict(voter);
	}

	/**
	 * Return Voters who belong to a given ElectoralDistrict.
	 * @param electoralDistrict the ElectoralDistrict
	 * @return a List with all Voters who are members of the ElectoralDistrict
	 * @throws EVException in case an error occurred during the restore operation
	 */
	public List<Voter> restoreVoterBelongsToElectoralDistrict(ElectoralDistrict electoralDistrict) throws EVException {
		return voterManager.restoreVoterBelongsToElectoralDistrict(electoralDistrict);
	}

	/**
	 * Delete a link between a Voter and a ElectoralDistrict to which the Voter belongs.
	 * @param voter the Voter
	 * @param electoralDistrict the ElectoralDistrict
	 * @throws EVException in case an error occurred during the delete operation
	 */
	public void deleteVoterBelongsToElectoralDistrict(Voter voter, ElectoralDistrict electoralDistrict) throws EVException {
		voterManager.deleteVoterBelongsToElectoralDistrict(voter, electoralDistrict);
	}

}
