package com.mu.game.top;

import java.util.ArrayList;

public class TopRewardInfo {
   private int topId;
   private int top;
   private ArrayList attrList = null;
   private String attrDes;
   private String nextDes;
   private String nextAttrDes;
   private int titleId;

   public int getTopId() {
      return this.topId;
   }

   public void setTopId(int topId) {
      this.topId = topId;
   }

   public int getTop() {
      return this.top;
   }

   public void setTop(int top) {
      this.top = top;
   }

   public ArrayList getAttrList() {
      return this.attrList;
   }

   public void setAttrList(ArrayList attrList) {
      this.attrList = attrList;
   }

   public String getAttrDes() {
      return this.attrDes;
   }

   public void setAttrDes(String attrDes) {
      this.attrDes = attrDes;
   }

   public String getNextDes() {
      return this.nextDes;
   }

   public void setNextDes(String nextDes) {
      this.nextDes = nextDes;
   }

   public String getNextAttrDes() {
      return this.nextAttrDes;
   }

   public void setNextAttrDes(String nextAttrDes) {
      this.nextAttrDes = nextAttrDes;
   }

   public int getTitleId() {
      return this.titleId;
   }

   public void setTitleId(int titleId) {
      this.titleId = titleId;
   }
}
