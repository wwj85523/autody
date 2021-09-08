package com.zywwj.autody.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;

import java.util.List;

public class PackUtil {

    /**
     * 获取所有App
     * @param context
     * @return
     */
    public static List<PackageInfo> getDeviceApp(Context context){
        return context.getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES);
    }

    /**
     * 判断App是否安装
     * @param context
     * @param packageName
     * @return
     */
   public static boolean isInstalled(Context context,String packageName){
       return getPackageInfo(context,packageName)!=null;
   }

    /**
     * 通过包名，获取信息
     * @param context
     * @param packageName
     * @return
     */
   public static PackageInfo getPackageInfo(Context context,String packageName){
       if(TextUtils.isEmpty(packageName)){
           return null;
       }
       try {
           return context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
       }catch (PackageManager.NameNotFoundException e){
           e.printStackTrace();
       }
       return null;
   }

    /**
     *
     * @param context
     * @param packageName
     * @return
     */
   public static int getVerCode(Context context,String packageName) {
       return getPackageInfo(context,packageName).versionCode;
   }

    /**
     *
     * @param context
     * @param packageName
     * @return
     */
    public static String getVerName(Context context,String packageName) {
        return getPackageInfo(context,packageName).versionName;
    }

    /**
     * 获取App名
     * @param context
     * @param packageName
     * @return
     */
    public static String getAppName(Context context,String packageName){
        return getPackageInfo(context,packageName).applicationInfo.loadLabel(context.getPackageManager()).toString();
    }

    /**
     * 获取安装包信息
     * @param context
     * @param apkPath
     * @return
     */
    public static PackageInfo getPackInfoByApk(Context context,String apkPath){
        return context.getPackageManager().getPackageArchiveInfo(apkPath,PackageManager.GET_ACTIVITIES);
    }


    /**
     * 打开应用
     * @param context
     * @param packageName
     * @return
     */
    public static boolean openPackage(Context context, String packageName) {
        Context pkgContext = getPackageContext(context, packageName);
        Intent intent = getAppOpenIntentByPackageName(context, packageName);
        if (pkgContext != null && intent != null) {
            pkgContext.startActivity(intent);
            return true;
        }
        return false;
    }

    public static Context getPackageContext(Context context, String packageName) {
        Context pkgContext = null;
        if (context.getPackageName().equals(packageName)) {
            pkgContext = context;
        } else {
            try {
                pkgContext = context.createPackageContext(packageName,Context.CONTEXT_IGNORE_SECURITY| Context.CONTEXT_INCLUDE_CODE);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return pkgContext;
    }

    public static Intent getAppOpenIntentByPackageName(Context context,String packageName){
        //Activity完整名
        String mainAct = null;
        //根据包名寻找
        PackageManager pkgMag = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED|Intent.FLAG_ACTIVITY_NEW_TASK);
        List<ResolveInfo> list = pkgMag.queryIntentActivities(intent,0);
        for (int i = 0; i < list.size(); i++) {
            ResolveInfo info = list.get(i);
            if (info.activityInfo.packageName.equals(packageName)) {
                mainAct = info.activityInfo.name;
                break;
            }
        }
        if (TextUtils.isEmpty(mainAct)) {
            return null;
        }
        intent.setComponent(new ComponentName(packageName, mainAct));
        return intent;
    }
}
