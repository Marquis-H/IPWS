<?php
//sae 数据库接口
$mysql = new SaeMysql();
$seid = $_REQUEST['seid'];
$hum = $_REQUEST['hum'];
$temp = $_REQUEST['temp'];
$wp = $_REQUEST['wp'];
$beam = $_REQUEST['beam'];
$time = $_REQUEST['time'];
$date = date("Y-m-d H:i:s", $time);
if ($wp == 0) {
    $wp = '关';
} else {
    $wp = '开';
}

$sql = "INSERT  INTO `getdata` ( `id`, `seid`, `hum`, `temp`, `wp`, `beam`, `time`) VALUES (null, '$seid' ,'$hum' ,'$temp' ,'$wp' ,'$beam' , '$date ' ) ";
$mysql->runSql($sql);
echo "数据上传成功！"; ?>

<?php
if ($mysql->errno() != 0) {
    die("Error:" . $mysql->errmsg());
}
?>