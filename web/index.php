<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>设备号</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <link href="signin.css" rel="stylesheet">

    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="js/html5shiv.js"></script>
    <![endif]-->
    <!-- Fav and touch icons -->

    <script type="text/javascript" src="js/jquery.min.js"></script>
    <script type="text/javascript" src="js/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/scripts.js"></script>
</head>
<body style="padding-top: 10px;">
<div class="container" style="width:350px">
    <form class="form-signin" action="../api/get/show.php?" method="get" id="form">
        <h2 class="form-signin-heading" style="text-align:center">输入设备号</h2>
        <input type="text" class="form-control" placeholder="设备号" name="seid" required autofocus>

        <p class="help-block">
            1. 设备号是你控制终端的唯一识别码；<br/>
            2. 只能查看控制终端的温湿度数据和控制状态。
        </p>
        <input class="btn btn-lg btn-primary btn-block" type="submit" name="show_se" id="show_se">
    </form>
</div>
</body>
</html>