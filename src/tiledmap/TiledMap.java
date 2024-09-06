package tiledmap;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import entity.Enemy;
import entity.Entity;
import entity.npcs.NPC;
import entity.npcs.NPCFactory;
import gamestate.GameStates;
import gamestate.StateMachine;
import gamestate.states.battle.BattleState;
import graphics.SpriteSheet;
import main.AssetSetter;
import main.Camera;
import main.GameManager;
import object.OBJFactory;
import object.OBJ_Empty;
import object.OBJ_Special_Hammer;
import object.Obj_Pickaxe;
import object.consumables.OBJ_Health_Potion;
import object.interactables.OBJ_Chest;
import object.interactables.OBJ_Rock;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import tools.ResourceLoader;

import static main.GameManager.*;

public class TiledMap {

    public int mapWidthInTiles;
    public int mapHeightInTiles;

    public static int camWidth;
    public static int camHeight;

    public int maxWorldCol;
    public int maxWorldRow;

    private Point startingPoint;

    public ArrayList<SpriteLayer> spriteLayers;

    // COLLISION
    private ArrayList<CollisionLayer> collisionLayers;
    private ArrayList<Rectangle> originalCollisionAreas;
    public ArrayList<Rectangle> collisionAreasForUpdate;

    // ENEMIES
    public ArrayList<Enemy> enemies = new ArrayList<>();
    public ArrayList<Enemy> boss = new ArrayList<>();

    // PORTALS
    public ArrayList<Portal> portals;
    public ArrayList<BattleLayer> battleAreas;

    // EVENTS
    public ArrayList<GameEvent> events;

    // OBJECTS

    public Tile[] tiles;

    public static String targetMapName;
    public static Point playerPos;
    public static String mapName;

    // BATTLE
    public Enemy enemyFound;
    public int battleCoolDown;

    public int mapNum = 0;

    public TiledMap(){

    }

    public TiledMap(String path) {

        events = new ArrayList<>();
        portals = new ArrayList<>();
        battleAreas = new ArrayList<>();
        loadMap(path);
        loadBosses();
        battleCoolDown = 300;
    }

    public String getMapName(){
        return mapName;
    }


    public void update() {
        updateCollisionAreas();
        updatePortals();
        updateBattleArea();
        updateNpcs();
        updateCameraBounds();
    }

    public void loadBosses(){
        boss = new ArrayList<>();

        boss.add(new Enemy("Crab", 5, 10, 20, 25, 20,
                new Point(), 50));
    }

    public void updateCameraBounds(){
        camera.setCameraBounds(screenWidth,
                screenHeight,
                mapWidthInTiles * 32,
                mapHeightInTiles * 32);
    }

    public void draw(Graphics2D g2, int layerIndex, Camera cam) {

        if (layerIndex >= 0 && layerIndex < spriteLayers.size()) {
            SpriteLayer spriteLayer = spriteLayers.get(layerIndex);
            int[] layerSprites = spriteLayer.getSpritesArray();

            for (int y = 0; y < mapHeightInTiles; y++) {
                for (int x = 0; x < mapWidthInTiles; x++) {
                    int currentSpriteId = layerSprites[x + y * mapWidthInTiles];

                    int worldX = x * tileSize;
                    int worldY = y * tileSize;
                    int screenX = worldX - (tileSize/2) - cam.getX();
                    int screenY = worldY - (tileSize/2) - cam.getY();

                    // Verifica se o sprite está dentro da área visível da tela
                    if (isVisibleOnScreen(screenX, screenY)) {
                        if (currentSpriteId != -1) {
                            if (tiles[currentSpriteId] != null) {
                                g2.drawImage(tiles[currentSpriteId].getSprite().getImage(), screenX, screenY, tileSize, tileSize, null);
                            }
                        }
                    }
                }
            }
        }


    }

    // Método para verificar se o sprite está dentro da área visível da tela
    private boolean isVisibleOnScreen(int screenX, int screenY) {
        return screenX + 32 >= -32 && screenX < screenWidth && screenY + 32 >= -32 && screenY < screenHeight;
    }

    private void loadEvents(String path) {
        try {
            // Obter o objeto JSON correspondente à camada de eventos
            JSONObject globalJSON = getJSONObject(ResourceLoader.loadTextFile(path));
            JSONArray layers = getJSONArray(globalJSON.get("layers").toString());

            for (int i = 0; i < layers.size(); i++) {
                JSONObject layerData = getJSONObject(layers.get(i).toString());
                String layerName = layerData.get("name").toString();

                if (layerName.equals("events")) {
                    JSONArray eventsArray = getJSONArray(layerData.get("objects").toString());

                    for (Object o : eventsArray) {
                        JSONObject eventData = getJSONObject(o.toString());

                        int x = getIntFromJSON(eventData, "x");
                        int y = getIntFromJSON(eventData, "y");
                        int width = getIntFromJSON(eventData, "width");
                        int height = getIntFromJSON(eventData, "height");

                        // Verificar se o campo "name" existe
                        String eventName = eventData.containsKey("name") ? eventData.get("name").toString() : "Unnamed Event";

                        // Ajustar a posição e o tamanho do retângulo para o sistema de coordenadas do mundo do jogo
                        Rectangle area = new Rectangle(x, y, width, height);

                        // Verificar se o objeto "properties" existe
                        if (eventData.containsKey("properties")) {
                            JSONObject properties = getJSONObject(eventData.get("properties").toString());

                            // Ler os atributos "eventType" e "parameters" do objeto "properties"
                            String eventType = properties.containsKey("eventType") ? properties.get("eventType").toString() : null;

                            String xPos = properties.containsKey("xPos") ? properties.get("xPos").toString() : null;
                            String yPos = properties.containsKey("yPos") ? properties.get("yPos").toString() : null;
                            String id = properties.containsKey("id") ? properties.get("id").toString() : null;

                            if(eventType != null && Objects.equals(eventType, "notPass")){
                                events.add(new GameEvent(area, eventType, eventName, xPos, yPos, id));
                            }
                            else{
                                events.add(new GameEvent(area, eventType, eventName, x, y, id));
                            }

                        } else {
                            // Tratar o caso onde properties é nulo
                            System.err.println("Evento sem propriedades: " + eventName);
                        }
                    }

                    // Se encontramos a camada de eventos, não precisamos verificar outras camadas
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPortals(String path) {
        try {
            // Obter o objeto JSON correspondente à camada de portais
            JSONObject globalJSON = getJSONObject(ResourceLoader.loadTextFile(path));
            JSONArray layers = getJSONArray(globalJSON.get("layers").toString());

            for (int i = 0; i < layers.size(); i++) {
                JSONObject layerData = getJSONObject(layers.get(i).toString());
                String layerName = layerData.get("name").toString();

                if (layerName.equals("portals")) {
                    JSONArray portalsArray = getJSONArray(layerData.get("objects").toString());

                    for (Object o : portalsArray) {
                        JSONObject portalData = getJSONObject(o.toString());

                        int x = getIntFromJSON(portalData, "x");
                        int y = getIntFromJSON(portalData, "y");
                        int width = getIntFromJSON(portalData, "width");
                        int height = getIntFromJSON(portalData, "height");
                        String mapName = portalData.get("name").toString();

                        // Ajustar a posição e o tamanho do retângulo para o sistema de coordenadas do mundo do jogo
                        Rectangle area = new Rectangle(x, y, width, height);

                        // Ler os atributos "target", "targetX" e "targetY" do objeto "properties"
                        JSONObject properties = getJSONObject(portalData.get("properties").toString());
                        String targetMapPath = properties.get("target").toString();
                        int targetX = getIntFromJSON(properties, "targetX");
                        int targetY = getIntFromJSON(properties, "targetY");
                        Point targetPosition = new Point(targetX, targetY);

                        // Criar e adicionar um novo portal à lista de portais
                        portals.add(new Portal(area, targetMapPath, targetPosition, mapName));
                    }

                    // Se encontramos a camada de portais, não precisamos verificar outras camadas
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBattleArea(String path) {
        try {
            // Obter o objeto JSON correspondente à camada de portais
            JSONObject globalJSON = getJSONObject(ResourceLoader.loadTextFile(path));
            JSONArray layers = getJSONArray(globalJSON.get("layers").toString());

            for (int i = 0; i < layers.size(); i++) {
                JSONObject layerData = getJSONObject(layers.get(i).toString());
                String layerName = layerData.get("name").toString();

                if (layerName.equals("battle")) {
                    JSONArray portalsArray = getJSONArray(layerData.get("objects").toString());

                    for (Object o : portalsArray) {
                        JSONObject portalData = getJSONObject(o.toString());

                        int x = getIntFromJSON(portalData, "x");
                        int y = getIntFromJSON(portalData, "y");
                        int width = getIntFromJSON(portalData, "width");
                        int height = getIntFromJSON(portalData, "height");

                        // Ajustar a posição e o tamanho do retângulo para o sistema de coordenadas do mundo do jogo
                        Rectangle area = new Rectangle(x, y, width, height);

                        // Ler os atributos "target", "targetX" e "targetY" do objeto "properties"
                        JSONObject properties = getJSONObject(portalData.get("properties").toString());
                        ArrayList<String> tempEnemies = new ArrayList<>();

                        String enemy1 = properties.get("enemy1").toString();
                        tempEnemies.add(enemy1);

                        String enemy2 = properties.get("enemy2").toString();
                        tempEnemies.add(enemy2);

                        String enemy3 = properties.get("enemy3").toString();
                        tempEnemies.add(enemy3);


                        // Criar e adicionar um novo portal à lista de portais
                        battleAreas.add(new BattleLayer(area, tempEnemies));
                    }

                    // Se encontramos a camada de portais, não precisamos verificar outras camadas
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMap(final String path){
        String content = ResourceLoader.loadTextFile(path);

        loadPortals(path);
        loadBattleArea(path);
        loadNpcs(path);
        loadObjects(path);
        loadEvents(path);

        // WIDTH, HEIGHT
        JSONObject globalJSON = getJSONObject(content);
        mapWidthInTiles = getIntFromJSON(globalJSON, "width");
        mapHeightInTiles = getIntFromJSON(globalJSON, "height");
        camWidth = mapWidthInTiles;
        camHeight = mapHeightInTiles;

        JSONObject mapProperties = (JSONObject) globalJSON.get("properties");
        mapNum = getIntFromJSON(mapProperties, "mapNum");

        // STARTING POINT
        /*JSONObject startingPoint = getJSONObject(globalJSON.get("start").toString());
        this.startingPoint = new Point(getIntFromJSON(startingPoint, "x"),
                getIntFromJSON(startingPoint, "y"));*/

        // LAYERS
        JSONArray layers = getJSONArray(globalJSON.get("layers").toString());

        this.spriteLayers = new ArrayList<>();
        this.collisionLayers = new ArrayList<>();

        // INITIALIZING LAYERS
        for (Object layer : layers) {
            JSONObject layerData = getJSONObject(layer.toString());
            String layerName = layerData.get("name").toString();

            int layerWidth = getIntFromJSON(layerData, "width");
            int layerHeight = getIntFromJSON(layerData, "height");
            int layerX = getIntFromJSON(layerData, "x");
            int layerY = getIntFromJSON(layerData, "y");
            String type = layerData.get("type").toString();

            maxWorldCol = layerWidth;
            maxWorldRow = layerHeight;

            switch (type) {
                case "tilelayer":
                    JSONArray sprites = getJSONArray(layerData.get("data").toString());
                    int[] layerSprites = new int[sprites.size()];
                    for (int j = 0; j < sprites.size(); j++) {
                        int spriteCode = Integer.parseInt(sprites.get(j).toString());
                        layerSprites[j] = spriteCode - 1;
                    }
                    this.spriteLayers.add(new SpriteLayer(layerWidth, layerHeight, layerX, layerY, layerSprites));
                    break;
                case "objectgroup":

                    if (!layerName.contains("object") &&
                            !layerName.contains("events") &&
                            !layerName.contains("portals") &&
                            !layerName.contains("battle") && !layerName.contains("npc")) {

                        JSONArray rectangles = getJSONArray(layerData.get("objects").toString());
                        Rectangle[] layerRectangles = new Rectangle[rectangles.size()];

                        for (int j = 0; j < rectangles.size(); j++) {
                            JSONObject rectangleData = getJSONObject(rectangles.get(j).toString());

                            int x = getIntFromJSON(rectangleData, "x");
                            int y = getIntFromJSON(rectangleData, "y");
                            int width = getIntFromJSON(rectangleData, "width");
                            int height = getIntFromJSON(rectangleData, "height");

                            if (x == 0) x = 1;
                            if (y == 0) y = 1;
                            if (width == 0) width = 1;
                            if (height == 0) height = 1;

                            Rectangle rectangle = new Rectangle(x, y, width, height);
                            layerRectangles[j] = rectangle;
                        }

                        this.collisionLayers.add(new CollisionLayer(layerWidth, layerHeight, layerX, layerY, layerRectangles));
                        break;
                    }
            }
        }

        // COMBINING COLLISIONS INTO SINGLE ARRAYLIST FOR EFFICIENCY
        originalCollisionAreas = new ArrayList<>();

        for (CollisionLayer collisionLayer : collisionLayers) {
            Rectangle[] rectangles = collisionLayer.getCollidables();

            originalCollisionAreas.addAll(Arrays.asList(rectangles));
        }

        // GET TOTAL NUMBER OF SPRITES ACROSS ALL LAYERS
        JSONArray spriteCollections = getJSONArray(globalJSON.get("tilesets").toString());
        int totalSprites = 0;
        for (int i = 0; i < spriteCollections.size(); i++) {
            JSONObject groupData = getJSONObject(spriteCollections.get(i).toString());
            totalSprites += getIntFromJSON(groupData, "tilecount");
        }

        tiles = new Tile[totalSprites];

        // ASSIGN NECESSARY SPRITES TO PALETTE BASED ON LAYERS
        for (int i = 0; i < spriteCollections.size(); i++) {
            JSONObject groupData = getJSONObject(spriteCollections.get(i).toString());

            String imageName = groupData.get("image").toString();
            int tileWidth = getIntFromJSON(groupData, "tilewidth");
            int tileHeight = getIntFromJSON(groupData, "tileheight");
            SpriteSheet sheet = new SpriteSheet(imageName,
                    tileWidth, tileHeight, false);

            int firstSpriteInCollection = getIntFromJSON(groupData, "firstgid") - 1;
            int lastSpriteInCollection = firstSpriteInCollection + getIntFromJSON(groupData, "tilecount") - 1;

            for (int j = 0; j < this.spriteLayers.size(); j++) {
                SpriteLayer currentLayer = this.spriteLayers.get(j);
                int[] layerSprites = currentLayer.getSpritesArray();

                for (int k = 0; k < layerSprites.length; k++) {
                    int currentSpriteId = layerSprites[k];
                    if (currentSpriteId >= firstSpriteInCollection && currentSpriteId <= lastSpriteInCollection) {
                        if (tiles[currentSpriteId] == null) {
                            tiles[currentSpriteId] = new Tile(sheet.getSprite(currentSpriteId - firstSpriteInCollection));
                        }
                    }
                }
            }
        }

        collisionAreasForUpdate = new ArrayList<>();
    }

    public void loadObjects(String path){
        try {
            // Obter o objeto JSON correspondente à camada de portais
            JSONObject globalJSON = getJSONObject(ResourceLoader.loadTextFile(path));
            JSONArray layers = getJSONArray(globalJSON.get("layers").toString());

            for (int i = 0; i < layers.size(); i++) {
                JSONObject layerData = getJSONObject(layers.get(i).toString());
                String layerName = layerData.get("name").toString();

                if (layerName.equals("object")) {
                    JSONArray objArray = getJSONArray(layerData.get("objects").toString());

                    for (Object o : objArray) {
                        JSONObject objData = getJSONObject(o.toString());

                        int x = getIntFromJSON(objData, "x");
                        int y = getIntFromJSON(objData, "y");

                        // Ler os atributos "target", "targetX" e "targetY" do objeto "properties"
                        JSONObject properties = getJSONObject(objData.get("properties").toString());
                        String objType = properties.get("type").toString();
                        String loot = properties.get("loot").toString();

                        Entity obj = OBJFactory.createObject(objType, properties);

                        assert obj != null;
                        obj.position.x = x;
                        obj.position.y = y;

                        JSONObject mapProperties = (JSONObject) globalJSON.get("properties");
                        mapNum = getIntFromJSON(mapProperties, "mapNum");

                        AssetSetter.setObject(x, y, mapNum, (object.Object) obj, loot);
                    }

                    // Se encontramos a camada de portais, não precisamos verificar outras camadas
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadNpcs(String path){
        try {
            // Obter o objeto JSON correspondente à camada de portais
            JSONObject globalJSON = getJSONObject(ResourceLoader.loadTextFile(path));
            JSONArray layers = getJSONArray(globalJSON.get("layers").toString());

            for (int i = 0; i < layers.size(); i++) {
                JSONObject layerData = getJSONObject(layers.get(i).toString());
                String layerName = layerData.get("name").toString();

                if (layerName.equals("npc")) {
                    JSONArray npcArray = getJSONArray(layerData.get("objects").toString());

                    for (Object o : npcArray) {
                        JSONObject npcData = getJSONObject(o.toString());

                        int x = getIntFromJSON(npcData, "x");
                        int y = getIntFromJSON(npcData, "y");

                        // Ler os atributos "target", "targetX" e "targetY" do objeto "properties"
                        JSONObject properties = getJSONObject(npcData.get("properties").toString());
                        String npcName = properties.get("name").toString();

                        NPC npc = NPCFactory.createNPC(npcName);

                        assert npc != null;
                        npc.position.x = x;
                        npc.position.y = y;

                        JSONObject mapProperties = (JSONObject) globalJSON.get("properties");
                        mapNum = getIntFromJSON(mapProperties, "mapNum");

                        AssetSetter.setNPC(npc, x, y, mapNum, npcName);
                    }

                    // Se encontramos a camada de portais, não precisamos verificar outras camadas
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateNpcs(){
        for (NPC npc : npcs.get(currentMap)) {
            if (npc != null) {
                npc.update();
            }
        }
    }

    private void updateBattleArea(){

        // System.out.println(battleCoolDown);

        for (BattleLayer battleArea : battleAreas) {
            Rectangle playerBounds = getPlayer().getPlayerBounds();
            Rectangle bArea = battleArea.getArea();

            if (getPlayer().isWalking && battleCoolDown <= 0 && playerBounds.intersects(bArea) && enemyFound == null) {

                //System.out.println("entrou na batalha!");
                //Bat
                enemies.add(
                        new Enemy(
                                battleArea.getEnemies().get(0), 5, 10, 5, 25, 20,
                                new Point(), 20));

                // Goblin
                enemies.add(
                        new Enemy(
                                battleArea.getEnemies().get(1), 2,5, 5, 15, 15,
                                new Point(), 50));

                // Smile
                enemies.add(
                        new Enemy(
                                battleArea.getEnemies().get(2),  7, 15, 5, 50, 35,
                                new Point(), 15));



                // Probabilidade de encontrar cada inimigo (valores relativos)
                int[] probabilidades = {3, 2, 1};

                // Use a classe Random para escolher aleatoriamente um inimigo com base nas probabilidades
                Random rand = new Random();
                int totalProbabilidade = 0;
                for (int prob : probabilidades) {
                    totalProbabilidade += prob;
                }
                int randNum = rand.nextInt(totalProbabilidade);
                int acumulado = 0;
                for (int i = 0; i < enemies.size(); i++) {
                    acumulado += probabilidades[i];
                    if (randNum < acumulado) {
                        enemyFound = enemies.get(i);
                        break;
                    }
                }

                if (enemyFound != null) {
                    //System.out.println("Você encontrou um " + enemyFound.getName() + "!");
                    // Inicie a batalha com o inimigo encontrado
                    gameStates = GameStates.BATTLE;
                    StateMachine.getInstance().change("Transition");
                    StateMachine.getInstance().add("Battle", new BattleState());

                } else {
                    //System.out.println("Você não encontrou nenhum inimigo.");
                }

                break; // Se o jogador entrar em um portal, não precisamos verificar os outros portais
            }

            if(playerBounds.intersects(bArea.getBounds())){
                if (getPlayer().isWalking){
                    battleCoolDown--;
                }
            }
        }
    }

    private void updatePortals() {

        for (Portal portal : portals) {
            Rectangle playerBounds = getPlayer().getPlayerBounds();
            Rectangle portalArea = portal.getArea();

            if (playerBounds.intersects(portalArea)) {

                //portals.clear();
                //battleAreas.clear();
                // O jogador entrou no portal, carregue o próximo mapa e posicione o jogador no local de destino

                String path = portal.getTargetMapPath();
                // Remover a parte "/maps/" da string
                String mapName = path.replace("/maps/", "");
                // Remover a extensão ".json" da string
                TiledMap.mapName = mapName.replace(".json", "");

                targetMapName = portal.getTargetMapPath();
                playerPos = portal.getTargetPosition();


                StateMachine.getInstance().change("Transition");
                gameStates = GameStates.TELEPORT;

                break; // Se o jogador entrar em um portal, não precisamos verificar os outros portais
            }
        }

    }

    private void updateCollisionAreas() {
        if (!collisionAreasForUpdate.isEmpty()) {
            collisionAreasForUpdate.clear();
        }

        for (int i = 0; i < originalCollisionAreas.size(); i++) {
            Rectangle initialRect = originalCollisionAreas.get(i);

            int xPoint = initialRect.x - getPlayer().position.x + MARGIN_X;
            int yPoint = initialRect.y - getPlayer().position.y + MARGIN_Y;

            final Rectangle finalRect = new Rectangle(xPoint, yPoint, initialRect.width, initialRect.height);

            collisionAreasForUpdate.add(finalRect);
        }
    }

    /*private void updateItemCollection() {
        if (!mapObjects.isEmpty()) {
            final Rectangle playerArea = new Rectangle(Elements.player.getPositionXInt(),
                    Elements.player.getPositionYInt(), Constants.SPRITE_SIDE, Constants.SPRITE_SIDE);

            for (int i = 0; i < mapObjects.size(); i++) {
                final UniqueTiledObject currentObject = mapObjects.get(i);

                final Rectangle currentObjectPosition = new Rectangle(
                        currentObject.getPosition().x,
                        currentObject.getPosition().y, Constants.SPRITE_SIDE,
                        Constants.SPRITE_SIDE);

                if (playerArea.intersects(currentObjectPosition) && ControlManager.keyboard.isPickingUp) {
                    Elements.inventory.pickUpItems(currentObject);
                    mapObjects.remove(i);
                }
            }
        }
    }*/

    /*private void updateAttacks() {
        if (mapEnemies.isEmpty()
                || Elements.player.getCurrentRange().isEmpty()
                || Elements.player.getEquipmentInventory().getWeapon1() instanceof Unarmed) {
            return;
        }

        if (ControlManager.keyboard.isAttacking) {
            ArrayList<Enemy> targetedEnemies = new ArrayList<>();

            if (Elements.player.getEquipmentInventory().getWeapon1().isPiercing()) {
                for (Enemy enemy : mapEnemies) {
                    if (Elements.player.getCurrentRange().get(0).intersects(enemy.getPosition())) {
                        targetedEnemies.add(enemy);
                    }
                }
            } else {
                Enemy closestEnemy = null;
                Double closestDistance = null;

                for (Enemy enemy : mapEnemies) {
                    if (Elements.player.getCurrentRange().get(0).intersects(enemy.getPosition())) {
                        Point playerPoint = new Point((int) Elements.player.getPositionX() / 32, (int) Elements.player.getPositionY() / 32);
                        Point enemyPoint = new Point((int) enemy.getPosition().getX(), (int) enemy.getPosition().getY());

                        Double currentDistance = DistanceCalculator.calculateDistanceBetweenPoints(playerPoint, enemyPoint);

                        if (closestEnemy == null) {
                            closestEnemy = enemy;
                            closestDistance = currentDistance;
                        } else if (currentDistance < closestDistance) {
                            closestEnemy = enemy;
                            closestDistance = currentDistance;
                        }
                    }
                }
                targetedEnemies.add(closestEnemy);
            }

            Elements.player.getEquipmentInventory().getWeapon1().attack(targetedEnemies);
        }

        Iterator<Enemy> iterator = mapEnemies.iterator();

        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();

            if (enemy.getCurrentHealth() <= 0) {
                iterator.remove();
                Elements.player.points += 100;
            }
        }
    }*/

    private JSONObject getJSONObject(final String JSONCode) {
        JSONParser parser = new JSONParser();
        JSONObject JSONObj = null;

        try {
            Object parsed = parser.parse(JSONCode);
            JSONObj = (JSONObject) parsed;
        } catch(ParseException e) {
            System.out.println("Position: " + e.getPosition());
            System.out.println(e);
        }

        return JSONObj;
    }

    private JSONArray getJSONArray(final String JSONCode) {
        JSONParser parser = new JSONParser();
        JSONArray JSONArray = null;

        try {
            Object parsed = parser.parse(JSONCode);
            JSONArray = (JSONArray) parsed;
        } catch(ParseException e) {
            System.out.println("Position: " + e.getPosition());
            System.out.println(e);
        }

        return JSONArray;
    }

    private int getIntFromJSON(final JSONObject JSONObj, final String key) {
        double value = Double.parseDouble(JSONObj.get(key).toString());
        return (int) Math.round(value); // ou Math.floor(value) ou Math.ceil(value) conforme necessário
    }

    public Point getInitialPosition() {
        return startingPoint;
    }

    public Rectangle getBounds(final int positionX, final int positionY) {
        int x = MARGIN_X - getPlayer().position.x + 32;
        int y = MARGIN_Y - getPlayer().position.y + 32;

        int width = this.mapWidthInTiles * tileSize - 32 * 2;
        int height = this.mapHeightInTiles * tileSize - 32 * 2;

        return new Rectangle(x, y, width, height);
    }

    public int checkObject(Entity entity, boolean player) {

        int index = 999;

        for (int i = 0; i < GameManager.obj[1].length; i++) {

            if(GameManager.obj[GameManager.currentMap][i] != null) {
                // Get entity's solid area position
                entity.solidArea.x = entity.position.x + entity.solidArea.x;
                entity.solidArea.y = entity.position.y + entity.solidArea.y;
                // Get object's solid area position
                GameManager.obj[GameManager.currentMap][i].solidArea.x = obj[currentMap][i].position.x + GameManager.obj[GameManager.currentMap][i].solidArea.x;
                GameManager.obj[GameManager.currentMap][i].solidArea.y = obj[currentMap][i].position.y + GameManager.obj[GameManager.currentMap][i].solidArea.y;

                switch(entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        if(entity.solidArea.intersects(GameManager.obj[GameManager.currentMap][i].solidArea)) {
                            if(GameManager.obj[GameManager.currentMap][i].collision) {
                                entity.collisionOn = true;
                            }
                            if(player) {
                                index = i;
                            }
                        }
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        if(entity.solidArea.intersects(GameManager.obj[GameManager.currentMap][i].solidArea)) {
                            if(obj[currentMap][i].collision) {
                                entity.collisionOn = true;
                            }
                            if(player) {
                                index = i;
                            }
                        }
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if(entity.solidArea.intersects(GameManager.obj[GameManager.currentMap][i].solidArea)) {
                            if(GameManager.obj[GameManager.currentMap][i].collision) {
                                entity.collisionOn = true;
                            }
                            if(player) {
                                index = i;
                            }
                        }
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if(entity.solidArea.intersects(GameManager.obj[GameManager.currentMap][i].solidArea)) {
                            if(GameManager.obj[GameManager.currentMap][i].collision) {
                                entity.collisionOn = true;
                            }
                            if(player) {
                                index = i;
                            }
                        }
                        break;
                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                GameManager.obj[GameManager.currentMap][i].solidArea.x = GameManager.obj[GameManager.currentMap][i].solidAreaDefaultX;
                GameManager.obj[GameManager.currentMap][i].solidArea.y = GameManager.obj[GameManager.currentMap][i].solidAreaDefaultY;

            }
        }
        return index;
    }

    public int checkEntity(Entity entity, ArrayList<ArrayList<NPC>> target) {
        int index = 999; // Inicialmente, não há colisão

        ArrayList<NPC> currentMapEntities = target.get(currentMap);

        for (int i = 0; i < currentMapEntities.size(); i++) {
            Entity targetEntity = currentMapEntities.get(i);
            if (targetEntity != null) {
                // Salvar as posições originais das áreas sólidas
                int entitySolidAreaX = entity.position.x + entity.solidArea.x;
                int entitySolidAreaY = entity.position.y + entity.solidArea.y;
                int targetSolidAreaX = targetEntity.position.x + targetEntity.solidArea.x;
                int targetSolidAreaY = targetEntity.position.y + targetEntity.solidArea.y;

                // Atualizar as posições das áreas sólidas
                entity.solidArea.x = entitySolidAreaX;
                entity.solidArea.y = entitySolidAreaY;
                targetEntity.solidArea.x = targetSolidAreaX;
                targetEntity.solidArea.y = targetSolidAreaY;

                // Verificar colisão baseada na direção
                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(targetEntity.solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(targetEntity.solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(targetEntity.solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(targetEntity.solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                }

                // Resetar as posições das áreas sólidas
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                targetEntity.solidArea.x = targetEntity.solidAreaDefaultX;
                targetEntity.solidArea.y = targetEntity.solidAreaDefaultY;

                // Se houver uma colisão, sair do loop
                if (entity.collisionOn) {
                    break;
                }
            }
        }
        return index;
    }


    public void checkPlayer(Entity entity){
        // Get entity's solid area position
        entity.solidArea.x = entity.position.x + entity.solidArea.x;
        entity.solidArea.y = entity.position.y + entity.solidArea.y;
        // Get object's solid area position
        getPlayer().solidArea.x = getPlayer().position.x + getPlayer().solidArea.x;
        getPlayer().solidArea.y = getPlayer().position.y + getPlayer().solidArea.y;

        switch(entity.direction) {
            case "up":
                entity.solidArea.y -= entity.speed;
                if(entity.solidArea.intersects(getPlayer().solidArea)) {

                    entity.collisionOn = true;

                    break;
                }

            case "down":
                entity.solidArea.y += entity.speed;
                if(entity.solidArea.intersects(getPlayer().solidArea)) {
                    entity.collisionOn = true;
                    break;
                }

            case "left":
                entity.solidArea.x -= entity.speed;
                if(entity.solidArea.intersects(getPlayer().solidArea)) {
                    entity.collisionOn = true;
                    break;
                }

            case "right":
                entity.solidArea.x += entity.speed;
                if(entity.solidArea.intersects(getPlayer().solidArea)) {
                    entity.collisionOn = true;
                    break;
                }

        }
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        getPlayer().solidArea.x = getPlayer().solidAreaDefaultX;
        getPlayer().solidArea.y = getPlayer().solidAreaDefaultY;
    }

    public ArrayList<GameEvent> getEvents() {
        return events;
    }
}
