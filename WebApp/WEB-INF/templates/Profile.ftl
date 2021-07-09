<html>
	<head>
		<title>eVote MyProfile</title>
        <link rel="stylesheet" href="style.css" />
	</head>

	<body>
		<h3>My Profile</h3>
        <h4>UserName: ${userName}</h4>
        <h4>First Name: ${firstName} <a href="EditVoterFirstName.html">Edit</a><h4>
        <h4>Last  Name: ${lastName} <a href= "EditVoterLastName.html">Edit</a><h4>
        <h4>age: ${age} <a href="EditVoterAge.html">Edit</a><h4>
        <h4>Address: ${address} <a href="EditVoterAddress.html">Edit</a><h4>
        <h4>Email: ${email} <a href="EditVoterEmail.html">Edit</a><h4>
        <h4>Electoral District ${district} <a href="EditVoterDistrict.html">Edit</a><h4>
        <h4><a href="EditVoterPassword.html">Change Password</a><h4>
    <h4><a href="DeleteVoterAccount">Delete Account</a><Strong>Warning:</Strong> you cannot log back in after clicking Delete Account.</h4>
        <h4>Back to the <a href="ShowMainWindow"> Main Screen </a><h4>
	</body>
</html>
