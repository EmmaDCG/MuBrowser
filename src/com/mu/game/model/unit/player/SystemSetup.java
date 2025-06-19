package com.mu.game.model.unit.player;

import com.mu.utils.buffer.BufferReader;
import com.mu.utils.buffer.BufferWriter;

public class SystemSetup {
   private boolean autoInTeamWhenBeInvite = true;
   private boolean autoInTeamWhenBeApply = true;
   private boolean showPlayer = true;
   private boolean showMonster = true;
   private boolean showEffect = true;
   private boolean showPet = true;

   public void reset(byte[] set) {
      if (set != null) {
         BufferReader reader = new BufferReader(set);

         try {
            this.setAutoInTeamWhenBeApply(reader.readBoolean());
            this.setAutoInTeamWhenBeInvite(reader.readBoolean());
            this.setShowPlayer(reader.readBoolean());
            this.setShowMonster(reader.readBoolean());
            this.setShowPet(reader.readBoolean());
            this.setShowEffect(reader.readBoolean());
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

   public byte[] getBytes() {
      BufferWriter writer = new BufferWriter();

      try {
         writer.writeBoolean(this.isAutoInTeamWhenBeApply());
         writer.writeBoolean(this.isAutoInTeamWhenBeInvite());
         writer.writeBoolean(this.isShowPlayer());
         writer.writeBoolean(this.isShowMonster());
         writer.writeBoolean(this.isShowPet());
         writer.writeBoolean(this.isShowEffect());
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

   public boolean isAutoInTeamWhenBeInvite() {
      return this.autoInTeamWhenBeInvite;
   }

   public void setAutoInTeamWhenBeInvite(boolean autoInTeamWhenBeInvite) {
      this.autoInTeamWhenBeInvite = autoInTeamWhenBeInvite;
   }

   public boolean isShowPet() {
      return this.showPet;
   }

   public void setShowPet(boolean showPet) {
      this.showPet = showPet;
   }

   public boolean isAutoInTeamWhenBeApply() {
      return this.autoInTeamWhenBeApply;
   }

   public void setAutoInTeamWhenBeApply(boolean autoInTeamWhenBeApply) {
      this.autoInTeamWhenBeApply = autoInTeamWhenBeApply;
   }

   public boolean isShowPlayer() {
      return this.showPlayer;
   }

   public void setShowPlayer(boolean showPlayer) {
      this.showPlayer = showPlayer;
   }

   public boolean isShowMonster() {
      return this.showMonster;
   }

   public void setShowMonster(boolean showMonster) {
      this.showMonster = showMonster;
   }

   public boolean isShowEffect() {
      return this.showEffect;
   }

   public void setShowEffect(boolean showEffect) {
      this.showEffect = showEffect;
   }
}
