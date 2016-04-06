<?php
$HOST = "";
$USER = "";
$PASS = "";
$DATABASE = "";
$table_US = "";
$table_val = "";
$table_emptyspace = "";
$con = mysqli_connect($HOST,$USER,$PASS,$DATABASE);

if (mysqli_connect_errno())
{
    echo "Failed to connect to MySQL: " . mysqli_connect_error();
    exit();
}
$sql_es = "SELECT `lat`, `lng`,`value` FROM ".$table_US." WHERE `value`>0";
$result = mysqli_query($con,$sql_es);

/**************************************************/
$i=0;
	while($row = mysqli_fetch_array($result)){
		$data[$i] = $row;
		$i++;
	}

/************************************************/
	echo json_encode($data);
	mysqli_close($con);
?>