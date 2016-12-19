<?php

#return arr of jsoon that present the DB.

if($_SERVER["REQUEST_METHOD"]=="POST")
{
	include 'connection.php';
	showLocation();
}
else
{
	echo "this page can be seen only with POST request. <br>
	 please use 'location app' for see the data. <br>
	 dont forget, yehuda livnoni is the king.";
}

function showLocation()
{
	global $connect;
	
	$query = " Select * FROM gpsdata; ";
	
	$result = mysqli_query($connect, $query);
	$number_of_rows = mysqli_num_rows($result);
	
	$temp_array = array();
	if($number_of_rows > 0)
	{
		while ($row = mysqli_fetch_assoc($result))
		{
			$temp_array[] = $row;
		}
	}
	
	header ('Content-Type: application/jjson');
	echo json_encode(array("Locations"=>$temp_array));
	mysqli_close($connect);
}

?>