package com.dev.toxa.integrate;

import android.util.Log;

public class LoggingNameClass {

     public String parseName(String parseName) {
         int index = 0;
         String[] nameClass = parseName.split("\\.");
         index = nameClass.length - 1;

         return nameClass[index];
     }
}
