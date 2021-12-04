<?php
include("dbConnect.php");
$response = array();
$sql="SELECT * FROM `Category`";
$result=mysqli_query($conn,$sql);

while($row=mysqli_fetch_array($result)){
array_push($response,array("Category"=>$row["category"]));
}

echo json_encode($response);
mysqli_close($conn);
exit();
?>