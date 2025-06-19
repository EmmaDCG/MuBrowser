package com.mu.game.plat;

import com.mu.game.model.unit.player.User;
import org.jboss.netty.channel.Channel;

public class LoginInfo {
   private String userName = "";
   private boolean needAntiAddiction = true;
   private int serverID = 0;
   private boolean isGuider = false;
   private String platServerName = "";
   private Channel newChannel;
   private Channel oldChannel;
   private boolean isClient = false;
   private String openId = "";
   private String openKey = "";
   private String pf = "";
   private String pfKey = "";
   private String access_token = "";
   private User user;
   private int openServerId;

   public void destroy() {
      this.userName = null;
      this.platServerName = null;
      this.openId = null;
      this.openKey = null;
      this.pf = null;
      this.pfKey = null;
      this.access_token = null;
      this.user = null;
      this.newChannel = null;
      this.oldChannel = null;
   }

   public final String getUserName() {
      return this.userName;
   }

   public final void setUserName(String userName) {
      this.userName = userName;
   }

   public final boolean isNeedAntiAddiction() {
      return this.needAntiAddiction;
   }

   public final void setNeedAntiAddiction(boolean needAntiAddiction) {
      this.needAntiAddiction = needAntiAddiction;
   }

   public final int getServerID() {
      return this.serverID;
   }

   public final void setServerID(int serverID) {
      this.serverID = serverID;
   }

   public final boolean isGuider() {
      return this.isGuider;
   }

   public final void setGuider(boolean isGuider) {
      this.isGuider = isGuider;
   }

   public final String getPlatServerName() {
      return this.platServerName;
   }

   public final void setPlatServerName(String platServerName) {
      this.platServerName = platServerName;
   }

   public final Channel getNewChannel() {
      return this.newChannel;
   }

   public final void setNewChannel(Channel newChannel) {
      this.newChannel = newChannel;
   }

   public final Channel getOldChannel() {
      return this.oldChannel;
   }

   public final void setOldChannel(Channel oldChannel) {
      this.oldChannel = oldChannel;
   }

   public final boolean isClient() {
      return this.isClient;
   }

   public final void setClient(boolean isClient) {
      this.isClient = isClient;
   }

   public final User getUser() {
      return this.user;
   }

   public String getOpenId() {
      return this.openId;
   }

   public void setOpenId(String openId) {
      this.openId = openId;
   }

   public String getOpenKey() {
      return this.openKey;
   }

   public void setOpenKey(String openKey) {
      this.openKey = openKey;
   }

   public String getPf() {
      return this.pf;
   }

   public void setPf(String pf) {
      this.pf = pf;
   }

   public String getPfKey() {
      return this.pfKey;
   }

   public void setPfKey(String pfKey) {
      this.pfKey = pfKey;
   }

   public String getAccess_token() {
      return this.access_token;
   }

   public void setAccess_token(String access_token) {
      this.access_token = access_token;
   }

   public final void setUser(User user) {
      this.user = user;
   }

   public int getOpenServerId() {
      return this.openServerId;
   }

   public void setOpenServerId(int openServerId) {
      this.openServerId = openServerId;
   }
}
