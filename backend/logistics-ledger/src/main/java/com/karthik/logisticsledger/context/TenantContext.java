package com.karthik.logisticsledger.context;


public class TenantContext {

    private static final ThreadLocal<Long> CURRENT_TENANT= new ThreadLocal<>();


   public static void setTenantId(Long id){
       CURRENT_TENANT.set(id);
   }

   public static Long getTenantId(){
       return CURRENT_TENANT.get();
   }

   public static void clear(){
       CURRENT_TENANT.remove();
   }
}
