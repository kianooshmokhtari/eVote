# `evote-schema.sql`
# Creates the schema for the eVote database.
# Written by Team 9: Will Runge, Jack Fisher, Joey Bruce, and Kianoosh Mokhtari


# remove existing relationship tables
DROP TABLE IF EXISTS IsMemberOf;
DROP TABLE IF EXISTS IsCandidateIn;
DROP TABLE IF EXISTS Includes;
DROP TABLE IF EXISTS HasBallot;
DROP TABLE IF EXISTS BelongsTo;
DROP TABLE IF EXISTS IsOpen;

# remove existing association class tables
DROP TABLE IF EXISTS VoteRecords;

# remove existing object tables
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Items;
DROP TABLE IF EXISTS Candidates;
DROP TABLE IF EXISTS Parties;
DROP TABLE IF EXISTS Ballots;
DROP TABLE IF EXISTS Districts;


# Table definition for table `Users`
CREATE TABLE Users (
	id			INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	type		ENUM ("voter", "officer") NOT NULL,
	username	VARCHAR(255) NOT NULL UNIQUE,
	userpass	VARCHAR(255) NOT NULL,
	firstname	VARCHAR(255) NOT NULL,
	lastname	VARCHAR(255) NOT NULL,
	email		VARCHAR(255) NOT NULL,
	address		VARCHAR(255) NOT NULL,

	# only used for "Voter" users
	age			INT UNSIGNED,
	voterID		VARCHAR(255) UNIQUE

	# CHECK (type IN ("voter", "officer")).
) ENGINE=InnoDB;


# Table definition for table `Items`
CREATE TABLE Items (
	id			INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	type		ENUM ("issue", "election") NOT NULL,
	votecount	INT UNSIGNED NOT NULL DEFAULT 0,

	# for issues only
	question	VARCHAR(255),
	yesCount	INT UNSIGNED,

	# for elections only
	office		VARCHAR(255),
	isPartisan	BOOLEAN

	# CHECK (type IN ("voter", "officer")).
) ENGINE=InnoDB;


# Table definition for table `Candidates`
CREATE TABLE Candidates (
	id			INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	name		VARCHAR(255) NOT NULL,
	votecount	INT UNSIGNED NOT NULL DEFAULT 0
) ENGINE=InnoDB;


# Table definition for table `Parties`
CREATE TABLE Parties (
	id		INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	name	VARCHAR(255) NOT NULL
) ENGINE=InnoDB;


# Table definition for table `Ballots`
CREATE TABLE Ballots (
	id			INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	openDate	DATE NOT NULL,
	closeDate	DATE NOT NULL,
	approved	BOOLEAN NOT NULL DEFAULT FALSE
) ENGINE=InnoDB;


# Table definition for table `Districts`
CREATE TABLE Districts (
	id		INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	name	VARCHAR(255) NOT NULL UNIQUE
) ENGINE=InnoDB;


# Table definition for table `VoteRecords`
CREATE TABLE VoteRecords (
	id			INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	date		TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	voterID		INT UNSIGNED NOT NULL,
	ballotID	INT UNSIGNED NOT NULL,

	FOREIGN KEY (voterID) REFERENCES Users(id) ON DELETE CASCADE,
	FOREIGN KEY (ballotID) REFERENCES Ballots(id) ON DELETE CASCADE
) ENGINE=InnoDB;


# Table definition for table `IsMemberOf`
CREATE TABLE IsMemberOf (
	id			INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	candidateID	INT UNSIGNED NOT NULL,
	partyID		INT UNSIGNED NOT NULL,

	FOREIGN KEY (candidateID) REFERENCES Candidates(id) ON DELETE CASCADE,
	FOREIGN KEY (partyID) REFERENCES Parties(id) ON DELETE CASCADE
) ENGINE=InnoDB;


# Table definition for table `IsCandidateIn`
CREATE TABLE IsCandidateIn (
	id			INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	candidateID	INT UNSIGNED NOT NULL,
	electionID	INT UNSIGNED NOT NULL,

	FOREIGN KEY (candidateID) REFERENCES Candidates(id) ON DELETE CASCADE,
	FOREIGN KEY (electionID) REFERENCES Items(id) ON DELETE CASCADE
) ENGINE=InnoDB;


# Table definition for table `Includes`
CREATE TABLE Includes (
	id			INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	ballotID	INT UNSIGNED NOT NULL,
	itemID		INT UNSIGNED NOT NULL,

	FOREIGN KEY (ballotID) REFERENCES Ballots(id) ON DELETE CASCADE,
	FOREIGN KEY (itemID) REFERENCES Items(id) ON DELETE CASCADE
) ENGINE=InnoDB;


# Table definition for table `HasBallot`
CREATE TABLE HasBallot (
	id			INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	districtID	INT UNSIGNED NOT NULL,
	ballotID	INT UNSIGNED NOT NULL,

	FOREIGN KEY (districtID) REFERENCES Districts(id) ON DELETE CASCADE,
	FOREIGN KEY (ballotID) REFERENCES Ballots(id) ON DELETE CASCADE
) ENGINE=InnoDB;


# Table definition for table `BelongsTo`
CREATE TABLE BelongsTo (
	id			INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	voterID		INT UNSIGNED NOT NULL UNIQUE,
	districtID	INT UNSIGNED NOT NULL,

	FOREIGN KEY (voterID) REFERENCES Users(id) ON DELETE CASCADE,
	FOREIGN KEY (districtID) REFERENCES Districts(id) ON DELETE CASCADE
) ENGINE=InnoDB;

# Table definition for table `IsOpen`
#CREATE TABLE IsOpen (
#    	id			INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
#    	ballotID	INT UNSIGNED NOT NULL,
#    	canOpen 		BOOLEAN NOT NULL DEFAULT FALSE,

#	FOREIGN KEY (ballotID) REFERENCES Ballots(id) ON DELETE CASCADE
#) ENGINE=InnoDB;



###################
# BEGIN TEST DATA #
###################

# Add a single elections officer with username and password both "admin"
INSERT INTO Users (type, username, userpass, firstname, lastname, email, address)
	VALUES ('officer', 'admin', 'admin', 'Elections', 'Officer', 'e.officer@mail.com', '123 Main St., Marcelline, MS');

# Add a single voter with username "misterblister" and password "demo"
INSERT INTO Users (type, username, userpass, firstname, lastname, email, address, age, voterID)
	VALUES ("voter", "misterblister", "demo", "Mister", "Blister", "this@that.com", "123 Sesame St.", 3, "misterblister2");

# Add a single voter with username and password "demo"
INSERT INTO Users (type, username, userpass, firstname, lastname, email, address, age, voterID)
	VALUES ("voter", "demo", "demo", "Demo", "McDemoFace", "demo@demo.com", "123 Demo Close", 4370, "demo3");

# Add 10 districts for testing.
INSERT INTO Districts (name)
	VALUES ("District 1");
INSERT INTO Districts (name)
	VALUES ("District 2");
INSERT INTO Districts (name)
	VALUES ("District 3");
INSERT INTO Districts (name)
	VALUES ("District 4");
INSERT INTO Districts (name)
	VALUES ("District 5");
INSERT INTO Districts (name)
	VALUES ("District 6");
INSERT INTO Districts (name)
	VALUES ("District 7");
INSERT INTO Districts (name)
	VALUES ("District 8");
INSERT INTO Districts (name)
	VALUES ("District 9");


# Add voters to the district
INSERT INTO BelongsTo (voterID, districtID)
	SELECT
		(SELECT id FROM Users WHERE username = 'misterblister') AS voterID,
		(SELECT id FROM Districts WHERE name = 'District 9') AS districtID;

INSERT INTO BelongsTo (voterID, districtID)
	SELECT
		(SELECT id FROM Users WHERE username = 'demo') AS voterID,
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
		(SELECT id FROM Districts WHERE name = 'District 9') AS districtID,
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
# Now we are in a "pre-voting" state, as in `evote-populate.sql` before
# "VOTING BEGINS HERE"
################################################################################
