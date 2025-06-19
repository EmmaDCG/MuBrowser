package com.mu.game.dungeon.imp.redfort;

import com.mu.config.MessageText;
import com.mu.game.dungeon.DungeonTemplate;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.ui.dm.DynamicMenuManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.dungeon.DunTimingPanel;
import com.mu.io.game.packet.imp.dungeon.RequestDungeonInfo;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.sys.OpenPanel;
import com.mu.utils.Time;
import com.mu.utils.Tools;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import jxl.Sheet;
import jxl.Workbook;

public class RedFortTemplate extends DungeonTemplate {
   private ArrayList floorList = new ArrayList();
   private ArrayList topRewardList = new ArrayList();
   private HashMap externalMap = new HashMap();
   private RedFortManager redFortManager = new RedFortManager();
   private int minFloor = 1;
   private int maxFloor = 1;
   private int prepareTime = 180;
   private int levelLimit = 20;
   private int activityTime = 0;
   private String des1;
   private String des2;
   private ArrayList rewardList = new ArrayList();
   private String faildTitle;
   private String fialdContent;
   private String successTitle;
   private String successContent;
   private String drawTitle;
   private String drawContent;
   private String quitTitle;
   private String quitContent;
   private String prepareStart;
   private String startStr;
   private String prepareToNext;
   private String endStr;
   private String panelStr;
   private int popBack = -1;
   private String popDes = "";

   public RedFortTemplate(Workbook wb) {
      super(5, wb);
   }

   public void init() throws Exception {
      this.initGeneral(this.wb.getSheet(1));
      this.initFloor(this.wb.getSheet(2));
      this.initExternal(this.wb.getSheet(3));
      this.initPanelInfo(this.wb.getSheet(4));
      this.initText(this.wb.getSheet(5));
      this.initTopReward(this.wb.getSheet(6));
      this.initBrodcast(this.wb.getSheet(7));
   }

   public boolean showDynamicMenu() {
      return false;
   }

   private void initPanelInfo(Sheet sheet) throws Exception {
      this.des1 = Tools.getCellValue(sheet.getCell("A2"));
      this.des2 = Tools.getCellValue(sheet.getCell("B2"));
      String dropStr = Tools.getCellValue(sheet.getCell("C2"));
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
            this.rewardList.add(item);
         }
      } else {
         tmp = dropStr.split(",");

         for(j = 0; j < tmp.length; ++j) {
            this.rewardList.add(ItemTools.createItem(Integer.parseInt(tmp[j]), 1, 2));
         }
      }

   }

   private void initGeneral(Sheet sheet) throws Exception {
      this.prepareTime = Tools.getCellIntValue(sheet.getCell("J2"));
      this.levelLimit = Tools.getCellIntValue(sheet.getCell("K2"));
      this.panelStr = Tools.getCellValue(sheet.getCell("L2"));
      this.activityTime += this.prepareTime;
      this.popBack = Tools.getCellIntValue(sheet.getCell("M2"));
      this.popDes = Tools.getCellValue(sheet.getCell("N2"));
      String timeStr = Tools.getCellValue(sheet.getCell("I2"));
      String[] tmp = timeStr.split(";");

      for(int i = 0; i < tmp.length; ++i) {
         Date date = Time.getDate(tmp[i].trim(), "HH:mm:ss");
         int[] time = new int[3];
         Calendar cd = Calendar.getInstance();
         cd.setTime(date);
         time[0] = cd.get(11);
         time[1] = cd.get(12);
         time[2] = cd.get(13);
         this.redFortManager.addStartTime(time);
      }

   }

   private void initTopReward(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int minTop = Tools.getCellIntValue(sheet.getCell("A" + i));
         int maxTop = Tools.getCellIntValue(sheet.getCell("B" + i));
         String rStr = Tools.getCellValue(sheet.getCell("C" + i));
         RedFortTopReward tr = new RedFortTopReward();
         tr.setMinTop(minTop);
         tr.setMaxTop(maxTop);
         String[] tmp = rStr.split(";");

         for(int j = 0; j < tmp.length; ++j) {
            String[] tmp2 = tmp[j].split(",");
            ItemDataUnit data = new ItemDataUnit(Integer.parseInt(tmp2[0]), Integer.parseInt(tmp2[1]), Integer.parseInt(tmp2[2]) == 1);
            Item item = ItemTools.createItem(2, data);
            tr.addData(data);
            tr.addItem(item);
         }

         this.topRewardList.add(tr);
      }

   }

   private void initText(Sheet sheet) throws Exception {
      this.faildTitle = Tools.getCellValue(sheet.getCell("A2"));
      this.fialdContent = Tools.getCellValue(sheet.getCell("B2"));
      this.successTitle = Tools.getCellValue(sheet.getCell("C2"));
      this.successContent = Tools.getCellValue(sheet.getCell("D2"));
      this.drawTitle = Tools.getCellValue(sheet.getCell("E2"));
      this.drawContent = Tools.getCellValue(sheet.getCell("F2"));
      this.quitTitle = Tools.getCellValue(sheet.getCell("G2"));
      this.quitContent = Tools.getCellValue(sheet.getCell("H2"));
   }

   private void initBrodcast(Sheet sheet) throws Exception {
      this.prepareStart = Tools.getCellValue(sheet.getCell("A2"));
      this.startStr = Tools.getCellValue(sheet.getCell("B2"));
      this.prepareToNext = Tools.getCellValue(sheet.getCell("C2"));
      this.endStr = Tools.getCellValue(sheet.getCell("D2"));
   }

   private void initExternal(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int proType = Tools.getCellIntValue(sheet.getCell("A" + i));
         String tmp = Tools.getCellValue(sheet.getCell("B" + i));
         String[] tmpStr = tmp.split(";");
         ArrayList list = new ArrayList();

         for(int j = 0; j < tmpStr.length; ++j) {
            list.add(Tools.parseExternalEntry(tmpStr[j]));
         }

         this.externalMap.put(proType, list);
      }

   }

   public RedFortTopReward getTopReward(int top) {
      if (top < 1) {
         return null;
      } else {
         Iterator var3 = this.topRewardList.iterator();

         RedFortTopReward tr;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            tr = (RedFortTopReward)var3.next();
         } while(top < tr.getMinTop() || top > tr.getMaxTop());

         return tr;
      }
   }

   public ArrayList getTopRewardItem(int top) {
      RedFortTopReward reward = this.getTopReward(top);
      return reward == null ? null : reward.getItemList();
   }

   public ArrayList getTopRewardItemData(int top) {
      RedFortTopReward reward = this.getTopReward(top);
      return reward == null ? null : reward.getDataList();
   }

   private void initFloor(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int floor = Tools.getCellIntValue(sheet.getCell("A" + i));
         int mapId = Tools.getCellIntValue(sheet.getCell("B" + i));
         String pointStr = Tools.getCellValue(sheet.getCell("C" + i));
         int duration = Tools.getCellIntValue(sheet.getCell("D" + i));
         RedFortFloor rf = new RedFortFloor(floor);
         rf.setDuration(duration);
         rf.setMapId(mapId);
         String[] tmpStr = pointStr.split(";");
         Point[] points = new Point[tmpStr.length];

         for(int j = 0; j < points.length; ++j) {
            String[] pStr = tmpStr[j].split(",");
            points[j] = new Point(Integer.parseInt(pStr[0]), Integer.parseInt(pStr[1]));
         }

         rf.setBornPoints(points);
         if (this.maxFloor < floor) {
            this.maxFloor = floor;
         }

         this.floorList.add(rf);
         this.activityTime += duration;
      }

   }

   public ArrayList getExternalEntryList(int proType) {
      return (ArrayList)this.externalMap.get(proType);
   }

   public int getPrepareTime() {
      return this.prepareTime;
   }

   public RedFortManager getRedFortManager() {
      return this.redFortManager;
   }

   public ArrayList getFloorList() {
      return this.floorList;
   }

   public void setFloorList(ArrayList floorList) {
      this.floorList = floorList;
   }

   public RedFortFloor getFloor(int floor) {
      return floor > this.floorList.size() ? null : (RedFortFloor)this.floorList.get(floor - 1);
   }

   public int getMinFloor() {
      return this.minFloor;
   }

   public void setMinFloor(int minFloor) {
      this.minFloor = minFloor;
   }

   public int getMaxFloor() {
      return this.maxFloor;
   }

   public void setMaxFloor(int maxFloor) {
      this.maxFloor = maxFloor;
   }

   public int getLevelLimit() {
      return this.levelLimit;
   }

   public int canEnter(Player player, Item ticket, Object... obj) {
      return player.getLevel() < this.levelLimit ? 1029 : super.canEnter(player, ticket, obj);
   }

   public String getDes1() {
      return this.des1;
   }

   public String getDes2() {
      return this.des2;
   }

   public ArrayList getRewardList() {
      return this.rewardList;
   }

   public String getCanotEnterMessage(int code, Item ticket) {
      return code == 1029 ? MessageText.getText(1029).replace("%s%", String.valueOf(this.levelLimit)) : super.getCanotEnterMessage(code, ticket);
   }

   public String getFaildTitle() {
      return this.faildTitle;
   }

   public String getFialdContent() {
      return this.fialdContent;
   }

   public String getSuccessTitle() {
      return this.successTitle;
   }

   public String getSuccessContent() {
      return this.successContent;
   }

   public String getDrawTitle() {
      return this.drawTitle;
   }

   public String getDrawContent() {
      return this.drawContent;
   }

   public String getQuitTitle() {
      return this.quitTitle;
   }

   public String getQuitContent() {
      return this.quitContent;
   }

   public int getActiveTime() {
      return this.activityTime;
   }

   public String getPrepareStart() {
      return this.prepareStart;
   }

   public String getStartStr() {
      return this.startStr;
   }

   public String getPrepareToNext() {
      return this.prepareToNext;
   }

   public String getEndStr() {
      return this.endStr;
   }

   public String getPanelStr() {
      return this.panelStr;
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
         dp.writeByte(1);
         dp.writeUTF(this.getName());
         dp.writeUTF(this.popDes);
         dp.writeShort(this.popBack);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return dp;
   }

   public void clickTimePanel(Player player) {
      DynamicMenu menu = DynamicMenuManager.getMenu(12);
      OpenPanel.open(player, menu.getPanelId()[0], menu.getPanelId()[1]);
   }

   public void writeDungeonInfo(Player player) throws Exception {
      RequestDungeonInfo packet = new RequestDungeonInfo();
      packet.writeByte(this.getTemplateID());
      packet.writeUTF(this.getDes1());
      packet.writeUTF(this.getDes2());
      ArrayList dropItem = this.getRewardList();
      packet.writeByte(dropItem.size());
      Iterator var5 = dropItem.iterator();

      while(var5.hasNext()) {
         Item item = (Item)var5.next();
         GetItemStats.writeItem(item, packet);
      }

      player.writePacket(packet);
   }
}
