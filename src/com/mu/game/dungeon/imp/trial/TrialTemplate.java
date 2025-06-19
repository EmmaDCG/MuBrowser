package com.mu.game.dungeon.imp.trial;

import com.mu.game.dungeon.DungeonTemplate;
import com.mu.game.model.equip.external.ExternalEntry;
import com.mu.game.model.guide.arrow.ArrowGuideManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.map.BigMonsterGroup;
import com.mu.game.model.trial.TrialConfigs;
import com.mu.game.model.unit.CreatureTemplate;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.dun.DunLogManager;
import com.mu.game.model.unit.player.dun.DunLogs;
import com.mu.io.game.packet.imp.dungeon.RequestDungeonInfo;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import jxl.Sheet;
import jxl.Workbook;

public class TrialTemplate extends DungeonTemplate {
   private HashMap monsterMap = new HashMap();
   private HashMap trialLevelMap = new HashMap();
   private String broadcastContent;
   private String linkStr;
   private int maxLevel = 0;

   public TrialTemplate(Workbook wb) {
      super(3, wb);
   }

   public void init() throws Exception {
      this.initGeneral(this.wb.getSheet(1));
      this.initTrialLevel(this.wb.getSheet(2));
      this.initMonsterStar(this.wb.getSheet(3));
      this.initMonsters();
   }

   private void initGeneral(Sheet sheet) {
      this.broadcastContent = Tools.getCellValue(sheet.getCell("I2"));
      this.linkStr = Tools.getCellValue(sheet.getCell("J2"));
   }

   private void initTrialLevel(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         TrialLevel tl = new TrialLevel(Tools.getCellIntValue(sheet.getCell("A" + i)), Tools.getCellIntValue(sheet.getCell("B" + i)));
         tl.setTargetDes(Tools.getCellValue(sheet.getCell("C" + i)));
         this.trialLevelMap.put(tl.getTrialLevel(), tl);
      }

   }

   private void initMonsters() throws Exception {
      Sheet[] sheets = this.wb.getSheets();

      for(int i = 4; i < sheets.length; ++i) {
         Sheet sheet = sheets[i];
         int rows = sheet.getRows();

         for(int j = 2; j <= rows; ++j) {
            TrialMonsterGroup group = new TrialMonsterGroup();
            BigMonsterGroup.parseMonsterGroup(group, sheet, j);
            group.setTrialLevel(Tools.getCellIntValue(sheet.getCell("T" + j)));
            group.setFace(BigMonsterGroup.parseFace(Tools.getCellValue(sheet.getCell("V" + j))));
            group.setZoom(Tools.getCellIntValue(sheet.getCell("W" + j)));
            group.setBroadCast(Tools.getCellIntValue(sheet.getCell("X" + j)) == 1);
            this.monsterMap.put(group.getTrialLevel(), group);
            if (group.getTrialLevel() > this.maxLevel) {
               this.maxLevel = group.getTrialLevel();
            }

            this.checkMonsterStar(group);
         }
      }

   }

   public int getMaxLevel() {
      return this.maxLevel;
   }

   public int canEnter(Player player, Item ticket, Object... obj) {
      int level = ((Integer)obj[0]).intValue();
      return level > TrialConfigs.getMaxLevel() ? 14032 : super.canEnter(player, ticket, obj);
   }

   public TrialLevel getTrialLevel(int level) {
      return (TrialLevel)this.trialLevelMap.get(level);
   }

   public TrialMonsterGroup getMonsterGroup(int level) {
      return (TrialMonsterGroup)this.monsterMap.get(level);
   }

   public void writeDungeonInfo(Player player) throws Exception {
      int warComment = player.getWarComment();
      TrialConfigs curConfig = TrialConfigs.getConfig(warComment);
      TrialConfigs nextConfig = TrialConfigs.getConfig(warComment + 1);
      TrialMonsterGroup mg = this.getMonsterGroup(warComment + 1);
      if (mg == null) {
         mg = this.getMonsterGroup(warComment);
      }

      RequestDungeonInfo packet = new RequestDungeonInfo();
      packet.writeByte(this.getTemplateID());
      packet.writeUTF(mg.getName());
      packet.writeShort(mg.getModelId());
      ArrayList list = CreatureTemplate.getTemplate(mg.getTemplateId()).getEquipEntry();
      packet.writeByte(list.size());
      Iterator var9 = list.iterator();

      while(var9.hasNext()) {
         ExternalEntry entry = (ExternalEntry)var9.next();
         packet.writeByte(entry.getType());
         packet.writeShort(entry.getModelID());
         packet.writeShort(entry.getEffectID());
      }

      packet.writeByte(mg.getZoom());
      DunLogManager dm = player.getDunLogsManager();
      DunLogs log = dm.getLog(this.getTemplateID());
      packet.writeByte(log == null ? 0 : log.getFinishTimes());
      packet.writeByte(this.getMaxTimes(player, 0));
      if (nextConfig == null) {
         packet.writeByte(1);
      } else {
         packet.writeByte(2);
      }

      if (curConfig == null) {
         curConfig = TrialConfigs.getZeroConfig();
      }

      packet.writeShort(curConfig.getLevel());
      packet.writeShort(curConfig.getIcon());
      packet.writeUTF(curConfig.getName());
      packet.writeShort(curConfig.getExpRate());
      packet.writeShort(curConfig.getMoneyRate());
      if (nextConfig != null) {
         packet.writeShort(nextConfig.getLevel());
         packet.writeShort(nextConfig.getIcon());
         packet.writeUTF(nextConfig.getName());
         packet.writeShort(nextConfig.getExpRate());
         packet.writeShort(nextConfig.getMoneyRate());
      }

      player.writePacket(packet);
      ArrowGuideManager.pushArrow(player, 18, (String)null);
   }

   public String getBroadcastContent() {
      return this.broadcastContent;
   }

   public String getLinkStr() {
      return this.linkStr;
   }

   public boolean showDynamicMenu() {
      return false;
   }
}
