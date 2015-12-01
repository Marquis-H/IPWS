<?php
//sae 数据库接口
$mysql = new SaeMysql();
//获取到post来的数据
$seid = $_REQUEST['seid'];
$open_hum = $_REQUEST['open_hum'];
$end_hum = $_REQUEST['end_hum'];
$open_time = $_REQUEST['open_time'];
$end_time = $_REQUEST['end_time'];
$pri = $_REQUEST['pri'];
$time = $_REQUEST['time'];
//validation
if ($seid == null || $pri == "1") {
    echo "数据设置失败！";
} else if ($pri == "0" && ($open_hum == null || $end_hum == null)) {
    echo "请设置湿度区间！";
} else if ($pri == "2" && ($open_time == null || $end_time == null)) {
    echo "请设置时间区间！";
} else if ($open_hum >= $end_hum || $open_time >= $end_time) {
    echo "数据区间设置错误！";
} else if (($open_hum > 100 || $open_hum < 0) || ($end_hum > 100 || $end_hum < 0) || preg_match('/^[0-9]*$/', $open_hum) == false || preg_match('/^[0-9]*$/', $end_hum) == false) {
    echo "数据区间设置无效！";
} else {
    //换算为0-255
    $open_hum = 252 - ($open_hum / 100 * 156);
    $end_hum = 252 - ($end_hum / 100 * 156);
    //写入数据库
    $sql = "INSERT  INTO `senddata` ( `id`, `seid`, `open_hum`, `end_hum`, `open_time`, `end_time`, `pri`, `time`) VALUES (null, '$seid' ,'$open_hum' ,'$end_hum' ,'$open_time' ,'$end_time' , '$pri ' , '$time') ";
    $mysql->runSql($sql);
    if ($mysql->errno() != 0) {
        die("Error:" . $mysql->errmsg());
    } else {
        echo "数据设置成功！";
    }
}