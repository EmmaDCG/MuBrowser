package com.mu.game.model.unit.player.pkMode;

public class PKMode {
   private PkEnum lastMode;
   private PkEnum mode;

   public PKMode() {
      this.lastMode = PkEnum.Mode_Peace;
      this.mode = PkEnum.Mode_Peace;
      this.mode = PkEnum.Mode_Peace;
   }

   public boolean isPKing() {
      boolean peace = this.mode == PkEnum.Mode_Peace;
      return !peace;
   }

   public void setPKModeById(int modeID) {
      this.mode = PkEnum.find(modeID);
   }

   public void setPkMode(PkEnum pkEnum) {
      this.mode = pkEnum;
   }

   public int getPKModeID() {
      return this.mode.getModeID();
   }

   public PkEnum getCurrentPKMode() {
      return this.mode;
   }

   public PkEnum getLastMode() {
      return this.lastMode;
   }

   public int getModeToDB() {
      int tmpMode = this.mode.getModeID();
      return !this.mode.isManulSwitch() ? this.lastMode.getModeID() : tmpMode;
   }

   public void changePkMode(PkEnum newMode) {
      this.lastMode = this.mode;
      this.mode = newMode;
   }
}
