<?php

define('hostname', 'localhost');
define('user', 'id277709_location');
define('password', 'gurc9999');
define('databaseName', 'id277709_location');

$link = mysqli_connect(hostname,user,password,databaseName);
if (!$link) {
    die('Could not connect to MySQL server: ' . mysql_error());
}
$dbname = 'id277709_location';
$db_selected = mysqli_select_db($link, $dbname);
if (!$db_selected) {
    die("Could not set $dbname: " . mysql_error());
}
$res = mysqli_query($link, 'select * from gpsdata');

while($row = mysqli_fetch_row($res))
{
	echo $row[0] , ',';
	echo $row[1], ',';
	echo $row[2], ',';
	echo $row[3], ',';
	echo $row[4], ',';
	echo $row[5] . "<br>" . PHP_EOL;
}



?>