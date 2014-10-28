<?php 
$hostname_localhost ="localhost";
$database_localhost ="Uploads";
$username_localhost ="root";
$password_localhost ="mohit100";
$con = mysql_connect($hostname_localhost,$username_localhost,$password_localhost)
or
trigger_error(mysql_error(),E_USER_ERROR);

mysql_select_db($database_localhost, $con);

$title = $_POST['Title'];
$url = $_POST['fileName'];

$query = "insert into Songs (title, likes, dislikes, views, uri) values ('.$title.', '0', '0', '0', '.$url.')";
$result = mysql_query($query) or die(mysql_error());

?>
