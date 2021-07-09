<html>
	<head>
		<title>Elections</title>
		<link rel="stylesheet" href="style.css" />
	</head>

	<body>
		<h1>Elections</h1>
		<#list elections as election>
 			<p>${election}
		</#list>

		<h3>Edit an Election</h3>

		<form method=post action="EditElection">

		<p>Existing election office: <input name="existingElectionOffice" type=text size="20">

		<p>Ballot ID: <input name="newElectionBallotID" type=text size="20">

		<p>New  election office: <input name="newElectionOffice" type=text size="20">

		<p>Partisan: <input type="checkbox" name="isPartisan" value="yes">Yes
		<input type="checkbox" name="isPartisan" value="no">No

		<p><input type=submit> <input type=reset>

		</form>

		<h3>Delete an Election</h3>

		<form method=post action="DeleteElection">

		<p>Existing Election ID: <input name="deleteElectionID" type=text size="20">

		<p><input type=submit> <input type=reset>

		</form>

		<p>Create <a href="CreateElection.html">a new election</a>

		<p><p>Return to the <a href="ShowMainWindow">main page</a>.
	</body>
</html>
