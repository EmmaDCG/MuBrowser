package com.mu.game.dungeon;

import com.mu.config.MessageText;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.map.BigMonsterGroup;
import com.mu.game.model.unit.monster.MonsterStar;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.dun.DunLogManager;
import com.mu.game.model.unit.player.dun.DunLogs;
import com.mu.io.game.packet.imp.dungeon.DunTimingPanel;
import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;
import jxl.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DungeonTemplate {
   protected HashMap monsterStarMap = new HashMap();
   protected int templateID = 0;
   protected int timeLimit = 1800;
   protected String name = "";
   protected int type = 1;
   protected int bornX = 0;
   protected int bornY = 0;
   protected int defaultMapID = -1;
   protected Workbook wb = null;
   protected int maxTimes = 0;
   protected int countdownTime = 0;
   private int inspireIngot = 10;
   private int inspireMoney = 200000;
   private Item globalTicket = null;
   private String moneyInspireDes;
   private String ingotInspireDes;
   protected boolean canInspire = false;
   private static Logger logger = LoggerFactory.getLogger(DungeonTemplate.class);

   public abstract void init() throws Exception;

   public DungeonTemplate(int templateID, Workbook wb) {
      this.templateID = templateID;
      this.wb = wb;
      this.initBasicInfo(this.wb.getSheet(1));
   }

   public void initDungeonLevel(Sheet sheet, int index, DungeonLevel dl) throws Exception {
      int level = Tools.getCellIntValue(sheet.getCell("A" + index));
      int minLevel = Tools.getCellIntValue(sheet.getCell("B" + index));
      int maxLevel = Tools.getCellIntValue(sheet.getCell("C" + index));
      int itemReq = Tools.getCellIntValue(sheet.getCell("D" + index));
      String rewardsStr = Tools.getCellValue(sheet.getCell("E" + index));
      String strength = Tools.getCellValue(sheet.getCell("F" + index));
      String levelStr = Tools.getCellValue(sheet.getCell("G" + index));
      String dropStr = Tools.getCellValue(sheet.getCell("H" + index));
      Item ri = null;
      if (ItemModel.hasItemModel(itemReq)) {
         ri = ItemTools.createItem(itemReq, 1, 2);
      }

      dl.setLevel(level);
      dl.setLevelStr(levelStr);
      dl.setMaxLevelReq(maxLevel);
      dl.setMinLevelReq(minLevel);
      dl.setRewardStr(rewardsStr);
      dl.setStrength(strength);
      dl.setReqItem(ri);
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
            dl.addDropItem(item);
         }
      } else {
         tmp = dropStr.split(",");

         for(j = 0; j < tmp.length; ++j) {
            dl.addDropItem(ItemTools.createItem(Integer.parseInt(tmp[j]), 1, 2));
         }
      }

   }

   public boolean isCanInspire() {
      return this.canInspire;
   }

   public void setCanInspire(boolean canInspire) {
      this.canInspire = canInspire;
   }

   public Item getGlobalTicket() {
      return this.globalTicket;
   }

   public void setGlobalTicket(Item globalTicket) {
      this.globalTicket = globalTicket;
   }

   public void initMonsterStar(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         MonsterStar star = MonsterStar.parseStar(sheet, i);
         HashMap map = (HashMap)this.monsterStarMap.get(star.getStar());
         if (map == null) {
            map = new HashMap();
            this.monsterStarMap.put(star.getStar(), map);
         }

         map.put(star.getLevel(), star);
      }

   }

   public MonsterStar getMonsterStar(int star, int level) {
      return (MonsterStar)((HashMap)this.monsterStarMap.get(star)).get(level);
   }

   public Item getTicket(Player player) {
      return null;
   }

   public void initBasicInfo(Sheet sheet) {
      this.name = Tools.getCellValue(sheet.getCell("B2"));
      this.defaultMapID = Tools.getCellIntValue(sheet.getCell("C2"));
      this.bornX = Tools.getCellIntValue(sheet.getCell("D2"));
      this.bornY = Tools.getCellIntValue(sheet.getCell("E2"));
      this.timeLimit = Tools.getCellIntValue(sheet.getCell("F2"));
      this.maxTimes = Tools.getCellIntValue(sheet.getCell("G2"));
      this.countdownTime = Tools.getCellIntValue(sheet.getCell("H2"));
   }

   public int getTimeLimit() {
      return this.timeLimit;
   }

   public void setTimeLimit(int timeLimit) {
      this.timeLimit = timeLimit;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public int getTemplateID() {
      return this.templateID;
   }

   public int getBornX() {
      return this.bornX;
   }

   public void setBornX(int bornX) {
      this.bornX = bornX;
   }

   public int getBornY() {
      return this.bornY;
   }

   public void setBornY(int bornY) {
      this.bornY = bornY;
   }

   public int getDefaultMapID() {
      return this.defaultMapID;
   }

   public void setDefaultMapID(int defaultMapID) {
      this.defaultMapID = defaultMapID;
   }

   public int canEnter(Player player, Item ticket, Object... obj) {
      if (player.isInDungeon()) {
         return 14002;
      } else {
         DunLogManager lm = player.getDunLogsManager();
         DunLogs log = lm.getLog(this.templateID);
         if (log != null && log.getFinishTimes() >= lm.getMaxtimes(this.templateID, 0)) {
            return 14011;
         } else if (this.getPlayerCoolDownTime(player) > 0) {
            return 14012;
         } else {
            if (ticket != null && !player.getBackpack().hasEnoughItem(ticket.getModelID(), ticket.getCount())) {
               Item gTiccket = this.getGlobalTicket();
               if (gTiccket == null) {
                  return 14009;
               }

               if (!player.getBackpack().hasEnoughItem(gTiccket.getModelID(), gTiccket.getCount())) {
                  return 14009;
               }
            }

            return 1;
         }
      }
   }

   public int getInspireIngot() {
      return this.inspireIngot;
   }

   public void setInspireIngot(int inspireGold) {
      this.inspireIngot = inspireGold;
   }

   public int getInspireMoney() {
      return this.inspireMoney;
   }

   public void setInspireMoney(int inspireMoney) {
      this.inspireMoney = inspireMoney;
   }

   public int getCountdownTime() {
      return this.countdownTime;
   }

   public void setCountdownTime(int cd) {
      this.countdownTime = cd;
   }

   public int getPlayerCoolDownTime(Player player) {
      DunLogManager manager = player.getDunLogsManager();
      DunLogs logs = manager.getLog(this.templateID);
      if (logs == null) {
         return -1;
      } else if (this.getCountdownTime() > 0) {
         int cd = this.getCountdownTime() - (int)(System.currentTimeMillis() - logs.getLastFinishTime()) / 1000;
         return cd > 0 ? cd : -1;
      } else {
         return -1;
      }
   }

   public int getPlayerLeftTimes(Player player, int smallId) {
      DunLogManager manager = player.getDunLogsManager();
      DunLogs logs = manager.getLog(this.templateID);
      if (logs == null) {
         return this.getMaxTimes(player, smallId);
      } else {
         int times = this.getMaxTimes(player, smallId) - logs.getFinishTimes();
         return times >= 0 && times <= 999 ? times : 0;
      }
   }

   public int getMaxTimes() {
      return this.maxTimes;
   }

   public int getMaxTimes(Player player, int smallId) {
      return this.maxTimes;
   }

   public void setMaxTimes(int maxTimes) {
      this.maxTimes = maxTimes;
   }

   public void writeDungeonInfo(Player player) throws Exception {
   }

   public String getCanotEnterMessage(int code, Item ticket) {
      switch(code) {
      case 14002:
         return MessageText.getText(14002);
      case 14003:
      case 14004:
      case 14005:
      case 14006:
      case 14007:
      case 14008:
      default:
         return MessageText.getText(code);
      case 14009:
         return MessageText.getText(14009).replace("%s%", ticket.getName());
      case 14010:
         return MessageText.getText(14010).replace("%s%", this.getName());
      case 14011:
         return MessageText.getText(14011).replace("%s%", this.getName());
      case 14012:
         return MessageText.getText(14012).replace("%s%", this.getName());
      }
   }

   public DunTimingPanel getTimingPanel() {
      return null;
   }

   public void clickTimePanel(Player player) {
   }

   public String getMoneyInspireDes() {
      return this.moneyInspireDes;
   }

   public void setMoneyInspireDes(String moneyInspireDes) {
      this.moneyInspireDes = moneyInspireDes;
   }

   public String getIngotInspireDes() {
      return this.ingotInspireDes;
   }

   public void setIngotInspireDes(String ingotInspireDes) {
      this.ingotInspireDes = ingotInspireDes;
   }

   public void writeShortcutBuyTicket(Player player, int key) {
   }

   public void checkMonsterStar(BigMonsterGroup group) {
      for(int i = group.getMinLevel(); i <= group.getMaxLevel(); ++i) {
         MonsterStar ms = this.getMonsterStar(group.getStar(), i);
         if (ms == null) {
            logger.error("monster star not found,template name = {},monster name = {},level = {}", new Object[]{this.getName(), group.getName(), i});
         }
      }

   }

   public abstract boolean showDynamicMenu();
}
