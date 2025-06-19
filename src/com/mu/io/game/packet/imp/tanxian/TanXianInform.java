package com.mu.io.game.packet.imp.tanxian;

import com.mu.game.model.tanxian.PlayerTanXianManager;
import com.mu.game.model.tanxian.TanXianConfigManager;
import com.mu.game.model.tanxian.TanXianData;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.Iterator;

public class TanXianInform extends ReadAndWritePacket {
   public TanXianInform(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      PlayerTanXianManager ptxm = null;
      if (player != null && (ptxm = player.getTanXianManager()) != null) {
         int level = ptxm.getLevel();
         int exp = ptxm.getExp();
         this.writeByte(TanXianConfigManager.getDataSize());
         Iterator it = TanXianConfigManager.getTanXianDataIterator();

         while(it.hasNext()) {
            TanXianData data = (TanXianData)it.next();
            this.writeShort(data.getId());
            this.writeUTF(data.getName());
            this.writeBoolean(data.getOpenLevel() <= level);
            if (data.getOpenLevel() <= level) {
               this.writeShort(data.getIcon());
            } else {
               this.writeByte(data.getOpenLevel());
            }
         }

         this.writeByte(level);
         this.writeInt(exp);
         this.writeInt(TanXianConfigManager.getLevelExp(level));
         boolean in = ptxm.inTanXian();
         this.writeBoolean(in);
         if (!in) {
            this.writeInt(TanXianConfigManager.TANXIAN_EXPEND_ITEM);
            this.writeInt(TanXianConfigManager.TANXIAN_EXPEND_INGOT);
            this.writeByte(ptxm.getRemainCount());
         }

         player.writePacket(this);
      }
   }

   public static void sendMsgTanXianSuccess(Player player) {
      try {
         TanXianInform packet = new TanXianInform(48005, (byte[])null);
         packet.writeBoolean(true);
         player.writePacket(packet);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
