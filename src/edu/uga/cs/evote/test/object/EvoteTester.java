/**
 * @file EvoteTester.java
 * @author Team 9: Joey Bruce, Jack Fisher, Kianoosh Mokhtari, Will Runge
 */

package edu.uga.cs.evote.test.object;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Iterator;
import java.sql.Connection;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.*;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;
import edu.uga.cs.evote.persistence.impl.DbUtils;


public class EvoteTester {

	// Max length for displaying issue questions.
	private static final int MAX_LEN = 58;


	  /////////////////
	 // MAIN METHOD //
	/////////////////

	// Main method: executes all tests required by the Part 6 instruction document.
	public static void main(String[] args) {

		System.out.println("================================================================================");
		System.out.println("TEAM 9 EVOTE PERSISTENCE AND OBJECT LAYER TESTS");
		System.out.println("Team 9 is Joey Bruce, Jack Fisher, Kianoosh Mokhtari, and Will Runge");
		System.out.println("--------------------------------------------------------------------------------");

		System.out.println("READ TEST -- RDBMS is empty:");
		System.out.println("----------------------------");
		readTest();		// Show there are no persistent objects.
		System.out.println("--------------------------------------------------------------------------------");

		System.out.println("WRITE TEST -- filling RDBMS");
		System.out.println("---------------------------");
		writeTest();	// Create new persistent objects.
		System.out.println("---------------------------");
		readTest();		// Show that persistent objects have been created.
		System.out.println("--------------------------------------------------------------------------------");

		System.out.println("VOTE TEST -- updating RDBMS to simulate voting");
		System.out.println("----------------------------------------------");
		voteTest();		// Update persistent objects to reflect users voting.
		System.out.println("----------------------------------------------");
		readTest();		// Show that persistent objects have been updated.
		System.out.println("--------------------------------------------------------------------------------");

		System.out.println("DELETE TEST -- removing all data from RDBMS");
		System.out.println("-------------------------------------------");
		deleteTest();	// Delete all persistent objects.
		System.out.println("-------------------------------------------");
		readTest();		// Show all persistent objects have been deleted.
		System.out.println("--------------------------------------------------------------------------------");

		System.out.println("END OF ALL TESTS");
		System.out.println("================================================================================");

	}




	  ////////////////////
	 // HELPER METHODS //
	////////////////////


	/**
	 * `readTest()` method: reads the contents of the persistent RDBMS
	 */
	private static void readTest() {

		Connection				conn =			null;
		ObjectLayerImpl			objectLayer =	null;
		PersistenceLayerImpl	persistence =	null;

		// Gets & verifies a database connection
		try {
			conn = DbUtils.connect();
		} catch(Exception seq) {
			System.err.println("ReadTest: Unable to obtain a database connection.");
		}
		if (conn == null) {
			System.out.println("ReadTest: failed to connect to the database");
			return;
		}

		// Obtains references to and connects the ObjectModel and Persistence modules
		objectLayer = new ObjectLayerImpl();
		persistence = new PersistenceLayerImpl(conn, objectLayer);
		objectLayer.setPersistence(persistence);

		try {

			// TESTS BEGIN HERE //

			System.out.println("Elections officer objects:");
			List<ElectionsOfficer> officers = objectLayer.findElectionsOfficer(null);
			for (ElectionsOfficer o : officers) {
				System.out.println(o);
			}
			System.out.println();

			System.out.println("Voter objects:");
			List<Voter> voters = objectLayer.findVoter(null);
			for (Voter v : voters) {
				System.out.print(v);
				System.out.println(" " + v.getElectoralDistrict().getName());
			}
			System.out.println();

			System.out.println("Electoral district objects:");
			List<ElectoralDistrict> districts = objectLayer.findElectoralDistrict(null);
			for (ElectoralDistrict d : districts) {
				System.out.println(d);
				voters = d.getVoters();
				System.out.print("\tVoters:  ");
				for (Voter v : voters)
					System.out.print(v.getUserName() + ", ");
				System.out.println();
				List<Ballot> ballots = d.getBallots();
				System.out.print("\tBallots:  ");
				for (Ballot b : ballots)
					System.out.print(b.getId() + ", ");
				System.out.println();
			}
			System.out.println();

			System.out.println("Ballot objects:");
			List<Ballot> ballots = objectLayer.findBallot(null);
			for (Ballot b : ballots) {
				System.out.println(b);
				List<BallotItem> items = b.getBallotItems();
				System.out.println("\tBallot items: ");
				for (BallotItem i : items) {
					System.out.print("\t\t" + i.getId() + ", ");
					if (i instanceof Issue) {
						if (((Issue) i).getQuestion().length() < MAX_LEN)
							System.out.println(((Issue) i).getQuestion());
						else
							System.out.println(((Issue) i).getQuestion().substring(0,MAX_LEN) + "...");
					}
					else if (i instanceof Election)
						System.out.println(((Election) i).getOffice());
				}
				System.out.println();
			}
			System.out.println();

			System.out.println("Issue objects:");
			List<Issue> issues = objectLayer.findIssue(null);
			for (Issue i : issues) {
				if (i.getQuestion().length() < MAX_LEN + 4)
					System.out.println("Issue[" + i.getId() + "] "
							+ i.getVoteCount() + " " + i.getYesCount() + " "
							+ i.getNoCount() + " " + i.getQuestion());
				else
					System.out.println("Issue[" + i.getId() + "] "
							+ i.getVoteCount() + " " + i.getYesCount() + " "
							+ i.getNoCount() + " "
							+ i.getQuestion().substring(0, MAX_LEN + 4) + "...");
			}
			System.out.println();

			System.out.println("Election objects:");
			List<Election> elections = objectLayer.findElection(null);
			for (Election e : elections) {
				System.out.println(e);
				List<Candidate> candidates = e.getCandidates();
				System.out.print("\tCandidates:  ");
				for (Candidate c : candidates)
					System.out.print(c.getName() + ", ");
				System.out.println();
			}
			System.out.println();

			System.out.println("Candidate objects:");
			List<Candidate> candidates = objectLayer.findCandidate(null);
			for (Candidate c : candidates) {
				if (c.getPoliticalParty() != null)
					System.out.println(c + " " + c.getPoliticalParty().getName()
							+ " " + c.getElection().getOffice());
				else
					System.out.println(c + " " + c.getPoliticalParty() + " "
							+ c.getElection().getOffice());
			}
			System.out.println();

			System.out.println("Party objects:");
			List<PoliticalParty> parties = objectLayer.findPoliticalParty(null);
			for (PoliticalParty p : parties) {
				System.out.println(p);
				candidates = p.getCandidates();
				System.out.print("\tCandidates:  ");
				for (Candidate c : candidates) {
					System.out.print(c.getName() + ", ");
				}
				System.out.println();
			}
			System.out.println();

			System.out.println("Vote Record objects:");
			List<VoteRecord> records = objectLayer.findVoteRecord(null);
			for (VoteRecord r : records) {
				System.out.println(r);
				System.out.println("\tVoter:  " + r.getVoter().getUserName());
				System.out.println("\tBallot: " + r.getBallot().getId());
			}

			// TESTS END HERE //

		} catch (EVException eve) {
			System.err.println("EVException: " + eve);
		} catch (Exception e) {
			System.out.flush();
			System.err.println("Exception: " + e);
		} finally {
			// Close the connection, NO MATTER WHAT!!!
			try {
				conn.close();
			} catch(Exception e) {
				System.err.println("Exception while closing connection: " + e);
			}
		}
	}


	/**
	 * `writeTest()` method: Creates objects and stores them in the RDBMS.
	 */
	private static void writeTest() {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date d1, d2;

		Connection				conn =			null;
		ObjectLayerImpl			objectLayer =	null;
		PersistenceLayerImpl	persistence =	null;

		// Gets & verifies a database connection
		try {
			conn = DbUtils.connect();
		} catch(Exception seq) {
			System.err.println("writeTest: Unable to obtain a database connection.");
		}
		if (conn == null) {
			System.out.println("writeTest: failed to connect to the database");
			return;
		}

		// Obtains references to and connects the ObjectModel and Persistence modules
		objectLayer = new ObjectLayerImpl();
		persistence = new PersistenceLayerImpl(conn, objectLayer);
		objectLayer.setPersistence(persistence);

		try {


			// TESTS BEGIN HERE //


			// Create 2 elections officers.
			System.out.print("Creating and storing two elections officers... ");
			System.out.flush();
			ElectionsOfficer john =
					objectLayer.createElectionsOfficer("John", "Doe", "johnd",
					"password", "johnd@gmail.com", "101 Broad St., Athens, GA 30605");
			ElectionsOfficer mk =
					objectLayer.createElectionsOfficer("M.K.", "Smith", "mk",
					"trustno1", "mksmit@gmail.com", "102 Broad St., Athens, GA 30605");
			persistence.storeElectionsOfficer(john);
			persistence.storeElectionsOfficer(mk);
			System.out.println("done.");


			// Create an electoral district.
			System.out.print("Creating and storing an electoral district... ");
			System.out.flush();
			ElectoralDistrict district9 = objectLayer.createElectoralDistrict("District 9");
			persistence.storeElectoralDistrict(district9);
			System.out.println("done.");


			// Create 2 voters that belong to the electoral district created.
			System.out.print("Creating and storing two voters... ");
			System.out.flush();
			Voter jane =
					objectLayer.createVoter("Jane", "Doe", "janed", "default",
						"janed@gmail.com", "103 Broad St., Athens, GA 30605", 28);
			Voter anna =
					objectLayer.createVoter("Anna", "Duncan", "annad",
						"badpassword", "annad@gmail.com", "104 Broad St., Athens, GA 30605", 50);
			jane.setVoterId("voter1");
			anna.setVoterId("voter2");
			persistence.storeVoter(jane);
			persistence.storeVoter(anna);
			System.out.println("done.");

			System.out.print("Assigning both voters to the created district... ");
			System.out.flush();
			jane.setElectoralDistrict(district9);
			anna.setElectoralDistrict(district9);
			System.out.println("done.");


			// Create 3 political parties.
			System.out.print("Creating and storing three political parties... ");
			System.out.flush();
			PoliticalParty rep = objectLayer.createPoliticalParty("Republican");
			PoliticalParty dem = objectLayer.createPoliticalParty("Democrat");
			PoliticalParty lib = objectLayer.createPoliticalParty("Libertarian");
			persistence.storePoliticalParty(rep);
			persistence.storePoliticalParty(dem);
			persistence.storePoliticalParty(lib);
			System.out.println("done.");


			// Create 2 ballots, each with 3 issues and 3 elections. One of the
			// elections on each ballot should be partisan. Each election on
			// every ballot should have 3 candidates. Make sure that if an
			// election is partisan, the candidates are members of political
			// parties (use the ones you created before).

			// Create a ballot
			System.out.print("Creating and storing the first ballot... ");
			System.out.flush();
			d1 = sdf.parse("08/11/2016");
			d2 = sdf.parse("01/02/2017");
			Ballot ballot1 = objectLayer.createBallot(d1, d2, false, district9);
			persistence.storeBallot(ballot1);
			System.out.println("done.");

			// Create 3 issues and add them to the ballot
			System.out.print("Creating and storing three issues... ");
			System.out.flush();
			Issue issue1 = objectLayer.createIssue("Should the death penalty be allowed?", ballot1);
			Issue issue2 = objectLayer.createIssue("Should ownership of private prisons be transferred to the state?", ballot1);
			Issue issue3 = objectLayer.createIssue("Should the state of Georgia be given power to run failing schools?", ballot1);
			persistence.storeIssue(issue1);
			persistence.storeIssue(issue2);
			persistence.storeIssue(issue3);
			System.out.println("done.");

			// Create 3 elections, each with 3 candidates. One election must be partisan.
			System.out.print("Creating and storing three elections... ");
			System.out.flush();
			Election election1 = objectLayer.createElection("President", true, ballot1);
			Election election2 = objectLayer.createElection("U.S. Representative", false, ballot1);
			Election election3 = objectLayer.createElection("Georgia Supreme Court", false, ballot1);
			persistence.storeElection(election1);
			persistence.storeElection(election2);
			persistence.storeElection(election3);
			System.out.println("done.");

			System.out.print("Creating nine candidates... ");
			System.out.flush();
			Candidate trump =	objectLayer.createCandidate("Donald Trump", rep, election1);
			Candidate clinton =	objectLayer.createCandidate("Hillary Clinton", dem, election1);
			Candidate johnson =	objectLayer.createCandidate("Gary Johnson", lib, election1);
			persistence.storeCandidate(trump);
			persistence.storeCandidate(clinton);
			persistence.storeCandidate(johnson);
			Candidate isakson =		objectLayer.createCandidate("Johnny Isakson", null, election2);
			Candidate barksdale =	objectLayer.createCandidate("Jim Barksdale", null, election2);
			Candidate nahmias =		objectLayer.createCandidate("David Nahmias", null, election2);
			persistence.storeCandidate(isakson);
			persistence.storeCandidate(barksdale);
			persistence.storeCandidate(nahmias);
			Candidate barnes =		objectLayer.createCandidate("Anne Barnes", null, election3);
			Candidate mcFadden =	objectLayer.createCandidate("Chris McFadden", null, election3);
			Candidate smith =		objectLayer.createCandidate("John Smith", null, election3);
			persistence.storeCandidate(barnes);
			persistence.storeCandidate(mcFadden);
			persistence.storeCandidate(smith);
			System.out.print("done.");


			// Create another ballot
			System.out.print("Creating and storing the second ballot... ");
			System.out.flush();
			d1 = sdf.parse("08/05/2016");
			d2 = sdf.parse("14/04/2017");
			Ballot ballot2 = objectLayer.createBallot(d1, d2, false, district9);
			persistence.storeBallot(ballot2);
			System.out.println("done.");

			// Create 3 issues and add them to the ballot
			System.out.print("Creating and storing three issues... ");
			System.out.flush();
			Issue issue4 = objectLayer.createIssue("Should the U.S. build additional nuclear power plants?", ballot2);
			Issue issue5 = objectLayer.createIssue("Should the U.S. maintain its embargo against Cuba?", ballot2);
			Issue issue6 = objectLayer.createIssue("Shall existing taxes on fireworks be used to fund the fire department?", ballot2);
			persistence.storeIssue(issue4);
			persistence.storeIssue(issue5);
			persistence.storeIssue(issue6);
			System.out.println("done.");

			// Create 3 elections, each with 3 candidates. One election must be partisan.
			System.out.print("Creating and storing three elections... ");
			System.out.flush();
			Election election4 = objectLayer.createElection("Georgia Court of Appeals Judge", true, ballot2);
			Election election5 = objectLayer.createElection("County Court Justice", false, ballot2);
			Election election6 = objectLayer.createElection("U.S. Senator", false, ballot2);
			persistence.storeElection(election4);
			persistence.storeElection(election5);
			persistence.storeElection(election6);
			System.out.println("done.");

			System.out.print("Creating nine candidates... ");
			System.out.flush();
			Candidate ryan =		objectLayer.createCandidate("Andrew Ryan", dem, election4);
			Candidate comstock =	objectLayer.createCandidate("Zachary Hale Comstock", rep, election4);
			Candidate emhyr =		objectLayer.createCandidate("Emhyr var Emreis", lib, election4);
			persistence.storeCandidate(ryan);
			persistence.storeCandidate(comstock);
			persistence.storeCandidate(emhyr);
			Candidate foltest =		objectLayer.createCandidate("Foltest", null, election5);
			Candidate demavend =	objectLayer.createCandidate("Demavend", null, election5);
			Candidate dijkstra =	objectLayer.createCandidate("Dijkstra", null, election5);
			persistence.storeCandidate(foltest);
			persistence.storeCandidate(demavend);
			persistence.storeCandidate(dijkstra);
			Candidate ciri =	objectLayer.createCandidate("Cirilla Fiona Elen Riannon", null, election6);
			Candidate roche =	objectLayer.createCandidate("Vernon Roche", null, election6);
			Candidate radovid =	objectLayer.createCandidate("Radovid V", null, election6);
			persistence.storeCandidate(ciri);
			persistence.storeCandidate(roche);
			persistence.storeCandidate(radovid);
			System.out.println("done.");

			// TESTS END HERE //


		} catch (EVException eve) {
			System.err.println("EVException: " + eve);
		} catch (Exception e) {
			System.out.flush();
			System.err.println("Exception: " + e);
		} finally {
			// Close the connection, NO MATTER WHAT!!!
			try {
				conn.close();
			} catch(Exception e) {
				System.err.println("Exception while closing connection: " + e);
			}
		}

	}


	/**
	 * `voteTest()` method: Updates RDBMS to simulate voting
	 */
	private static void voteTest() {

		Connection				conn =			null;
		ObjectLayerImpl			objectLayer =	null;
		PersistenceLayerImpl	persistence =	null;

		// Gets & verifies a database connection
		try {
			conn = DbUtils.connect();
		} catch(Exception seq) {
			System.err.println("voteTest: Unable to obtain a database connection.");
		}
		if (conn == null) {
			System.out.println("voteTest: failed to connect to the database");
			return;
		}

		// Obtains references to and connects the ObjectModel and Persistence modules
		objectLayer = new ObjectLayerImpl();
		persistence = new PersistenceLayerImpl(conn, objectLayer);
		objectLayer.setPersistence(persistence);

		try {

			// TESTS BEGIN HERE //

			// Create data to represent the voting of the 2 voters you have
			// created. Both voters should vote on both ballots you have
			// created. Also, the created data should represent the fact that
			// both voters voted on every issue and election on the ballot.

			// Both voters vote on both ballots
			System.out.print("Both voters voting on both ballots... ");
			System.out.flush();
			List<Ballot> ballots = objectLayer.findBallot(null);
			for (Ballot b : ballots) {
				List<BallotItem> items = b.getBallotItems();
				for (BallotItem i : items) {
					if (i instanceof Issue) {
						Issue issue = (Issue) i;
						issue.addYesVote();
						issue.addNoVote();
						objectLayer.storeIssue(issue);
					} else if (i instanceof Election) {
						Election e = (Election) i;
						e.addVote();
						e.addVote();
						persistence.storeElection(e);
						List<Candidate> candidates = e.getCandidates();
						int j = 0;
						for (Candidate c : candidates) {
							if (j < 2)
								c.addVote();
							j++;
							persistence.storeCandidate(c);
						}
					}
				}
			}
			System.out.println("done.");

			// Create vote records for both voters voting on both ballots.
			System.out.print("Creating vote records for both voters on both ballots... ");
			System.out.flush();
			List<Voter> voters = objectLayer.findVoter(null);
			for (Voter v : voters) {
				for (Ballot b : ballots) {
					VoteRecord record = objectLayer.createVoteRecord(b, v, new Date());
					persistence.storeVoteRecord(record);
				}
			}
			System.out.println("done.");

			// TESTS END HERE //

		} catch (EVException eve) {
			System.err.println("EVException: " + eve);
		} catch (Exception e) {
			System.out.flush();
			System.err.println("Exception: " + e);
		} finally {
			// Close the connection, NO MATTER WHAT!!!
			try {
				conn.close();
			} catch(Exception e) {
				System.err.println("Exception while closing connection: " + e);
			}
		}

	}

	/**
	 * `deleteTest()` method: Deletes all entries from the RDBMS
	 */
	private static void deleteTest() {

		Connection				conn =			null;
		ObjectLayerImpl			objectLayer =	null;
		PersistenceLayerImpl	persistence =	null;

		// Gets & verifies a database connection
		try {
			conn = DbUtils.connect();
		} catch(Exception seq) {
			System.err.println("deleteTest: Unable to obtain a database connection.");
		}
		if (conn == null) {
			System.out.println("deleteTest: failed to connect to the database");
			return;
		}

		// Obtains references to and connects the ObjectModel and Persistence modules
		objectLayer = new ObjectLayerImpl();
		persistence = new PersistenceLayerImpl(conn, objectLayer);
		objectLayer.setPersistence(persistence);

		try {

			// TESTS BEGIN HERE //

			System.out.print("Deleting elections officers... ");
			System.out.flush();
			List<ElectionsOfficer> officers = objectLayer.findElectionsOfficer(null);
			for (ElectionsOfficer o : officers)
				objectLayer.deleteElectionsOfficer(o);
			System.out.println("done.");

			System.out.print("Deleting voters... ");
			System.out.flush();
			List<Voter> voters = objectLayer.findVoter(null);
			for (Voter v : voters) {
				//persistence.deleteVoterBelongsToElectoralDistrict(v, v.getElectoralDistrict());
				objectLayer.deleteVoter(v);
			}
			System.out.println("done.");

			System.out.print("Deleting districts... ");
			System.out.flush();
			List<ElectoralDistrict> districts = objectLayer.findElectoralDistrict(null);
			for (ElectoralDistrict d : districts)
				objectLayer.deleteElectoralDistrict(d);
			System.out.println("done.");

			System.out.print("Deleting parties... ");
			System.out.flush();
			List<PoliticalParty> parties = objectLayer.findPoliticalParty(null);
			for (PoliticalParty p : parties)
				objectLayer.deletePoliticalParty(p);
			System.out.println("done.");

			System.out.print("Deleting candidates... ");
			System.out.flush();
			List<Candidate> candidates = objectLayer.findCandidate(null);
			for (Candidate c : candidates)
				objectLayer.deleteCandidate(c);
			System.out.println("done.");

			System.out.print("Deleting elections... ");
			System.out.flush();
			List<Election> elections = objectLayer.findElection(null);
			for (Election e : elections)
				objectLayer.deleteElection(e);
			System.out.println("done.");

			System.out.print("Deleting issues... ");
			System.out.flush();
			List<Issue> issues = objectLayer.findIssue(null);
			for (Issue i : issues)
				objectLayer.deleteIssue(i);
			System.out.println("done.");

			System.out.print("Deleting ballots... ");
			System.out.flush();
			List<Ballot> ballots = objectLayer.findBallot(null);
			for (Ballot b : ballots)
				objectLayer.deleteBallot(b);
			System.out.println("done.");

			System.out.print("Deleting vote records... ");
			System.out.flush();
			List<VoteRecord> records = objectLayer.findVoteRecord(null);
			for (VoteRecord v : records)
				objectLayer.deleteVoteRecord(v);
			System.out.println("done.");

			// TESTS END HERE //

		} catch (EVException eve) {
			System.err.println("EVException: " + eve);
		} catch (Exception e) {
			System.out.flush();
			System.err.println("Exception: " + e);
		} finally {
			// Close the connection, NO MATTER WHAT!!!
			try {
				conn.close();
			} catch(Exception e) {
				System.err.println("Exception while closing connection: " + e);
			}
		}

	}

}
