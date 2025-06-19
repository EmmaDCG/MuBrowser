// 
// Decompiled by Procyon v0.5.30
// 

package com.mu.game.model.map;

import com.mu.game.model.equip.external.ExternalEntry;
import com.mu.io.game.packet.imp.player.ExternalChange;
import com.mu.io.game.packet.imp.player.PlayerAttributes;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatList2Client;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.item.operation.OperationResult;
import com.mu.io.game.packet.imp.drop.PickoutItem;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.imp.SafeRevivalEvent;
import com.mu.io.game.packet.imp.player.PlayerDieDialog;
import com.mu.io.game.packet.imp.map.MapChangeLandscape;
import com.mu.utils.geom.ShapeUtil;
import java.awt.geom.Point2D;
import java.awt.Shape;
import java.util.HashSet;
import com.mu.io.game.packet.imp.map.AroundTransPoint;
import com.mu.io.game.packet.imp.map.TransferMap;
import com.mu.game.model.stats.FinalModify;
import com.mu.io.game.packet.imp.map.SwitchLine;
import com.mu.io.game.packet.imp.player.PlayerRevival;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.io.game.packet.imp.player.AroundPlayer;
import com.mu.io.game.packet.imp.drop.AroundDropItem;
import com.mu.io.game.packet.imp.material.AroundMaterial;
import java.util.Collection;
import com.mu.io.game.packet.imp.npc.UpdateNpcHeadIcon;
import com.mu.io.game.packet.imp.npc.AroundNpc;
import com.mu.io.game.packet.imp.panda.AroundPanda;
import com.mu.io.game.packet.imp.pet.AroundPet;
import com.mu.io.game.packet.imp.pkModel.ChangePkView;
import com.mu.io.game.packet.imp.map.UnitMove;
import com.mu.io.game.packet.imp.monster.AroundMonster;
import com.mu.io.game.packet.imp.sys.ListPacket;
import java.util.Iterator;
import com.mu.utils.Rnd;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import com.mu.game.model.pet.PlayerPetManager;
import java.util.Enumeration;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.List;
import com.mu.io.game.packet.imp.map.RemoveUnit;
import com.mu.game.model.unit.Unit;
import java.util.ArrayList;
import com.mu.game.model.unit.MapUnit;
import com.mu.game.model.unit.Creature;
import com.mu.utils.Tools;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.awt.Point;
import com.mu.game.model.unit.action.DelayAction;
import com.mu.game.model.unit.tp.TransPoint;
import java.util.HashMap;
import com.mu.game.model.drop.DropItem;
import com.mu.game.model.unit.material.Material;
import com.mu.game.model.unit.npc.Npc;
import com.mu.game.model.unit.monster.Monster;
import com.mu.game.model.panda.Panda;
import com.mu.game.model.pet.Pet;
import com.mu.game.model.unit.player.Player;
import java.util.concurrent.ConcurrentHashMap;
import java.awt.Rectangle;
import java.util.concurrent.ScheduledFuture;

public class Map
{
    public static final int Type_Normal = 1;
    public static final int Type_Dungeon = 2;
    public static final int Landscape_Land = 1;
    public static final int Landscape_water = 2;
    public static final int AREA_WIDTH = 10000;
    public static final int AREA_HEIGHT = 8000;
    public static final int Area_X_Number = 3;
    public static final int Area_Y_Number = 3;
    public static final int TileWidth = 600;
    public static final int TileCenter = 300;
    private int id;
    private String name;
    private int width;
    private int height;
    private int left;
    private int top;
    private int tileHorizontalNumber;
    private int tileVerticalNumber;
    private int areaWidth;
    private int areaHeight;
    private int weather;
    private int music;
    private int line;
    protected int areaMaxH;
    protected int areaMaxV;
    private byte[][] blocks;
    private ScheduledFuture<?> workFuture;
    protected Rectangle[][] mapArea;
    protected ConcurrentHashMap<Long, Player> playerMap;
    protected ConcurrentHashMap<Long, Pet> petMap;
    protected ConcurrentHashMap<Long, Panda> pandaMap;
    protected ConcurrentHashMap<Long, Monster> monsterMap;
    protected ConcurrentHashMap<Long, Npc> npcMap;
    protected ConcurrentHashMap<Long, Material> materialMap;
    protected ConcurrentHashMap<Long, DropItem> dropItemMap;
    protected HashMap<Long, TransPoint> transPointMap;
    protected ConcurrentHashMap<DelayAction, Boolean> enterMapActions;
    protected ConcurrentHashMap<DelayAction, Boolean> leaveMapActions;
    private boolean canZening;
    private int findWayID;
    private boolean canPk;
    private boolean canChangePkMode;
    private boolean pkPunishment;
    protected boolean isDestroy;
    private boolean shouldDestory;
    private long workTimes;
    private int landscape;
    private boolean checkLandscapeChange;
    private Point defaultPoint;
    private boolean canTransmit;
    private int robotSize;
    private static final Logger logger;
    long begin;
    
    static {
        logger = LoggerFactory.getLogger((Class)Map.class);
    }
    
    public Map(final int id) {
        this.width = 0;
        this.height = 0;
        this.left = 0;
        this.top = 0;
        this.tileHorizontalNumber = 0;
        this.tileVerticalNumber = 0;
        this.areaWidth = 10000;
        this.areaHeight = 8000;
        this.weather = 0;
        this.music = 0;
        this.line = 0;
        this.areaMaxH = 0;
        this.areaMaxV = 0;
        this.blocks = null;
        this.workFuture = null;
        this.mapArea = null;
        this.playerMap = Tools.newConcurrentHashMap2();
        this.petMap = Tools.newConcurrentHashMap2();
        this.pandaMap = Tools.newConcurrentHashMap2();
        this.monsterMap = Tools.newConcurrentHashMap2();
        this.npcMap = Tools.newConcurrentHashMap2();
        this.materialMap = Tools.newConcurrentHashMap2();
        this.dropItemMap = Tools.newConcurrentHashMap2();
        this.transPointMap = new HashMap<Long, TransPoint>();
        this.enterMapActions = null;
        this.leaveMapActions = null;
        this.canZening = true;
        this.findWayID = -1;
        this.canPk = true;
        this.canChangePkMode = true;
        this.pkPunishment = true;
        this.isDestroy = false;
        this.shouldDestory = false;
        this.workTimes = 0L;
        this.landscape = 1;
        this.checkLandscapeChange = false;
        this.defaultPoint = new Point();
        this.canTransmit = true;
        this.robotSize = 0;
        this.begin = System.currentTimeMillis();
        this.id = id;
        this.startWork();
    }
    
    public final String getName() {
        return this.name;
    }
    
    public final void setName(final String name) {
        this.name = name;
    }
    
    public boolean canChangePkMode() {
        return this.canChangePkMode;
    }
    
    public final void setCanChangePkMode(final boolean canChangePkMode) {
        this.canChangePkMode = canChangePkMode;
    }
    
    public boolean isCheckLandscapeChange() {
        return this.checkLandscapeChange;
    }
    
    public void setCheckLandscapeChange(final boolean checkLandscapeChange) {
        this.checkLandscapeChange = checkLandscapeChange;
    }
    
    public int getLandscape() {
        return this.landscape;
    }
    
    public void setLandscape(final int landscape) {
        this.landscape = landscape;
    }
    
    public boolean isSafe(final Creature creature) {
        return this.isSafe(creature.getX(), creature.getY());
    }
    
    public boolean isSafe(final int x, final int y) {
        return !this.isCanPk() || (!MapConfig.isUnSafe(this.id, x, y) && MapConfig.isSafe(this.id, x, y));
    }
    
    public final int getWidth() {
        return this.width;
    }
    
    public void setLeft(final int left) {
        this.left = left;
    }
    
    public int getLeft() {
        return this.left;
    }
    
    public void setTop(final int top) {
        this.top = top;
    }
    
    public int getTop() {
        return this.top;
    }
    
    public int getRight() {
        return this.left + this.width;
    }
    
    public int getBottom() {
        return this.top - this.height;
    }
    
    public final void setWidth(final int width) {
        this.width = width;
    }
    
    public boolean isValidPoint(final Point point) {
        return point.x >= this.left && point.x <= this.getRight() && point.y <= this.top && point.y >= this.getBottom();
    }
    
    public int getXByTile(final int tileX) {
        return tileX * 600 + this.left;
    }
    
    public int getYByTile(final int tileY) {
        return this.top - tileY * 600;
    }
    
    public int getXCenterByTile(final int tileX) {
        return tileX * 600 + this.left + 300;
    }
    
    public int getYCenterByTile(final int tileY) {
        return this.top - tileY * 600 - 300;
    }
    
    public final int getHeight() {
        return this.height;
    }
    
    public final void setHeight(final int height) {
        this.height = height;
    }
    
    public void initBlocks(final int horizontal, final int vertical) {
        this.blocks = new byte[horizontal][vertical];
        this.tileHorizontalNumber = horizontal;
        this.tileVerticalNumber = vertical;
    }
    
    public final int getWeather() {
        return this.weather;
    }
    
    public final void setWeather(final int weather) {
        this.weather = weather;
    }
    
    protected void createArea() {
        this.areaMaxH = this.width / this.areaWidth;
        this.areaMaxV = this.height / this.areaHeight;
        if (this.width % this.areaWidth != 0) {
            ++this.areaMaxH;
        }
        if (this.height % this.areaHeight != 0) {
            ++this.areaMaxV;
        }
        this.mapArea = new Rectangle[this.areaMaxH][this.areaMaxV];
        for (int i = 0; i < this.areaMaxH; ++i) {
            for (int j = 0; j < this.areaMaxV; ++j) {
                final int rectX = this.areaWidth * i + this.left - this.areaWidth;
                final int rectY = this.top - this.areaHeight * j - this.areaHeight - this.areaHeight;
                final Rectangle rect = new Rectangle(rectX, rectY, this.areaWidth * 3, this.areaHeight * 3);
                this.mapArea[i][j] = rect;
            }
        }
    }
    
    public final boolean isCanZening() {
        return this.canZening;
    }
    
    public final void setCanZening(final boolean canZening) {
        this.canZening = canZening;
    }
    
    public boolean isCanPk() {
        return this.canPk;
    }
    
    public void setCanPk(final boolean canPk) {
        this.canPk = canPk;
    }
    
    public boolean isPkPunishment() {
        return this.pkPunishment;
    }
    
    public void setPkPunishment(final boolean pkPunishment) {
        this.pkPunishment = pkPunishment;
    }
    
    public final int getFindWayID() {
        return this.findWayID;
    }
    
    public final void setFindWayID(final int findWayID) {
        this.findWayID = findWayID;
    }
    
    public final Rectangle getArea(final int x, final int y) {
        final int areaX = (x - this.left) / this.areaWidth;
        final int areaY = (this.top - y) / this.areaHeight;
        if (areaX < 0 || areaY < 0 || areaX >= this.mapArea.length || areaY >= this.mapArea[areaX].length) {
            return null;
        }
        return this.mapArea[areaX][areaY];
    }
    
    public Rectangle getArea(final Point p) {
        return this.getArea(p.x, p.y);
    }
    
    public int getAreaHeight() {
        return this.areaHeight;
    }
    
    public void setAreaHeight(final int areaHeight) {
        this.areaHeight = areaHeight;
    }
    
    public int getAreaWidth() {
        return this.areaWidth;
    }
    
    public void setAreaWidth(final int areaWidth) {
        this.areaWidth = areaWidth;
    }
    
    public void addMonster(final Monster monster) {
        this.monsterMap.put(monster.getID(), monster);
    }
    
    public void addNpc(final Npc npc) {
        this.npcMap.put(npc.getID(), npc);
    }
    
    public Npc getNpc(final long id) {
        return this.npcMap.get(id);
    }
    
    public void addMaterial(final Material m) {
        this.materialMap.put(m.getID(), m);
    }
    
    public void removeMaterial(final long id) {
        this.materialMap.remove(id);
    }
    
    public Material getMaterial(final long id) {
        return this.materialMap.get(id);
    }
    
    public ConcurrentHashMap<Long, Material> getMaterialMap() {
        return this.materialMap;
    }
    
    public void addDropItem(final DropItem item) {
        this.dropItemMap.put(item.getID(), item);
    }
    
    public DropItem getDropItem(final long id) {
        return this.dropItemMap.get(id);
    }
    
    public int getDropItemSize() {
        return this.dropItemMap.size();
    }
    
    public void addPlayer(final Player player, final Point p) {
        player.setMap(this);
        player.setPosition(p.x, p.y);
        this.playerMap.put(player.getID(), player);
        player.setWorldMapID(this.id);
        if (this.getMapType() != 2) {
            player.setWorldLine(this.line);
        }
    }
    
    public Player removePlayer(final Player player) {
        return this.playerMap.remove(player.getID());
    }
    
    public void addPet(final Pet pet) {
        pet.setMap(this);
        pet.setPosition(pet.rndPosition(this, pet.getOwner().getX(), pet.getOwner().getY()));
        this.petMap.put(pet.getID(), pet);
    }
    
    public void removePet(final Pet pet) {
        this.petMap.remove(pet.getID());
        pet.setMap(null);
    }
    
    public void addPanda(final Panda panda) {
        panda.setMap(this);
        panda.setPosition(panda.rndPosition(this, panda.getOwner().getX(), panda.getOwner().getY()));
        this.pandaMap.put(panda.getID(), panda);
    }
    
    public void removePanda(final Panda panda) {
        this.pandaMap.remove(panda.getID());
        panda.setMap(null);
    }
    
    public Pet getPet(final long id) {
        return this.petMap.get(id);
    }
    
    public Panda getPanda(final long id) {
        return this.pandaMap.get(id);
    }
    
    public Player getPlayer(final long id) {
        return this.playerMap.get(id);
    }
    
    public ConcurrentHashMap<Long, Player> getPlayerMap() {
        return this.playerMap;
    }
    
    public TransPoint getTransPoint(final long id) {
        return this.transPointMap.get(id);
    }
    
    public void addTranspoint(final TransPoint tp) {
        this.transPointMap.put(tp.getID(), tp);
    }
    
    public TransPoint removeTranspoint(final long id) {
        return this.transPointMap.remove(id);
    }
    
    public Monster removeMonster(final Monster monster) {
        return this.monsterMap.remove(monster.getID());
    }
    
    public ConcurrentHashMap<Long, Monster> getMonsterMap() {
        return this.monsterMap;
    }
    
    public int getMonsterSize() {
        return this.monsterMap.size();
    }
    
    public DropItem removeDropItem(final DropItem item) {
        return this.dropItemMap.remove(item.getID());
    }
    
    public Monster getMonster(final long id) {
        return this.monsterMap.get(id);
    }
    
    public Npc removeNpc(final Npc npc) {
        return this.npcMap.remove(npc.getID());
    }
    
    public final void removeUnit(final MapUnit unit) {
        switch (unit.getUnitType()) {
            case 1: {
                this.doRemovePlayerForDestroy((Player)unit);
                break;
            }
            case 2: {
                this.removeMonster((Monster)unit);
                break;
            }
            case 3: {
                this.removeNpc((Npc)unit);
                break;
            }
            case 5: {
                this.removeDropItem((DropItem)unit);
                break;
            }
            case 9: {
                this.removePlayer((Player)unit);
                break;
            }
            case 7: {
                this.removeMaterial(unit.getID());
                break;
            }
        }
    }
    
    public void doRemovePlayerForDestroy(final Player player) {
        player.getAggroList().clear();
        if (this.leaveMapActions != null) {
            final Enumeration<DelayAction> en = this.leaveMapActions.keys();
            while (en.hasMoreElements()) {
                en.nextElement().doAction(player);
            }
        }
        this.removePlayer(player);
        List<Unit> leaveList = new ArrayList<Unit>();
        leaveList.add(player);
        final Panda panda = player.getPanda();
        if (panda != null) {
            leaveList.add(panda);
            this.removePanda(panda);
        }
        final PlayerPetManager pm = player.getPetManager();
        if (pm != null) {
            final Pet pet = pm.getActivePet();
            if (pet != null) {
                leaveList.add(pet);
                this.removePet(pet);
                pet.getAggroList().clear();
            }
        }
        RemoveUnit leaveMap = new RemoveUnit(leaveList);
        this.sendPacketToAroundPlayer(leaveMap, player, false);
        leaveMap.destroy();
        leaveMap = null;
        leaveList.clear();
        leaveList = null;
    }
    
    public void startWork() {
        this.workFuture = ThreadFixedPoolManager.POOL_MAPWORK.scheduleTask(new Runnable() {
            @Override
            public void run() {
                Map.this.doWork(System.currentTimeMillis());
            }
        }, Rnd.get(20, 60), 20L);
    }
    
    protected void doWork(final long now) {
        if (this.isShouldDestory()) {
            this.destroy();
            return;
        }
        ++this.workTimes;
        final boolean isDoWork = this.workTimes % 3L == 0L;
        final boolean isDoDrop = this.workTimes % 500L == 0L;
        for (final Player p : this.playerMap.values()) {
            if (p.isShouldDestroy()) {
                p.destroy();
            }
            else {
                p.doPacket();
                if (!isDoWork) {
                    continue;
                }
                try {
                    p.doWork(this, now);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        final int playerSize = this.getPlayerSize();
        if (isDoWork) {
            if (this.line == 0 || playerSize > 0) {
                for (final Monster m : this.monsterMap.values()) {
                    try {
                        if (playerSize == 0) {
                            if (m.getBossRank() == 0) {
                                continue;
                            }
                            m.doWork(this, now);
                        }
                        else {
                            m.doWork(this, now);
                        }
                    }
                    catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
            for (final Pet pet : this.petMap.values()) {
                try {
                    pet.doWork(this, now);
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            for (final Panda panda : this.pandaMap.values()) {
                try {
                    panda.doWork(this, now);
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            for (final Npc n : this.npcMap.values()) {
                if (!n.isDynamic()) {
                    continue;
                }
                try {
                    n.doWork(this, now);
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            for (final Material i : this.materialMap.values()) {
                try {
                    i.doWork(this, now);
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        if (isDoDrop) {
            for (final DropItem item : this.dropItemMap.values()) {
                try {
                    item.doWork(this, now);
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
    
    public void sendPacketToAroundPlayer(final WriteOnlyPacket packet, final Point center) {
        final Rectangle area = this.getArea(center);
        if (area == null) {
            Map.logger.error("map id = {} center is {}", (Object)this.getID(), (Object)(String.valueOf(center.x) + center.y));
            return;
        }
        for (final Player player : this.playerMap.values()) {
            if (player.isDestroy()) {
                continue;
            }
            final Point p = player.getPosition();
            if (!area.contains(p) || !player.isEnterMap()) {
                continue;
            }
            player.writePacket(packet);
        }
    }
    
    public ArrayList<Player> getAroundPlayers(final Point point) {
        final ArrayList<Player> plist = new ArrayList<Player>();
        final Rectangle area = this.getArea(point);
        if (area != null) {
            for (final Player player : this.playerMap.values()) {
                final Point p = player.getPosition();
                if (!player.isDestroy() && area.contains(p) && player.isEnterMap() && !player.isRobot()) {
                    plist.add(player);
                }
            }
        }
        else {
            Map.logger.error("map id = {} point is {}", (Object)this.getID(), (Object)(String.valueOf(point.x) + "\t" + point.y));
        }
        return plist;
    }
    
    public ArrayList<Material> getAroundMaterialByTemplateId(final Point point, final int templateId) {
        final ArrayList<Material> mlist = new ArrayList<Material>();
        final Rectangle area = this.getArea(point);
        for (final Material m : this.materialMap.values()) {
            if (!area.contains(m.getPosition())) {
                continue;
            }
            if (m.getTemplateID() != templateId) {
                continue;
            }
            mlist.add(m);
        }
        return mlist;
    }
    
    public void sendPacketToAroundPlayer(final WriteOnlyPacket packet, final Player p, final boolean includeSelf) {
        final Rectangle area = this.getArea(p.getPosition());
        if (area == null) {
            Map.logger.error("wrong position map is {},position is {},{}", new Object[] { this.getID(), p.getPosition().x, p.getPosition().y });
            return;
        }
        for (final Player player : this.playerMap.values()) {
            if (player.isDestroy()) {
                continue;
            }
            if (!includeSelf && player.getID() == p.getID()) {
                continue;
            }
            if (!area.contains(player.getPosition()) || !player.isEnterMap()) {
                continue;
            }
            player.writePacket(packet);
        }
    }
    
    public void playerSwitchArea(final Player player, final Rectangle newAreas, final Rectangle oldArea) {
        final ListPacket listPacket = ListPacket.forClient();
        this.playerEnterNewAreas(player, newAreas, oldArea, listPacket);
        this.aroundMonsters(player, newAreas, oldArea, listPacket);
        this.aroundNpcs(player, newAreas, oldArea, listPacket);
        this.aroundMaterial(player, newAreas, oldArea, listPacket);
        this.aroundPet(player, newAreas, oldArea, listPacket);
        this.aroundPanda(player, newAreas, oldArea, listPacket);
        this.aroundDropItem(player, newAreas, oldArea, listPacket);
        player.writePacket(listPacket);
    }
    
    private void aroundMonsters(final Player player, final Rectangle newAreas, final Rectangle oldArea, final ListPacket listPacket) {
        try {
            ArrayList<Monster> newMonsters = new ArrayList<Monster>();
            ArrayList<Monster> newMovingMonsters = new ArrayList<Monster>();
            ArrayList<Monster> delMonsters = new ArrayList<Monster>();
            for (final Monster m : this.monsterMap.values()) {
                if (!m.isDestroy()) {
                    if (m.isDie()) {
                        continue;
                    }
                    final boolean inNewArea = newAreas.contains(m.getPosition());
                    final boolean inOldArea = oldArea != null && oldArea.contains(m.getPosition());
                    if (inNewArea && !inOldArea) {
                        newMonsters.add(m);
                        if (!m.isMoving()) {
                            continue;
                        }
                        newMovingMonsters.add(m);
                    }
                    else {
                        if (!inOldArea || inNewArea) {
                            continue;
                        }
                        delMonsters.add(m);
                    }
                }
            }
            if (!newMonsters.isEmpty()) {
                AroundMonster am = new AroundMonster(newMonsters);
                listPacket.addPacket(am);
                am.destroy();
                am = null;
            }
            for (final Monster i : newMovingMonsters) {
                Point[] points = i.getMovePath();
                if (points != null) {
                    UnitMove mm = new UnitMove(i, points);
                    listPacket.addPacket(mm);
                    points = null;
                    mm.destroy();
                    mm = null;
                }
            }
            if (!delMonsters.isEmpty()) {
                RemoveUnit rn = new RemoveUnit(delMonsters);
                listPacket.addPacket(rn);
                rn.destroy();
                rn = null;
            }
            newMonsters.clear();
            newMonsters = null;
            newMovingMonsters.clear();
            newMovingMonsters = null;
            delMonsters.clear();
            delMonsters = null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void aroundPet(final Player player, final Rectangle newAreas, final Rectangle oldArea, final ListPacket listPacket) {
        try {
            ArrayList<Pet> newPets = new ArrayList<Pet>();
            ArrayList<Pet> newMovingPets = new ArrayList<Pet>();
            ArrayList<Pet> delPets = new ArrayList<Pet>();
            List<Creature> pkViews = new ArrayList<Creature>();
            for (final Pet pet : this.petMap.values()) {
                final boolean inNewArea = newAreas.contains(pet.getPosition());
                final boolean inOldArea = oldArea != null && oldArea.contains(pet.getPosition());
                if (!inNewArea && !inOldArea) {
                    continue;
                }
                if (inNewArea && !inOldArea) {
                    if (pet.isDestroy() || pet.isDie() || pet.getOwner() == null || !pet.getOwner().isEnterMap() || pet.getOwner().getMap() == null) {
                        continue;
                    }
                    if (!pet.getOwner().getMap().equals(this)) {
                        continue;
                    }
                    newPets.add(pet);
                    final Player p = pet.getOwner();
                    final boolean canPkView = ChangePkView.canSeeKillView(player, p);
                    if (canPkView) {
                        pkViews.add(pet);
                    }
                    if (!pet.isMoving()) {
                        continue;
                    }
                    newMovingPets.add(pet);
                }
                else {
                    if (!inOldArea || inNewArea) {
                        continue;
                    }
                    delPets.add(pet);
                }
            }
            if (!newPets.isEmpty()) {
                AroundPet am = new AroundPet(player, newPets);
                listPacket.addPacket(am);
                am.destroy();
                am = null;
            }
            for (final Pet m : newMovingPets) {
                Point[] points = m.getMovePath();
                if (points != null) {
                    UnitMove mm = new UnitMove(m, points);
                    listPacket.addPacket(mm);
                    points = null;
                    mm.destroy();
                    mm = null;
                }
            }
            if (pkViews.size() > 0) {
                ChangePkView cpv = new ChangePkView(player, pkViews);
                listPacket.addPacket(cpv);
                cpv.destroy();
                cpv = null;
            }
            pkViews.clear();
            pkViews = null;
            if (!delPets.isEmpty()) {
                RemoveUnit rn = new RemoveUnit(delPets);
                listPacket.addPacket(rn);
                rn.destroy();
                rn = null;
            }
            newPets.clear();
            newPets = null;
            newMovingPets.clear();
            newMovingPets = null;
            delPets.clear();
            delPets = null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void aroundPanda(final Player player, final Rectangle newAreas, final Rectangle oldArea, final ListPacket listPacket) {
        try {
            ArrayList<Panda> newPandas = new ArrayList<Panda>();
            ArrayList<Panda> newMovingPandas = new ArrayList<Panda>();
            ArrayList<Panda> delPandas = new ArrayList<Panda>();
            for (final Panda panda : this.pandaMap.values()) {
                final boolean inNewArea = newAreas.contains(panda.getPosition());
                final boolean inOldArea = oldArea != null && oldArea.contains(panda.getPosition());
                if (inNewArea && !inOldArea) {
                    if (!panda.getOwner().isEnterMap()) {
                        continue;
                    }
                    if (panda.isDestroy()) {
                        continue;
                    }
                    newPandas.add(panda);
                    if (!panda.isMoving()) {
                        continue;
                    }
                    newMovingPandas.add(panda);
                }
                else {
                    if (!inOldArea || inNewArea) {
                        continue;
                    }
                    delPandas.add(panda);
                }
            }
            if (!newPandas.isEmpty()) {
                AroundPanda am = new AroundPanda(player, newPandas);
                listPacket.addPacket(am);
                am.destroy();
                am = null;
            }
            for (final Panda m : newMovingPandas) {
                Point[] points = m.getMovePath();
                if (points != null) {
                    UnitMove mm = new UnitMove(m, points);
                    listPacket.addPacket(mm);
                    points = null;
                    mm.destroy();
                    mm = null;
                }
            }
            if (!delPandas.isEmpty()) {
                RemoveUnit rn = new RemoveUnit(delPandas);
                listPacket.addPacket(rn);
                rn.destroy();
                rn = null;
            }
            newPandas.clear();
            newPandas = null;
            newMovingPandas.clear();
            newMovingPandas = null;
            delPandas.clear();
            delPandas = null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void aroundNpcs(final Player player, final Rectangle newArea, final Rectangle oldArea, final ListPacket listPacket) {
        try {
            ArrayList<Npc> newNpcs = new ArrayList<Npc>();
            ArrayList<Npc> delNpcs = new ArrayList<Npc>();
            ArrayList<Npc> newMovingNpcs = new ArrayList<Npc>();
            for (final Npc npc : this.npcMap.values()) {
                final boolean inNewArea = newArea.contains(npc.getPosition());
                final boolean inOldArea = oldArea != null && oldArea.contains(npc.getPosition());
                if (inNewArea && !inOldArea) {
                    if (npc.isDestroy() || npc.isDie()) {
                        continue;
                    }
                    newNpcs.add(npc);
                    if (!npc.isMoving()) {
                        continue;
                    }
                    newMovingNpcs.add(npc);
                }
                else {
                    if (!inOldArea || inNewArea) {
                        continue;
                    }
                    delNpcs.add(npc);
                }
            }
            if (newNpcs.size() > 0) {
                AroundNpc an = new AroundNpc(newNpcs);
                listPacket.addPacket(an);
                an.destroy();
                an = null;
                UpdateNpcHeadIcon ui = UpdateNpcHeadIcon.writeNpcHeadIcon(player, newNpcs);
                if (ui != null) {
                    listPacket.addPacket(ui);
                    ui.destroy();
                    ui = null;
                }
            }
            if (delNpcs.size() > 0) {
                RemoveUnit ru = new RemoveUnit(delNpcs);
                listPacket.addPacket(ru);
                ru.destroy();
                ru = null;
            }
            if (newMovingNpcs.size() > 0) {
                for (final Npc npc2 : newMovingNpcs) {
                    Point[] points = npc2.getMovePath();
                    if (points != null) {
                        UnitMove um = new UnitMove(npc2, points);
                        listPacket.addPacket(um);
                        points = null;
                        um.destroy();
                        um = null;
                    }
                }
            }
            newNpcs.clear();
            newNpcs = null;
            delNpcs.clear();
            delNpcs = null;
            newMovingNpcs.clear();
            newMovingNpcs = null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void aroundMaterial(final Player player, final Rectangle newArea, final Rectangle oldArea, final ListPacket listPacket) {
        try {
            ArrayList<Material> newMaterial = new ArrayList<Material>();
            ArrayList<Material> delMaterial = new ArrayList<Material>();
            for (final Material m : this.materialMap.values()) {
                final boolean inNewArea = newArea.contains(m.getPosition());
                final boolean inOldArea = oldArea != null && oldArea.contains(m.getPosition());
                if (inNewArea && !inOldArea) {
                    if (m.isDestroy() || m.isDisappear()) {
                        continue;
                    }
                    newMaterial.add(m);
                }
                else {
                    if (!inOldArea || inNewArea) {
                        continue;
                    }
                    delMaterial.add(m);
                }
            }
            if (newMaterial.size() > 0) {
                AroundMaterial am = new AroundMaterial(newMaterial, player);
                listPacket.addPacket(am);
                am.destroy();
                am = null;
            }
            if (delMaterial.size() > 0) {
                RemoveUnit ru = new RemoveUnit(delMaterial);
                listPacket.addPacket(ru);
                ru.destroy();
                ru = null;
            }
            newMaterial.clear();
            newMaterial = null;
            delMaterial.clear();
            delMaterial = null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void aroundDropItem(final Player player, final Rectangle newArea, final Rectangle oldArea, final ListPacket listPacket) {
        try {
            List<DropItem> newDrops = new ArrayList<DropItem>();
            List<DropItem> delDrops = new ArrayList<DropItem>();
            for (final DropItem m : this.dropItemMap.values()) {
                final boolean inNewArea = newArea.contains(m.getPosition());
                final boolean inOldArea = oldArea != null && oldArea.contains(m.getPosition());
                if (inNewArea && !inOldArea) {
                    if (m.isDestroy()) {
                        continue;
                    }
                    newDrops.add(m);
                }
                else {
                    if (!inOldArea || inNewArea) {
                        continue;
                    }
                    delDrops.add(m);
                }
            }
            if (newDrops.size() > 0) {
                AroundDropItem ad = new AroundDropItem(newDrops, player);
                listPacket.addPacket(ad);
                ad.destroy();
                ad = null;
            }
            if (delDrops.size() > 0) {
                RemoveUnit ru = new RemoveUnit(delDrops);
                listPacket.addPacket(ru);
                ru.destroy();
                ru = null;
            }
            newDrops.clear();
            newDrops = null;
            delDrops.clear();
            delDrops = null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void playerEnterNewAreas(final Player player, final Rectangle newAreas, final Rectangle oldArea, final ListPacket listPacket) {
        try {
            ArrayList<Player> newPlayers = new ArrayList<Player>();
            ArrayList<Player> newMovingPlayers = new ArrayList<Player>();
            ArrayList<Player> delPlayers = new ArrayList<Player>();
            UnitMove selfMove = null;
            if (player.isMoving() || player.isSprint()) {
                final Point[] movePath = player.getMovePath();
                if (movePath != null) {
                    selfMove = new UnitMove(player, movePath);
                }
            }
            ChangePkView pkView = new ChangePkView(player, true);
            RemoveUnit rmSelf = new RemoveUnit(new Unit[] { player });
            for (final Player p : this.playerMap.values()) {
                if (p.isDestroy()) {
                    continue;
                }
                final boolean inNewArea = newAreas.contains(p.getPosition());
                final boolean inOldArea = oldArea != null && oldArea.contains(p.getPosition());
                if (inNewArea && !inOldArea) {
                    if (!p.isEnterMap()) {
                        continue;
                    }
                    if (p.getID() == player.getID()) {
                        continue;
                    }
                    AroundPlayer selfAp = new AroundPlayer(player, p);
                    p.writePacket(selfAp);
                    selfAp.destroy();
                    selfAp = null;
                    final boolean canPkView = ChangePkView.canSeeKillView(p, player);
                    if (canPkView) {
                        p.writePacket(pkView);
                    }
                    if (selfMove != null) {
                        p.writePacket(selfMove);
                    }
                    newPlayers.add(p);
                    if (!p.isMoving() && !p.isSprint()) {
                        continue;
                    }
                    newMovingPlayers.add(p);
                }
                else {
                    if (!inOldArea || inNewArea) {
                        continue;
                    }
                    delPlayers.add(p);
                    p.writePacket(rmSelf);
                }
            }
            pkView.destroy();
            pkView = null;
            rmSelf.destroy();
            rmSelf = null;
            if (newPlayers.size() > 0) {
                AroundPlayer otherPlayers = new AroundPlayer(newPlayers, player);
                listPacket.addPacket(otherPlayers);
                otherPlayers.destroy();
                otherPlayers = null;
                List<Creature> ncs = new ArrayList<Creature>();
                ncs.addAll(newPlayers);
                ChangePkView pvs = new ChangePkView(player, ncs);
                listPacket.addPacket(pvs);
                pvs.destroy();
                pvs = null;
                ncs.clear();
                ncs = null;
            }
            if (newMovingPlayers.size() > 0) {
                for (final Player p2 : newMovingPlayers) {
                    Point[] points = p2.getMovePath();
                    if (points == null) {
                        continue;
                    }
                    UnitMove ms = new UnitMove(p2, points);
                    listPacket.addPacket(ms);
                    points = null;
                    ms.destroy();
                    ms = null;
                }
            }
            if (delPlayers.size() > 0) {
                RemoveUnit rm = new RemoveUnit(delPlayers);
                listPacket.addPacket(rm);
                rm.destroy();
                rm = null;
            }
            newPlayers.clear();
            newPlayers = null;
            newMovingPlayers.clear();
            newMovingPlayers = null;
            delPlayers.clear();
            delPlayers = null;
            if (selfMove != null) {
                selfMove.destroy();
                selfMove = null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void playerAppear(final Player sawer, final Player appear, final AroundPlayer ap, final ChangePkView pkView) {
        sawer.writePacket(ap);
        final boolean canPkView = ChangePkView.canSeeKillView(sawer, appear);
        if (canPkView) {
            sawer.writePacket(pkView);
        }
    }
    
    public void addLeaveMapAction(final DelayAction action) {
        if (this.leaveMapActions == null) {
            this.leaveMapActions = new ConcurrentHashMap<DelayAction, Boolean>(8, 0.75f, 2);
        }
        this.leaveMapActions.put(action, true);
    }
    
    public void removeLeaveMapAction(final DelayAction action) {
        if (this.leaveMapActions != null) {
            this.leaveMapActions.remove(action);
        }
    }
    
    public boolean leaveMap(final Player player) {
        if (!this.playerMap.containsKey(player.getID())) {
            return false;
        }
        if (this.leaveMapActions != null) {
            final Enumeration<DelayAction> en = this.leaveMapActions.keys();
            while (en.hasMoreElements()) {
                en.nextElement().doAction(player);
            }
        }
        RemoveUnit leaveMap = null;
        player.setEnterMap(false);
        this.removePlayer(player);
        final Pet pet = player.getPetManager().getActivePet();
        final Panda panda = player.getPanda();
        List<Unit> leaveList = new ArrayList<Unit>();
        leaveList.add(player);
        if (panda != null) {
            panda.setLastMoneyId(0L);
            this.removePanda(panda);
            panda.forceIdle();
            leaveList.add(panda);
        }
        if (pet != null) {
            pet.setLastAttackTarget(null);
            this.removePet(pet);
            pet.forceIdle();
            leaveList.add(pet);
            pet.getAggroList().clear();
        }
        leaveMap = new RemoveUnit(leaveList);
        leaveList.clear();
        leaveList = null;
        this.sendPacketToAroundPlayer(leaveMap, player, false);
        leaveMap.destroy();
        leaveMap = null;
        player.getAggroList().clear();
        return true;
    }
    
    public boolean switchMap(final Player player, final Map targetMap, final Point position) {
        if (!targetMap.isValidPoint(position)) {
            SystemMessage.writeMessage(player, 1033);
            return false;
        }
        final MapData data = MapConfig.getMapData(targetMap.getID());
        if (!data.canEnter(player, true)) {
            return false;
        }
        if (player.isDie()) {
            PlayerRevival.revival(player);
        }
        player.idle();
        if (!this.leaveMap(player)) {
            return false;
        }
        if (this.getMapType() == 1 && targetMap.getMapType() == 2) {
            player.setWorldPoint(player.getX(), player.getY());
        }
        targetMap.addPlayer(player, position);
        SwitchLine.pushCurrentLine(player);
        if (player.getBuffManager() != null) {
            player.getBuffManager().createAndStartBuff(player, 80004, 1, true, 0L, null);
        }
        TransferMap.playerSwitchMapDefault(player, targetMap.getID(), position.x, position.y);
        return true;
    }
    
    public void playerEnterMapSuccess(final Player player) {
        this.playerSwitchArea(player, player.getArea(), null);
        this.noticeTransPoint(player);
    }
    
    public void petEnterMapSuccess(final Pet pet) {
        UnitMove mm = null;
        if (pet.isMoving()) {
            final Point[] movePath = pet.getMovePath();
            if (movePath != null) {
                mm = new UnitMove(pet, movePath);
            }
        }
        final Rectangle area = pet.getArea();
        ChangePkView pkView = new ChangePkView(pet, true);
        for (final Player p : this.playerMap.values()) {
            if (!p.isDestroy() && area.contains(p.getPosition())) {
                if (!p.isEnterMap()) {
                    continue;
                }
                WriteOnlyPacket aroundself = pet.createAroundSelfPacket(p);
                p.writePacket(aroundself);
                aroundself.destroy();
                aroundself = null;
                final boolean canPkView = ChangePkView.canSeeKillView(p, pet.getOwner());
                if (canPkView) {
                    p.writePacket(pkView);
                }
                if (mm == null) {
                    continue;
                }
                p.writePacket(mm);
            }
        }
        pkView.destroy();
        pkView = null;
        if (mm != null) {
            mm.destroy();
            mm = null;
        }
    }
    
    public void pandaEnterMapSuccess(final Panda panda) {
        UnitMove mm = null;
        if (panda.isMoving()) {
            final Point[] movePath = panda.getMovePath();
            if (movePath != null) {
                mm = new UnitMove(panda, movePath);
            }
        }
        final Rectangle area = panda.getArea();
        WriteOnlyPacket aroundself = panda.createAroundSelfPacket(panda.getOwner());
        for (final Player p : this.playerMap.values()) {
            if (!p.isDestroy() && area.contains(p.getPosition())) {
                if (!p.isEnterMap()) {
                    continue;
                }
                p.writePacket(aroundself);
                if (mm == null) {
                    continue;
                }
                p.writePacket(mm);
            }
        }
        if (mm != null) {
            mm.destroy();
            mm = null;
        }
        aroundself.destroy();
        aroundself = null;
    }
    
    private void noticeTransPoint(final Player player) {
        try {
            if (this.transPointMap.size() > 0) {
                AroundTransPoint ap = new AroundTransPoint(this.transPointMap);
                player.writePacket(ap);
                ap.destroy();
                ap = null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean switchMap(final Player player, final int targetMapID, final Point position) {
        final LineMap lm = MapConfig.getLineMap(targetMapID);
        if (lm == null) {
            Map.logger.error("target Map not found id = " + targetMapID);
            return false;
        }
        Map tagetMap = null;
        if (player.getLevel() < 120) {
            tagetMap = lm.getEarlyMap();
        }
        else {
            tagetMap = lm.getMapByLine(player.getMap().getLine());
        }
        if (tagetMap == null) {
            tagetMap = lm.getEarlyMap();
        }
        return this.switchMap(player, tagetMap, position);
    }
    
    public final void setLine(final int line) {
        this.line = line;
    }
    
    public final int getLine() {
        return this.line;
    }
    
    public final int getID() {
        return this.id;
    }
    
    public int getPlayerSize() {
        return this.playerMap.size();
    }
    
    public int getMapType() {
        return 1;
    }
    
    public byte[][] getBlocks() {
        return this.blocks;
    }
    
    public int getTileHorizontalNumber() {
        return this.tileHorizontalNumber;
    }
    
    public void setTileHorizontalNumber(final int tn) {
        this.tileHorizontalNumber = tn;
    }
    
    public int getTileVerticalNumber() {
        return this.tileVerticalNumber;
    }
    
    public void setTileVerticalNumber(final int tn) {
        this.tileVerticalNumber = tn;
    }
    
    public void setMusic(final int music) {
        this.music = music;
    }
    
    public int getMusic() {
        return this.music;
    }
    
    public boolean isShouldDestory() {
        return this.shouldDestory;
    }
    
    public void setShouldDestory(final boolean shouldDestory) {
        this.shouldDestory = shouldDestory;
    }
    
    public void addEnterMapAction(final DelayAction action) {
        if (this.enterMapActions == null) {
            this.enterMapActions = Tools.newConcurrentHashMap2();
        }
        this.enterMapActions.put(action, true);
    }
    
    public void removeEnterMapAction(final DelayAction action) {
        if (this.enterMapActions != null) {
            this.enterMapActions.remove(action);
        }
    }
    
    public void doEnterMapSpecil(final Player player) {
        if (this.enterMapActions != null) {
            final Enumeration<DelayAction> en = this.enterMapActions.keys();
            while (en.hasMoreElements()) {
                en.nextElement().doAction(player);
            }
        }
        if (this.getUnitLandscape(player) == 2) {
            this.unitLandscapeChanged(2, player);
        }
    }
    
    public Point searchFeasiblePoint(int x, int y) {
        final int indexX = this.getTileX(x);
        final int indexY = this.getTileY(y);
        if (!this.tileIsBlocked(indexX, indexY)) {
            return new Point(x, y);
        }
        x = Math.max(0, Math.min(this.tileHorizontalNumber - 1, indexX));
        y = Math.max(0, Math.min(this.tileVerticalNumber - 1, indexY));
        final int maxInterval = Math.max(Math.max(y, this.tileVerticalNumber - 1 - y), Math.max(x, this.tileHorizontalNumber - 1 - x));
        boolean flag1 = true;
        boolean flag2 = true;
        boolean flag3 = true;
        boolean flag4 = true;
        int x2 = x;
        int x3 = x;
        int y2 = y;
        int y3 = y;
        for (int i = 1; i < maxInterval; i += 2) {
            x2 = Math.max(0, x - i);
            x3 = Math.min(this.tileHorizontalNumber - 1, x + i);
            y2 = Math.max(0, y - i);
            y3 = Math.min(this.tileVerticalNumber - 1, y + i);
            for (int j = x2; j <= x3 && flag1; ++j) {
                if (!this.tileIsBlocked(j, y2)) {
                    return new Point(this.getXByTile(j), this.getYByTile(y2));
                }
            }
            for (int j = y2; j <= y3 && flag2; ++j) {
                if (!this.tileIsBlocked(x3, j)) {
                    return new Point(this.getXByTile(x3), this.getYByTile(j));
                }
            }
            for (int j = x3; j >= x2 && flag3; --j) {
                if (!this.tileIsBlocked(j, y3)) {
                    return new Point(this.getXByTile(j), this.getYByTile(y3));
                }
            }
            for (int j = y3; j >= y2 && flag4; --j) {
                if (!this.tileIsBlocked(x2, j)) {
                    return new Point(this.getXByTile(x2), this.getYByTile(j));
                }
            }
            flag1 = (y2 != 0);
            flag2 = (x3 != this.tileHorizontalNumber - 1);
            flag3 = (y3 != this.tileVerticalNumber - 1);
            flag4 = (x2 != 0);
        }
        return null;
    }
    
    public boolean tileIsBlocked(final int tileX, final int tileY) {
        return tileX < 0 || tileY < 0 || tileX >= this.tileHorizontalNumber || tileY >= this.tileVerticalNumber || this.getBlocks()[tileX][tileY] == 1;
    }
    
    public int getTileX(final int x) {
        return (x - this.left) / 600;
    }
    
    public int getTileY(final int y) {
        return (this.top - y) / 600;
    }
    
    public boolean isBlocked(final int x, final int y) {
        return this.tileIsBlocked(this.getTileX(x), this.getTileY(y));
    }
    
    public Point searchDropPoint(int x, int y, final HashSet<String> dString, final int fixedIndex) {
        final int indexX = this.getTileX(x);
        final int indexY = this.getTileY(y);
        x = Math.max(0, Math.min(this.tileHorizontalNumber - 1, indexX));
        y = Math.max(0, Math.min(this.tileVerticalNumber - 1, indexY));
        int maxInterval = Math.max(Math.max(y, this.tileVerticalNumber - 1 - y), Math.max(x, this.tileHorizontalNumber - 1 - x));
        maxInterval = Math.min(11, maxInterval);
        boolean flag1 = true;
        boolean flag2 = true;
        boolean flag3 = true;
        boolean flag4 = true;
        int x2 = x;
        int x3 = x;
        int y2 = y;
        int y3 = y;
        int count = 0;
        for (int i = 1; i < maxInterval; i += 2) {
            x2 = Math.max(0, x - i);
            x3 = Math.min(this.tileHorizontalNumber - 1, x + i);
            y2 = Math.max(0, y - i);
            y3 = Math.min(this.tileVerticalNumber - 1, y + i);
            for (int j = x2; j <= x3 && flag1; j += 2) {
                if (j != indexX || y2 != indexY) {
                    if (!this.tileIsBlocked(j, y2)) {
                        final boolean flag5 = this.canDrop(j, y2, fixedIndex, count, dString);
                        if (flag5) {
                            return new Point(this.getXByTile(j), this.getYByTile(y2));
                        }
                        ++count;
                    }
                }
            }
            for (int j = y2; j <= y3 && flag2; j += 2) {
                if (x3 != indexX || j != indexY) {
                    if (!this.tileIsBlocked(x3, j)) {
                        final boolean flag5 = this.canDrop(x3, j, fixedIndex, count, dString);
                        if (flag5) {
                            return new Point(this.getXByTile(x3), this.getYByTile(j));
                        }
                        ++count;
                    }
                }
            }
            for (int j = x3; j >= x2 && flag3; j -= 2) {
                if (j != indexX || y3 != indexY) {
                    if (!this.tileIsBlocked(j, y3)) {
                        final boolean flag5 = this.canDrop(j, y3, fixedIndex, count, dString);
                        if (flag5) {
                            return new Point(this.getXByTile(j), this.getYByTile(y3));
                        }
                        ++count;
                    }
                }
            }
            for (int j = y3; j >= y2 && flag4; j -= 2) {
                if (x2 != indexX || j != indexY) {
                    if (!this.tileIsBlocked(x2, j)) {
                        final boolean flag5 = this.canDrop(x2, j, fixedIndex, count, dString);
                        if (flag5) {
                            return new Point(this.getXByTile(x2), this.getYByTile(j));
                        }
                        ++count;
                    }
                }
            }
            flag1 = (y2 != 0);
            flag2 = (x3 != this.tileHorizontalNumber - 1);
            flag3 = (y3 != this.tileVerticalNumber - 1);
            flag4 = (x2 != 0);
        }
        return null;
    }
    
    private boolean canDrop(final int x, final int y, final int fixedCount, final int count, final HashSet<String> dString) {
        final boolean flag = true;
        if (fixedCount != -1) {
            if (fixedCount == count) {
                return true;
            }
        }
        else {
            final String ds = String.valueOf(x) + "_" + y;
            if (dString.contains(ds)) {
                return false;
            }
        }
        return flag;
    }
    
    public List<Monster> getCanAttackMonster(final Creature attacker, final Shape arc, final Point p) {
        final List<Monster> monsters = new ArrayList<Monster>();
        Player player = null;
        switch (attacker.getUnitType()) {
            case 1: {
                player = (Player)attacker;
                break;
            }
            case 4: {
                player = ((Pet)attacker).getOwner();
                break;
            }
        }
        for (final Monster monster : this.monsterMap.values()) {
            if (!monster.isDie() && !monster.isDestroy()) {
                if (!monster.isCanBeAttacked()) {
                    continue;
                }
                if (player != null && !monster.canSee(player)) {
                    continue;
                }
                if (!arc.contains(monster.getActualPosition())) {
                    continue;
                }
                monsters.add(monster);
            }
        }
        return monsters;
    }
    
    public int getUnitLandscape(final MapUnit unit) {
        final HashMap<Shape, Integer> map = MapConfig.getMapData(this.id).getDiffrentLandscape();
        if (map != null) {
            for (final java.util.Map.Entry<Shape, Integer> entry : map.entrySet()) {
                final Shape shape = entry.getKey();
                if (shape.contains(unit.getPosition())) {
                    return entry.getValue();
                }
            }
        }
        return this.landscape;
    }
    
    public List<Player> getPlayerByRange(final Creature creature, final Shape shape, final Point center, final boolean includeSelf, final boolean includeDie) {
        final ArrayList<Player> plist = new ArrayList<Player>();
        long roleID = 0L;
        switch (creature.getUnitType()) {
            case 1: {
                roleID = creature.getID();
                break;
            }
            case 4: {
                roleID = ((Pet)creature).getOwner().getID();
                break;
            }
        }
        for (final Player p : this.playerMap.values()) {
            if (!p.isDestroy()) {
                if (!p.isEnterMap()) {
                    continue;
                }
                if (!includeSelf && p.getID() == roleID) {
                    continue;
                }
                if (!includeDie && p.isDie()) {
                    continue;
                }
                if (!shape.contains(p.getActualPosition())) {
                    continue;
                }
                plist.add(p);
            }
        }
        return plist;
    }
    
    public List<DropItem> getDropItemByRange(final Player player, final int range, final boolean onlyMoney) {
        final List<DropItem> itemList = new ArrayList<DropItem>();
        final Point center = player.getActualPosition();
        final Shape shape = ShapeUtil.createRoundShape(center, range);
        for (final DropItem dropItem : this.dropItemMap.values()) {
            if (dropItem.isDestroy()) {
                continue;
            }
            if (onlyMoney && !dropItem.getItem().getModel().isMoney()) {
                continue;
            }
            if (dropItem.getRemainProtectedForShow(player.getID()) > 0) {
                continue;
            }
            if (!shape.contains(dropItem.getActualPosition())) {
                continue;
            }
            itemList.add(dropItem);
        }
        return itemList;
    }
    
    public void destroy() {
        if (this.workFuture != null) {
            this.workFuture.cancel(true);
            this.workFuture = null;
        }
        final Iterator<Monster> it = this.monsterMap.values().iterator();
        while (it.hasNext()) {
            final Monster monster = it.next();
            it.remove();
            try {
                monster.destroy();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        final Iterator<Npc> it2 = this.npcMap.values().iterator();
        while (it2.hasNext()) {
            final Npc npc = it2.next();
            it2.remove();
            try {
                npc.destroy();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (final TransPoint tp : this.transPointMap.values()) {
            try {
                tp.destroy();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.transPointMap.clear();
        if (this.enterMapActions != null) {
            this.enterMapActions.clear();
            this.enterMapActions = null;
        }
        if (this.leaveMapActions != null) {
            this.leaveMapActions.clear();
            this.leaveMapActions = null;
        }
    }
    
    public void unitLandscapeChanged(final int newLandscape, final MapUnit unit) {
        switch (newLandscape) {
            case 2: {
                final MapChangeLandscape mc = new MapChangeLandscape(unit, true);
                this.sendPacketToAroundPlayer(mc, unit.getPosition());
                mc.destroy();
                break;
            }
            default: {
                final MapChangeLandscape mc = new MapChangeLandscape(unit, false);
                this.sendPacketToAroundPlayer(mc, unit.getPosition());
                mc.destroy();
                break;
            }
        }
    }
    
    public Point getDefaultPoint() {
        return this.defaultPoint;
    }
    
    public Point getDefaultPoint(final Player player) {
        return this.defaultPoint;
    }
    
    public void setDefaultPoint(final Point p) {
        this.defaultPoint.x = p.x;
        this.defaultPoint.y = p.y;
    }
    
    public HashMap<Long, TransPoint> getTransPointMap() {
        return this.transPointMap;
    }
    
    public void writeDieDialog(final Player player, final String killerName, final int countDwon) {
        PlayerDieDialog.open(player, killerName, countDwon);
    }
    
    public void broadcastPacket(final WriteOnlyPacket packet) {
        for (final Player p : this.playerMap.values()) {
            if (!p.isDestroy()) {
                if (!p.isEnterMap()) {
                    continue;
                }
                p.writePacket(packet);
            }
        }
    }
    
    public void playerDie(final Player player, final Creature attacker) {
        this.writeDieDialog(player, attacker.getName(), this.getRevivlContDown(player));
        player.addMomentEvent(new SafeRevivalEvent(player, this.getSafeRevivalMap(), this.getRevivlContDown(player)));
    }
    
    public int getRevivlContDown(final Player player) {
        return 10;
    }
    
    public Map getSafeRevivalMap() {
        return this;
    }
    
    public boolean pickUpItem(final DropItem di, final Player player) {
        if (!di.isRemoveProtect() && !di.getOwners().containsKey(player.getID())) {
            SystemMessage.writeMessage(player, 11002);
            PickoutItem.sendToClient(player, -1L);
            return false;
        }
        final OperationResult result = player.getItemManager().addItem(di.getItem(), 2);
        if (result.getResult() != 1) {
            SystemMessage.writeMessage(player, result.getResult());
            PickoutItem.sendToClient(player, -1L);
            return false;
        }
        PickoutItem.sendToClient(player, result.getItemID());
        di.disappear();
        return true;
    }
    
    public boolean canTransmit() {
        return this.canTransmit;
    }
    
    public void setCanTransmit(final boolean b) {
        this.canTransmit = b;
    }
    
    public boolean petCanShow() {
        return true;
    }
    
    public void writeRoleDetail(final Player player, final WriteOnlyPacket packet, final Player viewer) throws Exception {
        packet.writeDouble(player.getID());
        packet.writeUTF(player.getName());
        packet.writeByte(this.getViewFontColor(player, viewer));
        packet.writeByte(player.getTitleManager().getEquipId());
        packet.writeUTF(GangManager.getViewGangName(player.getGang()));
        packet.writeShort(player.getWarCommentNameIcon());
        packet.writeInt(player.getX());
        packet.writeInt(player.getY());
        packet.writeByte(player.getProfessionID());
        packet.writeBoolean(this.getUnitLandscape(player) == 2);
        packet.writeBoolean(player.isDie());
        final List<StatEnum> statList = StatList2Client.getAroundPlayerStats();
        packet.writeShort(statList.size());
        for (final StatEnum stat : statList) {
            final int value = player.getStatValue(stat);
            PlayerAttributes.writeStat(stat, value, packet);
        }
        final ArrayList<ExternalEntry> externalList = player.getCurrentExternal();
        ExternalChange.writeRoleExternal(externalList, packet);
        externalList.clear();
        packet.writeBoolean(player.isInRiding());
        AroundPlayer.writeBuffs(player, true, packet);
        final int[] icons = player.getVipIcons();
        packet.writeShort(icons[0]);
        packet.writeShort(icons[1]);
    }
    
    public boolean canChangeExteranl() {
        return true;
    }
    
    public boolean canBeEnemy() {
        return true;
    }
    
    public boolean canGangOperation() {
        return true;
    }
    
    public void addRobotSize() {
        ++this.robotSize;
    }
    
    public int getRobotSize() {
        return this.robotSize;
    }
    
    public int getTruePlayerSize() {
        return this.playerMap.size() - this.getRobotSize();
    }
    
    public void checkIdle(final Player player) {
    }
    
    public boolean canInTeam() {
        return true;
    }
    
    public boolean canLeaveGang() {
        return true;
    }
    
    public boolean canSwitchByLink() {
        return true;
    }
    
    public int getViewFontColor(final Player player, final Player viewer) {
        return player.getEvilEnumBeingSaw().getFont();
    }
}
