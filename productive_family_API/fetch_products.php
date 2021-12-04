<?php
include("dbConnect.php");
$response = array();
if(isset($_POST["category"])){   
    $category=$_POST["category"];

$result = mysqli_query($conn,"SELECT * FROM `Products` WHERE `prod_category` = '$category'");
while($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
    
  array_push($response,array("ID"=>$row["product_id"],"Name"=>$row["prod_name"],"Price"=>$row["rate"],"category"=>$row["prod_category"],
  "Image"=>$row["image"]));
}

}

else if (isset($_POST["query"])){
    
    $query=$_POST["query"];
    
    $result = mysqli_query($conn,"SELECT * FROM `Products` WHERE `prod_name` like '%$query%' ");
    $check = mysqli_affected_rows($conn);
    if($check){
    
        while($row = mysqli_fetch_array($result,MYSQLI_ASSOC)){
            
array_push($response,array("ID"=>$row["product_id"],"Name"=>$row["prod_name"],"Price"=>$row["rate"],"category"=>$row["prod_category"],
  "image"=>$row["image"]));

        }
    }
}

else{      //fetching all products to display in recycler view
    
    $result = mysqli_query($conn,"SELECT * FROM `Products`");
while($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
    
    
    
  array_push($response,array("ID"=>$row["product_id"],"Name"=>$row["prod_name"],"Price"=>$row["rate"],"category"=>$row["prod_category"],
  "image"=>$row["image"]));

}
    
    
}


echo json_encode($response);

mysqli_close($conn);
exit();

?>