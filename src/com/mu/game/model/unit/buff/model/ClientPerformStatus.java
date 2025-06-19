package com.mu.game.model.unit.buff.model;

import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jxl.Sheet;

public class ClientPerformStatus {
   private static HashMap statusMap = new HashMap();
   private int statusID;
   private boolean cannotMove;
   private boolean isDingshen;
   private boolean isXuanyun;
   private boolean isBingdong;
   private boolean isZhongdu;
   private boolean isYinshen;
   private boolean isChangeSpeed;
   private boolean isChangeAttackSpeed;
   private boolean isZhuoshao;
   private List performStatus = new ArrayList();

   public ClientPerformStatus() {
   }

   public ClientPerformStatus(int statusID, boolean cannotMove, boolean isDingshen, boolean isXuanyun, boolean isBingdong, boolean isZhongdu, boolean isYinshen, boolean isChangeSpeed, boolean isChangeAttackSpeed, boolean isZhuoshao) {
      this.statusID = statusID;
      this.cannotMove = cannotMove;
      this.isDingshen = isDingshen;
      this.isXuanyun = isXuanyun;
      this.isBingdong = isBingdong;
      this.isZhongdu = isZhongdu;
      this.isYinshen = isYinshen;
      this.isChangeSpeed = isChangeSpeed;
      this.isChangeAttackSpeed = isChangeAttackSpeed;
      this.isZhuoshao = isZhuoshao;
      if (cannotMove) {
         this.performStatus.add(Integer.valueOf(1));
      }

      if (isDingshen) {
         this.performStatus.add(Integer.valueOf(2));
      }

      if (isXuanyun) {
         this.performStatus.add(Integer.valueOf(3));
      }

      if (isBingdong) {
         this.performStatus.add(Integer.valueOf(4));
      }

      if (isZhongdu) {
         this.performStatus.add(Integer.valueOf(5));
      }

      if (isYinshen) {
         this.performStatus.add(Integer.valueOf(6));
      }

      if (isChangeSpeed) {
         this.performStatus.add(Integer.valueOf(8));
      }

      if (isChangeAttackSpeed) {
         this.performStatus.add(Integer.valueOf(7));
      }

      if (isZhuoshao) {
         this.performStatus.add(Integer.valueOf(9));
      }

   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 3; i <= rows; ++i) {
         int statusID = Tools.getCellIntValue(sheet.getCell("A" + i));
         boolean cannotMove = Tools.getCellIntValue(sheet.getCell("B" + i)) == 1;
         boolean isDingshen = Tools.getCellIntValue(sheet.getCell("C" + i)) == 1;
         boolean isXuanyun = Tools.getCellIntValue(sheet.getCell("D" + i)) == 1;
         boolean isBingdong = Tools.getCellIntValue(sheet.getCell("E" + i)) == 1;
         boolean isZhongdu = Tools.getCellIntValue(sheet.getCell("F" + i)) == 1;
         boolean isYinshen = Tools.getCellIntValue(sheet.getCell("G" + i)) == 1;
         boolean isChangeSpeed = Tools.getCellIntValue(sheet.getCell("H" + i)) == 1;
         boolean isChangeAttackSpeed = Tools.getCellIntValue(sheet.getCell("I" + i)) == 1;
         boolean isZhuoshao = Tools.getCellFloatValue(sheet.getCell("J" + i)) == 1.0F;
         ClientPerformStatus pStatus = new ClientPerformStatus(statusID, cannotMove, isDingshen, isXuanyun, isBingdong, isZhongdu, isYinshen, isChangeSpeed, isChangeAttackSpeed, isZhuoshao);
         statusMap.put(statusID, pStatus);
      }

   }

   public static ClientPerformStatus getPerformStatus(int statusID) {
      return (ClientPerformStatus)statusMap.get(statusID);
   }

   public List getPerformStatus() {
      return this.performStatus;
   }

   public void setPerformStatus(List performStatus) {
      this.performStatus = performStatus;
   }

   public int getStatusID() {
      return this.statusID;
   }

   public void setStatusID(int statusID) {
      this.statusID = statusID;
   }

   public boolean isCannotMove() {
      return this.cannotMove;
   }

   public void setCannotMove(boolean cannotMove) {
      this.cannotMove = cannotMove;
   }

   public boolean isDingshen() {
      return this.isDingshen;
   }

   public void setDingshen(boolean isDingshen) {
      this.isDingshen = isDingshen;
   }

   public boolean isXuanyun() {
      return this.isXuanyun;
   }

   public void setXuanyun(boolean isXuanyun) {
      this.isXuanyun = isXuanyun;
   }

   public boolean isBingdong() {
      return this.isBingdong;
   }

   public void setBingdong(boolean isBingdong) {
      this.isBingdong = isBingdong;
   }

   public boolean isZhongdu() {
      return this.isZhongdu;
   }

   public void setZhongdu(boolean isZhongdu) {
      this.isZhongdu = isZhongdu;
   }

   public boolean isYinshen() {
      return this.isYinshen;
   }

   public void setYinshen(boolean isYinshen) {
      this.isYinshen = isYinshen;
   }

   public boolean isChangeSpeed() {
      return this.isChangeSpeed;
   }

   public void setChangeSpeed(boolean isChangeSpeed) {
      this.isChangeSpeed = isChangeSpeed;
   }

   public boolean isChangeAttackSpeed() {
      return this.isChangeAttackSpeed;
   }

   public void setChangeAttackSpeed(boolean isChangeAttackSpeed) {
      this.isChangeAttackSpeed = isChangeAttackSpeed;
   }

   public boolean isZhuoshao() {
      return this.isZhuoshao;
   }

   public void setZhuoshao(boolean isZhuoshao) {
      this.isZhuoshao = isZhuoshao;
   }
}
