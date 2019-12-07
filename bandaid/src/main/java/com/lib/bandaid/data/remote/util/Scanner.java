package com.lib.bandaid.data.remote.util;

import android.content.Context;

import java.util.Enumeration;

import dalvik.system.DexFile;


public class Scanner {

    public static void scan(Context ctx, String entityPackage, IWhat what) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            DexFile dex = new DexFile(ctx.getPackageResourcePath());
            Enumeration<String> entries = dex.entries();
            String entryName;
            Class clazz;
            long t1 = System.currentTimeMillis();
            while (entries.hasMoreElements()) {
                entryName = entries.nextElement();
                if (entryName.contains(entityPackage)) {
                    if (entryName.toLowerCase().contains("controller")) {
                        clazz = dex.loadClass(entryName, classLoader);
                        what.execute(clazz);
                    }
                }
            }
            System.out.println(">>>>>>>>>:" + (System.currentTimeMillis() - t1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface IWhat {
        void execute(Class<?> clazz);

        void end();
    }

}
