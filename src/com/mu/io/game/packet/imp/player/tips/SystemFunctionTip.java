package com.mu.io.game.packet.imp.player.tips;

import com.mu.game.model.equip.compositenew.CompositeGuideModel;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;

public class SystemFunctionTip extends WriteOnlyPacket {
   public SystemFunctionTip(int type, Number id, Object... objects) {
      super(1005);

      try {
         this.writeByte(type);
         switch(type) {
         case 1:
         case 4:
         case 7:
            this.writeDouble((double)id.longValue());
         case 2:
         case 5:
         case 6:
         case 8:
         case 10:
         default:
            break;
         case 3:
         case 9:
            this.writeInt(id.intValue());
            break;
         case 11:
         case 12:
         case 13:
            String des = (String)objects[0];
            this.writeUTF(des);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public SystemFunctionTip(CompositeGuideModel guideModel) {
      super(1005);

      try {
         this.writeByte(10);
         GetItemStats.writeItem(guideModel.getItem(), this);
         this.writeShort(guideModel.getEquipResource());
         this.writeShort(guideModel.getIconResource());
         this.writeByte(guideModel.getComSort());
         this.writeByte(guideModel.getComType());
         this.writeShort(guideModel.getComID());
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void sendToClient(Player player, int type, Number id, Object... objects) {
      SystemFunctionTip sft = new SystemFunctionTip(type, id, objects);
      player.writePacket(sft);
      sft.destroy();
      sft = null;
   }

   public static void sendToGuide(Player player, CompositeGuideModel guideModel) {
      SystemFunctionTip sft = new SystemFunctionTip(guideModel);
      player.writePacket(sft);
      sft.destroy();
      sft = null;
      player.getArrowGuideManager().pushArrow(11, guideModel.getArrowDes());
   }
}
