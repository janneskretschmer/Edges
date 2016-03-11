<?php
$pdo=new PDO('mysql:dbname=janneskretschmer;host=localhost;charset=utf8','janneskretschmer','[password]');

if(isset($_GET['new_game'])){
    $sth = $pdo->prepare("INSERT INTO game_games(level,open) VALUES(:level,1)");
    $sth->bindParam(':level',$_GET['level']);
    $sth->execute();
    echo $pdo->lastInsertId();
}else
if(isset($_GET['list_games'])){
    $sth = $pdo->prepare("SELECT id,level FROM game_games WHERE open");
    $sth->execute();
    $temp = $sth->fetchAll(PDO::FETCH_ASSOC);
    $games = array();
    foreach($temp as $game){
        $games[$game['id']]=$game['level'];
    }
    echo json_encode($games);
}else
if(isset($_GET['is_game_open'])){
    $sth = $pdo->prepare("SELECT open FROM game_games WHERE id=:id");
    $sth->bindParam(':id',$_GET['is_game_open']);
    $sth->execute();
    echo $sth->fetch(PDO::FETCH_ASSOC)['open'];
}else
if(isset($_GET['join_game'])){
    $sth = $pdo->prepare("UPDATE game_games SET open=0 WHERE id=:id");
    $sth->bindParam(':id',$_GET['join_game']);
    $sth->execute();
}else
if(isset($_GET['move'])){
    $sth = $pdo->prepare("INSERT INTO game_moves(game,x,y,n) VALUES(:game,:x,:y,:n)
                          ON DUPLICATE KEY UPDATE x=:x,y=:y,n=:n");
    $sth->bindParam(':game',$_GET['game']);
    $sth->bindParam(':x',$_GET['x']);
    $sth->bindParam(':y',$_GET['y']);
    $sth->bindParam(':n',$_GET['n']);
    $sth->execute();
    echo 'ok';
}else
if(isset($_GET['is_move_allowed'])){
    $sth = $pdo->prepare("SELECT IF(n > :n,CONCAT(x,',',y),0) AS allowed FROM game_moves WHERE game=:game");
    $sth->bindParam(':n',$_GET['n']);
    $sth->bindParam(':game',$_GET['game']);
    $sth->execute();
    $res = $sth->fetch(PDO::FETCH_ASSOC);
    echo $res?$res['allowed']:'0';
}
?>