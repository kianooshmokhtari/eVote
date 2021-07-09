<html>
	<head>
		<title>Ballots</title>
		<link rel="stylesheet" href="style.css" />
	</head>

	<body>
		<h1>Ballots</h1>
		<#list ballots as ballot>
 			<p>${ballot}
		</#list>

		<h3>Edit a Ballot</h3>

		<form method=post action="EditBallot">

		<p>Existing Ballot ID: <input name="existingBallotID" type=text size="20">

		<p>New Ballot ID: <input name="newBallotID" type=text size="20">

		<p>New Open Date: <input name="newBallotOpenDate" type=text size="20">

		<p>New Close Date: <input name="newBallotCloseDate" type=text size="20">

		<p><input type=submit> <input type=reset>

		</form>

		<h3>Delete a Ballot</h3>

		<form method=post action="DeleteBallot">

		<p>Existing Ballot ID: <input name="deleteBallot" type=text size="20">

		<p><input type=submit> <input type=reset>

		</form>

		<p>Create a new <a href="CreateBallot.html">ballot</a>

		<p><p>Return to the <a href="ShowMainWindow">main page</a>.
	</body>
</html>
