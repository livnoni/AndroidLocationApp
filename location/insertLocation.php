<?php

if($_SERVER["REQUEST_METHOD"]=="POST")
{
	require 'connection.php';
	createLocation();
}
else
{
	echo "this page can be seen only with POST request. <br>
	 please use 'location app' for see the data. <br>
	 dont forget, yehuda livnoni is the king.";
}

function createLocation()
{
	global $connect;
	
	$latitude = $_POST["latitude"];
	$longitude = $_POST["longitude"];
	$altitude = $_POST["altitude"];
	$model = $_POST["model"];
	$date = $_POST["date"];

	
	$query = " Insert into gpsdata(latitude,longitude,altitude,model,date) values ('$latitude','$longitude','$altitude','$model','$date');";
	
	mysqli_query($connect, $query) or die (mysqli_error($connect));
	mysqli_close($connect);
}

?>