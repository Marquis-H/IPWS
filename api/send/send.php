<?php
//sae 数据库接口
$mysql = new SaeMysql();
//获取设备号和slug
$seid = $_REQUEST['seid'];
$setdata = $_REQUEST['set'];
//查询此设备在数据库时间最近的数据
$sql = "SELECT * FROM `senddata`   where seid='" . $seid . "'  order by  time  desc   LIMIT 1";
$data = $mysql->getData($sql);
//当slug为1，作为app的数据接口，显示在set里
if ($setdata == "1") {
    echo "seid:" . $data[0]["seid"] . "<br >open_hum:" . sprintf("%.0f", (252 - $data[0]["open_hum"]) / 156 * 100) . "<br >end_hum:" . sprintf("%.0f", (252 - $data[0]["end_hum"]) / 156 * 100) . "<br >open_time:" . $data[0]["open_time"] . "<br >end_time:" . $data[0]["end_time"] . "<br >pri:" . $data[0]["pri"] . "<br >uptime:" . $data[0]["time"] . "<br >";
} else if ($data == "") {
    echo "无数据！";
} else {
    //作为开发板的数据接口，作为设置参数
    echo "seid:" . $data[0]["seid"] . "<br >open_hum:" . $data[0]["open_hum"] . "<br >end_hum:" . $data[0]["end_hum"] . "<br >open_time:" . strtotime($data[0]["open_time"]) . "<br >end_time:" . strtotime($data[0]["end_time"]) . "<br >pri:" . $data[0]["pri"] . "<br >time:";
}