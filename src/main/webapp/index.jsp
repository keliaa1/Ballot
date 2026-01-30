<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Vote Now</title>
</head>
<body>

<h2>Vote for a Candidate</h2>

<form action="vote" method="post">
    Candidate ID:
    <input type="number" name="candidateId" required>
    <br><br>
    <input type="submit" value="Vote">
</form>

<br>
<a href="results">View Results</a>

</body>
</html>
