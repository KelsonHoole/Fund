package cn.hukecn.fund;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Kelson on 2015/12/5.
 */
public class AppThreadPool {
    private static AppThreadPool ourInstance = null;
    private static ExecutorService pool;

    public static AppThreadPool getInstance() {
        if(ourInstance == null)
            ourInstance = new AppThreadPool();
        return ourInstance;
    }

    private AppThreadPool() {
        pool = Executors.newCachedThreadPool();
    }

    public ExecutorService getPool(){
        return pool;
    }

//    public void execute(String url, AsyncHttp.HttpListener listener){
//        if (pool != null)
//        {
//            pool.execute(new Thread(new AsyncHttp(url,listener)));
//        }
//    }
//
//    public void shutDown(){
//        if(pool != null && !pool.isShutdown())
//        {
//            pool.shutdown();
//            pool = null;
//        }
//    }
}
