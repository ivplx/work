<?php
  
  $value = $_GET['value'];

  $file = fopen("data.txt","a+"); //資料寫入data.txt中
  fwrite($file,$value.PHP_EOL);
  fclose($file);
?>