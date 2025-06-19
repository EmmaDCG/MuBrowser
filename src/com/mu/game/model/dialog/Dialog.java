package com.mu.game.model.dialog;

import com.mu.utils.CommonRegPattern;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class Dialog {
   private long id;
   private String title;
   private int icon;
   private String contentStr;
   private List optionList;

   public Dialog(long id) {
      this.id = id;
   }

   public long getId() {
      return this.id;
   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public int getIcon() {
      return this.icon;
   }

   public void setIcon(int icon) {
      this.icon = icon;
   }

   public String getContentStr() {
      return this.contentStr;
   }

   public void setContentStr(String contentStr) {
      this.contentStr = contentStr;
   }

   public void setOptionList(String optionStr) {
      this.optionList = new ArrayList();
      Matcher m = CommonRegPattern.PATTERN_INT.matcher(optionStr);

      while(m.find()) {
         DialogOption option = DialogConfigManager.getOption(Integer.parseInt(m.group()));
         if (option != null) {
            this.optionList.add(option);
         }
      }

   }

   public List getOptionList() {
      return this.optionList;
   }

   public boolean contains(DialogOption option) {
      return this.optionList.contains(option);
   }
}
