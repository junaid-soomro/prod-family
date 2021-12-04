<?php
include("dbConnect.php");
$response=array();

$result = mysqli_query($conn,"select `Name`,Orders.order_id,Products.image,`prod_name`,`status`,`payment_type`,`price`,`address` from `users` INNER JOIN `Products` INNER JOIN `Orders` ON(Orders.FK_user_id=users.user_id) AND(Orders.FK_prod_id=Products.product_id)
    AND !(`status`='pending')");
while($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
    
  array_push($response,array("Order_ID"=>$row["order_id"],"Name"=>$row["prod_name"],"Price"=>$row["price"],"user"=>$row["Name"],
  "image"=>$row["image"],"payment_type"=>$row["payment_type"],
  "order_status"=>$row["status"]));

}

echo json_encode($response);
mysqli_close($conn);
exit();



?>