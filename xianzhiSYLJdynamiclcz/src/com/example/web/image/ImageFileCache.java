package com.example.web.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.xianzhisecuritycheck.main.SecurityCheckApp;

public class ImageFileCache {
    private static final String WHOLESALE_CONV = "";
                                                            
    private static final int MB = 1024*1024;
    private static final int CACHE_SIZE = 10;
    private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;
    /**
	 * ���洢���Ƿ����
	 * 
	 * @return
	 */
	public static boolean isHasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}                                                  
    public ImageFileCache() {
        //�����ļ�����
        removeCache(getDirectory());
    }
    public static void compressImage(String from , String to){
    	File file = new File(from);
    	Bitmap bitmap;
		if (file.length() > 1024*1024 * 2) {
			bitmap = PictureUtil.getimage(from, 480, 800, 1024*1024 * 2);
		}else{
			bitmap=PictureUtil.getSmallBitmap(from);
		}
		File tofile = new File(to);
        try {
        	FileOutputStream outStream = new FileOutputStream(tofile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outStream);
            bitmap.recycle();
            new File(from).delete();
            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {
            Log.w("ImageFileCache", "FileNotFoundException");
        } catch (IOException e) {
            Log.w("ImageFileCache", "IOException");
        }
	}                                                      
    /** �ӻ����л�ȡͼƬ **/
    public Bitmap getImage(final String url,String type) {    
        final String path = getDirectory()  +type+ convertUrlToFileName(url);
        Log.i(path, path);
        File file = new File(path);
        if (file.exists()) {
            Bitmap bmp;
            try {
            	bmp = PictureUtil.getSmallBitmap(path);
			} catch (OutOfMemoryError e) {
				// TODO: handle exception
				bmp = PictureUtil.getimage(path, 480, 800, 1024*1024 * 2);
			}
			if (bmp == null) {
			    file.delete();
			} else {
			    updateFileTime(path);
			    return bmp;
			}
            
        }
        return null;
    }
    private Bitmap convertBitmap(File file) throws IOException {
        Bitmap bitmap = null;
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        FileInputStream fis = new FileInputStream(file.getAbsolutePath());
        BitmapFactory.decodeStream(fis, null, o);
        fis.close();
        final int REQUIRED_SIZE = 70;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                        break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
        }
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inSampleSize = scale;
        fis = new FileInputStream(file.getAbsolutePath());
        bitmap = BitmapFactory.decodeStream(fis, null, op);
        fis.close();
        return bitmap;
}                                                          
    /** ��ͼƬ�����ļ����� **/
    public void saveBitmap(Bitmap bm, String url,String type) {
        if (bm == null) {
            return;
        }
        //�ж�sdcard�ϵĿռ�
        if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            //SD�ռ䲻��
            return;
        }
        String filename = convertUrlToFileName(url);
        String dir = getDirectory();
        File dirFile = new File(dir);
        if (!dirFile.exists())
            dirFile.mkdirs();            
        File file = new File(dir +"/" + type+filename);
        
        try {
            file.createNewFile();
            OutputStream outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 60, outStream);
            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {
            Log.w("ImageFileCache", "FileNotFoundException");
        } catch (IOException e) {
            Log.w("ImageFileCache", "IOException");
        }
    } 
                                                                
    /**
     * ����洢Ŀ¼�µ��ļ���С��
     * ���ļ��ܴ�С���ڹ涨��CACHE_SIZE����sdcardʣ��ռ�С��FREE_SD_SPACE_NEEDED_TO_CACHE�Ĺ涨
     * ��ôɾ��40%���û�б�ʹ�õ��ļ�
     */
    private boolean removeCache(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null) {
            return true;
        }
        if (!android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return false;
        }
                                                            
        int dirSize = 0;
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains(WHOLESALE_CONV)) {
                dirSize += files[i].length();
            }
        }
                                                            
        if (dirSize > CACHE_SIZE * MB || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            int removeFactor = (int) ((0.4 * files.length) + 1);
            Arrays.sort(files, new FileLastModifSort());
            for (int i = 0; i < removeFactor; i++) {
                if (files[i].getName().contains(WHOLESALE_CONV)) {
                    files[i].delete();
                }
            }
        }
                                                            
        if (freeSpaceOnSd() <= CACHE_SIZE) {
            return false;
        }
                                                                    
        return true;
    }
                                                                
    /** �޸��ļ�������޸�ʱ�� **/
    public void updateFileTime(String path) {
        File file = new File(path);
        long newModifiedTime = System.currentTimeMillis();
        file.setLastModified(newModifiedTime);
    }
                                                                
    /** ����sdcard�ϵ�ʣ��ռ� **/
    private int freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB = ((double)stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
        return (int) sdFreeMB;
    } 
                                                                
    /** ��urlת���ļ��� **/
    private String convertUrlToFileName(String url) {
        String[] strs = url.split("/");
        return strs[strs.length - 1] + WHOLESALE_CONV;
    }
                                                                
    /** ��û���Ŀ¼ **/
    private String getDirectory() {
        String dir = SecurityCheckApp.appPath;
        
        return dir;
    }
                                                                
    /** ȡSD��·�� **/
    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);  //�ж�sd���Ƿ����
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();  //��ȡ��Ŀ¼
        }
        if (sdDir != null) {
            return sdDir.toString();
        } else {
            return "";
        }
    } 
                                                            
    /**
     * �����ļ�������޸�ʱ���������
     */
    private class FileLastModifSort implements Comparator<File> {
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }
                                                            
}