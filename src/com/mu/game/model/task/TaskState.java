package com.mu.game.model.task;

public enum TaskState {
   NEW(0),
   RUN(1),
   OVER(3);

   private int value;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$task$TaskState;

   private TaskState(int value) {
      this.value = value;
   }

   public static TaskState valueOf(int value) {
      TaskState[] var4;
      int var3 = (var4 = values()).length;

      for(int var2 = 0; var2 < var3; ++var2) {
         TaskState state = var4[var2];
         if (state.getValue() == value) {
            return state;
         }
      }

      return NEW;
   }

   public int getValue() {
      return this.value;
   }

   public static TaskState.TaskClientState getClientState(Task task) {
      if (task == null) {
         return TaskState.TaskClientState.NEW;
      } else {
         switch($SWITCH_TABLE$com$mu$game$model$task$TaskState()[task.getState().ordinal()]) {
         case 1:
            return TaskState.TaskClientState.NEW;
         case 2:
            return task.isComplete() ? TaskState.TaskClientState.RUN_COMPLETED : TaskState.TaskClientState.RUN_UNCOMPLETED;
         case 3:
            return TaskState.TaskClientState.OVER;
         default:
            return TaskState.TaskClientState.valueOf(task.getState().getValue());
         }
      }
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$mu$game$model$task$TaskState() {
      int[] var10000 = $SWITCH_TABLE$com$mu$game$model$task$TaskState;
      if ($SWITCH_TABLE$com$mu$game$model$task$TaskState != null) {
         return var10000;
      } else {
         int[] var0 = new int[values().length];

         try {
            var0[NEW.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[OVER.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[RUN.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$mu$game$model$task$TaskState = var0;
         return var0;
      }
   }

   public static enum TaskClientState {
      NEW(TaskState.NEW.getValue()),
      RUN_UNCOMPLETED(TaskState.RUN.getValue()),
      RUN_COMPLETED(TaskState.RUN.getValue() + 1),
      OVER(TaskState.OVER.getValue());

      private int value;

      private TaskClientState(int value) {
         this.value = value;
      }

      public static TaskState.TaskClientState valueOf(int value) {
         TaskState.TaskClientState[] var4;
         int var3 = (var4 = values()).length;

         for(int var2 = 0; var2 < var3; ++var2) {
            TaskState.TaskClientState state = var4[var2];
            if (state.getValue() == value) {
               return state;
            }
         }

         return NEW;
      }

      public int getValue() {
         return this.value;
      }

      public boolean is(TaskState.TaskClientState tcs) {
         return this == tcs;
      }
   }
}
