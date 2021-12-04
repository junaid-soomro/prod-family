<?php
$id = $_POST["ID"];
$name = $_POST["Name"];
$email = $_POST["Email"];
$password = $_POST["password"];
$new_password = $_POST["new_password"];
include("dbConnect.php");
$response["success"]=false;
$response["PASSWORD"]=false;

$getID=mysqli_query($conn,"SELECT `password` FROM `users` WHERE `user_id` = $id");

while($row = mysqli_fetch_array($getID,MYSQLI_ASSOC)){
    
    $real_pass=$row['password'];
        
}



if($real_pass==$password){
if(isset($_POST["image"])){
    
    $img =$_POST["image"];
    $result2 = mysqli_query($conn,"UPDATE `users` SET `Name` = '$name' ,`Email` = '$email',`password`='$new_password',`image`='$img' WHERE `user_id`='$id'");
    $response["success"]=true;
}else{
    $result2 = mysqli_query($conn,"UPDATE `users` SET `Name` = '$name' ,`Email` = '$email',`password`='$new_password' WHERE `user_id`='$id'");
    $response["success"]=true;
}
}
else{
    
    $response["PASSWORD"]=true;
    
}


echo json_encode($response);
mysqli_close($conn);
exit();
?>