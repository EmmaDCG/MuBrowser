package com.mu.config;

import com.mu.game.CenterManager;
import com.mu.game.dungeon.DungeonManager;
import com.mu.game.model.chat.ChatChannelInfo;
import com.mu.game.model.chat.ChatLinkInfo;
import com.mu.game.model.chat.ChatProcess;
import com.mu.game.model.chat.newlink.NewCharactorLink;
import com.mu.game.model.chat.newlink.NewChatLink;
import com.mu.game.model.chat.newlink.NewItemLink;
import com.mu.game.model.chat.newlink.NewJoinGangLink;
import com.mu.game.model.chat.newlink.NewOpenPanelLink;
import com.mu.game.model.drop.model.WellShowItem;
import com.mu.game.model.fo.FunctionOpenManager;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangLevelData;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ShowItemManager;
import com.mu.game.model.ui.dm.DynamicMenuManager;
import com.mu.game.model.ui.dm.imp.dungeon.TrialMenu;
import com.mu.game.model.unit.monster.worldboss.WorldBossData;
import com.mu.game.model.unit.player.Player;
import com.mu.game.top.BigDevilTopInfo;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.chat.ForwardMessage;
import com.mu.io.game.packet.imp.sys.TopCenterMessage;
import com.mu.io.game.packet.imp.sys.TopMessage;
import com.mu.io.game.packet.imp.sys.ZMDMessage;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import jxl.Sheet;
import jxl.Workbook;

public class BroadcastManager {
   private static final HashMap channelMap = new HashMap();
   private static final HashMap linkMap = new HashMap();

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      initChannel(wb.getSheet(1));
      initLink(wb.getSheet(2));
   }

   private static void initLink(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int type = Tools.getCellIntValue(sheet.getCell("A" + i));
         String value = Tools.getCellValue(sheet.getCell("C" + i));
         String color = Tools.getCellValue(sheet.getCell("D" + i));
         String nColor = Tools.getCellValue(sheet.getCell("E" + i));
         ChatLinkInfo ci = new ChatLinkInfo(type);
         ci.setValue(value);
         ci.setColor(color);
         ci.setNewColor(nColor);
         linkMap.put(type, ci);
      }

   }

   private static void initChannel(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int id = Tools.getCellIntValue(sheet.getCell("A" + i));
         String name = Tools.getCellValue(sheet.getCell("C" + i));
         String color = Tools.getCellValue(sheet.getCell("D" + i));
         String newColor = Tools.getCellValue(sheet.getCell("E" + i));
         ChatChannelInfo ci = new ChatChannelInfo(id);
         ci.setName(name);
         ci.setColor(color);
         ci.setNewColor(newColor);
         channelMap.put(id, ci);
      }

   }

   public static ChatChannelInfo getChatChannelInfo(int id) {
      return (ChatChannelInfo)channelMap.get(id);
   }

   public static ChatLinkInfo getChatLinkInfo(int type) {
      return (ChatLinkInfo)linkMap.get(type);
   }

   public static ForwardMessage createNoneLinkMessage(int type, String msg) {
      ForwardMessage fm = new ForwardMessage();

      try {
         fm.writeByte(type);
         fm.writeDouble((double)System.currentTimeMillis());
         fm.writeUTF(msg);
         fm.writeByte(0);
         fm.writeShort(0);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return fm;
   }

   public static String createChannelMessage(int channelId, String msg, boolean isSystem) {
      ChatChannelInfo info = getChatChannelInfo(channelId);
      if (info == null) {
         return msg;
      } else {
         return isSystem ? "#DEF" + info.getNewColor() + MessageText.getText(17003) + msg : "#DEF" + info.getNewColor() + info.getName() + " " + msg;
      }
   }

   public static void broadcastGm(String msg) {
      ZMDMessage zm = new ZMDMessage(msg);
      CenterManager.worldBroadcast(zm);
      zm.destroy();
      zm = null;
      ForwardMessage fm = ChatProcess.createNewChatMessage(1, "#F{c=0xCC3333}" + msg + "#F", (NewChatLink[])null, true, (byte[])null);
      CenterManager.worldBroadcast(fm);
      fm.destroy();
      fm = null;
   }

   public static void broadcastGangCreate(String gangName, long gangId) {
      ZMDMessage zm = new ZMDMessage(MessageText.getText(24001).replace("%s%", gangName));
      CenterManager.worldBroadcast(zm);
      zm.destroy();
      zm = null;
      String msg = MessageText.getText(24002).replace("%s%", gangName);
      NewJoinGangLink jLink = new NewJoinGangLink(0, gangId, MessageText.getText(9031));
      msg = msg.replace("%m%", jLink.getContent());
      ForwardMessage fm = ChatProcess.createNewChatMessage(1, msg, new NewChatLink[]{jLink}, true, (byte[])null);
      CenterManager.worldBroadcast(fm);
      fm.destroy();
      fm = null;
   }

   public static void broadcastGangLevelUp(Gang gang) {
      GangLevelData gd = GangManager.getLevelData(gang.getLevel());
      String msg = MessageText.getText(24015).replace("%s%", gang.getName()).replace("%m%", gd.getName());
      ZMDMessage zm = new ZMDMessage(msg);
      CenterManager.worldBroadcast(zm);
      zm.destroy();
      zm = null;
      ForwardMessage fm = ChatProcess.createNewChatMessage(1, msg, (NewChatLink[])null, true, (byte[])null);
      CenterManager.worldBroadcast(fm);
      fm.destroy();
      fm = null;
   }

   public static void broadcastBigDevilNewRecord(BigDevilTopInfo info, String text) {
      ZMDMessage zm = new ZMDMessage(text.replace("%s%", info.getName()).replace("%m%", String.valueOf(info.getExp())));
      CenterManager.worldBroadcast(zm);
      zm.destroy();
      zm = null;
      NewCharactorLink cLink = new NewCharactorLink(0, info.getRid(), info.getName(), "", false);
      String msg = text.replace("%s%", cLink.getContent()).replace("%m%", String.valueOf(info.getExp()));
      ForwardMessage fm = ChatProcess.createNewChatMessage(1, msg, new NewChatLink[]{cLink}, true, (byte[])null);
      CenterManager.worldBroadcast(fm);
      fm.destroy();
      fm = null;
   }

   public static void broadcastBigDevilNoNewRecord(BigDevilTopInfo info, String text) {
      ZMDMessage zm = new ZMDMessage(text.replace("%s%", info.getName()));
      CenterManager.worldBroadcast(zm);
      zm.destroy();
      zm = null;
      NewCharactorLink cLink = new NewCharactorLink(0, info.getRid(), info.getName(), "", false);
      String msg = text.replace("%s%", cLink.getContent()).replace("%m%", String.valueOf(info.getExp()));
      ForwardMessage fm = ChatProcess.createNewChatMessage(1, msg, new NewChatLink[]{cLink}, true, (byte[])null);
      CenterManager.worldBroadcast(fm);
      fm.destroy();
      fm = null;
   }

   public static void broadcastBigDevilStart(String msg, int minutes) {
      String text = msg.replace("%s%", String.valueOf(minutes));
      ZMDMessage zm = new ZMDMessage(text);
      CenterManager.worldBroadcast(zm);
      zm.destroy();
      zm = null;
      ForwardMessage fm = ChatProcess.createNewChatMessage(1, text, (NewChatLink[])null, true, (byte[])null);
      CenterManager.worldBroadcast(fm);
      fm.destroy();
      fm = null;
   }

   public static void broadcastWellItem(List wellItemList) {
      StringBuffer sb = new StringBuffer();
      NewChatLink[] linkList = new NewChatLink[wellItemList.size() + 1];

      for(int i = 0; i < wellItemList.size(); ++i) {
         WellShowItem item = (WellShowItem)wellItemList.get(i);
         ShowItemManager.addShowItem(item.getItem());
         NewChatLink[] link = item.getNewLink();
         NewItemLink itemLink = item.createNewItemLink(i + 1);
         if (i == 0) {
            sb.append(item.getDesWithoutTime());
            linkList[0] = link[0];
         } else {
            sb.append(itemLink.getContent());
         }

         linkList[i + 1] = itemLink;
      }

      ForwardMessage fm = ChatProcess.createNewChatMessage(1, sb.toString(), linkList, true, (byte[])null);
      CenterManager.worldBroadcast(fm);
      fm.destroy();
      fm = null;
      linkList = null;
   }

   public static void broadcastTrial(Player player, String bossName, String content, String linkStr) {
      TrialMenu menu = (TrialMenu)DynamicMenuManager.getMenu(7);
      NewCharactorLink cLink = new NewCharactorLink(0, player.getID(), player.getName(), player.getVipImgText(), false);
      NewOpenPanelLink oLink = new NewOpenPanelLink(1, linkStr, menu.getPanelId()[0], menu.getPanelId()[1]);
      String msg = content.replace("%n%", cLink.getNoLinkContent()).replace("%b%", bossName);
      TopMessage zm = new TopMessage(msg);
      CenterManager.worldBroadcast(zm);
      zm.destroy();
      zm = null;
      msg = content.replace("%n%", cLink.getContent()).replace("%b%", bossName) + oLink.getContent();
      ForwardMessage fm = ChatProcess.createNewChatMessage(1, msg, new NewChatLink[]{cLink, oLink}, true, (byte[])null);
      CenterManager.worldBroadcast(fm);
      fm.destroy();
      fm = null;
   }

   public static void broadcastBossRefresh(WorldBossData data) {
      String bossName = data.getName();
      String mapName = data.getMapName();
      TopMessage tm = new TopMessage(MessageText.getText(24003).replace("%b%", bossName).replace("%m%", mapName));
      CenterManager.worldBroadcast(tm);
      tm.destroy();
      tm = null;
      String channelStr = createChannelMessage(1, MessageText.getText(24003).replace("%b%", bossName).replace("%m%", mapName), true);
      ForwardMessage fm = createNoneLinkMessage(1, channelStr);
      CenterManager.worldBroadcast(fm);
      fm.destroy();
      fm = null;
   }

   public static void broadcastStrengthen(Player player, Item item) {
      if (item.getStarLevel() >= 8) {
         ShowItemManager.addShowItem(item);
         NewCharactorLink pLink = new NewCharactorLink(0, player.getID(), player.getName(), player.getVipImgText(), false);
         NewItemLink iLink = new NewItemLink(1, item.getID(), item.getName(), item.getQuality(), false);
         NewOpenPanelLink oLink = new NewOpenPanelLink(2, MessageText.getText(24007), 23, 0);
         String topMsg = MessageText.getText(24012).replace("%n%", pLink.getNoLinkContent()).replace("%m%", iLink.getNoLinkContent()).replace("%s%", String.valueOf(item.getStarLevel()));
         String msg = MessageText.getText(24006).replace("%n%", pLink.getContent()).replace("%m%", iLink.getContent()).replace("%s%", String.valueOf(item.getStarLevel())).replace("%k%", oLink.getContent());
         TopCenterMessage tm = new TopCenterMessage(topMsg);
         CenterManager.worldBroadcast(tm);
         tm.destroy();
         tm = null;
         ForwardMessage fm = ChatProcess.createNewChatMessage(1, msg, new NewChatLink[]{pLink, iLink, oLink}, true, (byte[])null);
         CenterManager.worldBroadcast(fm);
         fm.destroy();
         fm = null;
         pLink.destroy();
         iLink.destroy();
         oLink.destroy();
      }
   }

   public static void broadcastComposite(Player player, Item item, String sufStr) {
      ShowItemManager.addShowItem(item);
      NewCharactorLink pLink = new NewCharactorLink(0, player.getID(), player.getName(), player.getVipImgText(), false);
      NewItemLink iLink = new NewItemLink(1, item.getID(), item.getName(), item.getQuality(), false);
      NewOpenPanelLink oLink = new NewOpenPanelLink(2, MessageText.getText(24011), 86, 0);
      String topMsg = MessageText.getText(24014).replace("%n%", pLink.getNoLinkContent()).replace("%m%", iLink.getNoLinkContent()).replace("%s%", sufStr);
      String msg = MessageText.getText(24010).replace("%n%", pLink.getContent()).replace("%m%", iLink.getContent()).replace("%s%", sufStr).replace("%k%", oLink.getContent());
      TopCenterMessage tm = new TopCenterMessage(topMsg);
      CenterManager.worldBroadcast(tm);
      tm.destroy();
      tm = null;
      ForwardMessage fm = ChatProcess.createNewChatMessage(1, msg, new NewChatLink[]{pLink, iLink, oLink}, true, (byte[])null);
      CenterManager.worldBroadcast(fm);
      fm.destroy();
      fm = null;
      pLink.destroy();
      iLink.destroy();
      oLink.destroy();
   }

   public static void broadcastAdditional(Player player, Item item) {
      if (item.getZhuijiaLevel() >= 4) {
         ShowItemManager.addShowItem(item);
         NewCharactorLink pLink = new NewCharactorLink(0, player.getID(), player.getName(), player.getVipImgText(), false);
         NewItemLink iLink = new NewItemLink(1, item.getID(), item.getName(), item.getQuality(), false);
         NewOpenPanelLink oLink = new NewOpenPanelLink(2, MessageText.getText(24009), 23, 1);
         String topMsg = MessageText.getText(24013).replace("%n%", pLink.getNoLinkContent()).replace("%m%", iLink.getNoLinkContent()).replace("%s%", String.valueOf(item.getZhuijiaLevel()));
         String msg = MessageText.getText(24008).replace("%n%", pLink.getContent()).replace("%m%", iLink.getContent()).replace("%s%", String.valueOf(item.getZhuijiaLevel())).replace("%k%", oLink.getContent());
         TopCenterMessage tm = new TopCenterMessage(topMsg);
         CenterManager.worldBroadcast(tm);
         tm.destroy();
         tm = null;
         ForwardMessage fm = ChatProcess.createNewChatMessage(1, msg, new NewChatLink[]{pLink, iLink, oLink}, true, (byte[])null);
         Iterator it = CenterManager.getAllPlayerIterator();

         while(it.hasNext()) {
            Player p = (Player)it.next();
            if (FunctionOpenManager.isOpen(player, 12)) {
               p.writePacket(fm);
            }
         }

         fm.destroy();
         fm = null;
         pLink.destroy();
         iLink.destroy();
         oLink.destroy();
      }
   }

   public static void broadcastPacket(WriteOnlyPacket packet) {
      Iterator it = CenterManager.getAllPlayerIterator();

      while(it.hasNext()) {
         try {
            ((Player)it.next()).writePacket(packet);
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

   }

   public static void broadcastSystemAndZmd(String msg) {
      broadcastGm(msg);
   }

   public static void broadcastLuolanMasterOnline(Player player) {
      NewCharactorLink link = new NewCharactorLink(0, player.getID(), player.getName(), player.getVipImgText(), true);
      String zmdMsg = DungeonManager.getLuolanManager().getTemplate().getCastellanOnline().replace("%s%", link.getNoLinkContent());
      String msg = DungeonManager.getLuolanManager().getTemplate().getCastellanOnline().replace("%s%", link.getContent());
      ZMDMessage zm = new ZMDMessage(zmdMsg);
      CenterManager.worldBroadcast(zm);
      zm.destroy();
      zm = null;
      ForwardMessage rm = ChatProcess.createNewChatMessage(7, msg, new NewChatLink[]{link}, true, (byte[])null);
      CenterManager.worldBroadcast(rm);
      rm.destroy();
      rm = null;
   }

   public static void broadcastGangMasterBeKilled(Player dead, Player killer) {
      Gang gang = dead.getGang();
      if (gang != null) {
         int post = gang.getMember(dead.getID()).getPost();
         String msg = MessageText.getText(24004);
         if (post == 1) {
            msg = MessageText.getText(24005);
         }

         NewCharactorLink deadLink = new NewCharactorLink(0, dead.getID(), dead.getName(), dead.getVipImgText(), false);
         NewCharactorLink killerLink = new NewCharactorLink(1, killer.getID(), killer.getName(), killer.getVipImgText(), false);
         String msg2 = msg.replace("%d%", deadLink.getNoLinkContent()).replace("%g%", gang.getName()).replace("%m%", dead.getMap().getName()).replace("%k%", killerLink.getNoLinkContent());
         msg = msg.replace("%d%", deadLink.getContent()).replace("%g%", gang.getName()).replace("%m%", dead.getMap().getName()).replace("%k%", killerLink.getContent());
         ForwardMessage fm = ChatProcess.createNewChatMessage(1, msg, new NewChatLink[]{deadLink, killerLink}, true, (byte[])null);
         CenterManager.worldBroadcast(fm);
         fm.destroy();
         fm = null;
         TopMessage tm = new TopMessage(msg2);
         CenterManager.worldBroadcast(tm);
         tm.destroy();
         tm = null;
      }
   }

   public static void broadcastRedPacket(Player player, String gangName, String content) {
      NewCharactorLink playerLink = new NewCharactorLink(0, player.getID(), player.getName(), player.getVipImgText(), false);
      String msg = content.replace("%s%", playerLink.getContent()).replace("%n%", gangName);
      ForwardMessage fm = ChatProcess.createNewChatMessage(1, msg, new NewChatLink[]{playerLink}, true, (byte[])null);
      CenterManager.worldBroadcast(fm);
      fm.destroy();
      fm = null;
      ZMDMessage zm = new ZMDMessage(msg);
      CenterManager.worldBroadcast(zm);
      zm.destroy();
      zm = null;
   }

   public static void broadcastFirstPay(Player player) {
      String msg = MessageText.getText(25105);
      NewCharactorLink playerLink = new NewCharactorLink(0, player.getID(), player.getName(), player.getVipImgText(), false);
      NewOpenPanelLink openLink = new NewOpenPanelLink(1, MessageText.getText(25106), 142, 0);
      msg = msg.replace("%s%", playerLink.getContent()).replace("%d%", openLink.getContent());
      ForwardMessage fm = ChatProcess.createNewChatMessage(1, msg, new NewChatLink[]{playerLink, openLink}, true, (byte[])null);
      CenterManager.worldBroadcast(fm);
      fm.destroy();
      fm = null;
      TopMessage zm = new TopMessage(msg);
      CenterManager.worldBroadcast(zm);
      zm.destroy();
      zm = null;
   }
}
