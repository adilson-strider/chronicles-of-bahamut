package battle;

import entity.Enemy;
import entity.Entity;
import entity.Player;
import gamestate.GameStates;
import gamestate.StateMachine;
import gamestate.states.battle.Action;
import gamestate.states.battle.VictoryState;
import graphics.Animation;
import graphics.SpriteSheet;
import main.GameManager;
import main.InputManager;
import object.Object;
import object.consumables.OBJ_Health_Potion;
import tiledmap.TiledMap;
import tools.DebugMode;
import ui.UI;
import tools.GameNotification;
import tools.ResourceLoader;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;

import static main.AssetSetter.*;

public class BattleManager {

    public static ArrayList<Action> playerActions;
    public static ArrayList<Action> enemyActions;

    public BufferedImage battleBG;
    public int menuItems = 3;

    public static boolean playerTurn;
    public static boolean enemyTurn;
    public static boolean isBattleOver;
    public int turnDelayCounter;
    public final int TURN_DELAY = 150;

    public Random random;

    // Variáveis para o efeito de piscar
    private static final int BLINK_DURATION = 30; // duração total do piscar (frames)
    private static final int BLINK_INTERVAL = 5; // intervalo de tempo entre os piscas (frames)
    private static int enemyAttackTurnIndex = 0; // Índice do inimigo que está atacando
    private static final int ATTACK_COOLDOWN = 200; // Cooldown de ataque para inimigos

    public static ArrayList<Enemy> enemies;

    // Variáveis para armazenar os turnos dos jogadores
    public static int currentPlayerTurn = 0;



    private static int playerAttackDelay = 100;

    private int playerIndex;


    public int enemyAttackDelay = 500;
    public static int playerTurnCoolDown = 0;
    public static boolean showBattleOptions = false;
    Point playerPos;
    public Enemy enemy;
    public static boolean isEnemyAttack = false;
    public static boolean isPlayerAttack = false;
    public static int coolDownEndBattle = 100;
    public static ArrayList<DamageText> damageList;
    public static BattleManager Instance;

    public static int goldAmount, expAmount;

    public static Point enemyPos;

    public static boolean isBoss;
    private boolean attackPlayerPhase;

    private int coodownBarWidth;



    // ===================== SETUP ==========================
    public BattleManager(boolean isBoss, int bossIndex){
        battleSetup(isBoss, bossIndex);
    }

    private void battleSetup(boolean isBoss, int bossIndex) {

        switch(TiledMap.mapName){
            case "overworld":
                battleBG = ResourceLoader.loadImage("/images/backgrounds/grass.jpg");
                break;
            case "dungeon4":
                battleBG = ResourceLoader.loadImage("/images/backgrounds/Cave1.png");
                break;
        }

        playerActions = new ArrayList<>();
        enemyActions = new ArrayList<>();

        this.playerTurn = false; // Começa com o turno do jogador
        this.isBattleOver = false;
        this.turnDelayCounter = TURN_DELAY;

        BattleManager.isBoss = isBoss;

        coodownBarWidth = 200;

        Random rand = new Random();
        enemies = new ArrayList<>();

        if(!BattleManager.isBoss){
            //int j = rand.nextInt(1, 101);
            int j = 75;

            if(j <= 75){
                enemies.add(GameManager.currentTiledMap.enemies.get(rand.nextInt(0, 3)));
                enemies.get(0).setPoint((GameManager.screenWidth/2)-(GameManager.tileSize*7),
                        (GameManager.screenHeight/2)-(GameManager.tileSize*2));
                enemyPos = enemies.get(0).battlePos;
                GameManager.notifications = new GameNotification("Um " + enemies.get(0).name + " apareceu!", 60);
            }
            else if(j <= 90){
                enemies.add(GameManager.currentTiledMap.enemies.get(0));
                enemyPos = enemies.get(0).battlePos;
                enemies.add(GameManager.currentTiledMap.enemies.get(1));
                enemyPos = enemies.get(1).battlePos;
                GameManager.notifications = new GameNotification("Luta contra dois inimigos!", 60);
            }
            else if(j <= 100){
                enemies.add(GameManager.currentTiledMap.enemies.get(0));
                enemyPos = enemies.get(0).battlePos;
                enemies.add(GameManager.currentTiledMap.enemies.get(1));
                enemyPos = enemies.get(1).battlePos;
                enemies.add(GameManager.currentTiledMap.enemies.get(2));
                enemyPos = enemies.get(2).battlePos;
                GameManager.notifications = new GameNotification("Um grupo de inimigos apareceu!", 60);
            }
        }
        else{
            enemies.add(GameManager.currentTiledMap.boss.get(bossIndex));
            enemies.get(bossIndex).setPoint((GameManager.screenWidth/2)-(GameManager.tileSize*7),
                    (GameManager.screenHeight/2)-(GameManager.tileSize*2));
            enemyPos = enemies.get(bossIndex).battlePos;
            GameManager.notifications = new GameNotification("Um " + enemies.get(bossIndex).name + " apareceu!", 60);
        }

        enemyTurn = true;
        UI.getInstance().battleIndex = 0;
        damageList = new ArrayList<>();
        this.random = new Random();

        // Inicializa expAmount e goldAmount
        expAmount = 0;
        goldAmount = 0;
        attackPlayerPhase = false;
    }

    // ===================== SETUP ==========================






    // ===================== UPDATE ==========================

    public void update(){
        executeTurn();
        updateNotifications();
        updateAnimations();
        updateBattle();
    }


    private void updateBattle(){

        // Antes do loop for dos jogadores, defina uma variável para armazenar o jogador atual
        /*for(Player p : GameManager.players){

            p.attackCooldown--;

            if(p.turnOn){
                showBattleOptions = true;
            }
        }*/

        if (showBattleOptions) {
            if (InputManager.getInstance().isKeyDown(KeyEvent.VK_ENTER)) {
                switch (UI.getInstance().battleIndex) {
                    case 0:
                        StateMachine.getInstance().change("EnemySelect");
                        break;
                    case 1:
                        // Adicionar lógica para magia
                        break;
                    case 2:
                        // Adicionar lógica para item
                        StateMachine.getInstance().change("BattleItem");
                        break;
                    case 3:
                        playerActions.add(new Action(Action.ActionType.ESCAPE, null, null));
                        break;
                }

                showBattleOptions = false;
            }

            updateIndex();
        }
    }

    private void updateNotifications(){
        GameManager.notifications.update();
    }

    private void updateAnimations() {

        if (currentPlayerTurn >= GameManager.players.size()) {
            currentPlayerTurn = 0;
        }

        GameManager.getPlayerAtIndex(currentPlayerTurn).playerAttackAnimation.update();

        if(GameManager.getPlayerAtIndex(currentPlayerTurn).currentWeapon != null){
            GameManager.getPlayerAtIndex(currentPlayerTurn).currentWeapon.weaponAnimation.update();
        }

        for (Enemy e : enemies) {
            if (e != null) {
                e.enemyAttackAnimation.update();
            }
        }

        // Atualizar lógica de piscar e ataque para cada inimigo
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            if (e.isBlinking) {
                e.blinkCounter++;
                if (e.blinkCounter >= BLINK_DURATION) {
                    e.isBlinking = false;
                    e.blinkCounter = 0;
                    // Executar ataque após piscar

                    if(e.hp > 0){
                        performEnemyAttack(e, GameManager.getPlayers());
                    }
                }
            }
        }
    }

    private void updateIndex(){

        if(!UI.getInstance().isBattleItem){
            if(InputManager.getInstance().isKeyDown(KeyEvent.VK_DOWN)){
                UI.getInstance().battleIndex++;
                GameManager.playSFX(0);

                if(UI.getInstance().battleIndex > menuItems){
                    UI.getInstance().battleIndex = 0;
                }
            }

            if(InputManager.getInstance().isKeyDown(KeyEvent.VK_UP)){
                UI.getInstance().battleIndex--;
                GameManager.playSFX(0);

                if(UI.getInstance().battleIndex < 0){
                    UI.getInstance().battleIndex = menuItems;
                }
            }
        }
    }

    // ===================== UPDATE ==========================






    // ===================== DRAW ============================

    public void draw(Graphics2D g2) {
        g2.drawImage(battleBG, 0, 0, GameManager.screenWidth, GameManager.screenHeight, null);

        drawEnemyAnimations(g2);
        drawBattlePanel(g2);

        for (int i = 0; i < GameManager.players.size(); i++) {
            drawPlayer(g2, GameManager.players.get(i).battlePos.x,
                    GameManager.players.get(i).battlePos.y,
                    GameManager.players.get(i));
        }

        drawPlayerAttackAnimations(g2, GameManager.players);


        drawEnemyAttackAnimations(g2, GameManager.players.get(playerIndex).battlePos.x,
                GameManager.players.get(playerIndex).battlePos.y);


        for (DamageText d : damageList) {
            g2.setColor(Color.WHITE);
            d.drawDamageText(g2);
        }


            g2.setColor(Color.WHITE);
            GameManager.notifications.drawNotification(g2, 0, 0);

        if (!damageList.isEmpty()) {
            if (!damageList.get(0).textCounter()) {
                damageList.clear();
            }
        }
    }

    private void drawEnemyAnimations(Graphics2D g2) {
        for (Enemy e : enemies) {
            if (e.hp > 0) {
                if (!e.isBlinking || (e.blinkCounter / BLINK_INTERVAL) % 2 == 0) {
                    g2.drawImage(
                            e.getBattleImage(),
                            e.battlePos.x,
                            e.battlePos.y,
                            e.getBattleImage().getWidth(),
                            e.getBattleImage().getHeight(), null);
                }
            }
        }
    }

    private void drawEnemyAttackAnimations(Graphics2D g2, int x, int y){

        for (Enemy e : enemies) {
            if (e != null) {
                if(e.enemyAttackAnimation.getCurrentFrame() != 0){
                    g2.drawImage(e.enemyAttackAnimation.getSprite(), x - GameManager.tileSize, y - GameManager.tileSize, 192, 192, null);
                }

                if(e.enemyAttackAnimation.getCurrentFrame() >= e.enemyAttackAnimation.getTotalFrames() - 1){
                    e.enemyAttackAnimation.reset();
                }
            }
        }
    }

    private void drawPlayerAttackAnimations(Graphics2D g2, ArrayList<Player> players) {
        if (currentPlayerTurn >= players.size()) {
            currentPlayerTurn = 0;
        }

        Player currentPlayer = GameManager.getPlayerAtIndex(currentPlayerTurn);

        if(currentPlayer.currentWeapon != null){
            // Desenhar a animação da arma, se existir
            if (currentPlayer.currentWeapon.weaponAnimation.getCurrentFrame() != 0) {
                g2.drawImage(currentPlayer.currentWeapon.weaponAnimation.getSprite(),
                        enemyPos.x - 18,
                        enemyPos.y - 60, 192, 192, null);
            }
        }

        // Desenhar a animação de ataque do jogador
        if (currentPlayer.playerAttackAnimation.getCurrentFrame() != 0) {
            currentPlayer.isAttackAnim = true;
            g2.drawImage(currentPlayer.playerAttackAnimation.getSprite(),
                    currentPlayer.battlePos.x - 58,
                    currentPlayer.battlePos.y - 54,
                    currentPlayer.playerAttackAnimation.getSprite().getWidth() - 34,
                    currentPlayer.playerAttackAnimation.getSprite().getHeight() - 34, null);
        }

        boolean weaponAnimationFinished = false;

        // Verificar se a animação da arma terminou
        if(currentPlayer.currentWeapon != null){
            weaponAnimationFinished = currentPlayer.currentWeapon.weaponAnimation.isFinished();
        }

        // Verificar se a animação de ataque do jogador terminou
        boolean playerAnimationFinished = currentPlayer.playerAttackAnimation.isFinished();

        if(weaponAnimationFinished){
            // Resetar as animações
            if (currentPlayer.currentWeapon != null) {
                currentPlayerTurn++;
                currentPlayer.currentWeapon.weaponAnimation.reset();
            }
        }

        // Atualizar turno se as animações terminaram
        if (playerAnimationFinished) {

            currentPlayer.playerAttackAnimation.reset();
            currentPlayer.isAttackAnim = false;

            // Atualizar o turno
            if(currentPlayer.currentWeapon == null){
                currentPlayerTurn++;
            }

            if (currentPlayerTurn >= players.size()) {
                currentPlayerTurn = 0;
                playerTurn = false;
                enemyTurn = true;
            }
            isPlayerAttack = false; // Reseta a flag de ataque do jogador
        }

        // Resetar animações de ataque dos jogadores que não estão no turno atual
        for (Player player : players) {
            if (player != currentPlayer && player.playerAttackAnimation.getCurrentFrame() >= player.playerAttackAnimation.getTotalFrames() - 1) {
                player.playerAttackAnimation.reset();
                player.isAttackAnim = false;
            }
        }
    }

    private void drawPlayer(Graphics2D g2, int x, int y, Player player) {
        if (player.hp <= 0) {
            g2.drawImage(player.deadPlayerBattleImage, x, y, player.deadPlayerBattleImage.getWidth() - 32, player.deadPlayerBattleImage.getHeight() - 32, null);
        } else {
            if (!player.isAttackAnim) {
                g2.drawImage(player.playerBattleImage, x, y, player.playerBattleImage.getWidth() - 32, player.playerBattleImage.getHeight() - 32, null);
            } else {
                // Desenhar a animação de ataque
                g2.drawImage(player.playerAttackAnimation.getSprite(),
                        player.battlePos.x - 58,
                        player.battlePos.y - 54,
                        player.playerAttackAnimation.getSprite().getWidth() - 34,
                        player.playerAttackAnimation.getSprite().getHeight() - 34, null);
            }
        }
    }



    private void drawBattlePanel(Graphics2D g2){
        UI.getInstance().drawBattlePanel(g2);

        if(showBattleOptions){
            UI.getInstance().drawBattlePanelOptions(g2);
        }
    }

    // ===================== DRAW ============================





    // ===================== ATTACK =========================

    private static void performPlayerAttack(Player player) {

        int damage = player.atk;
        enemyPos = enemies.get(UI.getInstance().monsterIndex).battlePos;

        playerTurnCoolDown = 200;
        enemies.get(UI.getInstance().monsterIndex).hp -= damage;
        damageList.add(new DamageText(new Point(enemyPos.x, enemyPos.y), damage, Color.WHITE));

        GameManager.playSFX(11);
        isPlayerAttack = false;
        playerAttackDelay = 100;
    }

    private void performEnemyAttack(Enemy e, ArrayList<Player> players) {

        int damage = e.atk;

        playerTurn = false;
        playerIndex = random.nextInt(players.size());

        Player targetPlayer = players.get(playerIndex);
        targetPlayer.hp -= damage;
        damageList.add(new DamageText(new Point(targetPlayer.battlePos.x, targetPlayer.battlePos.y), damage, Color.WHITE));

        e.enemyAttackAnimation.start();

        GameManager.playSFX(12);
        // Inimigo deve piscar novamente após o ataque para indicar o próximo ataque
        e.isBlinking = false;
        e.blinkCounter = 0;
        e.cooldown = ATTACK_COOLDOWN; // Aplicar cooldown após o ataque

        // Incrementar o índice do turno do ataque do inimigo
        enemyAttackTurnIndex++;
        if (enemyAttackTurnIndex >= enemies.size()) {
            enemyAttackTurnIndex = 0; // Resetar para o primeiro inimigo
        }
    }

    // ===================== ATTACK =========================






    // ===================== ESCAPE =========================

    private void battleEscape(){
        int i = random.nextInt(101);

        if (i <= 25){
            escape();
        }
        else {
            cantEscape();
        }
        GameManager.playSFX(2);
    }

    private void escape(){
        GameManager.gameStates = GameStates.RUN;
        StateMachine.getInstance().change("Transition");
        GameManager.currentTiledMap.battleCoolDown = 200;
        // gp.tiledMap.enemyFound =  null;
    }

    private void cantEscape(){
        UI.getInstance().battleIndex = 0;
        enemyTurn = true;
        isEnemyAttack = true;
        playerTurn = false;
        showBattleOptions = false;
        playerTurnCoolDown = 300;
    }

    // ===================== ESCAPE =========================





    // ================= MAIN LOOP BATTLE ===================

    private void selectEnemyActions() {
        for (Enemy enemy : enemies) {
            Player targetPlayer = GameManager.players.get(random.nextInt(GameManager.players.size()));
            enemyActions.add(new Action(Action.ActionType.ATTACK, enemy, targetPlayer));
        }
        enemyTurn = false;
    }

    public static void executeTurn() {
        if (!isBattleOver()) {
            playerTurnCoolDown--;

            String stateName = StateMachine.getInstance().getCurrentState().getName();

            if (playerTurnCoolDown <= 0 && Objects.equals(stateName, "Battle")) {
                playerTurn = true;
                enemyTurn = true;
                showBattleOptions = true;
                playerTurnCoolDown = 100;
            }

            if (playerTurn) {

                // Ações do turno do jogador aqui
                if (isPlayerAttack) {
                    playerAttackDelay--;
                    if (playerAttackDelay <= 0) {
                        // Lógica de ataque do jogador
                        Action playerAction = playerActions.removeFirst();
                        if (playerAction.type == Action.ActionType.ATTACK) {
                            performPlayerAttack((Player) playerAction.source);

                            GameManager.getPlayerAtIndex(currentPlayerTurn).playerAttackAnimation.start();

                            if(GameManager.getPlayerAtIndex(currentPlayerTurn).currentWeapon != null){
                                GameManager.getPlayerAtIndex(currentPlayerTurn).currentWeapon.weaponAnimation.start();
                            }

                            enemyTurn = false;
                        }
                        /*else if(playerAction.type == Action.ActionType.ITEM){
                            useItem((Object) playerAction.source, (int) playerAction.target);
                        }*/

                        //enemies.removeIf(e -> e.hp <= 0);

                    }
                    showBattleOptions = false;
                }
            }

            if (enemyTurn) {
                // Atualizar o cooldown de ataque de cada inimigo
                for (Enemy e : enemies) {
                    if (e.cooldown > 0) {
                        e.cooldown--;
                        //System.out.println(e.hp);
                    }
                }

                // Só processa o ataque do inimigo se ele estiver dentro dos limites do array
                if (enemyAttackTurnIndex < enemies.size()) {
                    Enemy currentEnemy = enemies.get(enemyAttackTurnIndex);
                    if (currentEnemy.cooldown <= 0) {

                        currentEnemy.attackDelay--;
                        if (!currentEnemy.isBlinking && currentEnemy.attackDelay <= 0) {
                            // Começar a piscar antes do ataque
                            currentEnemy.isBlinking = true;
                            currentEnemy.blinkCounter = 0;
                            currentEnemy.attackDelay = ATTACK_COOLDOWN; // Resetar o delay do ataque para o próximo ataque
                        }
                    }
                }
            }
        }

        if (GameManager.getPlayer().hp <= 0) {
            handlePlayerDeath();
        }

        if(allEnemyIsDead()){
            handleVictory();
        }
    }

    // ================= MAIN LOOP BATTLE ===================


    public static boolean allEnemyIsDead(){
        for(Enemy e : enemies){
            if(e.hp > 0){
                return false;
            }
        }

        return true;
    }



    // ================= BATTLE CONDITIONS ==================

    private static void handlePlayerDeath() {
        coolDownEndBattle--;
        if (coolDownEndBattle <= 0) {
            GameManager.gameStates = GameStates.GAME_OVER;
            StateMachine.getInstance().change("Transition");
        }
        showBattleOptions = false;
    }

    private static void handleVictory() {
        coolDownEndBattle--;
        if (coolDownEndBattle <= 0) {
            StateMachine.getInstance().add("victory", new VictoryState());
            StateMachine.getInstance().change("victory");
            GameManager.gameStates = GameStates.VICTORY;
        }

        GameManager.checkLevelUp(GameManager.getPlayers());
    }

    private static boolean allPlayersDefeated() {
        for (Player player : GameManager.players) {
            if (player.hp > 0) {
                return false;
            }
        }
        return true;
    }

    private static boolean isBattleOver() {
        return allPlayersDefeated() || enemies.isEmpty();
    }

    // ================= BATTLE CONDITIONS ==================
}
