<html>
	<head>
		<title>Political Parties</title>
		<link rel="stylesheet" href="style.css" />
	</head>

	<body>
		<h1>Political Parties</h1>
		<#list parties as party>
 			<p>${party}
		</#list>

		<h3>Edit a Political Party</h3>

		<form method=post action="EditPoliticalParty">

		<p>Existing Party name: <input name="existingParty" type=text size="20">

		<p>New Party name: <input name="newParty" type=text size="20">

		<p><input type=submit> <input type=reset>

		</form>

		<h3>Delete a Political Party</h3>

		<form method=post action="DeleteParty">

		<p>Existing Party name: <input name="deletePartyName" type=text size="20">

		<p><input type=submit> <input type=reset>

		</form>

		<p>Create a new <a href="CreatePoliticalParty.html">party</a>

		<p><p>Return to the <a href="ShowMainWindow">main page</a>.
	</body>
</html>
