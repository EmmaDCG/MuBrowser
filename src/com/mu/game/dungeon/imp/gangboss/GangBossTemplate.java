package com.mu.game.dungeon.imp.gangboss;

import com.mu.game.dungeon.DungeonTemplate;
import com.mu.game.model.drop.model.MonsterDrop;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.service.StringTools;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.model.SkillModel;
import com.mu.io.game.packet.imp.dungeon.DunTimingPanel;
import com.mu.io.game.packet.imp.dungeon.RequestDungeonInfo;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.sys.OpenPanel;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import jxl.Sheet;
import jxl.Workbook;

public class GangBossTemplate extends DungeonTemplate {
   private HashMap bossInfoMap = new HashMap();
   private GangBossManager manager = new GangBossManager(this);
   private int popBack = -1;
   private String popDes = "";
   private String popTitle = "";

   public GangBossTemplate(Workbook wb) {
      super(10, wb);
      this.canInspire = true;
   }

   public void init() throws Exception {
      this.initGeneral(this.wb.getSheet(1));
      this.initMonsterStar(this.wb.getSheet(2));
      this.initBoss(this.wb.getSheet(3));
   }

   public void clickTimePanel(Player player) {
      OpenPanel.open(player, 59, 1);
   }

   private void initGeneral(Sheet sheet) {
      this.setInspireMoney(Tools.getCellIntValue(sheet.getCell("I2")));
      this.setInspireIngot(Tools.getCellIntValue(sheet.getCell("J2")));
      this.setMoneyInspireDes(Tools.getCellValue(sheet.getCell("K2")));
      this.setIngotInspireDes(Tools.getCellValue(sheet.getCell("L2")));
      this.popBack = Tools.getCellIntValue(sheet.getCell("M2"));
      this.popDes = Tools.getCellValue(sheet.getCell("N2"));
      this.popTitle = Tools.getCellValue(sheet.getCell("O2"));
   }

   private void initBoss(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int bossId = Tools.getCellIntValue(sheet.getCell("A" + i));
         String name = Tools.getCellValue(sheet.getCell("B" + i));
         int header = Tools.getCellIntValue(sheet.getCell("C" + i));
         int sumLevel = Tools.getCellIntValue(sheet.getCell("D" + i));
         int contributionReq = Tools.getCellIntValue(sheet.getCell("E" + i));
         int sumNumber = Tools.getCellIntValue(sheet.getCell("F" + i));
         int templateid = Tools.getCellIntValue(sheet.getCell("G" + i));
         int mapId = Tools.getCellIntValue(sheet.getCell("H" + i));
         int level = Tools.getCellIntValue(sheet.getCell("I" + i));
         int star = Tools.getCellIntValue(sheet.getCell("J" + i));
         int x = Tools.getCellIntValue(sheet.getCell("K" + i));
         int y = Tools.getCellIntValue(sheet.getCell("L" + i));
         float speed = Tools.getCellFloatValue(sheet.getCell("M" + i));
         int radius = Tools.getCellIntValue(sheet.getCell("N" + i));
         int giveUpDis = Tools.getCellIntValue(sheet.getCell("O" + i));
         int searchDis = Tools.getCellIntValue(sheet.getCell("P" + i));
         int maxMoveDis = Tools.getCellIntValue(sheet.getCell("Q" + i));
         int attackDis = Tools.getCellIntValue(sheet.getCell("R" + i));
         int minAttackDis = Tools.getCellIntValue(sheet.getCell("S" + i));
         String skillIDStr = Tools.getCellValue(sheet.getCell("T" + i));
         String dropShowStr = Tools.getCellValue(sheet.getCell("U" + i));
         int loopSize = Tools.getCellIntValue(sheet.getCell("V" + i));
         String dropStr = Tools.getCellValue(sheet.getCell("W" + i));
         List skillList = StringTools.analyzeIntegerList(skillIDStr, ",");
         if (skillList == null || skillList.size() < 1) {
            (new Exception("怪物技能没有配置")).printStackTrace();
         }

         Iterator var29 = skillList.iterator();

         while(var29.hasNext()) {
            Integer skillID = (Integer)var29.next();
            if (!SkillModel.hasModel(skillID.intValue())) {
               (new Exception("怪物 分布 - 技能id不存在")).printStackTrace();
            }
         }

         MonsterDrop md = new MonsterDrop(loopSize, dropStr, "战盟boss");
         md.setType(2);
         GangBossGroup bg = new GangBossGroup();
         bg.setAi(1);
         bg.setAttackDist(attackDis);
         bg.setBornRadius(0);
         bg.setBossRank(2);
         bg.setContributionReq(contributionReq);
         bg.setGiveUpDist(giveUpDis);
         bg.setHeader(header);
         bg.setMapId(mapId);
         bg.setMaxLevel(level);
         bg.setMoveRadius(radius);
         bg.setMaxMoveDist(maxMoveDis);
         bg.setMinAttackDist(minAttackDis);
         bg.setMinLevel(level);
         bg.setName(name);
         bg.setNum(1);
         bg.setRevivalTime(-1L);
         bg.setSerachDist(searchDis);
         bg.setSkillList(skillList);
         bg.setSpeed(speed * 100.0F);
         bg.setStar(star);
         bg.setSummonLevel(sumLevel);
         bg.setSummonNumber(sumNumber);
         bg.setTemplateId(templateid);
         bg.setX(x);
         bg.setY(y);
         bg.setBossId(bossId);
         bg.addDrops(md);
         String[] tmp;
         int j;
         if (dropShowStr.indexOf(";") != -1) {
            tmp = dropShowStr.split(";");

            for(j = 0; j < tmp.length; ++j) {
               String[] itemStr = tmp[j].split(",");
               ItemDataUnit du = new ItemDataUnit(Integer.parseInt(itemStr[0]), 1);
               du.setStatRuleID(Integer.parseInt(itemStr[1]));
               du.setHide(true);
               du.setBind(false);
               Item item = ItemTools.createItem(2, du);
               bg.addDropShowItem(item);
            }
         } else {
            tmp = dropShowStr.split(",");

            for(j = 0; j < tmp.length; ++j) {
               bg.addDropShowItem(ItemTools.createItem(Integer.parseInt(tmp[j]), 1, 2));
            }
         }

         this.bossInfoMap.put(bossId, bg);
      }

   }

   public HashMap getBossMap() {
      return this.bossInfoMap;
   }

   public GangBossGroup getBossInfo(int id) {
      return (GangBossGroup)this.bossInfoMap.get(id);
   }

   public void writeDungeonInfo(Player player) throws Exception {
      Gang gang = player.getGang();
      if (gang != null) {
         RequestDungeonInfo packet = new RequestDungeonInfo();
         packet.writeByte(this.getTemplateID());
         packet.writeDouble((double)gang.getContribution());
         packet.writeByte(this.bossInfoMap.size());
         Iterator it = this.bossInfoMap.values().iterator();

         while(true) {
            while(it.hasNext()) {
               GangBossGroup gb = (GangBossGroup)it.next();
               packet.writeByte(gb.getBossId());
               packet.writeShort(gb.getHeader());
               packet.writeUTF(gb.getName());
               packet.writeShort(gb.getSummonLevel());
               packet.writeInt(gb.getContributionReq());
               packet.writeByte(gang.getLeftSummonTimes(gb));
               ArrayList list = gb.getDropShowList();
               packet.writeByte(list.size());
               Iterator var8 = list.iterator();

               while(var8.hasNext()) {
                  Item item = (Item)var8.next();
                  GetItemStats.writeItem(item, packet);
               }

               GangBoss gangBoss = this.manager.getGangBoss(gang.getId());
               if (gangBoss != null && gangBoss.getBossGroup().getBossId() == gb.getBossId() && !gangBoss.isComplete()) {
                  packet.writeBoolean(true);
                  packet.writeInt(gangBoss.getActiveTime());
               } else {
                  packet.writeBoolean(false);
               }
            }

            player.writePacket(packet);
            packet.destroy();
            packet = null;
            return;
         }
      }
   }

   public DunTimingPanel getTimingPanel() {
      DunTimingPanel dp = new DunTimingPanel();

      try {
         dp.writeByte(4);
         dp.writeUTF(this.popTitle);
         dp.writeUTF(this.popDes);
         dp.writeShort(this.popBack);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return dp;
   }

   public GangBossManager getManager() {
      return this.manager;
   }

   public boolean showDynamicMenu() {
      return false;
   }
}
