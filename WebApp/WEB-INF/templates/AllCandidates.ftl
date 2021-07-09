<html>
	<head>
		<title>Candidates</title>
		<link rel="stylesheet" href="style.css" />
	</head>

	<body>
		<h1>Candidates</h1>
		<#list candidates as candidate>
 			<p>${candidate}
		</#list>
		
		<h3>Edit a Candidate</h3>

		<form method=post action="EditCandidate">
		
		<p>Existing Candidate name: <input name="existingCandidateName" type=text size="20">
		
		<p>Name change: <input name="newCandidateName" type=text size="20">
		
		<p>Party change: <input name="newCandidateParty" type=text size="20">
		
		<p>Office change: <input name="newCandidateOffice" type=text size="20">
		
		<p><input type=submit> <input type=reset>
		
		</form>
		
		<h3>Delete a Candidate</h3>

		<form method=post action="DeleteCandidate">
		
		<p>Existing Candidate name: <input name="deleteCandidateName" type=text size="20">
		
		<p><input type=submit> <input type=reset>
		
		</form>
		
		<p>Create <a href="CreateCandidate.html">a new candidate</a>
		
		<p><p>Return to the <a href="ShowMainWindow">main page</a>.
	</body>
</html>
