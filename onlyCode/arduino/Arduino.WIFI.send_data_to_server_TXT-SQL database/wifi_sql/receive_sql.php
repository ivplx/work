<?php
  
  $value = $_GET['value'];

  //user information
  $host = "Sever IP";
  $user = "使用者名稱";
  $pass = "使用者密碼";

  //database information
  $databaseName = "資料庫名稱";
  $tableName = "資料表名稱";

  //Connect to mysql database
  $con = mysql_connect($host,$user,$pass);
  $dbs = mysql_select_db($databaseName, $con);

  //Query database for data
  $result = mysql_query("insert into $tableName (data) VALUES ($value)");

  //store matrix
  if($result==1)
    echo "success";
  else
    echo "error";
?>