<?php
  require "./config/config.php";
  //IP、帳號資訊
  // $host = "127.0.0.1";
  // $user = "root";
  // $pass = "";
  //資料庫資訊
  // $databaseName = "lightdb";
  // $tableName = "light";
  //連線資料庫伺服器
  $con = mysql_connect($host,$user,$pass);
  //連結資料庫
  $dbs = mysql_select_db($databaseName, $con);
  //Query database for data
  $sq1 = "SELECT * FROM $tableName";
  $result = mysql_query($sq1);
  //取出資料
  $data=array();
  while ($row = mysql_fetch_array($result)){
    array_push($data, $row);
  }
  //輸出並且轉成json格式
  echo json_encode($data);
?>