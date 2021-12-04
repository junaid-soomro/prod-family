<?php
$password = $_POST["password"];
$email = $_POST["email"];
$name = $_POST["name"];
$type = $_POST["user_type"];
$image = $_POST["image"];

//$filePath = "user_images/$email.png";
//$url = "http://www.meezotech.com/junaid/user_images/$email.png";

include("dbConnect.php");
$response=array();
$response["success"]=false;
$check=mysqli_query($conn,"SELECT * FROM `users` WHERE `Email`='$email'");
$affected=mysqli_affected_rows($conn);
if($affected>0){
  $response["status"]="USERNAME";
}
else{
  
  $result=mysqli_query($conn,"INSERT INTO `users` (`Name`, `Password`, `Email`, `user_type`,`image`) VALUES ('$name', '$password', '$email', '$type','$image')");
  
  $response["success"]=true;
}
echo json_encode($response);#encoding RESPONSE into a JSON and returning.
mysqli_close($conn);
exit();
?>