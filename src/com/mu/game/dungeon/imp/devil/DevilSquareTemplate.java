package com.mu.game.dungeon.imp.devil;

import com.mu.game.dungeon.DungeonReward;
import com.mu.game.dungeon.DungeonTemplate;
import com.mu.game.dungeon.MultiReceiveInfo;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.mall.ShortcutBuyPanel;
import com.mu.game.model.map.BigMonsterGroup;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.dun.DunLogManager;
import com.mu.game.model.unit.player.dun.DunLogs;
import com.mu.game.model.vip.effect.VIPEffectType;
import com.mu.io.game.packet.imp.dungeon.RequestDungeonInfo;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.mall.OpenShortcutBuyAndUse;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import jxl.Sheet;
import jxl.Workbook;

public class DevilSquareTemplate extends DungeonTemplate {
   private HashMap levelMap = new HashMap();
   private HashMap rankMap = new HashMap();
   private ArrayList multiReciveList = new ArrayList();
   private int maxRank = 0;
   private String playDes;

   public DevilSquareTemplate(Workbook wb) {
      super(2, wb);
      this.canInspire = true;
   }

   public void init() throws Exception {
      this.initGeneral(this.wb.getSheet(1));
      this.initSqureLevel(this.wb.getSheet(2));
      this.initSquareRank(this.wb.getSheet(3));
      this.initSquareReward(this.wb.getSheet(4));
      this.initMultiRecive(this.wb.getSheet(5));
      this.initMonsterStar(this.wb.getSheet(6));
      this.initMonsters();
   }

   private void initGeneral(Sheet sheet) {
      this.setInspireMoney(Tools.getCellIntValue(sheet.getCell("I2")));
      this.setInspireIngot(Tools.getCellIntValue(sheet.getCell("J2")));
      this.setMoneyInspireDes(Tools.getCellValue(sheet.getCell("K2")));
      this.setIngotInspireDes(Tools.getCellValue(sheet.getCell("L2")));
      this.playDes = Tools.getCellValue(sheet.getCell("M2"));
      int gtId = Tools.getCellIntValue(sheet.getCell("N2"));
      this.setGlobalTicket(ItemTools.createItem(gtId, 1, 2));
   }

   private void initMultiRecive(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int index = Tools.getCellIntValue(sheet.getCell("A" + i));
         int times = Tools.getCellIntValue(sheet.getCell("B" + i));
         int ingot = Tools.getCellIntValue(sheet.getCell("C" + i));
         int vipLevel = Tools.getCellIntValue(sheet.getCell("D" + i));
         String str = Tools.getCellValue(sheet.getCell("E" + i));
         String rt = Tools.getCellValue(sheet.getCell("F" + i));
         MultiReceiveInfo info = new MultiReceiveInfo();
         info.setIndex(index);
         info.setTimes(times);
         info.setIngot(ingot);
         info.setVipLevel(vipLevel);
         info.setReceiveName(str);
         info.setReceiveTitle(rt);
         this.multiReciveList.add(info);
      }

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

         DevilSquareLevel dl = this.getDevilSquareLevel(level);
         if (dl != null) {
            dl.addReward(rank, re);
         }
      }

   }

   private void initSquareRank(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         DevilSquareRank dr = new DevilSquareRank();
         int rank = Tools.getCellIntValue(sheet.getCell("A" + i));
         int minKill = Tools.getCellIntValue(sheet.getCell("B" + i));
         int maxKill = Tools.getCellIntValue(sheet.getCell("C" + i));
         String target = Tools.getCellValue(sheet.getCell("D" + i));
         int buffId = Tools.getCellIntValue(sheet.getCell("F" + i));
         int buffLevel = Tools.getCellIntValue(sheet.getCell("G" + i));
         String buffDes = Tools.getCellValue(sheet.getCell("H" + i));
         String ls = Tools.getCellValue(sheet.getCell("I" + i));
         if (rank > this.maxRank) {
            this.maxRank = rank;
         }

         dr.setRank(rank);
         dr.setMinKill(minKill);
         dr.setMaxKill(maxKill);
         dr.setTarget(target);
         dr.setBuffId(buffId);
         dr.setBuffLevel(buffLevel);
         dr.setExpAdditionDes(buffDes);
         dr.setLevelUpStr(ls);
         this.rankMap.put(rank, dr);
      }

   }

   public int getMaxRank() {
      return this.maxRank;
   }

   public void writeShortcutBuyTicket(Player player, int key) {
      try {
         DevilSquareLevel bl = this.getPlayerFitLevel(player);
         if (bl == null) {
            return;
         }

         OpenShortcutBuyAndUse osbu = new OpenShortcutBuyAndUse();
         osbu.writeInt(key);
         osbu.writeUTF(bl.getShortcutBuyTitle());
         TreeMap map = bl.getBuyItemMap();
         osbu.writeByte(map.size());
         Iterator it = map.values().iterator();

         while(it.hasNext()) {
            GetItemStats.writeItem((Item)it.next(), osbu);
         }

         player.writePacket(osbu);
         osbu.destroy();
         osbu = null;
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   private void initSqureLevel(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         DevilSquareLevel dl = new DevilSquareLevel();
         this.initDungeonLevel(sheet, i, dl);
         this.levelMap.put(dl.getLevel(), dl);
         dl.setShortcutBuyTitle(Tools.getCellValue(sheet.getCell("K" + i)));
         String canSellStr = Tools.getCellValue(sheet.getCell("I" + i));
         int j;
         if (canSellStr != null && !canSellStr.trim().equals("")) {
            String[] tmp = canSellStr.trim().split(",");
            int[] in = new int[tmp.length];

            for(j = 0; j < in.length; ++j) {
               in[j] = Integer.parseInt(tmp[j]);
            }

            dl.setCanSellItem(in);
         }

         String buyStr = Tools.getCellValue(sheet.getCell("J" + i));
         if (buyStr != null && !buyStr.trim().equals("")) {
            String[] tmp = buyStr.trim().split(";");

            for(j = 0; j < tmp.length; ++j) {
               String[] iStr = tmp[j].split(",");
               int modelId = Integer.parseInt(iStr[0]);
               int price = Integer.parseInt(iStr[1]);
               boolean isBind = Integer.parseInt(iStr[2]) == 1;
               int moneyType = Integer.parseInt(iStr[3]);
               Item item = ShortcutBuyPanel.addSellItem("DevilSquare ", modelId, price, moneyType, isBind, -1L);
               dl.addBuyItem(j, item);
            }
         }
      }

   }

   public DevilSquareRank getRank(int killNumber) {
      Iterator it = this.rankMap.values().iterator();

      DevilSquareRank rank;
      do {
         if (!it.hasNext()) {
            return (DevilSquareRank)this.rankMap.get(Integer.valueOf(0));
         }

         rank = (DevilSquareRank)it.next();
      } while(rank.getMinKill() > killNumber || rank.getMaxKill() < killNumber);

      return rank;
   }

   private void initMonsters() throws Exception {
      Sheet[] sheets = this.wb.getSheets();

      for(int i = 7; i < sheets.length; ++i) {
         Sheet sheet = sheets[i];
         int rows = sheet.getRows();

         for(int j = 2; j <= rows; ++j) {
            DevilMonsterGroup group = new DevilMonsterGroup();
            BigMonsterGroup.parseMonsterGroup(group, sheet, j);
            group.setBatch(Tools.getCellIntValue(sheet.getCell("T" + j)));
            group.setLevelId(Tools.getCellIntValue(sheet.getCell("U" + j)));
            DevilSquareLevel dl = (DevilSquareLevel)this.levelMap.get(group.getLevelId());
            dl.addMonsterGroup(group, group.getBatch());
            this.checkMonsterStar(group);
         }
      }

   }

   public HashMap getLevelMap() {
      return this.levelMap;
   }

   public DevilSquareLevel getDevilSquareLevel(int levelId) {
      return (DevilSquareLevel)this.levelMap.get(levelId);
   }

   public DevilSquareLevel getPlayerFitLevel(Player player) {
      Iterator it = this.levelMap.values().iterator();

      DevilSquareLevel dl;
      do {
         if (!it.hasNext()) {
            return null;
         }

         dl = (DevilSquareLevel)it.next();
      } while(dl.getMinLevelReq() > player.getLevel() || dl.getMaxLevelReq() < player.getLevel());

      return dl;
   }

   public int canEnter(Player player, Item ticket, Object... obj) {
      DevilSquareLevel level = (DevilSquareLevel)obj[0];
      if (this.getPlayerFitLevel(player).getLevel() != level.getLevel()) {
         return 14010;
      } else {
         DunLogManager lm = player.getDunLogsManager();
         DunLogs log = lm.getLog(this.templateID);
         return log != null && log.notReceivd() ? 14043 : super.canEnter(player, ticket, obj);
      }
   }

   public boolean showDynamicMenu() {
      return false;
   }

   public MultiReceiveInfo getMultiReceiveInfo(int index) {
      return index >= 0 && index <= this.multiReciveList.size() ? (MultiReceiveInfo)this.multiReciveList.get(index) : null;
   }

   public ArrayList getMultiReciveList() {
      return this.multiReciveList;
   }

   public void writeDungeonInfo(Player player) throws Exception {
      DevilSquareLevel dl = this.getPlayerFitLevel(player);
      if (dl != null) {
         RequestDungeonInfo packet = new RequestDungeonInfo();
         packet.writeByte(this.getTemplateID());
         packet.writeByte(dl.getLevel());
         packet.writeUTF(dl.getLevelStr());
         packet.writeUTF(dl.getStrength());
         ArrayList list = dl.getDropList();
         packet.writeByte(list.size());
         Iterator var6 = list.iterator();

         Item ticket;
         while(var6.hasNext()) {
            ticket = (Item)var6.next();
            GetItemStats.writeItem(ticket, packet);
         }

         ticket = dl.getReqItem();
         if (player.getBackpack().hasEnoughItem(ticket.getModel().getID(), ticket.getCount())) {
            GetItemStats.writeItem(ticket, packet);
         } else {
            Item gTicket = this.getGlobalTicket();
            if (gTicket != null && player.getBackpack().hasEnoughItem(gTicket.getModel().getID(), gTicket.getCount())) {
               GetItemStats.writeItem(gTicket, packet);
            } else {
               GetItemStats.writeItem(ticket, packet);
            }
         }

         packet.writeByte(this.getPlayerLeftTimes(player, 0));
         packet.writeUTF(this.playDes);
         player.writePacket(packet);
      }
   }

   public int getMaxTimes(Player player, int smallId) {
      int times = super.getMaxTimes(player, smallId) + player.getVIPManager().getEffectIntegerValue(VIPEffectType.VE_25);
      int recoverTimes = player.getOffLineManager().getRecoverTimes(this.getTemplateID());
      return times + recoverTimes;
   }
}
