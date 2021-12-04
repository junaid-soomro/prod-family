<?php
include("dbConnect.php");
$response = array();
$sql="SELECT * FROM `users`";
$result=mysqli_query($conn,$sql);

while($row=mysqli_fetch_array($result)){
array_push($response,array("Name"=>$row["Name"],"Email"=>$row["Email"],"TYPE"=>$row["user_type"],"Status"=>$row["user_status"]));
}

echo json_encode($response);
mysqli_close($conn);
exit();
?>