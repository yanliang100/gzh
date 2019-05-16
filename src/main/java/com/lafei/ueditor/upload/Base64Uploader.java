package com.lafei.ueditor.upload;

import com.lafei.ueditor.PathFormat;
import com.lafei.ueditor.define.AppInfo;
import com.lafei.ueditor.define.BaseState;
import com.lafei.ueditor.define.FileType;
import com.lafei.ueditor.define.State;
import com.lafei.ueditor.spring.EditorController;
import org.apache.tomcat.util.codec.binary.Base64;

import java.util.Map;

public class Base64Uploader {

    public static State save(String content, Map<String, Object> conf) {
        byte[] data = decode(content);
        long maxSize = (Long) conf.get("maxSize");
        if (!validSize(data, maxSize)) {
            return new BaseState(false, AppInfo.MAX_SIZE);
        }
        String suffix = FileType.getSuffix("JPG");
        String savePath = PathFormat.parse((String) conf.get("savePath"), (String) conf.get("filename"));
        savePath = savePath + suffix;
        String physicalPath = PathFormat.format(EditorController.properties.getPhysicalPath() + "/" + savePath);
        State storageState = StorageManager.saveBinaryFile(data, physicalPath);
        if (storageState.isSuccess()) {
            storageState.putInfo("url", PathFormat.format(conf.get("contextPath") + "/" + EditorController.properties.getUrlPrefix() + savePath));
            storageState.putInfo("type", suffix);
            storageState.putInfo("original", "");
        }
        return storageState;
    }

    private static byte[] decode(String content) {
        return Base64.decodeBase64(content);
    }

    private static boolean validSize(byte[] data, long length) {
        return data.length <= length;
    }

}
