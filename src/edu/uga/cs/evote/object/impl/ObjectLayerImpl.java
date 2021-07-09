/**
 * @file ObjectLayerImpl.java
 * @author Team 9: Joey Bruce. Jack Fisher, Kianoosh Mokhtari, Will Runge
 */

package edu.uga.cs.evote.object.impl;

import java.util.Date;
import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.entity.*;
import edu.uga.cs.evote.entity.impl.*;

public class ObjectLayerImpl implements ObjectLayer {

	// Instance variable
	PersistenceLayer persistence;

	// Default constructor
	public ObjectLayerImpl() {
		this.persistence = null;
		System.out.println("ObjectLayerImpl.ObjectLayerImpl(): initialized");
	}

	// Specified constructor
	public ObjectLayerImpl(PersistenceLayer persistence) {
		this.persistence = persistence;
		System.out.println("ObjectLayerImpl.ObjectLayerImpl(persistence): initialized");
	}

	// Sets the persistence layer
	public void setPersistence(PersistenceLayer persistence) {
		this.persistence = persistence;
	}

	/**
	 * Create a new ElectionsOfficer object, given the set of initial attribute values.
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param userName the user name (login name)
	 * @param password the password
	 * @param emailAddress the email address
	 * @param address the address
	 * @return a new ElectionsOfficer object instance with the given attribute values
	 * @throws EVException in case either firstName, lastName, or userName is null
	 */
	public ElectionsOfficer createElectionsOfficer(	String firstName,
													String lastName,
													String userName,
													String password,
													String emailAddress,
													String address ) throws EVException {
		ElectionsOfficerImpl officer = new ElectionsOfficerImpl(
				firstName, lastName, userName, password, emailAddress, address);
		officer.setId(-1);
		officer.setPersistenceLayer(persistence);
		return officer;
	}

	/**
	 * Create a new ElectionsOfficer object with undefined attribute values.
	 * @return a new ElectionsOfficer object instance
	 */
	public ElectionsOfficer createElectionsOfficer() {
		ElectionsOfficerImpl officer = new ElectionsOfficerImpl();
		officer.setPersistenceLayer(persistence);
		return officer;
	}

	/**
	 * Return a List of ElectionsOfficer objects satisfying the search criteria given in the modelElectionsOfficer object.
	 * @param modelElectionsOfficer a model ElectionsOfficer object specifying the search criteria
	 * @return a List of the located ElectionsOfficer objects
	 * @throws EVException in case there is a problem with the retrieval of the requested objects
	 */
	public List<ElectionsOfficer> findElectionsOfficer(ElectionsOfficer modelElectionsOfficer) throws EVException {
		return persistence.restoreElectionsOfficer(modelElectionsOfficer);
	}

	/**
	 * Store a given ElectionsOfficer object in persistent data store.
	 * @param electionsOfficer the object to be persisted
	 * @throws EVException in case there was an error while persisting the object
	 */
	public void storeElectionsOfficer(ElectionsOfficer electionsOfficer) throws EVException {
		persistence.storeElectionsOfficer(electionsOfficer);
	}

	/**
	 * Delete this ElectionsOfficer object.
	 * @param electionsOfficer the object to be deleted.
	 * @throws EVException in case there is a problem with the deletion of the object
	 */
	public void deleteElectionsOfficer(ElectionsOfficer electionsOfficer) throws EVException {
		persistence.deleteElectionsOfficer(electionsOfficer);
	}

	/**
	 * Create a new Voter object, given the set of initial attribute values.
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param userName the user (login) name
	 * @param password the password
	 * @param emailAddress the email address
	 * @param address the Voter's address
	 * @param age the Voter's age
	 * @return a new Voter object instance with the given attribute values
	 * @throws EVException in case any of the String parameters is null or if age is not positive
	 */
	public Voter createVoter(	String firstName, String lastName, String userName,
								String password, String emailAddress, String address, int age ) throws EVException {
		VoterImpl voter = new VoterImpl(firstName, lastName, userName, password,
										emailAddress, address, age);
		
		voter.setPersistenceLayer(persistence);
		return voter;
	}

	/**
	 * Create a new Voter object with undefined attribute values.
	 * @return a new Voter object instance
	 */
	public Voter createVoter() {
		VoterImpl voter = new VoterImpl();
		voter.setPersistenceLayer(persistence);
		return voter;
	}

	/**
	 * Return a List of Voter objects satisfying the search criteria given in the modelVoter object.
	 * @param modelVoter a model Voter object specifying the search criteria
	 * @return a List of the located Voter objects
	 * @throws EVException in case there is a problem with the retrieval of the requested objects
	 */
	public List<Voter> findVoter(Voter modelVoter) throws EVException {
		return persistence.restoreVoter(modelVoter);
	}

	/**
	 * Store a given Voter object in persistent data store.
	 * @param voter the object to be persisted
	 * @throws EVException in case there was an error while persisting the object
	 */
	public void storeVoter(Voter voter) throws EVException {
		persistence.storeVoter(voter);
	}

	/**
	 * Delete this Voter object.
	 * @param voter the object to be deleted.
	 * @throws EVException in case there is a problem with the deletion of the object
	 */
	public void deleteVoter(Voter voter) throws EVException {
		persistence.deleteVoter(voter);
	}

	/**
	 * Create a new PoliticalParty object, given the set of initial attribute values.
	 * @param name the name of the political party
	 * @return a new PoliticalParty object instance with the given attribute values
	 * @throws EVException in case name is null
	 */
	public PoliticalParty createPoliticalParty(String name) throws EVException {
		PoliticalPartyImpl party = new PoliticalPartyImpl(name);
		//party.setId(-1);
		party.setPersistenceLayer(persistence);
		return party;
	}

	/**
	 * Create a new PoliticalParty object with undefined attribute values.
	 * @return a new PoliticalParty object instance
	 */
	public PoliticalParty createPoliticalParty() {
		PoliticalPartyImpl party = new PoliticalPartyImpl();
		party.setPersistenceLayer(persistence);
		return party;
	}

	/**
	 * Return a List of PoliticalParty objects satisfying the search criteria given in the modelPoliticalParty object.
	 * @param modelPoliticalParty a model PoliticalParty object specifying the search criteria
	 * @return a List of the located PoliticalParty objects
	 * @throws EVException in case there is a problem with the retrieval of the requested objects
	 */
	public List<PoliticalParty> findPoliticalParty(PoliticalParty modelPoliticalParty) throws EVException {
		return persistence.restorePoliticalParty(modelPoliticalParty);
	}

	/**
	 * Store a given PoliticalParty object in persistent data store.
	 * @param politicalParty the object to be persisted
	 * @throws EVException in case there was an error while persisting the object
	 */
	public void storePoliticalParty(PoliticalParty politicalParty) throws EVException{
		persistence.storePoliticalParty(politicalParty);
	}

	/**
	 * Delete this PoliticalParty object.
	 * @param politicalParty the object to be deleted.
	 * @throws EVException in case there is a problem with the deletion of the object
	 */
	public void deletePoliticalParty(PoliticalParty politicalParty) throws EVException {
		persistence.deletePoliticalParty(politicalParty);
	}

	/**
	 * Create a new ElectoralDistrict object, given the set of initial attribute values.
	 * @param name the name of the political party
	 * @return a new ElectoralDistrict object instance with the given attribute values
	 * @throws EVException in case name is null
	 */
	public ElectoralDistrict createElectoralDistrict(String name) throws EVException {
		ElectoralDistrictImpl district = new ElectoralDistrictImpl(name);
		//district.setId(-1);
		district.setPersistenceLayer(persistence);
		return district;
	}

	/**
	 * Create a new ElectoralDistrict object with undefined attribute values.
	 * @return a new ElectoralDistrict object instance
	 */
	public ElectoralDistrict createElectoralDistrict() {
		ElectoralDistrictImpl district = new ElectoralDistrictImpl();
		district.setPersistenceLayer(persistence);
		return district;
	}

	/**
	 * Return a List of ElectoralDistrict objects satisfying the search criteria given in the modelElectoralDistrict object.
	 * @param modelElectoralDistrict a model ElectoralDistrict object specifying the search criteria
	 * @return a List of the located ElectoralDistrict objects
	 * @throws EVException in case there is a problem with the retrieval of the requested objects
	 */
	public List<ElectoralDistrict> findElectoralDistrict(ElectoralDistrict modelElectoralDistrict) throws EVException {
		return persistence.restoreElectoralDistrict(modelElectoralDistrict);
	}

	/**
	 * Store a given ElectoralDistrict object in persistent data store.
	 * @param electoralDistrict the object to be persisted
	 * @throws EVException in case there was an error while persisting the object
	 */
	public void storeElectoralDistrict(ElectoralDistrict electoralDistrict) throws EVException {
		persistence.storeElectoralDistrict(electoralDistrict);
	}

	/**
	 * Delete this ElectoralDistrict object.
	 * @param electoralDistrict the object to be deleted.
	 * @throws EVException in case there is a problem with the deletion of the object
	 */
	public void deleteElectoralDistrict(ElectoralDistrict electoralDistrict) throws EVException {
		persistence.deleteElectoralDistrict(electoralDistrict);
	}

	/**
	 * Create a new Ballot object, given the set of initial attribute values.
	 * @param openDate the date when the ballot should be open for voting
	 * @param closeDate the date when the ballot should be closed for voting
	 * @param approved indication if the ballot has been approved
	 * @param electoralDistrict the electoral district of this ballot
	 * @return a new Ballot object instance with the given attribute values
	 * @throws EVException in case any of the arguments are null or if the electoralDistrict is not persistent
	 */
	public Ballot createBallot(	Date openDate, Date closeDate, boolean approved,
								ElectoralDistrict electoralDistrict)
								throws EVException {
		BallotImpl ballot =
				new BallotImpl(openDate, closeDate, approved, electoralDistrict);
		//ballot.setId(-1);
		ballot.setPersistenceLayer(persistence);
		return ballot;
	}

	/**
	 * Create a new Ballot object with undefined attribute values.
	 * @return a new Ballot object instance
	 */
	public Ballot createBallot() {
		BallotImpl ballot = new BallotImpl();
		ballot.setPersistenceLayer(persistence);
		return ballot;
	}

	/**
	 * Return a List of Ballot objects satisfying the search criteria given in the modelBallot object.
	 * @param modelBallot a model Ballot object specifying the search criteria
	 * @return a List of the located Ballot objects
	 * @throws EVException in case there is a problem with the retrieval of the requested objects
	 */
	public List<Ballot> findBallot(Ballot modelBallot) throws EVException {
		return persistence.restoreBallot(modelBallot);
	}

	/**
	 * Store a given Ballot object in persistent data store.
	 * @param ballot the object to be persisted
	 * @throws EVException in case there was an error while persisting the object
	 */
	public void storeBallot(Ballot ballot) throws EVException {
		persistence.storeBallot(ballot);
	}

	/**
	 * Delete this Ballot object.
	 * @param ballot the object to be deleted.
	 * @throws EVException in case there is a problem with the deletion of the object
	 */
	public void deleteBallot(Ballot ballot) throws EVException {
		persistence.deleteBallot(ballot);
	}

	/**
	 * Create a new Candidate object, given the set of initial attribute values.
	 * @param name the name of the candidate
	 * @param politicalParty the political party the candidate belongs to; it may be null for non partisan elections
	 * @param election the election for which this candidate is running
	 * @return a new Candidate object instance with the given attribute values
	 * @throws EVException in case either the name or the politicalParty are null
	 */
	public Candidate createCandidate(String name, PoliticalParty politicalParty, Election election) throws EVException {
		CandidateImpl candidate = new CandidateImpl(name, politicalParty, election);
		// candidate.setId(-1);
		candidate.setPersistenceLayer(persistence);
		return candidate;
	}

	/**
	 * Create a new Candidate object with undefined attribute values.
	 * @return a new Candidate object instance
	 */
	public Candidate createCandidate() {
		CandidateImpl candidate = new CandidateImpl();
		candidate.setPersistenceLayer(persistence);
		return candidate;
	}

	/**
	 * Return a List of Candidate objects satisfying the search criteria given in the modelCandidate object.
	 * @param modelCandidate a model Candidate object specifying the search criteria
	 * @return a List of the located Candidate objects
	 * @throws EVException in case there is a problem with the retrieval of the requested objects
	 */
	public List<Candidate> findCandidate(Candidate modelCandidate) throws EVException {
		return persistence.restoreCandidate(modelCandidate);
	}

	/**
	 * Store a given Candidate object in persistent data store.
	 * @param candidate the object to be persisted
	 * @throws EVException in case there was an error while persisting the object
	 */
	public void storeCandidate(Candidate candidate) throws EVException {
		persistence.storeCandidate(candidate);
	}

	/**
	 * Delete this Candidate object.
	 * @param candidate the object to be deleted.
	 * @throws EVException in case there is a problem with the deletion of the object
	 */
	public void deleteCandidate(Candidate candidate) throws EVException {
		persistence.deleteCandidate(candidate);
	}

	/**
	 * Create a new Issue object, given the set of initial attribute values.
	 * @param question the question of the this issue
	 * @return a new Issue object instance with the given attribute value
	 * @throws EVException in case question is null
	 */
	public Issue createIssue(String question, Ballot ballot) throws EVException {
		IssueImpl issue = new IssueImpl(question, ballot);
		// issue.setId(-1);
		issue.setPersistenceLayer(persistence);
		return issue;
	}

	/**
	 * Create a new Issue object with undefined attribute values.
	 * @return a new Issue object instance
	 */
	public Issue createIssue() {
		IssueImpl issue = new IssueImpl();
		issue.setPersistenceLayer(persistence);
		return issue;
	}

	/**
	 * Return a List of Issue objects satisfying the search criteria given in the modelIssue object.
	 * @param modelIssue a model Issue object specifying the search criteria
	 * @return a List of the located Issue objects
	 * @throws EVException in case there is a problem with the retrieval of the requested objects
	 */
	public List<Issue> findIssue(Issue modelIssue) throws EVException {
		return persistence.restoreIssue(modelIssue);
	}

	/**
	 * Store a given Issue object in persistent data store.
	 * @param issue the object to be persisted
	 * @throws EVException in case there was an error while persisting the object
	 */
	public void storeIssue(Issue issue) throws EVException {
		persistence.storeIssue(issue);
	}

	/**
	 * Delete this Issue object.
	 * @param issue the object to be deleted.
	 * @throws EVException in case there is a problem with the deletion of the object
	 */
	public void deleteIssue(Issue issue) throws EVException {
		persistence.deleteIssue(issue);
	}

	/**
	 * Create a new Election object, given the set of initial attribute values.
	 * @param office the office this Election is for
	 * @param isPartisan indication if this Election is partisan
	 * @return a new Election object instance with the given attribute value
	 * @throws EVException in case question is null
	 */
	public Election createElection(String office, boolean isPartisan, Ballot ballot) throws EVException {
		ElectionImpl election = new ElectionImpl(office, isPartisan, ballot);
		// election.setId(-1);
		election.setPersistenceLayer(persistence);
		return election;
	}

	/**
	 * Create a new Election object with undefined attribute values.
	 * @return a new Election object instance
	 */
	public Election createElection() {
		ElectionImpl election = new ElectionImpl();
		election.setPersistenceLayer(persistence);
		return election;
	}

	/**
	 * Return a List of Election objects satisfying the search criteria given in the modelElection object.
	 * @param modelElection a model Election object specifying the search criteria
	 * @return a List of the located Election objects
	 * @throws EVException in case there is a problem with the retrieval of the requested objects
	 */
	public List<Election> findElection(Election modelElection) throws EVException {
		return persistence.restoreElection(modelElection);
	}

	/**
	 * Store a given Election object in persistent data store.
	 * @param election the object to be persisted
	 * @throws EVException in case there was an error while persisting the object
	 */
	public void storeElection(Election election) throws EVException {
		persistence.storeElection(election);
	}

	/**
	 * Delete this Election object.
	 * @param election the object to be deleted.
	 * @throws EVException in case there is a problem with the deletion of the object
	 */
	public void deleteElection(Election election) throws EVException {
		persistence.deleteElection(election);
	}

	/**
	 * Create a new VoteRecord object, given the set of initial attribute values.
	 * @param ballot the Ballot for which a vote has been cast
	 * @param voter the Voter who cast a vote
	 * @param date the Date when the vote has been cast
	 * @return a new VoteRecord object instance with the given attribute value
	 * @throws EVException in case either of the arguments is null
	 */
	public VoteRecord createVoteRecord(Ballot ballot, Voter voter, Date date) throws EVException {
		VoteRecordImpl record = new VoteRecordImpl(ballot, voter, date);
		record.setPersistenceLayer(persistence);
		return record;
	}

	/**
	 * Create a new VoteRecord object with undefined attribute values.
	 * @return a new VoteRecord object instance
	 */
	public VoteRecord createVoteRecord() {
		VoteRecordImpl record = new VoteRecordImpl();
		record.setPersistenceLayer(persistence);
		return record;
	}

	/**
	 * Return a List of VoteRecord objects satisfying the search criteria given in the modelVoteRecord object.
	 * @param modelVoteRecord a model VoteRecord object specifying the search criteria
	 * @return a List of the located VoteRecord objects
	 * @throws EVException in case there is a problem with the retrieval of the requested objects
	 */
	public List<VoteRecord> findVoteRecord(VoteRecord modelVoteRecord) throws EVException {
		return persistence.restoreVoteRecord(modelVoteRecord);
	}

	/**
	 * Store a given VoteRecord object in persistent data store.
	 * @param voteRecord the object to be persisted
	 * @throws EVException in case there was an error while persisting the object
	 */
	public void storeVoteRecord(VoteRecord voteRecord) throws EVException {
		persistence.storeVoteRecord(voteRecord);
	}

	/**
	 * Delete this VoteRecord object.
	 * @param voteRecord the object to be deleted.
	 * @throws EVException in case there is a problem with the deletion of the object
	 */
	public void deleteVoteRecord(VoteRecord voteRecord) throws EVException {
		persistence.deleteVoteRecord(voteRecord);
	}

}
