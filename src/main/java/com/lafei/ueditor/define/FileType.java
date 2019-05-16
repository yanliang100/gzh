package com.lafei.ueditor.define;

import java.util.HashMap;
import java.util.Map;

public class FileType {

    private static final String JPG = "JPG";

    private static final Map<String, String> TYPES = new HashMap<String, String>() {{
        put(FileType.JPG, ".jpg");
    }};

    public static String getSuffix(String key) {
        return FileType.TYPES.get(key);
    }

    /**
     * 根据给定的文件名,获取其后缀信息
     */
    public static String getSuffixByFilename(String filename) {
        return filename.substring(filename.lastIndexOf(".")).toLowerCase();
    }

}
