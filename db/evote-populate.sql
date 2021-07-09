# `evote-populate.sql`
# Populates the evote database with test data.
# Written by Team 9: Will Runge, Jack Fisher, Joey Bruce, and Kianoosh Mokhtari


# Create two elections officers
INSERT INTO Users (type, username, userpass, firstname, lastname, email, address)
	VALUES ('officer', 'johnd', 'password', 'John', 'Doe', 'johnd@gmail.com', '101 Broad St., Athens, GA. 30605');
INSERT INTO Users (type, username, userpass, firstname, lastname, email, address)
	VALUES ('officer', 'mk', 'trustno1', 'M.K.', 'Smith', 'mksmith@gmail.com', '102 Broad St., Athens, GA. 30605');


# Create an electoral district
INSERT INTO Districts (name)
	VALUES ('District 1');


# Create two voters
INSERT INTO Users (type, username, userpass, firstname, lastname, email, address, age, voterID)
	VALUES ('voter', 'janed', 'default', 'Jane', 'Doe', 'janed@gmail.com', '103 Broad St., Athens, GA. 30605', 28, "janed");
INSERT INTO Users (type, username, userpass, firstname, lastname, email, address, age, voterID)
	VALUES ('voter', 'annad', 'badpassword', 'Anna', 'Duncan', 'annad@gmail.com', '104 Broad St., Athens, GA. 30605', 50, "annad");


# Add voters to the district
INSERT INTO BelongsTo (voterID, districtID)
	SELECT
		(SELECT id FROM Users WHERE username = 'janed') AS voterID,
		(SELECT id FROM Districts WHERE name = 'District 1') AS districtID;
INSERT INTO BelongsTo (voterID, districtID)
	SELECT
		(SELECT id FROM Users WHERE username = 'annad') AS voterID,
		(SELECT id FROM Districts WHERE name = 'District 1') AS districtID;


# Create three political parties
INSERT INTO Parties (name)
	VALUES ('Republican');
INSERT INTO Parties (name)
	VALUES ('Democrat');
INSERT INTO Parties (name)
	VALUES ("Libertarian");


# Create two ballots
INSERT INTO Ballots (openDate, closeDate, approved)
	VALUES ('2016-11-08', '2017-02-01', FALSE );
INSERT INTO Ballots (openDate, closeDate, approved)
	VALUES ('2016-05-08', '2017-04-14', FALSE );


# Add ballots to the district
INSERT INTO HasBallot (districtID, ballotID)
	SELECT
		(SELECT id FROM Districts WHERE name = 'District 1') AS districtID,
		(SELECT id FROM Ballots WHERE openDate = '2016-11-08') AS ballotID;
INSERT INTO HasBallot (districtID, ballotID)
	SELECT
		(SELECT id FROM Districts WHERE name = 'District 1') AS districtID,
		(SELECT id FROM Ballots WHERE openDate = '2016-05-08') AS ballotID;


# Create 3 issues and 3 elections for EACH ballot (12 ballot items total).
INSERT INTO Items (type, votecount, question, yesCount)
	VALUES ('issue', 0, 'Should the death penalty be allowed?', 0 );
INSERT INTO Items (type, votecount, question, yesCount)
	VALUES ('issue', 0, 'Should ownership of private prisons be transferred to the state?', 0 );
INSERT INTO Items (type, votecount, question, yesCount)
	VALUES ('issue', 0, 'Is the growth of charter schools good for education in America?', 0 );
INSERT INTO Items (type, votecount, office, isPartisan)
	VALUES ('election', 0, 'President', TRUE );
INSERT INTO Items (type, votecount, office, isPartisan)
	VALUES ('election', 0, 'U.S. Representative', FALSE );
INSERT INTO Items (type, votecount, office, isPartisan)
	VALUES ('election', 0, 'Georgia Supreme Court', FALSE );

INSERT INTO Items (type, votecount, question, yesCount)
	VALUES ('issue', 0, 'Should the United States build additional nuclear power plants?', 0 );
INSERT INTO Items (type, votecount, question, yesCount)
	VALUES ('issue', 0, 'Should the United States Maintain its embargo against Cuba?', 0 );
INSERT INTO Items (type, votecount, question, yesCount)
	VALUES ('issue', 0, 'Shall existing taxes be allocated to fund relief programs for sexually abused children?', 0);
INSERT INTO Items (type, votecount, office, isPartisan)
	VALUES ('election', 0, 'Georgia Courts of Appeal Judge', FALSE );
INSERT INTO Items (type, votecount, office, isPartisan)
	VALUES ('election', 0, 'County Court Justice', FALSE );
INSERT INTO Items (type, votecount, office, isPartisan)
	VALUES ('election', 0, 'U.S. Senator', TRUE );


# Assign first six items to one ballot, next six to the other ballot
INSERT INTO Includes (ballotID, itemID)
	SELECT
		(SELECT id FROM Ballots WHERE id = 1) AS ballotID,
		(SELECT id FROM Items WHERE id = 1) AS itemID;
INSERT INTO Includes (ballotID, itemID)
	SELECT
		(SELECT id FROM Ballots WHERE id = 1) AS ballotID,
		(SELECT id FROM Items WHERE id = 2) AS itemID;
INSERT INTO Includes (ballotID, itemID)
	SELECT
		(SELECT id FROM Ballots WHERE id = 1) AS ballotID,
		(SELECT id FROM Items WHERE id = 3) AS itemID;
INSERT INTO Includes (ballotID, itemID)
	SELECT
		(SELECT id FROM Ballots WHERE id = 1) AS ballotID,
		(SELECT id FROM Items WHERE id = 4) AS itemID;
INSERT INTO Includes (ballotID, itemID)
	SELECT
		(SELECT id FROM Ballots WHERE id = 1) AS ballotID,
		(SELECT id FROM Items WHERE id = 5) AS itemID;
INSERT INTO Includes (ballotID, itemID)
	SELECT
		(SELECT id FROM Ballots WHERE id = 1) AS ballotID,
		(SELECT id FROM Items WHERE id = 6) AS itemID;

INSERT INTO Includes (ballotID, itemID)
	SELECT
		(SELECT id FROM Ballots WHERE id = 2) AS ballotID,
		(SELECT id FROM Items WHERE id = 7) AS itemID;
INSERT INTO Includes (ballotID, itemID)
	SELECT
		(SELECT id FROM Ballots WHERE id = 2) AS ballotID,
		(SELECT id FROM Items WHERE id = 8) AS itemID;
INSERT INTO Includes (ballotID, itemID)
	SELECT
		(SELECT id FROM Ballots WHERE id = 2) AS ballotID,
		(SELECT id FROM Items WHERE id = 9) AS itemID;
INSERT INTO Includes (ballotID, itemID)
	SELECT
		(SELECT id FROM Ballots WHERE id = 2) AS ballotID,
		(SELECT id FROM Items WHERE id = 10) AS itemID;
INSERT INTO Includes (ballotID, itemID)
	SELECT
		(SELECT id FROM Ballots WHERE id = 2) AS ballotID,
		(SELECT id FROM Items WHERE id = 11) AS itemID;
INSERT INTO Includes (ballotID, itemID)
	SELECT
		(SELECT id FROM Ballots WHERE id = 2) AS ballotID,
		(SELECT id FROM Items WHERE id = 12) AS itemID;

# Create three candidates for each election. 3 x 6 = 18 total candidates.
INSERT INTO Candidates (name, voteCount)
	VALUES ('Donald Trump', 0);
INSERT INTO Candidates (name, voteCount)
	VALUES ('Hillary Clinton', 0);
INSERT INTO Candidates (name, voteCount)
	VALUES ("Gary Johnson", 0);
INSERT INTO Candidates (name, voteCount)
	VALUES ('Johnny Isakson', 0);
INSERT INTO Candidates (name, voteCount)
	VALUES ('Jim Barksdale', 0);
INSERT INTO Candidates (name, voteCount)
	VALUES ('David Nahmias', 0);
INSERT INTO Candidates (name, voteCount)
	VALUES ('Anne Barnes', 0);
INSERT INTO Candidates (name, voteCount)
	VALUES ('Chris McFadden', 0);
INSERT INTO Candidates (name, voteCount)
	VALUES ('John Smith', 0);
INSERT INTO Candidates (name, voteCount)
	VALUES ("Andrew Ryan", 0);
INSERT INTO Candidates (name, voteCount)
	VALUES ("Zachary Hale Comstock", 0);
INSERT INTO Candidates (name, voteCount)
	VALUES ("Emhyr var Emreis", 0);
INSERT INTO Candidates (name, voteCount)
	VALUES ("Foltest", 0);
INSERT INTO Candidates (name, voteCount)
	VALUES ("Demavend", 0);
INSERT INTO Candidates (name, voteCount)
	VALUES ("Sigismund Dijkstra", 0);
INSERT INTO Candidates (name, voteCount)
	VALUES ("Cirilla Fiona Elen Riannon", 0);
INSERT INTO Candidates (name, voteCount)
	VALUES ("Vernon Roche", 0);
INSERT INTO Candidates (name, voteCount)
	VALUES ("Radovid V", 0);


# Assign candidates to elections.
INSERT INTO IsCandidateIn (candidateID, electionID)
	SELECT
		(SELECT id FROM Candidates WHERE name = 'Donald Trump') AS candidateID,
		(SELECT id FROM Items WHERE office = 'President') AS electionID;
INSERT INTO IsCandidateIn (candidateID, electionID)
	SELECT
		(SELECT id FROM Candidates WHERE name = 'Hillary Clinton') AS candidateID,
		(SELECT id FROM Items WHERE office = 'President') AS electionID;
INSERT INTO IsCandidateIn (candidateID, electionID)
	SELECT
		(SELECT id FROM Candidates WHERE name = "Gary Johnson") AS candidateID,
		(SELECT id FROM Items WHERE office = "President") AS electionID;

INSERT INTO IsCandidateIn (candidateID, electionID)
	SELECT
		(SELECT id FROM Candidates WHERE name = 'Johnny Isakson') AS candidateID,
		(SELECT id FROM Items WHERE office = 'U.S. Senator') AS electionID;
INSERT INTO IsCandidateIn (candidateID, electionID)
	SELECT
		(SELECT id FROM Candidates WHERE name = 'Jim Barksdale') AS candidateID,
		(SELECT id FROM Items WHERE office = 'U.S. Senator') AS electionID;
INSERT INTO IsCandidateIn (candidateID, electionID)
	SELECT
		(SELECT id FROM Candidates WHERE name = "Andrew Ryan") AS candidateID,
		(SELECT id FROM Items WHERE office = "U.S. Senator") AS electionID;

INSERT INTO IsCandidateIn (candidateID, electionID)
	SELECT
		(SELECT id FROM Candidates WHERE name = 'David Nahmias') AS candidateID,
		(SELECT id FROM Items WHERE office = 'Georgia Supreme Court') AS electionID;
INSERT INTO IsCandidateIn (candidateID, electionID)
	SELECT
		(SELECT id FROM Candidates WHERE name = "Vernon Roche") AS candidateID,
		(SELECT id FROM Items WHERE office = 'Georgia Supreme Court') AS electionID;
INSERT INTO IsCandidateIn (candidateID, electionID)
	SELECT
		(SELECT id FROM Candidates WHERE name = "Zachary Hale Comstock") AS candidateID,
		(SELECT id FROM Items WHERE office = 'Georgia Supreme Court') AS electionID;

INSERT INTO IsCandidateIn (candidateID, electionID)
	SELECT
		(SELECT id FROM Candidates WHERE name = 'Anne Barnes') AS candidateID,
		(SELECT id FROM Items WHERE office = 'Georgia Courts of Appeal Judge') AS electionID;
INSERT INTO IsCandidateIn (candidateID, electionID)
	SELECT
		(SELECT id FROM Candidates WHERE name = 'Chris McFadden') AS candidateID,
		(SELECT id FROM Items WHERE office = 'Georgia Courts of Appeal Judge') AS electionID;
INSERT INTO IsCandidateIn (candidateID, electionID)
	SELECT
		(SELECT id FROM Candidates WHERE name = "Emhyr var Emreis") AS candidateID,
		(SELECT id FROM Items WHERE office = 'Georgia Courts of Appeal Judge') AS electionID;

INSERT INTO IsCandidateIn (candidateID, electionID)
	SELECT
		(SELECT id FROM Candidates WHERE name = 'John Smith') AS candidateID,
		(SELECT id FROM Items WHERE office = 'County Court Justice') AS electionID;
INSERT INTO IsCandidateIn (candidateID, electionID)
	SELECT
		(SELECT id FROM Candidates WHERE name = "Foltest") AS candidateID,
		(SELECT id FROM Items WHERE office = 'County Court Justice') AS electionID;
INSERT INTO IsCandidateIn (candidateID, electionID)
	SELECT
		(SELECT id FROM Candidates WHERE name = "Demavend") AS candidateID,
		(SELECT id FROM Items WHERE office = 'County Court Justice') AS electionID;

INSERT INTO IsCandidateIn (candidateID, electionID)
	SELECT
		(SELECT id FROM Candidates WHERE name = "Sigismund Dijkstra") AS candidateID,
		(SELECT id FROM Items WHERE office = "U.S. Representative") AS electionID;
INSERT INTO IsCandidateIn (candidateID, electionID)
	SELECT
		(SELECT id FROM Candidates WHERE name = "Cirilla Fiona Elen Riannon") AS candidateID,
		(SELECT id FROM Items WHERE office = "U.S. Representative") AS electionID;
INSERT INTO IsCandidateIn (candidateID, electionID)
	SELECT
		(SELECT id FROM Candidates WHERE name = "Radovid V") AS candidateID,
		(SELECT id FROM Items WHERE office = "U.S. Representative") AS electionID;


# Assign political parties to candidates in partisan elections.
INSERT INTO IsMemberOf (candidateID, partyID)
	SELECT
		(SELECT id FROM Candidates WHERE name = "Donald Trump") AS candidateID,
		(SELECT id FROM Parties WHERE name = "Republican") AS partyID;
INSERT INTO IsMemberOf (candidateID, partyID)
	SELECT
		(SELECT id FROM Candidates WHERE name = "Hillary Clinton") AS candidateID,
		(SELECT id FROM Parties WHERE name = "Democrat") AS partyID;
INSERT INTO IsMemberOf (candidateID, partyID)
	SELECT
		(SELECT id FROM Candidates WHERE name = "Gary Johnson") AS candidateID,
		(SELECT id FROM Parties WHERE name = "Libertarian") AS partyID;

INSERT INTO IsMemberOf (candidateID, partyID)
	SELECT
		(SELECT id FROM Candidates WHERE name = "Johnny Isakson") AS candidateID,
		(SELECT id FROM Parties WHERE name = "Republican") AS partyID;
INSERT INTO IsMemberOf (candidateID, partyID)
	SELECT
		(SELECT id FROM Candidates WHERE name = "Jim Barksdale") AS candidateID,
		(SELECT id FROM Parties WHERE name = "Democrat") AS partyID;
INSERT INTO IsMemberOf (candidateID, partyID)
	SELECT
		(SELECT id FROM Candidates WHERE name = "Andrew Ryan") AS candidateID,
		(SELECT id FROM Parties WHERE name = "Libertarian") AS partyID;




################################################################################
# VOTING BEGINS HERE
# Create data representing the voting of the 2 voters.
# Both voters should vote on both ballots.
# Both voters should vote on every item on each ballot.
################################################################################


# Both voters vote on the first ballot
UPDATE Items
	SET voteCount = 2, yesCount = 1
	WHERE id = 1;
UPDATE Items
	SET voteCount = 2, yesCount = 2
	WHERE id = 2;
UPDATE Items
	SET voteCount = 2, yesCount = 0
	WHERE id = 3;


UPDATE Candidates
	SET voteCount = 1
	WHERE name = "Hillary Clinton" OR name = "Gary Johnson";
UPDATE Items
	SET voteCount = 2
	WHERE office = "President";
UPDATE Candidates
	SET voteCount = 2
	WHERE name = "Zachary Hale Comstock";
UPDATE Items
	SET voteCount = 2
	WHERE office = "Georgia Supreme Court";
UPDATE Candidates
	SET voteCount = 1
	WHERE name = "Emhyr var Emreis" OR name = "Chris McFadden";
UPDATE Items
	SET voteCount = 2
	WHERE office = "Georgia Courts of Appeal Judge";


# Create vote records for both voters and the first ballot
INSERT INTO VoteRecords (voterID, ballotID)
	SELECT
		(SELECT id FROM Users WHERE username = "janed") as voterID,
		(SELECT id FROM Ballots WHERE id = 1) as ballotID;
INSERT INTO VoteRecords (voterID, ballotID)
	SELECT
		(SELECT id FROM Users WHERE username = "annad") as voterID,
		(SELECT id FROM Ballots WHERE id = 1) as ballotID;


# Both voters vote on the second ballot
UPDATE Items
	SET voteCount = 2, yesCount = 1
	WHERE id = 7;
UPDATE Items
	SET voteCount = 2, yesCount = 0
	WHERE id = 8;
UPDATE Items
	SET voteCount = 2, yesCount = 2
	WHERE id = 9;

UPDATE Candidates
	SET voteCount = 1
	WHERE name = "Johnny Isakson" OR name = "Andrew Ryan";
UPDATE Items
	SET voteCount = 2
	WHERE office = "U.S. Senator";
UPDATE Candidates
	SET voteCount = 1
	WHERE name = "Foltest" OR name = "Demavend";
UPDATE Items
	SET voteCount = 2
	WHERE office = "County Court Justice";
UPDATE Candidates
	SET voteCount = 2
	WHERE name = "Cirilla Fiona Elen Riannon";
UPDATE Items
	SET voteCount = 2
	WHERE office = "U.S. Representative";


# Create vote records for both voters and the second ballot
INSERT INTO VoteRecords (voterID, ballotID)
	SELECT
		(SELECT id FROM Users WHERE username = "janed") as voterID,
		(SELECT id FROM Ballots WHERE id = 2) as ballotID;
INSERT INTO VoteRecords (voterID, ballotID)
	SELECT
		(SELECT id FROM Users WHERE username = "annad") as voterID,
		(SELECT id FROM Ballots WHERE id = 2) as ballotID;
