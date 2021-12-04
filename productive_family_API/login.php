<?php
$username = $_POST["username"];
$password = $_POST["password"];
include("dbConnect.php");
$response = array();
$response["success"] = false;
$response["status"]="INVALID";
$result = mysqli_query($conn, "SELECT * FROM `users` WHERE `Email` = '$username' AND `Password` = '$password'");
$affected = mysqli_affected_rows($conn);
if ($affected > 0) {
  #USER DETAILS MATCH
    $response["success"] = true;
    while ($row = mysqli_fetch_array($result, MYSQLI_ASSOC)) {
        $response["ID"]=$row['user_id'];
        $response["Name"]=$row['Name'];
        $response["Email"]=$row['Email'];
        $response["Type"]=$row['user_type'];
        $response["Status"]=$row['user_status'];
        $response["image"]=$row['image'];
    }
}
else{
  $userCheck = mysqli_query($conn, "SELECT * FROM `users` WHERE `Email` = '$username'");
  $userAffected = mysqli_affected_rows($conn);
  if($userAffected>0){
    $response["status"]="PASSWORD";
  }
}
echo json_encode($response);
mysqli_close($conn);
exit();
?>