package com.mu.game.model.map;

import com.mu.game.IDFactory;
import com.mu.game.model.dialog.DialogConfigManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.map.enter.req.EnterMapLevelRequirement;
import com.mu.game.model.map.enter.req.EnterMapRequiremenetFactory;
import com.mu.game.model.map.enter.req.EnterMapRequirement;
import com.mu.game.model.unit.material.Material;
import com.mu.game.model.unit.material.MaterialGroup;
import com.mu.game.model.unit.material.MaterialTemplate;
import com.mu.game.model.unit.monster.Monster;
import com.mu.game.model.unit.monster.MonsterStar;
import com.mu.game.model.unit.npc.Npc;
import com.mu.game.model.unit.robot.RobotManager;
import com.mu.game.model.unit.skill.model.SkillDBEntry;
import com.mu.game.model.unit.tp.TransPoint;
import com.mu.utils.Rnd;
import com.mu.utils.Tools;
import com.mu.utils.buffer.BufferReader;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Float;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import jxl.Sheet;
import jxl.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapConfig {
    private static final Logger logger = LoggerFactory.getLogger(MapConfig.class);
    private static HashMap lineMaps = new HashMap();
    private static ConcurrentHashMap mapInfoes = new ConcurrentHashMap();
    private static HashMap safeShape = new HashMap();
    private static HashMap unSafeShape = new HashMap();
    private static HashMap worldLinkMap = new HashMap();
    private static HashMap worldPointMap = new HashMap();
    private static HashMap groupMap = new HashMap();
    private static final HashMap npcInfoMap = new HashMap();
    public static final int CommonMap = 0;
    public static final int GameMap = 1;
    public static final int InterMap = 2;

    public static boolean isWorldMap(int mapID) {
        MapData mapData = getMapData(mapID);
        return mapData != null ? mapData.isWorldMap() : false;
    }

    public static void initMonsterGroup(InputStream in) throws Exception {
        Workbook wb = Workbook.getWorkbook(in);
        Sheet[] sheets = wb.getSheets();

        for (int i = 1; i < sheets.length; ++i) {
            Sheet sheet = sheets[i];
            int mapID = Integer.parseInt(sheet.getName());
            MapData md = getMapData(mapID);
            int rows = sheet.getRows();

            for (int j = 2; j <= rows; ++j) {
                BigMonsterGroup mg = new BigMonsterGroup();
                BigMonsterGroup.parseMonsterGroup(mg, sheet, j);
                md.addMonsterGroup(mg);
            }
        }

    }

    public static void lineToBlocks(ArrayList blockList, Map map, int x1, int y1, int x2, int y2) {
        Line2D line = new Float((float) x1, (float) y1, (float) x2, (float) y2);
        int minX = Math.min(map.getTileX(x1), map.getTileX(x2));
        int maxX = Math.max(map.getTileX(x1), map.getTileX(x2));
        int minY = Math.min(map.getTileY(y1), map.getTileY(y2));
        int maxY = Math.max(map.getTileY(y1), map.getTileY(y2));

        for (int i = minX; i <= maxX; ++i) {
            for (int j = minY; j <= maxY; ++j) {
                Rectangle rec = new Rectangle(map.getXByTile(i), map.getYByTile(j), 600, 600);
                if (rec.intersectsLine(line)) {
                    blockList.add(new int[]{i, j});
                }
            }
        }

    }

    public static void initMaterialGroup(InputStream in) throws Exception {
        Workbook wb = Workbook.getWorkbook(in);
        Sheet[] sheets = wb.getSheets();

        for (int i = 1; i < sheets.length; ++i) {
            Sheet sheet = sheets[i];
            int mapID = Integer.parseInt(sheet.getName());
            MapData md = getMapData(mapID);
            int rows = sheet.getRows();

            for (int j = 2; j <= rows; ++j) {
                MaterialGroup mg = new MaterialGroup();
                mg.setName(Tools.getCellValue(sheet.getCell("A" + j)));
                mg.setTemplateID(Tools.getCellIntValue(sheet.getCell("B" + j)));
                MaterialTemplate template = MaterialTemplate.getTemplate(mg.getTemplateID());
                if (template == null) {
                    throw new Exception("template not found " + mg.getTemplateID() + "\t" + mg.getName());
                }

                mg.setRefreshTime(Tools.getCellIntValue(sheet.getCell("C" + j)) * 1000);
                mg.setX(Tools.getCellIntValue(sheet.getCell("D" + j)));
                mg.setY(Tools.getCellIntValue(sheet.getCell("E" + j)));
                mg.setNum(Tools.getCellIntValue(sheet.getCell("F" + j)));
                mg.setRadius(Tools.getCellIntValue(sheet.getCell("G" + j)));
                mg.setModelID(template.getModelId());
                mg.setCollectTime(template.getCollectTime());
                mg.setDisappear(template.isCanDisappear());
                mg.setMapID(mapID);
                md.addMaterialGroup(mg);
            }
        }

    }

    public static void initNpcInfo(InputStream in) throws Exception {
        Workbook wb = Workbook.getWorkbook(in);
        Sheet[] sheets = wb.getSheets();

        for (int i = 1; i < sheets.length; ++i) {
            Sheet sheet = sheets[i];
            int mapID = Integer.parseInt(sheet.getName());
            MapData md = getMapData(mapID);
            int rows = sheet.getRows();

            for (int j = 2; j <= rows; ++j) {
                NpcInfo info = new NpcInfo();
                info.setId(Tools.getCellIntValue(sheet.getCell("A" + j)));
                info.setName(Tools.getCellValue(sheet.getCell("B" + j)));
                info.setTemplateId(Tools.getCellIntValue(sheet.getCell("C" + j)));
                info.setX(Tools.getCellIntValue(sheet.getCell("D" + j)));
                info.setY(Tools.getCellIntValue(sheet.getCell("E" + j)));
                String[] faceStr = Tools.getCellValue(sheet.getCell("F" + j)).split(",");
                info.setFace(new int[]{Integer.parseInt(faceStr[0]), Integer.parseInt(faceStr[1])});
                info.setDialogId((long) Tools.getCellIntValue(sheet.getCell("G" + j)));
                md.addNpcInfo(info);
                npcInfoMap.put((long) info.getId(), info);
            }
        }

    }

    public static NpcInfo getNpcInfo(long npcId) {
        return (NpcInfo) npcInfoMap.get(npcId);
    }

    public static void initSmallMapElement(InputStream in) throws Exception {
        Workbook wb = Workbook.getWorkbook(in);
        Sheet[] sheets = wb.getSheets();

        for (int i = 1; i < sheets.length; ++i) {
            Sheet sheet = sheets[i];
            int mapID = Integer.parseInt(sheet.getName());
            MapData md = getMapData(mapID);
            int rows = sheet.getRows();

            for (int j = 2; j <= rows; ++j) {
                SmallMapElement se = new SmallMapElement();
                se.setId(Long.parseLong(Tools.getCellValue(sheet.getCell("A" + j))));
                se.setName(Tools.getCellValue(sheet.getCell("B" + j)));
                se.setType(Tools.getCellIntValue(sheet.getCell("C" + j)));
                se.setX(Tools.getCellIntValue(sheet.getCell("D" + j)));
                se.setY(Tools.getCellIntValue(sheet.getCell("E" + j)));
                md.addSmallMapElement(se);
            }
        }

    }

    public static void initWorldMapInfo(InputStream in) throws Exception {
        Workbook wb = Workbook.getWorkbook(in);
        Sheet sheet = wb.getSheet(1);
        int rows = sheet.getRows();

        for (int i = 2; i <= rows; ++i) {
            int mapID = Tools.getCellIntValue(sheet.getCell("A" + i));
            String name = Tools.getCellValue(sheet.getCell("B" + i));
            int modelID = Tools.getCellIntValue(sheet.getCell("C" + i));
            int smallMap = Tools.getCellIntValue(sheet.getCell("D" + i));
            int findWayID = Tools.getCellIntValue(sheet.getCell("E" + i));
            int music = Tools.getCellIntValue(sheet.getCell("F" + i));
            boolean isShow = Tools.getCellIntValue(sheet.getCell("H" + i)) == 1;
            boolean isOpen = Tools.getCellIntValue(sheet.getCell("I" + i)) == 1;
            int groupID = Tools.getCellIntValue(sheet.getCell("L" + i));
            int x = Tools.getCellIntValue(sheet.getCell("S" + i));
            int y = Tools.getCellIntValue(sheet.getCell("T" + i));
            int worldMapX = Tools.getCellIntValue(sheet.getCell("J" + i));
            int worldMapY = Tools.getCellIntValue(sheet.getCell("K" + i));
            String iconStr = Tools.getCellValue(sheet.getCell("G" + i));
            String[] iconS = iconStr.split(",");
            int recommendMinLevel = Tools.getCellIntValue(sheet.getCell("N" + i));
            int recommendLevel = Tools.getCellIntValue(sheet.getCell("O" + i));
            if (iconS.length == 3) {
                int[] mapIcons = new int[]{Integer.parseInt(iconS[0]), Integer.parseInt(iconS[1]), Integer.parseInt(iconS[2])};
                boolean canPk = Tools.getCellIntValue(sheet.getCell("U" + i)) == 1;
                boolean canZening = Tools.getCellIntValue(sheet.getCell("V" + i)) == 1;
                int nameImage = Tools.getCellIntValue(sheet.getCell("X" + i));
                boolean canChangePk = Tools.getCellIntValue(sheet.getCell("Z" + i)) == 1;
                int interMapType = Tools.getCellIntValue(sheet.getCell("AA" + i));
                int lineNumber = Tools.getCellIntValue(sheet.getCell("W" + i));
                boolean isMainCity = Tools.getCellIntValue(sheet.getCell("M" + i)) == 1;
                int cityPkModel = Tools.getCellIntValue(sheet.getCell("N" + i));
                String bigMapDes = Tools.getCellValue(sheet.getCell("R" + i));
                int landscape = Tools.getCellIntValue(sheet.getCell("AD" + i));
                boolean checkLandscapeChange = Tools.getCellIntValue(sheet.getCell("AE" + i)) == 1;
                int configId = Tools.getCellIntValue(sheet.getCell("AF" + i));
                String groupName = Tools.getCellValue(sheet.getCell("AI" + i));
                boolean pkPunish = Tools.getCellIntValue(sheet.getCell("AL" + i)) == 1;
                String groupStr = Tools.getCellValue(sheet.getCell("AH" + i));
                int[] mapGroup = null;
                if (groupStr != null && !groupStr.trim().equals("")) {
                    String[] tmpGroup = groupStr.trim().split(",");
                    mapGroup = new int[tmpGroup.length];

                    for (int j = 0; j < mapGroup.length; ++j) {
                        mapGroup[j] = Integer.parseInt(tmpGroup[j]);
                    }
                }

                MapData md = new MapData(mapID);
                md.setMapName(name);
                md.setModelID(modelID);
                md.setSmallID(smallMap);
                md.setGroupID(groupID);
                md.setWorldMapX(worldMapX);
                md.setWorldMapY(worldMapY);
                md.setRecommendMinLevel(recommendMinLevel);
                md.setRecommendLevel(recommendLevel);
                md.setMusic(music);
                md.setOpen(isOpen);
                md.setDefaultX(x);
                md.setDefaultY(y);
                md.setFindWayID(findWayID);
                md.setCanPk(canPk);
                md.setCanZening(canZening);
                md.setNameImage(nameImage);
                md.setCanChangePkModel(canChangePk);
                md.setLineNumber(lineNumber);
                md.setInterMapType(interMapType);
                md.setIcons(mapIcons);
                md.setMainCity(isMainCity);
                md.setCityPkModel(cityPkModel);
                md.setBigMapDes(bigMapDes);
                md.setLandscape(landscape);
                md.setCheckLandscapeChange(checkLandscapeChange);
                md.setConfigId(configId);
                md.setShow(isShow);
                md.setPkPunish(pkPunish);
                md.setMapGroup(mapGroup);
                if (groupName != null && !groupName.trim().equals("")) {
                    md.setGroupName(groupName.trim());
                }

                String dropStr = Tools.getCellValue(sheet.getCell("AG" + i));
                if (dropStr != null) {
                    String[] drops = dropStr.split(";");

                    for (int j = 0; j < drops.length; ++j) {
                        String[] tmp = drops[j].split(",");
                        if (tmp.length == 2) {
                            int itemModelId = Integer.parseInt(tmp[0].trim());
                            int statRuleID = Integer.parseInt(tmp[1].trim());
                            ItemDataUnit du = new ItemDataUnit(itemModelId, 1);
                            du.setStatRuleID(statRuleID);
                            du.setHide(true);
                            du.setBind(false);
                            Item item = ItemTools.createItem(2, du);
                            md.addDropItem(item);
                        }
                    }
                }

                String reqStr = Tools.getCellValue(sheet.getCell("AJ" + i));
                if (reqStr != null && reqStr.length() != 0) {
                    String[] tmpReq = reqStr.split(";");

                    for (int j = 0; j < tmpReq.length; ++j) {
                        EnterMapRequirement req = EnterMapRequiremenetFactory.createRequirement(tmpReq[j]);
                        if (req != null) {
                            md.addEnterMapRequirement(req);
                            if (req instanceof EnterMapLevelRequirement) {
                                EnterMapLevelRequirement er = (EnterMapLevelRequirement) req;
                                md.setReqLevel(er.getLevel());
                            }
                        }
                    }
                }

                md.setBackMusic(Tools.getCellIntValue(sheet.getCell("AK" + i)));
                addMapData(md);
            }
        }

        initWorldLink(wb.getSheet(2));
        initTranspont(wb.getSheet(3));
        initMapGroup(wb.getSheet(4));
        initLandscapeArea(wb.getSheet(5));
        wb.close();
    }

    public static int[] getResurrectionPoint(int mapID) {
        MapData mapData = (MapData) mapInfoes.get(mapID);
        return new int[]{mapID, mapData.getDefaultX(), mapData.getDefaultY()};
    }

    private static Shape getShape(int geometryType, String str) {
        String[] p;
        if (geometryType != 1) {
            if (geometryType == 2) {
                p = str.split(";");
                String[] fixed = p[0].split(",");
                Ellipse2D ellipse = new java.awt.geom.Ellipse2D.Float((float) Integer.parseInt(fixed[0]), (float) Integer.parseInt(fixed[1]), (float) Integer.parseInt(p[1]), (float) Integer.parseInt(p[2]));
                return ellipse;
            } else {
                return null;
            }
        } else {
            p = str.split(";");
            int[] xPoint = new int[p.length];
            int[] yPoint = new int[p.length];

            for (int i = 0; i < xPoint.length; ++i) {
                String[] tmp = p[i].split(",");
                xPoint[i] = Integer.parseInt(tmp[0]);
                yPoint[i] = Integer.parseInt(tmp[1]);
            }

            Polygon pg = new Polygon(xPoint, yPoint, xPoint.length);
            return pg;
        }
    }

    private static void initMap(Map map, byte[] data) {
        try {
            BufferReader reader = new BufferReader(data);
            map.setLeft(reader.readInt());
            map.setTop(reader.readInt());
            map.setWidth(reader.readInt());
            map.setHeight(reader.readInt());
            int blockWidth = reader.readInt();
            int blockHeight = reader.readInt();
            map.initBlocks(blockWidth, blockHeight);
            byte[] bytes = new byte[blockWidth * blockHeight];
            reader.readBytes(bytes);

            for (int i = 0; i < blockWidth; ++i) {
                System.arraycopy(bytes, i * blockHeight, map.getBlocks()[i], 0, blockHeight);
            }

            reader.destroy();
        } catch (IOException var7) {
            var7.printStackTrace();
        }

    }

    public static Map createMap(MapData data, int line) throws Exception {
        Map map = new Map(data.getMapID());
        initMap(map, data.getConfigData());
        map.setLine(line);
        map.createArea();
        map.setCanZening(data.isCanZening());
        map.setCanPk(data.isCanPk());
        map.setFindWayID(data.getFindWayID());
        map.setName(data.getMapName());
        map.setCanChangePkMode(data.isCanChangePkModel());
        map.setLandscape(data.getLandscape());
        map.setCheckLandscapeChange(data.isCheckLandscapeChange());
        map.setDefaultPoint(new Point(data.getDefaultX(), data.getDefaultY()));
        map.setPkPunishment(data.isPkPunish());
        Iterator it = data.getTransPointMap().values().iterator();

        while (it.hasNext()) {
            TranspointData td = (TranspointData) it.next();
            TransPoint tp = new TransPoint(td.getId(), map, td.getTargetMapID(), td.getX(), td.getY(), td.getTargetX(), td.getTargetY(), td.getName(), td.getWorldX(), td.getWorldY());
            map.addTranspoint(tp);
        }

        initMonster(map);
        initNpc(map);
        initMaterial(map);
        if (line == 0) {
            RobotManager.createRobot(map);
        }

        return map;
    }

    private static void initNpc(Map map) {
        MapData md = getMapData(map.getID());
        ArrayList list = md.getNpcList();
        Iterator var4 = list.iterator();

        while (var4.hasNext()) {
            NpcInfo info = (NpcInfo) var4.next();
            Npc npc = new Npc((long) info.getId(), map);
            npc.setName(info.getName());
            npc.setLevel(400);
            npc.setX(info.getX());
            npc.setY(info.getY());
            npc.setTemplateId(info.getTemplateId());
            npc.setFace(info.getFace()[0], info.getFace()[1]);
            npc.setDialog(DialogConfigManager.getDialog(info.getDialogId()));
            map.addNpc(npc);
        }

    }

    private static void initMaterial(Map map) {
        MapData md = getMapData(map.getID());
        ArrayList list = md.getMaterialList();
        Iterator var4 = list.iterator();

        while (var4.hasNext()) {
            MaterialGroup mg = (MaterialGroup) var4.next();
            int minX = mg.getX() - mg.getRadius();
            int maxX = mg.getX() + mg.getRadius();
            int minY = mg.getY() - mg.getRadius();
            int maxY = mg.getY() + mg.getRadius();

            for (int i = 0; i < mg.getNum(); ++i) {
                Material material = new Material(IDFactory.getTemporaryID(), map, mg.getTemplateID(), mg.getName(), mg.getModelID());
                material.setCanDisappear(mg.isDisappear());
                material.setCollectTime(mg.getCollectTime());
                material.setRefreshTime(mg.getRefreshTime());
                material.setPosition(Rnd.get(minX, maxX), Rnd.get(minY, maxY));
                material.getTemplate().initReqAndRew(material);
                map.addMaterial(material);
            }
        }

    }

    private static void initMonster(Map map) {
        MapData md = getMapData(map.getID());
        ArrayList list = md.getMonsterList();
        Iterator var4 = list.iterator();

        while (var4.hasNext()) {
            BigMonsterGroup mg = (BigMonsterGroup) var4.next();
            int minX = mg.getX() - mg.getBornRadius();
            int maxX = mg.getX() + mg.getBornRadius();
            int minY = mg.getY() - mg.getBornRadius();
            int maxY = mg.getY() + mg.getBornRadius();

            for (int i = 0; i < mg.getNum(); ++i) {
                Monster monster = new Monster(IDFactory.getTemporaryID(), map);
                monster.setAttackDistance(mg.getAttackDist());
                monster.setLevel(Rnd.get(mg.getMinLevel(), mg.getMaxLevel()));
                monster.setBornPoint(map.searchFeasiblePoint(Rnd.get(minX, maxX), Rnd.get(minY, maxY)));
                monster.setFace(Rnd.get(-100, 100), Rnd.get(-100, 100));
                monster.setMaxMoveDistance(mg.getMaxMoveDist());
                monster.setMinAttackDistance(mg.getMinAttackDist());
                monster.setModelId(mg.getModelId());
                monster.setMoveRadius(mg.getMoveRadius());
                monster.setName(mg.getName());
                monster.setPosition(monster.getBornPoint());
                monster.setRevivalTime(mg.getRevivalTime());
                monster.setSearchDistance(mg.getSerachDist());
                monster.setTemplateId(mg.getTemplateId());
                MonsterStar monsterStar = MonsterStar.getMonsterStar(mg.getStar(), monster.getLevel());
                if (monsterStar != null) {
                    monster.setRewardExp(monsterStar.getExp());
                    monster.getProperty().inits(monsterStar.getHp(), monsterStar.getMp(), monsterStar.getMinAtt(), monsterStar.getMaxAtt(), monsterStar.getDef(), monsterStar.getHit(), monsterStar.getAvd(), (int) mg.getSpeed(), monsterStar.getOtherStats());
                    monster.setHp(monsterStar.getHp());
                    monster.setMp(monsterStar.getMp());
                    monster.addDrops(monsterStar.getDrops());
                    Iterator var13 = mg.getSkillList().iterator();

                    while (var13.hasNext()) {
                        Integer skillID = (Integer) var13.next();
                        SkillDBEntry entry = new SkillDBEntry(skillID.intValue(), 1, 0, false, 0);
                        monster.getSkillManager().loadSkill(entry);
                    }

                    map.addMonster(monster);
                    monster.setAI(mg.getAi());
                }
            }
        }

    }

    public static void initBaseMaps() throws Exception {
        Iterator it = mapInfoes.values().iterator();

        while (it.hasNext()) {
            MapData data = (MapData) it.next();
            if (data.getConfigData() != null) {
                Map map = createMap(data, 0);
                LineMap lm = new LineMap(map);
                lineMaps.put(map.getID(), lm);
            }
        }

    }

    public static ArrayList getMapPlayerSizeList() {
        ArrayList list = new ArrayList();
        Iterator it = lineMaps.values().iterator();

        while (it.hasNext()) {
            LineMap lm = (LineMap) it.next();
            Map[] maps = lm.getAllMaps();

            for (int i = 0; i < maps.length; ++i) {
                if (maps[i].getTruePlayerSize() > 0) {
                    list.add(maps[i]);
                }
            }
        }

        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Map) o2).getTruePlayerSize() > ((Map) o2).getTruePlayerSize() ? -1 : 1;
            }
        });

        ArrayList list2 = new ArrayList();
        Iterator var7 = list.iterator();

        while (var7.hasNext()) {
            Map map = (Map) var7.next();
            list2.add(new String[]{map.getName(), "" + (map.getLine() + 1), "" + map.getTruePlayerSize()});
        }

        list.clear();
        list = null;
        return list2;
    }

    public static ArrayList getMapOnlineSize() {
        ArrayList list = new ArrayList();
        Iterator it = lineMaps.values().iterator();

        while (it.hasNext()) {
            LineMap lm = (LineMap) it.next();
            Map[] maps = lm.getAllMaps();

            for (int i = 0; i < maps.length; ++i) {
                if (maps[i].getTruePlayerSize() > 0) {
                    list.add(maps[i]);
                }
            }
        }

        return list;
    }

    public static LineMap getLineMap(int mapID) {
        return (LineMap) lineMaps.get(mapID);
    }

    public static void initMapEvent(InputStream in) {
    }

    public static void addMapData(MapData data) {
        mapInfoes.put(data.getMapID(), data);
    }

    public static ConcurrentHashMap getAllMapData() {
        return mapInfoes;
    }

    public static MapData getMapData(int mapID) {
        return (MapData) mapInfoes.get(mapID);
    }

    public static HashMap getWorldLinkMap() {
        return worldLinkMap;
    }

    public static HashMap getWorldPointMap() {
        return worldPointMap;
    }

    public static void initTranspont(Sheet sheet) throws Exception {
        int row = sheet.getRows();

        for (int i = 2; i <= row; ++i) {
            int mapID = Tools.getCellIntValue(sheet.getCell("A" + i));
            String name = Tools.getCellValue(sheet.getCell("B" + i));
            int x = Tools.getCellIntValue(sheet.getCell("C" + i));
            int y = Tools.getCellIntValue(sheet.getCell("D" + i));
            int targetMapID = Tools.getCellIntValue(sheet.getCell("E" + i));
            int targetX = Tools.getCellIntValue(sheet.getCell("F" + i));
            int targetY = Tools.getCellIntValue(sheet.getCell("G" + i));
            int worldX = Tools.getCellIntValue(sheet.getCell("H" + i));
            int worldY = Tools.getCellIntValue(sheet.getCell("I" + i));
            TranspointData td = new TranspointData(IDFactory.getTemporaryID());
            td.setMapID(mapID);
            td.setName(name);
            td.setTargetMapID(targetMapID);
            td.setTargetX(targetX);
            td.setTargetY(targetY);
            td.setX(x);
            td.setY(y);
            td.setWorldX(worldX);
            td.setWorldY(worldY);
            MapData md = getMapData(mapID);
            if (md != null) {
                md.addTransPoint(td);
            }
        }

    }

    public static void initMapGroup(Sheet sheet) throws Exception {
        int row = sheet.getRows();

        for (int i = 2; i <= row; ++i) {
            int id = Tools.getCellIntValue(sheet.getCell("A" + i));
            String name = Tools.getCellValue(sheet.getCell("B" + i));
            String des = Tools.getCellValue(sheet.getCell("C" + i));
            int back = Tools.getCellIntValue(sheet.getCell("D" + i));
            MapGroup mg = new MapGroup(id);
            mg.setName(name);
            mg.setDes(des);
            mg.setBackGroup(back);
            groupMap.put(id, mg);
        }

    }

    public static HashMap getGroupMap() {
        return groupMap;
    }

    public static void initLandscapeArea(Sheet sheet) throws Exception {
        int row = sheet.getRows();

        for (int i = 2; i <= row; ++i) {
            int mapId = Tools.getCellIntValue(sheet.getCell("A" + i));
            String str = Tools.getCellValue(sheet.getCell("B" + i));
            int type = Tools.getCellIntValue(sheet.getCell("C" + i));
            String[] p = str.split(";");
            int[] xPoint = new int[p.length];
            int[] yPoint = new int[p.length];

            for (int j = 0; j < xPoint.length; ++j) {
                String[] tmp = p[i].split(",");
                xPoint[j] = Integer.parseInt(tmp[0]);
                yPoint[j] = Integer.parseInt(tmp[1]);
            }

            Polygon pg = new Polygon(xPoint, yPoint, xPoint.length);
            getMapData(mapId).addDifLandscape(pg, type);
        }

    }

    public static void initWorldLink(Sheet sheet) throws Exception {
        int row = sheet.getRows();

        for (int i = 2; i <= row; ++i) {
            int wordId = Tools.getCellIntValue(sheet.getCell("B" + i));
            String adjId = Tools.getCellValue(sheet.getCell("C" + i));
            worldLinkMap.put(wordId, adjId);
            String tpStr = Tools.getCellValue(sheet.getCell("D" + i));
            String[] tpS = tpStr.split(";");

            for (int j = 0; j < tpS.length; ++j) {
                String[] tmp1 = tpS[j].split(",");
                String[] tmp2 = tmp1[0].split(":");
                String[] tmp3 = tmp1[1].split(":");
                if (!worldPointMap.containsKey(tmp2[0] + "," + tmp2[1]) && !worldPointMap.containsKey(tmp2[1] + "," + tmp2[0])) {
                    worldPointMap.put(tmp2[0] + "," + tmp2[1], new String[]{tmp2[0] + "," + tmp2[1], tmp3[0], tmp3[1]});
                }
            }
        }

    }

    public static Map getDefaultMap(int mapID) {
        LineMap lm = (LineMap) lineMaps.get(mapID);
        return lm != null ? lm.getMapByLine(0) : null;
    }

    public static boolean isSafe(int mapID, int x, int y) {
        Shape shape = (Shape) safeShape.get(mapID);
        return shape != null ? shape.contains((double) x, (double) y) : false;
    }

    public static boolean isUnSafe(int mapID, int x, int y) {
        Shape shape = (Shape) unSafeShape.get(mapID);
        return shape != null ? shape.contains((double) x, (double) y) : false;
    }

    public static int getMusic(int mapID) {
        MapData md = getMapData(mapID);
        return md == null ? 0 : md.getMusic();
    }

    public static HashMap getLineMaps() {
        return lineMaps;
    }
}
