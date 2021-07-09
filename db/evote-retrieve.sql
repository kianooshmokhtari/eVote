# `evote-retrieve.sql`
# Populates the evote database with test data.
# Written by Team 9: Will Runge, Jack Fisher, Joey Bruce, and Kianoosh Mokhtari

# List all elections officers
SELECT *
FROM Users u
WHERE u.type = "officer";

# List all electoral districts
SELECT *
FROM Districts;

# List all voters and their electoral districts.
SELECT *
FROM Users u
	INNER JOIN BelongsTo b ON u.id = b.voterID
	INNER JOIN Districts d ON d.id = b.districtID;

# List all political parties
SELECT *
FROM Parties;

# List all ballots, including their issues and elections
SELECT b.id AS ballotID, it.question, it.office
FROM Ballots b, Items it, Includes ic
WHERE b.id = ic.ballotID AND ic.itemID = it.id;

# List all issues
SELECT *
FROM Items i
WHERE i.type = "issue";

# List all non-partisan elections, each including the candidates
SELECT i.office, c.name
FROM Items i, Candidates c, IsCandidateIn ici
WHERE i.id = ici.electionID AND c.id = ici.candidateID AND i.isPartisan = FALSE;

# List all partisan elections, each including the candidates and their political
# parties
SELECT i.office, c.name AS candidate, p.name AS party
FROM Candidates c, Items i, IsMemberOf imo, Parties p, IsCandidateIn ici
WHERE imo.candidateID = c.id AND imo.partyID = p.id AND ici.candidateID = c.id AND ici.electionID = i.id AND i.isPartisan = TRUE
ORDER BY i.office;

# List all issues and their results, including the numbers of all cast and all
# yes votes.
SELECT i.question, i.yesCount, i.voteCount AS totalCount
FROM Items i
WHERE i.type = "issue";

# List all elections and their results, including the total number of votes cast
# and the number of votes cast for each candidate. (You do not have to list
# parties for each candidtate.)
SELECT i.office, c.name AS candidate, c.voteCount AS votesForCandidate, i.voteCount AS votesForOffice
FROM Candidates c, IsCandidateIn ici, Items i
WHERE ici.candidateID = c.id AND ici.electionID = i.id;
