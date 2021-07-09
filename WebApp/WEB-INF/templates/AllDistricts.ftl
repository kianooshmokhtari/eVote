<html>
	<head>
		<title>Electoral Districts</title>
		<link rel="stylesheet" href="style.css" />
	</head>

	<body>
		<h1>Electoral Districts</h1>
		<#list districts as district>
 			<p>${district}
		</#list>
		
		<h3>Edit an Electoral District</h3>

		<form method=post action="EditElectoralDistrict">
		
		<p>Existing District name: <input name="existingDistrict" type=text size="20">
		
		<p>New District name: <input name="newDistrict" type=text size="20">
		
		<p><input type=submit> <input type=reset>
		
		</form>
		
		<h3>Delete an Electoral District</h3>

		<form method=post action="DeleteDistrict">
		
		<p>Existing District name: <input name="deleteDistrictName" type=text size="20">
		
		<p><input type=submit> <input type=reset>
		
		</form>
		
		<p>Create <a href="createElectoralDistrict.html">a new district</a>
		
		<p><p>Return to the <a href="ShowMainWindow">main page</a>.
	</body>
</html>
