package org.example.counterN;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class CounterN {
   private final AtomicLong countN=new AtomicLong(0);

   public void increment(){
       countN.incrementAndGet();
   }

   public long getCount(){
       return countN.get();
   }

   public void reset(){
       countN.set(0);
   }
}
