<?php
include("dbConnect.php");

$response["success"]=false;

$USER = $_POST["USER"];
$price = $_POST["price"];
$method = $_POST["method"];
$address = $_POST["address"];
$PROD = $_POST["PROD"];

$result = mysqli_query($conn,"INSERT INTO `Orders` (`FK_prod_id`,`FK_user_id`,
`payment_type`,`address`,`price`)VALUES('$PROD','$USER','$method','$address'
,'$price')");

$check = mysqli_affected_rows($conn);
if($check>0){
    
    $response["success"]=true;
    
}

$result = mysqli_query($conn,"");





echo json_encode($response);
mysqli_close($conn);
exit();


?>