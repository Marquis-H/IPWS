<?php
//sae 数据库接口
$mysql = new SaeMysql();

$seid = $_REQUEST['seid'];
$setdata = $_REQUEST['set'];

$sql = "SELECT * FROM `senddata`   where seid='" . $seid . "'  order by  time  desc   LIMIT 1";
$data = $mysql->getData($sql);

if ($setdata == "1") {
    echo "seid:" . $data[0]["seid"] . "<br >open_hum:" . sprintf("%.0f", (252 - $data[0]["open_hum"]) / 156 * 100) . "<br >end_hum:" . sprintf("%.0f", (252 - $data[0]["end_hum"]) / 156 * 100) . "<br >open_time:" . $data[0]["open_time"] . "<br >end_time:" . $data[0]["end_time"] . "<br >pri:" . $data[0]["pri"] . "<br >uptime:" . $data[0]["time"] . "<br >";
} else if ($data == "") {
    echo "无数据！";
} else {
    echo "seid:" . $data[0]["seid"] . "<br >open_hum:" . $data[0]["open_hum"] . "<br >end_hum:" . $data[0]["end_hum"] . "<br >open_time:" . strtotime($data[0]["open_time"]) . "<br >end_time:" . strtotime($data[0]["end_time"]) . "<br >pri:" . $data[0]["pri"] . "<br >time:";

}