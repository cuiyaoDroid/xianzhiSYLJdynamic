package com.example.web.image;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.util.Log;


public class ImageMemoryCache {
    /**
     * ���ڴ��ȡ�����ٶ������ģ�Ϊ�˸����޶�ʹ���ڴ棬����ʹ�������㻺�档
     * Ӳ���û��治�����ױ����գ��������泣�����ݣ������õ�ת�������û��档
     */
    private static final int SOFT_CACHE_SIZE = 15;  //�����û�������
	private static LruCache<String, Bitmap> mLruCache;  //Ӳ���û���
    private static LinkedHashMap<String, SoftReference<Bitmap>> mSoftCache;  //�����û���
    private int DELAY_BEFORE_PURGE=10*1000;                                                      
    public ImageMemoryCache(Context context) {
    	int memClass = ((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        int cacheSize = 1024 * 1024 * memClass / 4;  //Ӳ���û���������Ϊϵͳ�����ڴ��1/4
        Log.i("memoryallocsize", ""+cacheSize);
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
            protected int sizeOf(String key, Bitmap value) {
                if (value != null)
                    return value.getRowBytes() * value.getHeight();
                else
                    return 0;
            }
                                                                                          
            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                if (oldValue != null)
                    // Ӳ���û�����������ʱ�򣬻����LRU�㷨�����û�б�ʹ�õ�ͼƬת��������û���
                    mSoftCache.put(key, new SoftReference<Bitmap>(oldValue));
            }
        };
        mSoftCache = new LinkedHashMap<String, SoftReference<Bitmap>>(SOFT_CACHE_SIZE, 0.75f, true) {
            private static final long serialVersionUID = 6040103833179403725L;
            @Override
            protected boolean removeEldestEntry(Entry<String, SoftReference<Bitmap>> eldest) {
                if (size() > SOFT_CACHE_SIZE){    
                    return true;  
                }  
                return false; 
            }
        };
    }
    private final Handler purgeHandler = new Handler();

    private final Runnable purger = new Runnable() {
        public void run() {
        	clearCache();
        }
    };  


    /**
     * Allow a new delay before the automatic cache clear is done.
     */
    public void resetPurgeTimer() {
        purgeHandler.removeCallbacks(purger);
        purgeHandler.postDelayed(purger, DELAY_BEFORE_PURGE);
    }
    /**
     * �ӻ����л�ȡͼƬ
     */
    public Bitmap getBitmapFromCache(String url) {
        Bitmap bitmap;
        //�ȴ�Ӳ���û����л�ȡ
        synchronized (mLruCache) {
            bitmap = mLruCache.get(url);
            if (bitmap != null) {
                //����ҵ��Ļ�����Ԫ���Ƶ�LinkedHashMap����ǰ�棬�Ӷ���֤��LRU�㷨�������ɾ��
                mLruCache.remove(url);
                mLruCache.put(url, bitmap);
                return bitmap;
            }
        }
        //���Ӳ���û������Ҳ������������û�������
        synchronized (mSoftCache) { 
            SoftReference<Bitmap> bitmapReference = mSoftCache.get(url);
            if (bitmapReference != null) {
                bitmap = bitmapReference.get();
                if (bitmap != null) {
                    //��ͼƬ�ƻ�Ӳ����
                    mLruCache.put(url, bitmap);
                    mSoftCache.remove(url);
                    return bitmap;
                } else {
                    mSoftCache.remove(url);
                }
            }
        }
        return null;
    } 
                                                                                  
    /**
     * ���ͼƬ������
     */
    public void addBitmapToCache(String url, Bitmap bitmap) {
        if (bitmap != null) {
            synchronized (mLruCache) {
                mLruCache.put(url, bitmap);
            }
        }
    }
                                                                                  
    public void clearCache() {
        mSoftCache.clear();
    }
}