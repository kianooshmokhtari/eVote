<html>
	<head>
		<title>Issues</title>
		<link rel="stylesheet" href="style.css" />
	</head>

	<body>
		<h3>Issues</h3>
		<#list issues as issue>
 			<p>${issue}
		</#list>
		
	
	<h3>Edit an Issue</h3>

		<form method=post action="EditIssue">

		<p>Existing issue ID: <input name="existingIssueID" type=text size="20">

		<p>New  question: <input name="newIssueQuestion" type=text size="20">
		
		<p>Ballot ID: <input name="newIssueBallotID" type=text size="20">

		<p><input type=submit> <input type=reset>

		</form>
	
	<h3>Delete an Issue</h3>
	
	<form method=post action="DeleteIssue">

		<p>Existing Issue ID: <input name="deleteIssueID" type=text size="20">

		<p><input type=submit> <input type=reset>

	</form>
	
	<p>Create <a href="CreateIssue.html">a new issue</a>
	
	<p><p>Return to the <a href="ShowMainWindow">main page</a>.
	</body>
</html>
