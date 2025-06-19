package com.mu.game;

import org.jboss.netty.channel.Channel;

public class RemoteChannelInfo {
   private String userName = null;
   private boolean needAntiAddiction = false;
   private int serverID = -1;
   private long rid = -1L;
   private Channel c2gChannel = null;
   private Channel g2rChannel = null;

   public RemoteChannelInfo(Channel c2gChannel, Channel g2rChannel) {
      this.c2gChannel = c2gChannel;
      this.g2rChannel = g2rChannel;
   }

   public long getRid() {
      return this.rid;
   }

   public void setRid(long rid) {
      this.rid = rid;
   }

   public Channel getC2gChannel() {
      return this.c2gChannel;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public Channel getG2rChannel() {
      return this.g2rChannel;
   }

   public String getUserName() {
      return this.userName;
   }

   public boolean isNeedAntiAddiction() {
      return this.needAntiAddiction;
   }

   public void setNeedAntiAddiction(boolean needAntiAddiction) {
      this.needAntiAddiction = needAntiAddiction;
   }

   public int getServerID() {
      return this.serverID;
   }

   public void setServerID(int serverID) {
      this.serverID = serverID;
   }
}
