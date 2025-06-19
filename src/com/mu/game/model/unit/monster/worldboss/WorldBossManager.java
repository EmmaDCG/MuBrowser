package com.mu.game.model.unit.monster.worldboss;

import com.mu.game.IDFactory;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.map.Map;
import com.mu.game.model.map.MapConfig;
import com.mu.game.model.map.MapData;
import com.mu.game.model.service.StringTools;
import com.mu.game.model.unit.monster.MonsterStar;
import com.mu.game.model.unit.skill.model.SkillDBEntry;
import com.mu.game.model.unit.skill.model.SkillModel;
import com.mu.utils.Rnd;
import com.mu.utils.Tools;
import java.awt.Point;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import jxl.Sheet;
import jxl.Workbook;

public class WorldBossManager {
   public static final int Boss_World = 1;
   public static final int Boss_Tower = 2;
   public static final int Boss_Single = 3;
   private static HashMap bossDataMap = new HashMap();
   private static HashMap bossMap = new HashMap();
   private static ArrayList groupList = new ArrayList();

   public static WorldBossData getBossData(int id) {
      return (WorldBossData)bossDataMap.get(id);
   }

   public static void addBossData(WorldBossData data) {
      bossDataMap.put(data.getBossId(), data);
   }

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      intBaseInfo(wb.getSheet(1));
      initDetailData(wb.getSheet(2));
      initGroup(wb.getSheet(3));
   }

   private static void initGroup(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         groupList.add(new String[]{Tools.getCellValue(sheet.getCell("A" + i)), Tools.getCellValue(sheet.getCell("B" + i))});
      }

   }

   public static ArrayList getCanKillList() {
      ArrayList list = new ArrayList();
      Iterator it = bossDataMap.values().iterator();

      while(it.hasNext()) {
         WorldBossData data = (WorldBossData)it.next();
         if (data.getBossType() != 3) {
            WorldBoss boss = getBoss(data.getBossId());
            long nextTime = (long)boss.getNextRevivalTime();
            if (nextTime <= 0L) {
               list.add(data);
            }
         }
      }

      return list;
   }

   public static ArrayList getGroupList() {
      return groupList;
   }

   private static void intBaseInfo(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int id = Tools.getCellIntValue(sheet.getCell("A" + i));
         WorldBossData data = new WorldBossData(id);
         data.setName(Tools.getCellValue(sheet.getCell("B" + i)));
         data.setBossType(Tools.getCellIntValue(sheet.getCell("D" + i)));
         data.setZoom(Tools.getCellIntValue(sheet.getCell("E" + i)));
         String dropStr = Tools.getCellValue(sheet.getCell("C" + i));
         data.setReqLevel(Tools.getCellIntValue(sheet.getCell("F" + i)));
         data.setTelTime((long)Tools.getCellIntValue(sheet.getCell("G" + i)) * 1000L);
         String[] tmp;
         int j;
         if (dropStr.indexOf(";") != -1) {
            tmp = dropStr.split(";");

            for(j = 0; j < tmp.length; ++j) {
               String[] itemStr = tmp[j].split(",");
               ItemDataUnit du = new ItemDataUnit(Integer.parseInt(itemStr[0]), 1);
               du.setStatRuleID(Integer.parseInt(itemStr[1]));
               du.setHide(true);
               du.setBind(false);
               Item item = ItemTools.createItem(2, du);
               data.addDrop(item);
            }
         } else {
            tmp = dropStr.split(",");

            for(j = 0; j < tmp.length; ++j) {
               data.addDrop(ItemTools.createItem(Integer.parseInt(tmp[j]), 1, 2));
            }
         }

         bossDataMap.put(id, data);
      }

   }

   public static HashMap getBossDataMap() {
      return bossDataMap;
   }

   public static HashMap getBossMap() {
      return bossMap;
   }

   public static WorldBoss getBoss(int bossId) {
      return (WorldBoss)bossMap.get(bossId);
   }

   private static void initDetailData(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int id = Tools.getCellIntValue(sheet.getCell("A" + i));
         WorldBossData data = getBossData(id);
         if (data != null) {
            data.setTemplateId(Tools.getCellIntValue(sheet.getCell("B" + i)));
            data.setAi(Tools.getCellIntValue(sheet.getCell("C" + i)));
            data.setRevivalTime((long)Tools.getCellIntValue(sheet.getCell("D" + i)) * 1000L);
            data.setMinLevel(Tools.getCellIntValue(sheet.getCell("E" + i)));
            data.setMaxLevel(data.getMinLevel());
            data.setStar(Tools.getCellIntValue(sheet.getCell("F" + i)));
            data.setX(Tools.getCellIntValue(sheet.getCell("G" + i)));
            data.setY(Tools.getCellIntValue(sheet.getCell("H" + i)));
            data.setSpeed(Tools.getCellFloatValue(sheet.getCell("I" + i)) * 100.0F);
            data.setMoveRadius(Tools.getCellIntValue(sheet.getCell("J" + i)));
            data.setGiveUpDist(Tools.getCellIntValue(sheet.getCell("K" + i)));
            data.setSerachDist(Tools.getCellIntValue(sheet.getCell("L" + i)));
            data.setMaxMoveDist(Tools.getCellIntValue(sheet.getCell("M" + i)));
            data.setAttackDist(Tools.getCellIntValue(sheet.getCell("N" + i)));
            data.setMinAttackDist(Tools.getCellIntValue(sheet.getCell("O" + i)));
            String skillIDStr = Tools.getCellValue(sheet.getCell("P" + i));
            List skillList = StringTools.analyzeIntegerList(skillIDStr, ",");
            if (skillList == null || skillList.size() < 1) {
               (new Exception("怪物技能没有配置")).printStackTrace();
            }

            Iterator var8 = skillList.iterator();

            while(var8.hasNext()) {
               Integer skillID = (Integer)var8.next();
               if (!SkillModel.hasModel(skillID.intValue())) {
                  (new Exception("怪物 分布 - 技能id不存在")).printStackTrace();
               }
            }

            data.setSkillList(skillList);
            data.setMapId(Tools.getCellIntValue(sheet.getCell("Q" + i)));
            MapData md = MapConfig.getMapData(data.getMapId());
            data.setMapName(md.getMapName());
         }
      }

   }

   public static void createBoss() throws Exception {
      Iterator it = bossDataMap.values().iterator();

      while(true) {
         WorldBossData data;
         Map map;
         WorldBoss boss;
         MonsterStar monsterStar;
         do {
            do {
               if (!it.hasNext()) {
                  return;
               }

               data = (WorldBossData)it.next();
            } while(data.getBossType() == 3);

            map = MapConfig.getDefaultMap(data.getMapId());
            boss = new WorldBoss(IDFactory.getTemporaryID(), map, data.getBossId());
            boss.setAttackDistance(data.getAttackDist());
            boss.setLevel(data.getMinLevel());
            boss.setBornPoint(new Point(data.getX(), data.getY()));
            boss.setFace(Rnd.get(-100, 100), Rnd.get(-100, 100));
            boss.setMaxMoveDistance(data.getMaxMoveDist());
            boss.setMinAttackDistance(data.getMinAttackDist());
            boss.setModelId(data.getModelId());
            boss.setMoveRadius(data.getMoveRadius());
            boss.setName(data.getName());
            boss.setPosition(boss.getBornPoint());
            boss.setRevivalTime(data.getRevivalTime());
            boss.setSearchDistance(data.getSerachDist());
            boss.setTemplateId(data.getTemplateId());
            monsterStar = MonsterStar.getMonsterStar(data.getStar(), boss.getLevel());
         } while(monsterStar == null);

         boss.setRewardExp(monsterStar.getExp());
         boss.getProperty().inits(monsterStar.getHp(), monsterStar.getMp(), monsterStar.getMinAtt(), monsterStar.getMaxAtt(), monsterStar.getDef(), monsterStar.getHit(), monsterStar.getAvd(), (int)data.getSpeed(), monsterStar.getOtherStats());
         boss.setHp(monsterStar.getHp());
         boss.setMp(monsterStar.getMp());
         boss.addDrops(monsterStar.getDrops());
         Iterator var6 = data.getSkillList().iterator();

         while(var6.hasNext()) {
            Integer skillID = (Integer)var6.next();
            SkillDBEntry entry = new SkillDBEntry(skillID.intValue(), 1, 0, false, 0);
            boss.getSkillManager().loadSkill(entry);
         }

         map.addMonster(boss);
         boss.setAI(data.getAi());
         bossMap.put(boss.getBossId(), boss);
      }
   }
}
