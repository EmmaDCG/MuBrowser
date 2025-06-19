package com.mu.game.model.unit.robot;

import com.mu.utils.Rnd;
import java.awt.Point;
import java.util.ArrayList;

public class RobotInfo {
   private String name;
   private Point point;
   private ArrayList equips;
   private ArrayList skills;
   private int pro;
   private int level;
   private int serachDistance = 10000;
   private int radius = 10000;
   private ArrayList words = null;

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Point getPoint() {
      return this.point;
   }

   public void setPoint(Point point) {
      this.point = point;
   }

   public ArrayList getEquips() {
      return this.equips;
   }

   public void setEquips(ArrayList equips) {
      this.equips = equips;
   }

   public int getRadius() {
      return this.radius;
   }

   public void setRadius(int radius) {
      this.radius = radius;
   }

   public ArrayList getSkills() {
      return this.skills;
   }

   public void setSkills(ArrayList skills) {
      this.skills = skills;
   }

   public int getPro() {
      return this.pro;
   }

   public void setPro(int pro) {
      this.pro = pro;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public int getSerachDistance() {
      return this.serachDistance;
   }

   public void setSerachDistance(int serachDistance) {
      this.serachDistance = serachDistance;
   }

   public void addWord(String word) {
      if (this.words == null) {
         this.words = new ArrayList();
      }

      this.words.add(word);
   }

   public String getRndWord() {
      return this.words != null && this.words.size() != 0 ? (String)this.words.get(Rnd.get(this.words.size())) : null;
   }
}
