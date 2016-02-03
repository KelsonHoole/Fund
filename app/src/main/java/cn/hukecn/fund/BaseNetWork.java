//package cn.hukecn.fund;
//
//import android.content.Context;
//
///**
// * Created by Kelson on 2015/11/9.
// */
//public class BaseNetWork {
//    public static BaseNetWork instance = null;
//    public static AppThreadPool pool = null;
//    public static Context appContext = null;
//    private BaseNetWork(){ pool = AppThreadPool.getInstance();};
//
//    synchronized public static BaseNetWork getInstance(Context context){
//        if(context != null)
//        {
//            appContext = context;
//        }
//
//        if(instance == null)
//        {
//            instance = new BaseNetWork();
//        }
//        return instance;
//    }
//
////    public void getFundInfo(String fundID, AsyncHttp.HttpListener listener)
////    {
////        if(pool != null)
////            pool.execute(AppConfig.BASEURL+fundID+".js?rt="+System.currentTimeMillis(),listener);
////    }
//
////    public void getLatestIndex(AsyncHttp.HttpListener listener){
////        if(pool != null)
////            pool.execute(AppConfig.LATESTINDEXURL,listener);
////
////
////    }
//
////   public void poolShutDown(){
////         if(pool != null)
////            pool.shutDown();
////    }
//
//}
