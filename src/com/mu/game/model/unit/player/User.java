package com.mu.game.model.unit.player;

import com.mu.config.Global;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.unit.player.bluevip.BlueVip;
import com.mu.utils.buffer.BufferReader;
import com.mu.utils.buffer.BufferWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class User {
   public static final int Vip_None = 0;
   public static final int Vip_Normal = 1;
   public static final int Vip_Year = 2;
   public static final int Vip_Luxury = 3;
   public static final int Vip_Luxury_Year = 4;
   private String name = null;
   private int ingot = 0;
   private boolean needAntiAddiction = true;
   private long lastLogoutTime = 0L;
   private long lastLoginTime = 0L;
   private int restTime = 0;
   private int onlineTime = 0;
   private int serverID = Global.getServerID();
   private int openServerId = 0;
   private int warZoneId = Global.getServerID();
   private int totoalPay = 0;
   private int platId = -1;
   private ConcurrentHashMap consumeMap = null;
   private CopyOnWriteArrayList payList = new CopyOnWriteArrayList();
   private int shadow = 2;
   private boolean antiAliasing = true;
   private int perspective = 0;
   private boolean isMute = false;
   private int music = 50;
   private int sound = 50;
   private int vipType = 0;
   private int vipLevel = 0;
   private String openId;
   private String openKey;
   private String pf;
   private String pfKey;
   private String access_token;
   private String remoteIp = "127.0.0.1";
   private BlueVip blueVip = new BlueVip();

   public User(String name) {
      this.name = name;
   }

   public int getIngot() {
      return this.ingot;
   }

   public void setIngot(int ingot) {
      this.ingot = ingot;
   }

   public String getName() {
      return this.name;
   }

   public void setTotalPay(int pay) {
      this.totoalPay = pay;
   }

   public boolean isNeedAntiAddiction() {
      return this.needAntiAddiction;
   }

   public void setNeedAntiAddiction(boolean needAntiAddiction) {
      this.needAntiAddiction = needAntiAddiction;
   }

   public long getLastLogoutTime() {
      return this.lastLogoutTime;
   }

   public void setLastLogoutTime(long lastLogoutTime) {
      this.lastLogoutTime = lastLogoutTime;
   }

   public long getLastLoginTime() {
      return this.lastLoginTime;
   }

   public void setLastLoginTime(long lastLoginTime) {
      this.lastLoginTime = lastLoginTime;
   }

   public int getRestTime() {
      return this.restTime;
   }

   public void setRestTime(int restTime) {
      this.restTime = restTime;
   }

   public int getOnlineTime() {
      return this.onlineTime;
   }

   public void setOnlineTime(int onlineTime) {
      this.onlineTime = onlineTime;
   }

   public void addOnlineTime(int time) {
      this.onlineTime += time;
   }

   public int getPlatId() {
      return this.platId;
   }

   public void setPlatId(int platId) {
      this.platId = platId;
   }

   public void destroy() {
      this.name = null;
      if (this.payList != null) {
         this.payList.clear();
         this.payList = null;
      }

      if (this.consumeMap != null) {
         this.consumeMap.clear();
         this.consumeMap = null;
      }

   }

   public int getServerID() {
      return this.serverID;
   }

   public void setServerID(int serverID) {
      this.serverID = serverID;
   }

   public void addPay(int ingot, long payMillis) {
      this.payList.add(new PayInfo(payMillis, ingot));
      this.totoalPay += ingot;
   }

   public int getPay(long day) {
      int ingot = 0;
      Iterator var5 = this.payList.iterator();

      while(var5.hasNext()) {
         PayInfo info = (PayInfo)var5.next();
         if (info.getPayDay() == day) {
            ingot += info.getIngot();
         }
      }

      return ingot;
   }

   public int getPay(long begin, long end) {
      int count = 0;
      Iterator var7 = this.payList.iterator();

      while(var7.hasNext()) {
         PayInfo info = (PayInfo)var7.next();
         if (info.getPayTime() >= begin && info.getPayTime() <= end) {
            count += info.getIngot();
         }
      }

      return count;
   }

   public void addConsume(int ingot, long day, boolean inDB) {
      if (this.consumeMap == null) {
         this.consumeMap = new ConcurrentHashMap(8, 0.75F, 2);
      }

      Integer in = (Integer)this.consumeMap.get(day);
      int value = 0;
      if (in == null) {
         value = ingot;
      } else {
         value = in.intValue() + ingot;
      }

      this.consumeMap.put(day, value);
   }

   public PayInfo getLastPay() {
      int size = this.payList.size();
      return size > 0 ? (PayInfo)this.payList.get(size - 1) : null;
   }

   public String getRemoteIp() {
      return this.remoteIp;
   }

   public void setRemoteIp(String remoteIp) {
      this.remoteIp = remoteIp;
   }

   public int getConsume(long day) {
      if (this.consumeMap == null) {
         return 0;
      } else {
         Integer in = (Integer)this.consumeMap.get(day);
         return in == null ? 0 : in.intValue();
      }
   }

   public int getConsume(long begin, long end) {
      if (this.consumeMap == null) {
         return 0;
      } else {
         int count = 0;
         Iterator it = this.consumeMap.entrySet().iterator();

         while(it.hasNext()) {
            Entry entry = (Entry)it.next();
            long day = ((Long)entry.getKey()).longValue();
            if (day >= begin && day <= end) {
               count += ((Integer)entry.getValue()).intValue();
            }
         }

         return count;
      }
   }

   public int getTotalPay() {
      return this.totoalPay;
   }

   public int getShadow() {
      return this.shadow;
   }

   public void setShadow(int shadow) {
      if (shadow >= 0 && shadow <= 2) {
         this.shadow = shadow;
      }
   }

   public boolean isAntiAliasing() {
      return this.antiAliasing;
   }

   public void setAntiAliasing(boolean antiAliasing) {
      this.antiAliasing = antiAliasing;
   }

   public int getPerspective() {
      return this.perspective;
   }

   public void setPerspective(int perspective) {
      if (perspective >= 0 && perspective <= 2) {
         this.perspective = perspective;
      }
   }

   public boolean isMute() {
      return this.isMute;
   }

   public void setMute(boolean isMute) {
      this.isMute = isMute;
   }

   public int getMusic() {
      return this.music;
   }

   public void setMusic(int music) {
      if (music >= 0 && music <= 100) {
         this.music = music;
      }
   }

   public int getSound() {
      return this.sound;
   }

   public void setSound(int sound) {
      if (sound >= 0 && sound <= 100) {
         this.sound = sound;
      }
   }

   public void resetSetup(byte[] set) {
      if (set != null && set.length != 0) {
         BufferReader reader = new BufferReader(set);

         try {
            this.setShadow(reader.readByte());
            this.setAntiAliasing(reader.readBoolean());
            this.setPerspective(reader.readByte());
            this.setMute(reader.readBoolean());
            this.setMusic(reader.readByte());
            this.setSound(reader.readByte());
         } catch (Exception var12) {
            var12.printStackTrace();
         } finally {
            try {
               reader.destroy();
            } catch (Exception var11) {
               var11.printStackTrace();
            }

         }

      }
   }

   public CopyOnWriteArrayList getPayList() {
      return this.payList;
   }

   public int getWarZoneId() {
      return this.warZoneId;
   }

   public void setWarZoneId(int warZoneId) {
      this.warZoneId = warZoneId;
   }

   public byte[] getSetupBytes() {
      BufferWriter writer = new BufferWriter();

      try {
         writer.writeByte(this.getShadow());
         writer.writeBoolean(this.isAntiAliasing());
         writer.writeByte(this.getPerspective());
         writer.writeBoolean(this.isMute());
         writer.writeByte(this.getMusic());
         writer.writeByte(this.getSound());
         writer.flush();
         byte[] bytes = writer.toByteArray();
         byte[] var4 = bytes;
         return var4;
      } catch (Exception var7) {
         var7.printStackTrace();
      } finally {
         writer.destroy();
      }

      return null;
   }

   public int getVipType() {
      return this.vipType;
   }

   public void setVipType(int vipType) {
      this.vipType = vipType;
   }

   public int getVipLevel() {
      return this.vipLevel;
   }

   public void setVipLevel(int vipLevel) {
      this.vipLevel = vipLevel;
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

   public BlueVip getBlueVip() {
      return this.blueVip;
   }

   public int getOpenServerId() {
      return this.openServerId;
   }

   public void setOpenServerId(int openServerId) {
      this.openServerId = openServerId;
   }

   public int getTotleRed(int redId) {
      ArrayList bindList = GangManager.getBindRedList();
      int count = 0;
      Iterator var5 = this.payList.iterator();

      while(true) {
         while(var5.hasNext()) {
            PayInfo info = (PayInfo)var5.next();
            int size = bindList.size();

            for(int i = size - 1; i >= 0; --i) {
               int[] in = (int[])bindList.get(i);
               if (info.getIngot() >= in[1]) {
                  if (redId == in[0]) {
                     count += info.getIngot() / in[1];
                  }
                  break;
               }
            }
         }

         return count;
      }
   }
}
