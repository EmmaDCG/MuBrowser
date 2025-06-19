package com.mu.io.game.packet.imp.friend;

import com.mu.db.manager.FriendDBManager;
import com.mu.game.model.friend.Friend;
import com.mu.game.model.friend.FriendBlessInfo;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.utils.Time;
import java.util.ArrayList;
import java.util.Iterator;

public class InitFriend extends ReadAndWritePacket {
   public InitFriend(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public static InitFriend createInitFriend(long rid) {
      InitFriend packet = new InitFriend(11200, (byte[])null);
      ArrayList list = FriendDBManager.getFriendList(rid);
      ArrayList blessList = FriendDBManager.getBlessList(rid);
      int blessTimes = FriendDBManager.getRoleBlessTimes(rid);
      long[] l = FriendDBManager.getRoleWishInfo(rid);

      try {
         packet.writeByte(list.size());
         Iterator var8 = list.iterator();

         while(var8.hasNext()) {
            Friend f = (Friend)var8.next();
            packet.writeLong(f.getId());
            packet.writeByte(f.getType());
            packet.writeUTF(f.getName());
            packet.writeLong(f.getAddTime());
            packet.writeShort(f.getBeKilledTimes());
            packet.writeUTF(f.getBlueTag());
            packet.writeInt(f.getFriendDegree());
            packet.writeShort(f.getLevel());
            packet.writeLong(f.getOfflineTime());
            packet.writeByte(f.getProfession());
            packet.writeInt(f.getServerId());
            packet.writeInt(f.getZdl());
         }

         packet.writeByte(blessList.size());
         var8 = blessList.iterator();

         while(var8.hasNext()) {
            FriendBlessInfo bi = (FriendBlessInfo)var8.next();
            packet.writeLong(bi.getId());
            packet.writeUTF(bi.getName());
            packet.writeLong(bi.getTime());
         }

         packet.writeInt(blessTimes);
         packet.writeInt((int)l[0]);
         packet.writeLong(l[1]);
         packet.writeLong(l[2]);
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         list.clear();
         list = null;
      }

      return packet;
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int size = this.readUnsignedByte();

      for(int i = 0; i < size; ++i) {
         Friend friend = new Friend(this.readLong(), this.readByte());
         friend.setName(this.readUTF());
         friend.setAddTime(this.readLong());
         friend.setBeKilledTimes(this.readUnsignedShort());
         friend.setBlueTag(this.readUTF());
         friend.setFriendDegree(this.readInt());
         friend.setLevel(this.readShort());
         friend.setOfflineTime(this.readLong());
         friend.setProfession(this.readUnsignedByte());
         friend.setServerId(this.readInt());
         friend.setZdl(this.readInt());
         player.getFriendManager().addFriendByInit(friend);
      }

      int blessSize = this.readUnsignedByte();

      int blessTimes;
      for(blessTimes = 0; blessTimes < blessSize; ++blessTimes) {
         FriendBlessInfo fi = new FriendBlessInfo();
         fi.setId(this.readLong());
         fi.setName(this.readUTF());
         fi.setTime(this.readLong());
         player.getFriendManager().addBlessRecord(fi);
      }

      blessTimes = this.readInt();
      int lucky = this.readInt();
      long luckyDay = this.readLong();
      long receiveDay = this.readLong();
      player.getFriendManager().setBlessTimes(blessTimes);
      long today = Time.getDayLong();
      if (luckyDay == today) {
         player.getFriendManager().setLucky(lucky);
      }

      if (receiveDay == today) {
         player.getFriendManager().setHasReceived(true);
      }

   }
}
