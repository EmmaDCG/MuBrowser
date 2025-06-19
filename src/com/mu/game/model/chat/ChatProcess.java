package com.mu.game.model.chat;

import com.mu.config.BroadcastManager;
import com.mu.config.MessageText;
import com.mu.game.CenterManager;
import com.mu.game.model.chat.link.ChatLink;
import com.mu.game.model.chat.newlink.NewCharactorLink;
import com.mu.game.model.chat.newlink.NewChatLink;
import com.mu.game.model.chat.newlink.NewExechangeBuyLink;
import com.mu.game.model.chat.newlink.NewItemLink;
import com.mu.game.model.drop.model.WellShowItem;
import com.mu.game.model.friend.FriendManager;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.item.ShowItemManager;
import com.mu.game.model.item.box.magic.MagicRecord;
import com.mu.game.model.market.MarketItem;
import com.mu.game.model.market.record.MarketRecord;
import com.mu.game.model.team.Team;
import com.mu.game.model.team.Teammate;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.bluevip.BlueIcon;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.chat.ChatSiLiao;
import com.mu.io.game.packet.imp.chat.ForwardMessage;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.Time;
import com.mu.utils.buffer.BufferReader;
import java.util.ArrayList;
import java.util.Iterator;

public class ChatProcess {
   public static final int CHAT_TYPE_AROUND = 0;
   public static final int CHAT_TYPE_WORLD = 1;
   public static final int CHAT_TYPE_PRIVATE = 2;
   public static final int CHAT_TYPE_TEAM = 3;
   public static final int CHAT_TYPE_GANG = 4;
   public static final int CHAT_TYPE_HORN = 5;
   public static final int CHAT_TYPE_TRANSE = 6;
   public static final int CHAT_TYPE_SYSTEM = 7;
   public static final int CHAT_TYPE_SYSTEM_FORCE = 8;

   public static String createSelfStr(Player player) {
      String self = MessageText.getText(17002);
      ChatLinkInfo info = BroadcastManager.getChatLinkInfo(4);
      if (info == null) {
         return self;
      } else {
         BlueIcon bi = player.getUser().getBlueVip().getBlueIcon();
         return bi.getText() + "#F{" + info.getNewColor() + "}" + info.getValue().replace("%v%", "").replace("%n%", self) + "#F：";
      }
   }

   public static void process(int type, Player player, String msg, byte[] data) throws Exception {
      BufferReader reader = new BufferReader(data);
      int linkLength = reader.readUnsignedByte();

      for(int i = 0; i < linkLength; ++i) {
         int linkType = reader.readByte();
         switch(linkType) {
         case 0:
            reader.readShort();
            reader.readInt();
            reader.readInt();
            break;
         case 5:
            long itemId = (long)reader.readDouble();
            reader.readUTF();
            reader.readByte();
            ShowItemManager.addShowItem(player.getID(), itemId);
         }
      }

      NewCharactorLink cl = new NewCharactorLink(0, player.getID(), player.getName(), player.getUser().getBlueVip().getBlueIcon().getText(), true);
      ForwardMessage fm = createNewChatMessage(type, cl.getContent() + "：" + msg, new NewChatLink[]{cl}, false, data);
      ForwardMessage selfFm = createSelfChatMessage(type, player, msg, data);
      switch(type) {
      case 0:
         doAroundMessage(player, fm, selfFm);
         break;
      case 1:
         doWorldMessage(player, fm, selfFm);
      case 2:
      default:
         break;
      case 3:
         doTeamMessage(player, fm, selfFm);
      }

      fm.destroy();
      fm = null;
      selfFm.destroy();
      selfFm = null;
      reader.destroy();
      reader = null;
   }

   public static void processPersinal(Player player, long targetId, String msg, byte[] data) throws Exception {
      BufferReader reader = new BufferReader(data);
      int linkLength = reader.readUnsignedByte();

      for(int i = 0; i < linkLength; ++i) {
         int linkType = reader.readByte();
         switch(linkType) {
         case 0:
            reader.readShort();
            reader.readInt();
            reader.readInt();
            break;
         case 5:
            long itemId = (long)reader.readDouble();
            reader.readUTF();
            reader.readByte();
            ShowItemManager.addShowItem(player.getID(), itemId);
         }
      }

      ChatSiLiao cs = ChatSiLiao.createPersionalPacket(player, targetId, msg, data);
      player.writePacket(cs);
      cs.destroy();
      cs = null;
      Player otherPlayer = CenterManager.getPlayerByRoleID(targetId);
      ChatSiLiao cs2;
      if (otherPlayer != null) {
         if (player.getFriendManager().isInBlack(targetId)) {
            cs2 = ChatSiLiao.createPersionalPacket(targetId, MessageText.getText(17013), (byte[])null);
            player.writePacket(cs2);
            cs2.destroy();
            cs2 = null;
            return;
         }

         if (otherPlayer.getFriendManager().isInBlack(player.getID())) {
            return;
         }

         cs2 = ChatSiLiao.createPersionalPacket(player, player.getID(), msg, data);
         otherPlayer.writePacket(cs2);
         cs2.destroy();
         cs2 = null;
      } else {
         cs2 = ChatSiLiao.createPersionalPacket(targetId, MessageText.getText(1021), (byte[])null);
         player.writePacket(cs2);
         cs2.destroy();
         cs2 = null;
      }

      reader.destroy();
      reader = null;
   }

   public static void processGang(Player player, String msg, byte[] data) throws Exception {
      Gang gang = player.getGang();
      if (gang == null) {
         SystemMessage.writeMessage(player, 9034);
      } else {
         BufferReader reader = new BufferReader(data);
         int linkLength = reader.readUnsignedByte();

         for(int i = 0; i < linkLength; ++i) {
            int linkType = reader.readByte();
            switch(linkType) {
            case 0:
               reader.readShort();
               reader.readInt();
               reader.readInt();
               break;
            case 5:
               long itemId = (long)reader.readDouble();
               reader.readUTF();
               reader.readByte();
               ShowItemManager.addShowItem(player.getID(), itemId);
            }
         }

         ChatSiLiao cs = ChatSiLiao.createGangPacket(player, msg, data);
         gang.broadcast(cs);
         cs.destroy();
         cs = null;
         reader.destroy();
         reader = null;
      }
   }

   private static void doWorldMessage(Player player, ForwardMessage msg, ForwardMessage selfMsg) {
      Iterator it = CenterManager.getAllPlayerIterator();

      while(it.hasNext()) {
         Player p = (Player)it.next();
         if (p.getID() == player.getID()) {
            p.writePacket(selfMsg);
         } else {
            FriendManager manager = p.getFriendManager();
            if (manager != null && !manager.isInBlack(player.getID())) {
               p.writePacket(msg);
            }
         }
      }

   }

   public static ForwardMessage createNewChatMessage(int type, String msg, NewChatLink[] lists, boolean isSystem, byte[] data) {
      ForwardMessage fm = new ForwardMessage();
      String tmpMsg = BroadcastManager.createChannelMessage(type, msg, isSystem);

      try {
         fm.writeByte(type);
         fm.writeDouble((double)System.currentTimeMillis());
         fm.writeUTF(tmpMsg);
         if (lists == null) {
            fm.writeByte(0);
         } else {
            fm.writeByte(lists.length);
            NewChatLink[] var10 = lists;
            int var9 = lists.length;

            for(int var8 = 0; var8 < var9; ++var8) {
               NewChatLink link = var10[var8];
               link.writeDetail(fm);
            }
         }

         if (data == null) {
            fm.writeShort(0);
         } else {
            fm.writeShort(data.length);
            fm.writeBytes(data);
         }
      } catch (Exception var11) {
         var11.printStackTrace();
      }

      return fm;
   }

   public static void writeLinkMessage(String msg, ChatLink[] links, WriteOnlyPacket packet) {
      try {
         packet.writeUTF(msg);
         if (links == null) {
            packet.writeByte(0);
         } else {
            packet.writeByte(links.length);
            ChatLink[] var6 = links;
            int var5 = links.length;

            for(int var4 = 0; var4 < var5; ++var4) {
               ChatLink link = var6[var4];
               link.writeDetail(packet);
            }
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public static void writeNewLinkMessage(String msg, NewChatLink[] links, WriteOnlyPacket packet) {
      try {
         packet.writeUTF(msg);
         if (links == null) {
            packet.writeByte(0);
         } else {
            packet.writeByte(links.length);
            NewChatLink[] var6 = links;
            int var5 = links.length;

            for(int var4 = 0; var4 < var5; ++var4) {
               NewChatLink link = var6[var4];
               link.writeDetail(packet);
            }
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public static ForwardMessage createSelfChatMessage(int type, Player player, String msg, byte[] data) {
      String self = MessageText.getText(17002);
      ChatLinkInfo info = BroadcastManager.getChatLinkInfo(4);
      String tmpMsg = msg;
      if (info != null) {
         String vipTagText = player.getUser().getBlueVip().getBlueIcon().getText();
         tmpMsg = vipTagText + "#F{" + info.getNewColor() + "}" + info.getValue().replace("%v%", "").replace("%n%", self) + "#F：" + msg;
      }

      tmpMsg = BroadcastManager.createChannelMessage(type, tmpMsg, false);
      ForwardMessage fm = new ForwardMessage();

      try {
         fm.writeByte(type);
         fm.writeDouble((double)System.currentTimeMillis());
         fm.writeUTF(tmpMsg);
         fm.writeByte(0);
         if (data == null) {
            fm.writeShort(0);
         } else {
            fm.writeShort(data.length);
            fm.writeBytes(data);
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }

      return fm;
   }

   public static void writeChannelNoLinkMessageByServer(int type, Player player, String msg) {
      try {
         String otherMsg = BroadcastManager.createChannelMessage(type, msg, true);
         ForwardMessage fm = new ForwardMessage();
         fm.writeByte(type);
         fm.writeDouble((double)System.currentTimeMillis());
         fm.writeUTF(otherMsg);
         fm.writeByte(0);
         fm.writeShort(0);
         player.writePacket(fm);
         fm.destroy();
         fm = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   private static void doAroundMessage(Player player, ForwardMessage msg, ForwardMessage selfMsg) {
      player.writePacket(selfMsg);
      ArrayList list = player.getMap().getAroundPlayers(player.getPosition());
      Iterator var5 = list.iterator();

      while(var5.hasNext()) {
         Player p = (Player)var5.next();
         FriendManager manager = p.getFriendManager();
         if (manager != null && !manager.isInBlack(player.getID()) && p.getID() != player.getID()) {
            p.writePacket(msg);
         }
      }

   }

   private static void doTeamMessage(Player player, ForwardMessage msg, ForwardMessage selfMsg) {
      player.writePacket(selfMsg);
      Team team = player.getCurrentTeam();
      if (team != null) {
         Iterator var5 = team.getMateList().iterator();

         while(var5.hasNext()) {
            Teammate mate = (Teammate)var5.next();
            Player p = mate.getPlayer();
            if (p != null && p.getID() != player.getID()) {
               p.writePacket(msg);
            }
         }
      }

   }

   public static void writeBuyRecord(MarketRecord record, WriteOnlyPacket packet) {
      try {
         ShowItemManager.addMarketItem(record.getItem());
         String msg = MessageText.getText(16618);
         String time = Time.getTimeStr(record.getTransactionTime(), "HH:mm:ss");
         NewCharactorLink cl = new NewCharactorLink(0, record.getSalerID(), record.getSalerName(), "", false);
         NewItemLink il = new NewItemLink(1, record.getItem().getID(), record.getItem().getName(), record.getItem().getQuality(), false);
         msg = msg.replace("%p%", cl.getContent()).replace("%i%", il.getContent()).replace("%n%", String.valueOf(record.getCount())).replace("%z%", String.valueOf(record.getGainMoney() + record.getTax())).replace("%t%", time);
         writeNewLinkMessage(msg, new NewChatLink[]{cl, il}, packet);
         cl.destroy();
         il.destroy();
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public static void writeSellRecord(MarketRecord record, WriteOnlyPacket packet) {
      try {
         ShowItemManager.addMarketItem(record.getItem());
         String msg = MessageText.getText(16617);
         String time = Time.getTimeStr(record.getTransactionTime(), "HH:mm:ss");
         NewCharactorLink cl = new NewCharactorLink(0, record.getBuyerID(), record.getBuyerName(), "", false);
         NewItemLink il = new NewItemLink(1, record.getItem().getID(), record.getItem().getName(), record.getItem().getQuality(), false);
         msg = msg.replace("%p%", cl.getContent()).replace("%i%", il.getContent()).replace("%n%", String.valueOf(record.getCount())).replace("%z%", String.valueOf(record.getGainMoney())).replace("%m%", String.valueOf(record.getTax())).replace("%t%", time);
         writeNewLinkMessage(msg, new NewChatLink[]{cl, il}, packet);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public static void spellWellDrop(WellShowItem dsItem) {
      String time = Time.getTimeStr(dsItem.getTime(), "MM-dd HH:mm:ss");
      NewCharactorLink clNew = new NewCharactorLink(0, dsItem.getRoleID(), dsItem.getRoleName(), "", false);
      NewItemLink ilNew = createNewItemLink(dsItem, 1);
      String des = clNew.getContent() + dsItem.getNewReason() + ilNew.getContent();
      String newMsg = "#F{e=3}" + time + "#F" + "  " + des;
      dsItem.setNewDes(newMsg);
      dsItem.setDesWithoutTime(des);
      dsItem.setNewLink(new NewChatLink[]{clNew, ilNew});
   }

   public static NewItemLink createNewItemLink(WellShowItem dsItem, int index) {
      NewItemLink il = new NewItemLink(index, dsItem.getItem().getID(), dsItem.getItem().getName(), dsItem.getItem().getQuality(), false);
      return il;
   }

   public static String spellMagicBox(MagicRecord record) {
      NewCharactorLink clNew = new NewCharactorLink(0, record.getRoleID(), record.getRoleName(), "", false);
      NewItemLink ilNew = new NewItemLink(1, record.getItemID(), record.getItem().getName(), record.getItem().getQuality(), false);
      String msg = MessageText.getText(3053);
      msg = msg.replaceAll("%n%", clNew.getContent());
      msg = msg.replaceAll("%s%", ilNew.getContent());
      record.setDes(msg);
      record.setLink(new NewChatLink[]{clNew, ilNew});
      return msg;
   }

   public static void exchangeCall(Player player, MarketItem mi) {
      try {
         ShowItemManager.addShowItem(mi.getItem());
         String msg = MessageText.getText(16615);
         NewCharactorLink playerLink = new NewCharactorLink(0, player.getID(), player.getName(), player.getVipImgText(), true);
         NewItemLink itemLink = new NewItemLink(1, mi.getItem().getID(), mi.getItem().getName(), mi.getItem().getQuality(), false);
         NewExechangeBuyLink buyLink = new NewExechangeBuyLink(2, mi.getItem().getID());
         NewItemLink itemLink2 = new NewItemLink(0, mi.getItem().getID(), mi.getItem().getName(), mi.getItem().getQuality(), false);
         String itemMsg = msg.replace("%s%", itemLink.getContent()).replace("%n%", String.valueOf(mi.getItem().getCount())).replace("%m%", String.valueOf(mi.getPrice()));
         ForwardMessage fm = createNewChatMessage(6, playerLink.getContent() + itemMsg + buyLink.getContent(), new NewChatLink[]{playerLink, itemLink, buyLink}, false, (byte[])null);
         itemMsg = msg.replace("%s%", itemLink2.getContent()).replace("%n%", String.valueOf(mi.getItem().getCount())).replace("%m%", String.valueOf(mi.getPrice()));
         String selfMsg = createSelfStr(player) + itemMsg;
         ForwardMessage selfFm = createNewChatMessage(6, selfMsg, new NewChatLink[]{itemLink2}, false, (byte[])null);
         doWorldMessage(player, fm, selfFm);
         playerLink.destroy();
         itemLink.destroy();
         buyLink.destroy();
         itemLink2.destroy();
         fm.destroy();
         fm = null;
         selfFm.destroy();
         selfFm = null;
      } catch (Exception var11) {
         var11.printStackTrace();
      }

   }
}
