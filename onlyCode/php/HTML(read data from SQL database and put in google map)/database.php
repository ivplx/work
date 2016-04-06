<?php 
		$ADDRESS = "000.000.000.000";
		$USER = "12313";
		$PASSWORD = "000000";
		$DATABASE = "lightdb";
	//1.connect to database
		$con = mysqli_connect($ADDRESS,$USER,$PASSWORD,$DATABASE);
		// Check connection
		if (mysqli_connect_errno())
	  	{
	  		echo "Failed to connect to MySQL: " . mysqli_connect_error();
	  	}
	  	mysqli_set_charset($con,"utf8");
	//2.request data(SELECT * FROM table)
		$result = mysqli_query($con,"SELECT DISTINCT location FROM light3");
	//3.put data into array(my_sqli_fetch)
		$out = [];
		$i=0;
		
		while ($row = mysqli_fetch_array($result, MYSQLI_BOTH)) {
			$out[$i] = $row;
			$i++;
		}
	//4.convert into json file format
		echo json_encode($out);
	//5.close database connection
		mysqli_free_result($result);
		mysqli_close($con);
?>