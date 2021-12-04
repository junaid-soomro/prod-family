<?php

include("dbConnect.php");

$name=$_POST["name"];
$category=$_POST["category"];
$rate = $_POST["rate"];
$image = $_POST["image"];

$response["success"]="lol";
$response["status"]=false;

if(isset($_POST["id"])){
    
    $id=$_POST["id"];
        
    $result = mysqli_query($conn,"UPDATE `Products` SET `prod_name`='$name',`prod_category`='$category',`rate`='$rate',`image`='$image' WHERE `product_id`=$id");
    
        
    
    $response["status"]=true;
    
    
}
else if(isset($_POST["delete"])){
    $ID=$_POST["delete"];
    
    $result = mysqli_query($conn,"DELETE FROM `Products` WHERE `product_id`=$ID");
    $response["status"]=true;
    
    
}
else{
    
    $result=mysqli_query($conn,"INSERT INTO `Products` (`prod_name`,`prod_category`,`rate`,`image`)VALUES('$name','$category','$rate','$image')");
    
    
    $response["status"]=true;    
    

}

echo json_encode($response);
mysqli_close($conn);
exit();


?>