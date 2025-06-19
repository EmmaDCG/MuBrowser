package com.mu.executor.imp.item;

import com.mu.db.manager.ItemDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.item.Item;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class UpdateItemExecutor extends Executable {
   public static final int Type_Count = 1;
   public static final int Type_Slot = 2;
   public static final int Type_Bind = 3;
   public static final int Type_Container = 4;
   public static final int Type_StarLevel = 5;
   public static final int Type_BasisStats = 6;
   public static final int Type_OtherStats = 7;
   public static final int Type_Stats = 8;
   public static final int Type_Stones = 9;
   public static final int Type_Runes = 10;
   public static final int Type_Durability = 11;
   public static final int Type_ToShop = 12;
   public static final int Type_Sortout = 13;
   public static final int Type_ZhuijiaLevel = 14;
   public static final int Type_Upgrade = 15;

   public UpdateItemExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      ItemDBManager.updateItem(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) {
      try {
         Item item = (Item)obj[0];
         int type = ((Integer)obj[1]).intValue();
         packet.writeLong(item.getID());
         packet.writeByte(type);
         switch(type) {
         case 1:
            packet.writeInt(item.getCount());
            break;
         case 2:
            packet.writeShort(item.getSlot());
            break;
         case 3:
            packet.writeBoolean(item.isBind());
            break;
         case 4:
            packet.writeByte(item.getContainerType());
            packet.writeShort(item.getSlot());
            packet.writeInt(item.getCount());
            break;
         case 5:
            packet.writeByte(item.getStarLevel());
            packet.writeByte(item.getOnceMaxStarLevel());
            packet.writeInt(item.getStarUpTimes());
            packet.writeBoolean(item.isBind());
            packet.writeUTF(item.getRuneStr());
            break;
         case 6:
            packet.writeUTF(item.getBasisStr());
            break;
         case 7:
            packet.writeUTF(item.getOtherStr());
            break;
         case 8:
            packet.writeUTF(item.getBasisStr());
            packet.writeUTF(item.getOtherStr());
            break;
         case 9:
            packet.writeUTF(item.getStoneStr());
            break;
         case 10:
            packet.writeUTF(item.getRuneStr());
            break;
         case 11:
            packet.writeShort(item.getDurability());
            break;
         case 12:
            packet.writeByte(item.getContainerType());
            packet.writeShort(item.getSlot());
            packet.writeInt(item.getMoney());
            packet.writeByte(item.getMoneyType());
            break;
         case 13:
            packet.writeShort(item.getSlot());
            packet.writeInt(item.getCount());
            break;
         case 14:
            packet.writeByte(item.getZhuijiaLevel());
            break;
         case 15:
            packet.writeInt(item.getModelID());
            packet.writeByte(item.getSocket());
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
