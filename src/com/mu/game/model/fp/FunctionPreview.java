package com.mu.game.model.fp;

import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.unit.player.Player;
import java.util.ArrayList;
import java.util.HashMap;

public class FunctionPreview implements Comparable {
   private int id;
   private int sort;
   private String name;
   private int openLevel;
   private int receiveLevel;
   private FunctionPreview prePreview;
   private FunctionPreview nextPreview;
   private HashMap headerMap = new HashMap();
   private int header;
   private HashMap modelMap = new HashMap();
   private ArrayList rewardList = new ArrayList();
   private HashMap rewardMap = new HashMap();
   private String des1;
   private String des2;

   public FunctionPreview(int id) {
      this.id = id;
   }

   public int getSort() {
      return this.sort;
   }

   public void setSort(int sort) {
      this.sort = sort;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getOpenLevel() {
      return this.openLevel;
   }

   public void setOpenLevel(int openLevel) {
      this.openLevel = openLevel;
   }

   public int getReceiveLevel() {
      return this.receiveLevel;
   }

   public void setReceiveLevel(int receiveLevel) {
      this.receiveLevel = receiveLevel;
   }

   public int getId() {
      return this.id;
   }

   public int compareTo(FunctionPreview o) {
      return this.getSort() - o.getSort();
   }

   public FunctionPreview getPrePreview() {
      return this.prePreview;
   }

   public void setPrePreview(FunctionPreview prePreview) {
      this.prePreview = prePreview;
   }

   public FunctionPreview getNextPreview() {
      return this.nextPreview;
   }

   public void setNextPreview(FunctionPreview nextPreview) {
      this.nextPreview = nextPreview;
   }

   public void setHeader(int header) {
      this.header = header;
   }

   public void setHeader(int proType, int model) {
      this.headerMap.put(proType, model);
   }

   public int getHeader(Player player) {
      Integer m = (Integer)this.headerMap.get(player.getProType());
      return m == null ? this.header : m.intValue();
   }

   public String getDes1() {
      return this.des1;
   }

   public void setDes1(String des1) {
      this.des1 = des1;
   }

   public String getDes2() {
      return this.des2;
   }

   public void setDes2(String des2) {
      this.des2 = des2;
   }

   public void setModel(int proType, int model) {
      this.modelMap.put(proType, model);
   }

   public void addReward(ItemDataUnit data) {
      this.rewardList.add(data);
   }

   public void addReward(int pro, ItemDataUnit data) {
      ArrayList list = (ArrayList)this.rewardMap.get(pro);
      if (list == null) {
         list = new ArrayList();
         this.rewardMap.put(pro, list);
      }

      list.add(data);
   }

   public ArrayList getRewardList(Player player) {
      int proType = player.getProType();
      ArrayList list = (ArrayList)this.rewardMap.get(proType);
      return list == null ? this.rewardList : list;
   }

   @Override
   public int compareTo(Object o) {
      return 0;
   }
}
