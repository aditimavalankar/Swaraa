<?php
$hostname_localhost ="localhost";
$database_localhost ="Uploads";
$username_localhost ="root";
$password_localhost ="mohit100";
$con = mysql_connect($hostname_localhost,$username_localhost,$password_localhost)
or
trigger_error(mysql_error(),E_USER_ERROR);

mysql_select_db($database_localhost, $con);

$username = $_POST['username'];
$password = $_POST['password'];

$query_search = "select * from Songs";
$result = mysql_query($query_search) or die(mysql_error());

$rarr=array();
$index=0;
while($row = mysql_fetch_assoc($result)) // loop to give you the data in an associative array so you can use it however.
{
         $rarr[$index] = $row;
	      $index++;
}
$var=array();
//$var["name"]="arnav";
$var["object"]=$rarr;
echo json_encode($var);
?>

