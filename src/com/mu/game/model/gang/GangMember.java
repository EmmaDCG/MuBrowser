package com.mu.game.model.gang;

import com.mu.game.CenterManager;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.bluevip.BlueVip;
import com.mu.io.game.packet.imp.player.PlayerAttributes;
import org.jboss.netty.channel.Channel;

public class GangMember {
   private long id;
   private long gangId;
   private int level;
   private int profession;
   private int post = 0;
   private String userName = null;
   private String name = "";
   private long offlineTime = System.currentTimeMillis();
   private int vipLevel = 1;
   private int warPost = 0;
   private String blueTag = "0";
   private int curContribution = 0;
   private int hisContribution = 0;
   private int sid;

   public GangMember(long id, long gangId) {
      this.id = id;
      this.gangId = gangId;
   }

   public long getId() {
      return this.id;
   }

   public int getPost() {
      return this.post;
   }

   public void setPost(int post) {
      this.post = post;
   }

   public long getGangId() {
      return this.gangId;
   }

   public void setGangId(long gangId) {
      this.gangId = gangId;
   }

   public Gang getGang() {
      return GangManager.getGang(this.gangId);
   }

   public Player getPlayer() {
      return CenterManager.getPlayerByRoleID(this.id);
   }

   public boolean isOnline() {
      return CenterManager.isOnline(this.id);
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getUserName() {
      return this.userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public Channel getInterChannel() {
      return CenterManager.getInterChannel(this.userName, this.sid);
   }

   public int getLevel() {
      Player player = this.getPlayer();
      if (player != null) {
         this.level = player.getLevel();
      }

      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public int getProfession() {
      Player player = this.getPlayer();
      if (player != null) {
         this.profession = player.getProfessionID();
      }

      return this.profession;
   }

   public void setProfession(int profession) {
      this.profession = profession;
   }

   public long getOfflineTime() {
      return this.offlineTime;
   }

   public void setOfflineTime(long offlineTime) {
      this.offlineTime = offlineTime;
   }

   public int getVipLevel() {
      Player player = this.getPlayer();
      if (player != null) {
         this.vipLevel = player.getVipShowLevel();
      }

      return this.vipLevel;
   }

   public void setVipLevel(int vipLevel) {
      this.vipLevel = vipLevel;
   }

   public int getSid() {
      return this.sid;
   }

   public void setSid(int sid) {
      this.sid = sid;
   }

   public int getWarPost() {
      Gang gang = this.getGang();
      if (gang != null) {
         if (!gang.isWinner()) {
            return 0;
         } else if (gang.getMasterId() == this.id) {
            return 1;
         } else {
            return this.warPost == 1 ? 0 : this.warPost;
         }
      } else {
         return 0;
      }
   }

   public void setWarPost(int warPost) {
      this.warPost = warPost;
   }

   public String getBlueTag() {
      return this.blueTag;
   }

   public void setBlueTag(String blueTag) {
      this.blueTag = blueTag;
   }

   public int[] getBlueIcons() {
      return BlueVip.getBlueIcon(this.getBlueTag()).getIcons();
   }

   public String getVipImgText() {
      return BlueVip.getBlueIcon(this.getBlueTag()).getText();
   }

   public int getCurContribution() {
      return this.curContribution;
   }

   public void setCurContribution(int curContribution) {
      this.curContribution = curContribution;
   }

   public void addCurContribution(int con) {
      this.curContribution += con;
      Player player = this.getPlayer();
      if (player != null) {
         PlayerAttributes.sendToClient(player, StatEnum.Contribution);
      }

   }

   public void reduceContribution(int con) {
      this.curContribution -= con;
      if (this.curContribution < 0) {
         this.curContribution = 0;
      }

      Player player = this.getPlayer();
      if (player != null) {
         PlayerAttributes.sendToClient(player, StatEnum.Contribution);
      }

   }

   public int getHisContribution() {
      return this.hisContribution;
   }

   public void setHisContribution(int hisContribution) {
      this.hisContribution = hisContribution;
   }

   public void addHisContribution(int con) {
      this.hisContribution += con;
      Player player = this.getPlayer();
      if (player != null) {
         PlayerAttributes.sendToClient(player, StatEnum.HisContribution);
      }

   }
}
