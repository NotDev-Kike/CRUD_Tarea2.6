<?php
$usuario = "enrique";
$realm = "API CRUD";
$passwordPlano = "1234";

$ha1 = md5("$usuario:$realm:$passwordPlano");
echo "HA1: $ha1\n";
