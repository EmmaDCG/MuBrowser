package com.mu.game.model.equip.equipSet;

import com.mu.game.model.item.Item;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class EquipSet {
   private int setID;
   private HashMap equipIDs = null;
   private List stats = null;
   private int domineering = 0;

   public EquipSet(int setID) {
      this.setID = setID;
      this.equipIDs = new HashMap();
      this.stats = new ArrayList();
   }

   public EquipSetModel getModel() {
      return EquipSetModel.getModel(this.setID);
   }

   public boolean addEquip(Item item) {
      if (item.getSetID() != this.setID) {
         return false;
      } else {
         EquipSetModel model = this.getModel();
         if (!model.getEquipIDs().contains(item.getModelID())) {
            return false;
         } else {
            this.addEuqipID(item.getModelID(), item.getID());
            this.calStats();
            return true;
         }
      }
   }

   public boolean delEquip(Item item) {
      if (this.setID != item.getSetID()) {
         return false;
      } else {
         EquipSetModel model = this.getModel();
         if (!model.getEquipIDs().contains(item.getModelID())) {
            return false;
         } else {
            this.delEquipID(item.getModelID(), item.getID());
            this.calStats();
            return true;
         }
      }
   }

   private void addEuqipID(int modelID, long objID) {
      HashSet objIds = (HashSet)this.equipIDs.get(modelID);
      if (objIds == null) {
         objIds = new HashSet();
         this.equipIDs.put(modelID, objIds);
      }

      objIds.add(objID);
   }

   private void delEquipID(int modelID, long objID) {
      HashSet objIDs = (HashSet)this.equipIDs.get(modelID);
      if (objIDs != null) {
         objIDs.remove(objID);
         if (objIDs.size() < 1) {
            this.equipIDs.remove(modelID);
         }
      }

   }

   public int getStatsCount() {
      return this.stats.size();
   }

   private void calStats() {
      this.stats.clear();
      int size = this.getItemSize();
      int domi = 0;
      EquipSetModel model = EquipSetModel.getModel(this.setID);
      Iterator var5 = model.getSetStats().keySet().iterator();

      while(var5.hasNext()) {
         int i = ((Integer)var5.next()).intValue();
         if (i <= size) {
            List fms = (List)model.getSetStats().get(i);
            this.stats.addAll(fms);
            domi += model.getDomiByCount(i);
         }
      }

      this.setDomineering(domi);
   }

   public int getItemSize() {
      int size = 0;

      HashSet itemIDs;
      for(Iterator var3 = this.equipIDs.values().iterator(); var3.hasNext(); size += itemIDs.size()) {
         itemIDs = (HashSet)var3.next();
      }

      return size;
   }

   public HashSet getEquipModelIDs() {
      HashSet ids = new HashSet();
      ids.addAll(this.equipIDs.keySet());
      return ids;
   }

   public boolean hasModelID(int modelID) {
      return this.equipIDs.containsKey(modelID);
   }

   public int getSetID() {
      return this.setID;
   }

   public List getStats() {
      return this.stats;
   }

   public HashSet getItemIDs(int modelID) {
      return (HashSet)this.equipIDs.get(modelID);
   }

   public int getDomineering() {
      return this.domineering;
   }

   public void setDomineering(int domineering) {
      this.domineering = domineering;
   }

   public void destroy() {
      Iterator var2 = this.equipIDs.values().iterator();

      while(var2.hasNext()) {
         HashSet objID = (HashSet)var2.next();
         objID.clear();
      }

      this.equipIDs.clear();
      this.stats.clear();
   }
}
