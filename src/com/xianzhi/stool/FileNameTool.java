package com.xianzhi.stool;

import java.io.FileInputStream;

import org.apache.http.util.EncodingUtils;

import com.xianzhisylj.dynamic.InitActivity;


public class FileNameTool {
	public static String getExtensionName(String filename) {   
        if ((filename != null) && (filename.length() > 0)) {   
            int dot = filename.lastIndexOf('/');   
            if ((dot >-1) && (dot < (filename.length() - 1))) {   
                return filename.substring(dot + 1);   
            }   
        }   
        return filename;   
    }
	static String codetype(byte[] head) {  
		String type = "";  
		byte[] codehead = new byte[3];  
		System.arraycopy(head, 0, codehead, 0, 3);  
		if(codehead[0] == -1 && codehead[1] == -2) {  
			type = "UTF-16";  
		}  
		else if(codehead[0] == -2 && codehead[1] == -1) {  
			type = "UNICODE";  
		}  
		else if(codehead[0] == -17 && codehead[1] == -69 && codehead[2] == -65) {  
			type = "UTF-8";  
		}  
		else {  
			type = "GB2312";  
		}  
		return type;  
	}
	public static String getToken(){
		String txt = "";  
		try {  
			// �ļ�·��  
			String filename = InitActivity.SDCardRoot+InitActivity.RAIL+"/token.txt"; 
//			System.out.println("Ҫ��ȡ��"+filename);
			// �ļ�����ȡ�ļ�  
			FileInputStream fin = new FileInputStream(filename);  
			// ����ַ�����  
			int length = fin.available();  
			// �����ֽ�����  
			byte[] buffer = new byte[length];  
			// ���ֽ�������������  
			fin.read(buffer);  
			// �ر��ļ���  
			fin.close();  
			// ��ñ����ʽ  
			String type = codetype(buffer);  
			// ʹ�ñ����ʽ�������  
			txt = EncodingUtils.getString(buffer, type); 
//			System.out.println("��ȡ����"+txt);
			return txt;
		}  
		catch(Exception e) {  
			// TODO: handle exception  
			e.printStackTrace();
		}  
		return "";

	}
}
