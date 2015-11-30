<meta charset="utf-8">
<?php
//sae 数据库接口
$mysql = new SaeMysql();

if ($_GET[show_se]) {
    $seid = $_GET["seid"];
    $sql = "SELECT * FROM `getdata`   where seid='" . $seid . "'  order by  time  desc   LIMIT 1";
    $data = $mysql->getData($sql);
    if ($data == "") {
        echo "<script>alert('无数据！');</script>";
        echo '<meta http-equiv="refresh" content="0;URL=http://ipws.sinaapp.com/getdata/index.php" />';
        exit();
    }
    ?>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>控制终端</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="description" content="">
  <meta name="author" content="">
    <link href="../../web/css/bootstrap.min.css" rel="stylesheet">
    <link href="../css/style.css" rel="stylesheet">
    <link href="signin.css" rel="stylesheet">
  <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
  <!--[if lt IE 9]>
    <script src="js/html5shiv.js"></script>
  <![endif]-->
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/scripts.js"></script>
</head>
    <body style="padding-top: 10px;">
    <div class="container" style="width:350px" >
    <h2 class="form-signin-heading" style="text-align:center">控制终端数据</h2>
    <p class="help-block" >
    <p class="help-block" style="text-align:center">设备号：<?php echo $data[0]["seid"] ?>&nbsp;&nbsp;更新时间：<?php echo $data[0]["time"] ?></p>
    <p class="help-block" style="font-size: 20px">湿度：<?php echo sprintf("%.2f", (252 - $data[0]["hum"]) / 156 * 100) . " %" ?><br >温度：<?php echo $data[0]["temp"] ?><br >光照：<?php echo $data[0]["beam"] ?><br >水泵状态：<?php echo $data[0]["wp"] ?><br >
    <p><a href="#" style=" text-decoration:none;">  <button  class="btn btn-lg btn-primary btn-block"  type="button">更新</button></a></p>
    </div>
  </body>
</html>
<?php } else {
    echo "<script>alert('错误打开！');</script>";
    echo '<meta http-equiv="refresh" content="0;URL=http://ipws.sinaapp.com/getdata/index.php" />';
    exit();
}
?>