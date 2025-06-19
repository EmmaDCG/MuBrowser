package com.mu.io.game.packet.imp.friend;

import com.mu.game.model.friend.Friend;
import com.mu.game.model.friend.FriendManager;
import com.mu.game.model.friend.FriendlyInfo;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class GetFriendTipsInfo extends ReadAndWritePacket {
   public GetFriendTipsInfo(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long fid = (long)this.readDouble();
      int type = this.readByte();
      boolean reqDes = this.readBoolean();
      Friend f = player.getFriendManager().getFriend(fid, type);
      if (f != null) {
         Player friendPlayer = f.getFriendPlayer();
         this.writeDouble((double)f.getId());
         this.writeByte(f.getType());
         this.writeShort(f.getLevel());
         this.writeByte(f.getProfession());
         this.writeInt(f.getZdl());
         this.writeUTF(f.getGangName());
         if (friendPlayer != null) {
            this.writeShort(friendPlayer.getMapID());
         } else {
            this.writeShort(-1);
         }

         if (type == 0) {
            FriendlyInfo cuInfo = FriendManager.getCurrentFriendly(f);
            FriendlyInfo nextInfo = FriendManager.getNextFriendlyInfo(f);
            this.writeShort(cuInfo.getExpBouns());
            if (cuInfo.getLevel() == FriendManager.getMaxFriendlyLevel()) {
               this.writeShort(cuInfo.getExpBouns());
            } else {
               this.writeShort(nextInfo == null ? cuInfo.getExpBouns() : nextInfo.getExpBouns());
            }

            this.writeInt(nextInfo == null ? 0 : nextInfo.getMinExp());
         }

         if (reqDes) {
            this.writeUTF(FriendManager.getFriendTipsDes());
         }

         player.writePacket(this);
      }

   }
}
