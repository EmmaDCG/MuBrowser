package com.mu.io.game.packet.imp.friend;

import com.mu.game.model.friend.Friend;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.ArrayList;
import java.util.Iterator;

public class GetFriendsInfo extends ReadAndWritePacket {
   public GetFriendsInfo(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int size = this.readUnsignedByte();
      ArrayList list = new ArrayList();

      for(int i = 0; i < size; ++i) {
         long rid = (long)this.readDouble();
         int type = this.readByte();
         Friend friend = player.getFriendManager().getFriend(rid, type);
         if (friend != null) {
            list.add(friend);
         }
      }

      this.writeByte(list.size());
      Iterator var10 = list.iterator();

      while(var10.hasNext()) {
         Friend friend = (Friend)var10.next();
         Player friendPlayer = friend.getFriendPlayer();
         this.writeDouble((double)friend.getId());
         this.writeByte(friend.getType());
         this.writeUTF(friend.getName());
         this.writeShort(friend.getHeader());
         switch(friend.getType()) {
         case 0:
            this.writeInt(friend.getFriendDegree());
            if (friendPlayer != null) {
               this.writeBoolean(friendPlayer.getFriendManager().hasBeBlessed(player.getID()));
            } else {
               this.writeBoolean(false);
            }
            break;
         case 1:
            this.writeShort(friend.getBeKilledTimes());
            break;
         default:
            this.writeDouble((double)friend.getAddTime());
         }

         boolean isOnline = friendPlayer != null;
         this.writeBoolean(isOnline);
         if (!isOnline) {
            this.writeDouble((double)friend.getOfflineTime());
         }

         int[] blueIcons = friend.getBlueIcons();
         this.writeShort(blueIcons[0]);
         this.writeShort(blueIcons[1]);
      }

      list.clear();
      player.writePacket(this);
   }
}
