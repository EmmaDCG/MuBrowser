package com.mu.game.dungeon.imp.bigdevil;

import com.mu.game.dungeon.DungeonReward;
import com.mu.game.dungeon.DungeonTemplate;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.map.BigMonsterGroup;
import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.ui.dm.DynamicMenuManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.top.BigDevilTopInfo;
import com.mu.game.top.DungeonTopManager;
import com.mu.io.game.packet.imp.dungeon.BigDevilTopRequest;
import com.mu.io.game.packet.imp.dungeon.DunTimingPanel;
import com.mu.io.game.packet.imp.dungeon.RequestDungeonInfo;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.sys.OpenPanel;
import com.mu.utils.Time;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import jxl.Sheet;
import jxl.Workbook;

public class BigDevilSquareTemplate extends DungeonTemplate {
   private HashMap levelMap = new HashMap();
   private HashMap rankMap = new HashMap();
   private BigDevilSquareManager bigDevilManager = new BigDevilSquareManager(this);
   private ArrayList topRewardList = new ArrayList();
   private int maxRank = 0;
   private int prepareTime = 300;
   private int maxPlayer = 10;
   private int minLevel = 9999;
   private String prepareStartStr;
   private String startStr;
   private String endStr;
   private String text;
   private String topRewardTitle;
   private String topRewardContent;
   private String mailDes;
   private String enterMsg;
   private String newRecord;
   private String noNewRecord;
   private int popBack = -1;
   private String popDes = "";

   public BigDevilSquareTemplate(Workbook wb) {
      super(6, wb);
   }

   public void init() throws Exception {
      this.initGeneral(this.wb.getSheet(1));
      this.initSqureLevel(this.wb.getSheet(2));
      this.initSquareRank(this.wb.getSheet(3));
      this.initSquareReward(this.wb.getSheet(4));
      this.initBrodcast(this.wb.getSheet(5));
      this.initTopReward(this.wb.getSheet(6));
      this.initMonsterStar(this.wb.getSheet(7));
      this.initMonsters();
   }

   private void initGeneral(Sheet sheet) {
      this.setInspireMoney(Tools.getCellIntValue(sheet.getCell("I2")));
      this.setInspireIngot(Tools.getCellIntValue(sheet.getCell("J2")));
      this.prepareTime = Tools.getCellIntValue(sheet.getCell("K2"));
      this.maxPlayer = Tools.getCellIntValue(sheet.getCell("L2"));
      this.text = Tools.getCellValue(sheet.getCell("P2"));
      this.topRewardTitle = Tools.getCellValue(sheet.getCell("Q2"));
      this.topRewardContent = Tools.getCellValue(sheet.getCell("R2"));
      this.mailDes = Tools.getCellValue(sheet.getCell("S2"));
      this.popBack = Tools.getCellIntValue(sheet.getCell("T2"));
      this.popDes = Tools.getCellValue(sheet.getCell("U2"));
      String timeStr = Tools.getCellValue(sheet.getCell("M2"));
      String[] tmp = timeStr.split(";");

      for(int i = 0; i < tmp.length; ++i) {
         Date date = Time.getDate(tmp[i].trim(), "HH:mm:ss");
         int[] time = new int[3];
         Calendar cd = Calendar.getInstance();
         cd.setTime(date);
         time[0] = cd.get(11);
         time[1] = cd.get(12);
         time[2] = cd.get(13);
         this.bigDevilManager.addStartTime(time);
      }

   }

   private void initBrodcast(Sheet sheet) throws Exception {
      this.prepareStartStr = Tools.getCellValue(sheet.getCell("A2"));
      this.startStr = Tools.getCellValue(sheet.getCell("B2"));
      this.endStr = Tools.getCellValue(sheet.getCell("C2"));
      this.enterMsg = Tools.getCellValue(sheet.getCell("D2"));
      this.newRecord = Tools.getCellValue(sheet.getCell("E2"));
      this.noNewRecord = Tools.getCellValue(sheet.getCell("F2"));
   }

   private void initTopReward(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         String name = Tools.getCellValue(sheet.getCell("B" + i));
         int minTop = Tools.getCellIntValue(sheet.getCell("C" + i));
         int maxTop = Tools.getCellIntValue(sheet.getCell("D" + i));
         String rewardStr = Tools.getCellValue(sheet.getCell("E" + i));
         int sort = Tools.getCellIntValue(sheet.getCell("F" + i));
         BigDevilTopReward br = new BigDevilTopReward();
         br.setMinTop(minTop);
         br.setMaxTop(maxTop);
         br.setName(name);
         br.setSort(sort);
         String[] tmp = rewardStr.split(";");

         for(int j = 0; j < tmp.length; ++j) {
            String[] itemStr = tmp[j].split(",");
            int modelId = Integer.parseInt(itemStr[0]);
            int num = Integer.parseInt(itemStr[1]);
            boolean isBind = Integer.parseInt(itemStr[2]) == 1;
            Item item = ItemTools.createItem(modelId, num, 2);
            item.setBind(isBind);
            ItemDataUnit unit = new ItemDataUnit(modelId, num, isBind);
            br.addRewardItem(item);
            br.addRewardData(unit);
         }

         this.addTopReward(br);
      }

      Collections.sort(this.topRewardList);
   }

   public void addTopReward(BigDevilTopReward br) {
      this.topRewardList.add(br);
   }

   public ArrayList getTopRewardList() {
      return this.topRewardList;
   }

   public BigDevilTopReward getTopReward(int top) {
      if (top > 0) {
         Iterator var3 = this.topRewardList.iterator();

         while(var3.hasNext()) {
            BigDevilTopReward br = (BigDevilTopReward)var3.next();
            if (top >= br.getMinTop() && top <= br.getMaxTop()) {
               return br;
            }
         }
      }

      return (BigDevilTopReward)this.topRewardList.get(this.topRewardList.size() - 1);
   }

   private void initSquareReward(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         DungeonReward re = new DungeonReward();
         int level = Tools.getCellIntValue(sheet.getCell("A" + i));
         int rank = Tools.getCellIntValue(sheet.getCell("B" + i));
         long exp = Tools.getCellLongValue(sheet.getCell("C" + i));
         int money = Tools.getCellIntValue(sheet.getCell("D" + i));
         re.setExp(exp);
         re.setMoney(money);
         String str = Tools.getCellValue(sheet.getCell("E" + i));
         if (str != null && str.trim().length() > 0) {
            String[] tmp = str.split(";");

            for(int j = 0; j < tmp.length; ++j) {
               String[] itemStr = tmp[j].split(",");
               int modelId = Integer.parseInt(itemStr[0]);
               int num = Integer.parseInt(itemStr[1]);
               boolean isBind = Integer.parseInt(itemStr[2]) == 1;
               Item item = ItemTools.createItem(modelId, num, 2);
               item.setBind(isBind);
               ItemDataUnit unit = new ItemDataUnit(modelId, num, isBind);
               re.addItem(item);
               re.addItemData(unit);
            }
         }

         BigDevilSquareLevel dl = this.getDevilSquareLevel(level);
         if (dl != null) {
            dl.addReward(rank, re);
         }
      }

   }

   private void initSquareRank(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         BigDevilSquareRank dr = new BigDevilSquareRank();
         int rank = Tools.getCellIntValue(sheet.getCell("A" + i));
         int minKill = Tools.getCellIntValue(sheet.getCell("B" + i));
         int maxKill = Tools.getCellIntValue(sheet.getCell("C" + i));
         String target = Tools.getCellValue(sheet.getCell("D" + i));
         if (rank > this.maxRank) {
            this.maxRank = rank;
         }

         dr.setRank(rank);
         dr.setMinKill(minKill);
         dr.setMaxKill(maxKill);
         dr.setTarget(target);
         this.rankMap.put(rank, dr);
      }

   }

   public int getMaxRank() {
      return this.maxRank;
   }

   private void initSqureLevel(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         BigDevilSquareLevel dl = new BigDevilSquareLevel();
         this.initDungeonLevel(sheet, i, dl);
         this.levelMap.put(dl.getLevel(), dl);
         if (dl.getMinLevelReq() < this.minLevel) {
            this.minLevel = dl.getMinLevelReq();
         }
      }

   }

   public BigDevilSquareRank getRank(int killNumber) {
      Iterator it = this.rankMap.values().iterator();

      BigDevilSquareRank rank;
      do {
         if (!it.hasNext()) {
            return (BigDevilSquareRank)this.rankMap.get(Integer.valueOf(0));
         }

         rank = (BigDevilSquareRank)it.next();
      } while(rank.getMinKill() > killNumber || rank.getMaxKill() < killNumber);

      return rank;
   }

   private void initMonsters() throws Exception {
      Sheet[] sheets = this.wb.getSheets();

      for(int i = 8; i < sheets.length; ++i) {
         Sheet sheet = sheets[i];
         int rows = sheet.getRows();

         for(int j = 2; j <= rows; ++j) {
            BigDevilMonsterGroup group = new BigDevilMonsterGroup();
            BigMonsterGroup.parseMonsterGroup(group, sheet, j);
            group.setBatch(Tools.getCellIntValue(sheet.getCell("T" + j)));
            group.setLevelId(Tools.getCellIntValue(sheet.getCell("U" + j)));
            BigDevilSquareLevel dl = (BigDevilSquareLevel)this.levelMap.get(group.getLevelId());
            dl.addMonsterGroup(group, group.getBatch());
            this.checkMonsterStar(group);
         }
      }

   }

   public HashMap getLevelMap() {
      return this.levelMap;
   }

   public BigDevilSquareLevel getDevilSquareLevel(int levelId) {
      return (BigDevilSquareLevel)this.levelMap.get(levelId);
   }

   public BigDevilSquareLevel getPlayerFitLevel(Player player) {
      Iterator it = this.levelMap.values().iterator();

      BigDevilSquareLevel dl;
      do {
         if (!it.hasNext()) {
            return null;
         }

         dl = (BigDevilSquareLevel)it.next();
      } while(dl.getMinLevelReq() > player.getLevel() || dl.getMaxLevelReq() < player.getLevel());

      return dl;
   }

   public int canEnter(Player player, Item ticket, Object... obj) {
      BigDevilSquareLevel level = (BigDevilSquareLevel)obj[0];
      if (player.getLevel() < this.minLevel) {
         return 14044;
      } else {
         BigDevilSquareLevel fitLevel = this.getPlayerFitLevel(player);
         if (fitLevel != null && fitLevel.getLevel() == level.getLevel()) {
            if (!this.getBigDevilManager().isOpen()) {
               return 14001;
            } else {
               return this.getBigDevilManager().isBegin() ? 14039 : super.canEnter(player, ticket, obj);
            }
         } else {
            return 14010;
         }
      }
   }

   private int getBdStatus() {
      if (!this.bigDevilManager.isOpen()) {
         return 1;
      } else {
         return this.bigDevilManager.isBegin() ? 3 : 2;
      }
   }

   public void writeTopInfo(Player player) {
      try {
         BigDevilSquareLevel level = this.getPlayerFitLevel(player);
         if (level == null) {
            return;
         }

         BigDevilTopRequest bt = new BigDevilTopRequest();
         CopyOnWriteArrayList list = DungeonTopManager.getBigDevilList();
         if (list == null) {
            bt.writeByte(0);
         } else {
            bt.writeByte(list.size());

            for(int i = 0; i < list.size(); ++i) {
               BigDevilTopInfo info = (BigDevilTopInfo)list.get(i);
               bt.writeDouble((double)info.getRid());
               bt.writeUTF(info.getName());
               bt.writeByte(i + 1);
               bt.writeDouble((double)info.getExp());
            }
         }

         ArrayList rList = this.getTopRewardList();
         bt.writeByte(rList.size());
         Iterator var7 = rList.iterator();

         while(var7.hasNext()) {
            BigDevilTopReward br = (BigDevilTopReward)var7.next();
            bt.writeByte(br.getSort());
            bt.writeUTF(br.getName());
            ArrayList iList = br.getRewardItemList();
            bt.writeByte(iList.size());
            Iterator var10 = iList.iterator();

            while(var10.hasNext()) {
               Item item = (Item)var10.next();
               GetItemStats.writeItem(item, bt);
            }
         }

         bt.writeUTF(this.mailDes);
         player.writePacket(bt);
         bt.destroy();
         bt = null;
      } catch (Exception var11) {
         var11.printStackTrace();
      }

   }

   public void writeDungeonInfo(Player player) throws Exception {
      BigDevilSquareLevel level = this.getPlayerFitLevel(player);
      if (level != null) {
         RequestDungeonInfo packet = new RequestDungeonInfo();
         packet.writeByte(this.getTemplateID());
         packet.writeByte(level.getLevel());
         packet.writeUTF(level.getLevelStr());
         packet.writeUTF(this.getText());
         packet.writeByte(this.getPlayerLeftTimes(player, 0));
         packet.writeByte(this.getBdStatus());
         if (this.getBdStatus() == 1) {
            packet.writeInt(this.bigDevilManager.getNextOpenTimeLeft());
         }

         player.writePacket(packet);
      }
   }

   public boolean showDynamicMenu() {
      return false;
   }

   public int getMinLevel() {
      return this.minLevel;
   }

   public BigDevilSquareManager getBigDevilManager() {
      return this.bigDevilManager;
   }

   public int getPrepareTime() {
      return this.prepareTime;
   }

   public int getMaxPlayer() {
      return this.maxPlayer;
   }

   public String getPrepareStartStr() {
      return this.prepareStartStr;
   }

   public String getEnterMsg() {
      return this.enterMsg;
   }

   public String getStartStr() {
      return this.startStr;
   }

   public String getEndStr() {
      return this.endStr;
   }

   public String getText() {
      return this.text;
   }

   public String getTopRewardTitle() {
      return this.topRewardTitle;
   }

   public String getTopRewardContent() {
      return this.topRewardContent;
   }

   public int getPopBack() {
      return this.popBack;
   }

   public String getPopDes() {
      return this.popDes;
   }

   public DunTimingPanel getTimingPanel() {
      DunTimingPanel dp = new DunTimingPanel();

      try {
         dp.writeByte(2);
         dp.writeUTF(this.getName());
         dp.writeUTF(this.popDes);
         dp.writeShort(this.popBack);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return dp;
   }

   public String getNewRecord() {
      return this.newRecord;
   }

   public String getNoNewRecord() {
      return this.noNewRecord;
   }

   public int getMaxTimes(Player player, int smallId) {
      int times = super.getMaxTimes(player, smallId);
      int recoverTimes = player.getOffLineManager().getRecoverTimes(this.getTemplateID());
      return times + recoverTimes;
   }

   public void clickTimePanel(Player player) {
      DynamicMenu menu = DynamicMenuManager.getMenu(13);
      OpenPanel.open(player, menu.getPanelId()[0], menu.getPanelId()[1]);
   }
}
