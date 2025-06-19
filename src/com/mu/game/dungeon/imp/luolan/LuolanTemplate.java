package com.mu.game.dungeon.imp.luolan;

import com.mu.game.dungeon.DungeonTemplate;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.item.Item;
import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.ui.dm.DynamicMenuManager;
import com.mu.game.model.unit.monster.MonsterStar;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.dungeon.DunTimingPanel;
import com.mu.io.game.packet.imp.sys.OpenPanel;
import com.mu.utils.Tools;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.HashMap;
import jxl.Sheet;
import jxl.Workbook;

public class LuolanTemplate extends DungeonTemplate {
   private HashMap gateMap = new HashMap();
   private HashMap bornMap = new HashMap();
   private HashMap materialMap = new HashMap();
   private Polygon gearArea = null;
   private long gTime = 5000L;
   private String gText = "";
   private LuolanManager manager;
   private int marktimes = 20;
   private HashMap panelModelMap = new HashMap();
   private HashMap buffMap = new HashMap();
   private HashMap warPostNameMap = new HashMap();
   private ArrayList dailyItemList = null;
   private ArrayList dailyUnitList = null;
   private ArrayList victoryItemList = null;
   private ArrayList victoryUnitList = null;
   private ArrayList failItemList = null;
   private ArrayList failUnitList = null;
   private HashMap postTitleMap = new HashMap();
   private HashMap titlePostMap = new HashMap();
   private String popDes = null;
   private int popBack = -1;
   private String qualification1 = null;
   private String qualification2 = null;
   private String rule = null;
   private String invitationText = null;
   private String broadcastWhenOpen = null;
   private String breakGate = null;
   private String occupyRevivalStatue = null;
   private String occupyGear = null;
   private String prepareNotice = null;
   private String startStr = null;
   private String markStr = null;
   private String hasVictory = null;
   private String noVictory = null;
   private String castellanOnline = null;
   private String victoryMailTitle = null;
   private String victoryMailContent = null;
   private String failMailTitle = null;
   private String failMailContent = null;

   public LuolanTemplate(Workbook wb) {
      super(9, wb);
   }

   public void init() throws Exception {
      this.initOthers(this.wb.getSheet(1));
      this.initBornPoints(this.wb.getSheet(2));
      this.initMonsterStar(this.wb.getSheet(4));
      this.initGate(this.wb.getSheet(3));
      this.initMaterial(this.wb.getSheet(5));
      this.initGearArea(this.wb.getSheet(6));
      this.initPanelModel(this.wb.getSheet(7));
      this.initWarPostNameMap(this.wb.getSheet(8));
      this.initReward(this.wb.getSheet(9));
      this.initTitle(this.wb.getSheet(10));
      this.initBuff(this.wb.getSheet(11));
      this.initDes(this.wb.getSheet(12));
      this.manager = new LuolanManager(this);
      this.manager.init();
   }

   public boolean showDynamicMenu() {
      return false;
   }

   private void initDes(Sheet sheet) throws Exception {
      this.qualification1 = Tools.getCellValue(sheet.getCell("B2"));
      this.qualification2 = Tools.getCellValue(sheet.getCell("B3"));
      this.rule = Tools.getCellValue(sheet.getCell("B4"));
      this.invitationText = Tools.getCellValue(sheet.getCell("B5"));
      this.broadcastWhenOpen = Tools.getCellValue(sheet.getCell("B6"));
      this.breakGate = Tools.getCellValue(sheet.getCell("B7"));
      this.occupyRevivalStatue = Tools.getCellValue(sheet.getCell("B8"));
      this.occupyGear = Tools.getCellValue(sheet.getCell("B9"));
      this.prepareNotice = Tools.getCellValue(sheet.getCell("B10"));
      this.startStr = Tools.getCellValue(sheet.getCell("B11"));
      this.markStr = Tools.getCellValue(sheet.getCell("B12"));
      this.hasVictory = Tools.getCellValue(sheet.getCell("B13"));
      this.noVictory = Tools.getCellValue(sheet.getCell("B14"));
      this.castellanOnline = Tools.getCellValue(sheet.getCell("B15"));
      this.victoryMailTitle = Tools.getCellValue(sheet.getCell("B16"));
      this.victoryMailContent = Tools.getCellValue(sheet.getCell("B17"));
      this.failMailTitle = Tools.getCellValue(sheet.getCell("B18"));
      this.failMailContent = Tools.getCellValue(sheet.getCell("B19"));
   }

   private void initBuff(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int wap = Tools.getCellIntValue(sheet.getCell("A" + i));
         int id = Tools.getCellIntValue(sheet.getCell("B" + i));
         int level = Tools.getCellIntValue(sheet.getCell("C" + i));
         this.buffMap.put(wap, new int[]{id, level});
      }

   }

   public String getOccupyGear() {
      return this.occupyGear;
   }

   private void initTitle(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int post = Tools.getCellIntValue(sheet.getCell("A" + i));
         int titleId = Tools.getCellIntValue(sheet.getCell("B" + i));
         this.postTitleMap.put(post, titleId);
         this.titlePostMap.put(titleId, post);
      }

   }

   public int getTitleId(int warPost) {
      return ((Integer)this.postTitleMap.get(warPost)).intValue();
   }

   public HashMap getAllTitle() {
      return this.postTitleMap;
   }

   public boolean isGangWarTitle(int titleId) {
      return this.titlePostMap.containsKey(titleId);
   }

   private void initOthers(Sheet sheet) throws Exception {
      this.marktimes = Tools.getCellIntValue(sheet.getCell("I2"));
      this.popBack = Tools.getCellIntValue(sheet.getCell("J2"));
      this.popDes = Tools.getCellValue(sheet.getCell("K2"));
   }

   public void initReward(Sheet sheet) throws Exception {
      String dailyStr = Tools.getCellValue(sheet.getCell("B2"));
      this.dailyItemList = Tools.parseItemList(dailyStr);
      this.dailyUnitList = Tools.parseItemDataUnitList(dailyStr);
      String victoryStr = Tools.getCellValue(sheet.getCell("B3"));
      this.victoryItemList = Tools.parseItemList(victoryStr);
      this.victoryUnitList = Tools.parseItemDataUnitList(victoryStr);
      String failStr = Tools.getCellValue(sheet.getCell("B4"));
      this.failItemList = Tools.parseItemList(failStr);
      this.failUnitList = Tools.parseItemDataUnitList(failStr);
   }

   public void initGearArea(Sheet sheet) throws Exception {
      Point[] points = new Point[]{this.parsePoint(Tools.getCellValue(sheet.getCell("A2"))), this.parsePoint(Tools.getCellValue(sheet.getCell("B2"))), this.parsePoint(Tools.getCellValue(sheet.getCell("C2"))), this.parsePoint(Tools.getCellValue(sheet.getCell("D2")))};
      this.gTime = (long)Tools.getCellIntValue(sheet.getCell("E2"));
      this.gText = Tools.getCellValue(sheet.getCell("F2"));
      int[] xPoints = new int[]{points[0].x, points[1].x, points[2].x, points[3].x};
      int[] yPoints = new int[]{points[0].y, points[1].y, points[2].y, points[3].y};
      this.gearArea = new Polygon(xPoints, yPoints, 4);
   }

   private Point parsePoint(String str) throws Exception {
      String[] pStr = str.split(",");
      return new Point(Integer.parseInt(pStr[0]), Integer.parseInt(pStr[1]));
   }

   public void initWarPostNameMap(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int id = Tools.getCellIntValue(sheet.getCell("A" + i));
         String name = Tools.getCellValue(sheet.getCell("B" + i));
         this.warPostNameMap.put(id, name);
      }

   }

   public String getWarPostName(int post) {
      String name = (String)this.warPostNameMap.get(post);
      return name == null ? (String)this.warPostNameMap.get(Integer.valueOf(4)) : name;
   }

   public void initPanelModel(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int post = Tools.getCellIntValue(sheet.getCell("A" + i));
         String[] tmp = Tools.getCellValue(sheet.getCell("B" + i)).split(";");

         for(int j = 0; j < tmp.length; ++j) {
            String[] model = tmp[j].split(",");
            int pro = Integer.parseInt(model[0]);
            int mid = Integer.parseInt(model[1]);
            int zoom = Integer.parseInt(model[2]);
            HashMap map = (HashMap)this.panelModelMap.get(post);
            if (map == null) {
               map = new HashMap();
               this.panelModelMap.put(post, map);
            }

            map.put(pro, new int[]{mid, zoom});
         }
      }

   }

   public void initMaterial(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int id = Tools.getCellIntValue(sheet.getCell("A" + i));
         int templateId = Tools.getCellIntValue(sheet.getCell("B" + i));
         int x = Tools.getCellIntValue(sheet.getCell("C" + i));
         int y = Tools.getCellIntValue(sheet.getCell("D" + i));
         String[] face = Tools.getCellValue(sheet.getCell("E" + i)).split(",");
         String[] fwPoint = Tools.getCellValue(sheet.getCell("G" + i)).split(",");
         LuolanMaterialGroup group = new LuolanMaterialGroup();
         group.setX(x);
         group.setY(y);
         group.setTemplateID(templateId);
         group.setFace(new int[]{Integer.parseInt(face[0]), Integer.parseInt(face[1])});
         group.setCollectionText(Tools.getCellValue(sheet.getCell("F" + i)));
         group.setFindwayPoint(new Point(Integer.parseInt(fwPoint[0]), Integer.parseInt(fwPoint[1])));
         this.materialMap.put(id, group);
      }

   }

   public void initBornPoints(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int id = Tools.getCellIntValue(sheet.getCell("A" + i));
         String[] point = Tools.getCellValue(sheet.getCell("B" + i)).split(",");
         this.bornMap.put(id, new Point(Integer.parseInt(point[0]), Integer.parseInt(point[1])));
      }

   }

   public void initGate(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         GateData gd = new GateData();
         gd.setId(Tools.getCellIntValue(sheet.getCell("A" + i)));
         gd.setName(Tools.getCellValue(sheet.getCell("B" + i)));
         gd.setX(Tools.getCellIntValue(sheet.getCell("C" + i)));
         gd.setY(Tools.getCellIntValue(sheet.getCell("D" + i)));
         gd.setTemplateId(Tools.getCellIntValue(sheet.getCell("F" + i)));
         gd.setStar(Tools.getCellIntValue(sheet.getCell("G" + i)));
         gd.setMinLevel(Tools.getCellIntValue(sheet.getCell("H" + i)));
         gd.setMaxLevel(gd.getMinLevel());
         String blockStr = Tools.getCellValue(sheet.getCell("E" + i));
         String[] bs = blockStr.split(";");
         ArrayList blockList = new ArrayList(bs.length);

         String[] fwTmpPoint;
         for(int j = 0; j < bs.length; ++j) {
            fwTmpPoint = bs[j].split(",");
            blockList.add(new int[]{Integer.parseInt(fwTmpPoint[0]), Integer.parseInt(fwTmpPoint[1])});
         }

         gd.setBlockList(blockList);
         String[] faceStr = Tools.getCellValue(sheet.getCell("I" + i)).split(",");
         gd.setFace(new int[]{Integer.parseInt(faceStr[0]), Integer.parseInt(faceStr[1])});
         gd.setAi(0);
         fwTmpPoint = Tools.getCellValue(sheet.getCell("J" + i)).split(",");
         gd.setFindwayPoint(new Point(Integer.parseInt(fwTmpPoint[0]), Integer.parseInt(fwTmpPoint[1])));
         this.gateMap.put(gd.getId(), gd);
      }

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

   public long getgTime() {
      return this.gTime;
   }

   public String getgText() {
      return this.gText;
   }

   public HashMap getGateMap() {
      return this.gateMap;
   }

   public HashMap getBornMap() {
      return this.bornMap;
   }

   public HashMap getMaterialMap() {
      return this.materialMap;
   }

   public Polygon getGearArea() {
      return this.gearArea;
   }

   public LuolanManager getManager() {
      return this.manager;
   }

   public int getMarktimes() {
      return this.marktimes;
   }

   public ArrayList getDailyItemList() {
      return this.dailyItemList;
   }

   public ArrayList getDailyUnitList() {
      return this.dailyUnitList;
   }

   public String getQualification1() {
      return this.qualification1;
   }

   public String getQualification2() {
      return this.qualification2;
   }

   public String getRule() {
      return this.rule;
   }

   public ArrayList getVictoryItemList() {
      return this.victoryItemList;
   }

   public ArrayList getVictoryUnitList() {
      return this.victoryUnitList;
   }

   public ArrayList getFailItemList() {
      return this.failItemList;
   }

   public ArrayList getFailUnitList() {
      return this.failUnitList;
   }

   public int[] getPanelModel(int profession, int post) {
      int[] models = (int[])((HashMap)this.panelModelMap.get(post)).get(profession);
      return models == null ? (int[])((HashMap)this.panelModelMap.get(Integer.valueOf(3))).get(Integer.valueOf(0)) : models;
   }

   public String getInvitationText() {
      return this.invitationText;
   }

   public void clickTimePanel(Player player) {
      DynamicMenu menu = DynamicMenuManager.getMenu(22);
      OpenPanel.open(player, menu.getPanelId()[0], menu.getPanelId()[1]);
   }

   public String getBreakGate() {
      return this.breakGate;
   }

   public String getOccupyRevivalStatue() {
      return this.occupyRevivalStatue;
   }

   public String getBroadcastWhenOpen() {
      return this.broadcastWhenOpen;
   }

   public String getPrepareNotice() {
      return this.prepareNotice;
   }

   public DunTimingPanel getTimingPanel() {
      DunTimingPanel dp = new DunTimingPanel();

      try {
         dp.writeByte(3);
         dp.writeUTF(this.getName());
         dp.writeUTF(this.popDes);
         dp.writeShort(this.popBack);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return dp;
   }

   public int canEnter(Player player, Item ticket, Object... obj) {
      Gang gang = (Gang)obj[0];
      Luolan luolan = (Luolan)obj[1];
      if (gang == null) {
         return 9034;
      } else if (luolan == null) {
         return 9049;
      } else {
         int top = luolan.getGangTop(gang.getId());
         return top == -1 ? 9050 : super.canEnter(player, ticket, obj);
      }
   }

   public int getWarPostByTitle(int titleId) {
      Integer in = (Integer)this.titlePostMap.get(titleId);
      return in == null ? -1 : in.intValue();
   }

   public String getStartStr() {
      return this.startStr;
   }

   public int[] getBuff(int post) {
      return (int[])this.buffMap.get(post);
   }

   public HashMap getBuffMap() {
      return this.buffMap;
   }

   public String getMarkStr() {
      return this.markStr;
   }

   public String getHasVictory() {
      return this.hasVictory;
   }

   public String getNoVictory() {
      return this.noVictory;
   }

   public String getCastellanOnline() {
      return this.castellanOnline;
   }

   public String getVictoryMailTitle() {
      return this.victoryMailTitle;
   }

   public String getVictoryMailContent() {
      return this.victoryMailContent;
   }

   public String getFailMailTitle() {
      return this.failMailTitle;
   }

   public String getFailMailContent() {
      return this.failMailContent;
   }
}
