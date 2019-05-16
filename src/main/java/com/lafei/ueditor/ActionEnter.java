package com.lafei.ueditor;

import com.lafei.ueditor.define.ActionMap;
import com.lafei.ueditor.define.AppInfo;
import com.lafei.ueditor.define.BaseState;
import com.lafei.ueditor.define.State;
import com.lafei.ueditor.hunter.ImageHunter;
import com.lafei.ueditor.spring.EditorController;
import com.lafei.ueditor.spring.EditorUploader;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ActionEnter {

    private HttpServletRequest request = null;

    private String actionType = null;

    private ConfigManager configManager = null;

    public ActionEnter(HttpServletRequest request, String configFile) {
        this.request = request;
        this.actionType = request.getParameter("action");
        this.configManager = ConfigManager.getInstance(configFile);
    }

    public String exec() {
        String callbackName = this.request.getParameter("callback");
        if (callbackName != null) {
            if (!validCallbackName(callbackName)) {
                return new BaseState(false, AppInfo.ILLEGAL).toJSONString();
            }
            return callbackName + "(" + this.invoke() + ");";
        } else {
            return this.invoke();
        }
    }

    private String invoke() {
        if (actionType == null || !ActionMap.MAPPING.containsKey(actionType)) {
            return new BaseState(false, AppInfo.INVALID_ACTION).toJSONString();
        }
        if (this.configManager == null || !this.configManager.valid()) {
            return new BaseState(false, AppInfo.CONFIG_ERROR).toJSONString();
        }
        State state = null;
        int actionCode = ActionMap.getType(this.actionType);
        Map<String, Object> conf = null;
        EditorUploader upload = EditorController.editorUploader;
        switch (actionCode) {
            case ActionMap.CONFIG:
                return this.configManager.getAllConfig().toString();
            case ActionMap.UPLOAD_IMAGE:
            case ActionMap.UPLOAD_SCRAWL:
            case ActionMap.UPLOAD_VIDEO:
            case ActionMap.UPLOAD_FILE:
                conf = this.configManager.getConfig(actionCode);
                conf.put("contextPath", request.getContextPath());
                if ("true".equals(conf.get("isBase64"))) {
                    state = upload.base64Upload(request, conf);
                } else {
                    state = upload.binaryUpload(request, conf);
                }
                break;
            case ActionMap.CATCH_IMAGE:
                conf = configManager.getConfig(actionCode);
                conf.put("rootPath", EditorController.properties.getPhysicalPath());
                conf.put("urlPrefix", EditorController.properties.getUrlPrefix());
                conf.put("contextPath", request.getContextPath());
                String[] list = this.request.getParameterValues((String) conf.get("fieldName"));
                state = new ImageHunter(conf).capture(list);
                break;
            case ActionMap.LIST_IMAGE:
                conf = configManager.getConfig(actionCode);
                conf.put("contextPath", request.getContextPath());
                state = upload.listImage(this.getStartIndex(), conf);
                break;
            case ActionMap.LIST_FILE:
                conf = configManager.getConfig(actionCode);
                conf.put("contextPath", request.getContextPath());
                state = upload.listFile(this.getStartIndex(), conf);
                break;
        }
        assert state != null;
        return state.toJSONString();
    }

    private int getStartIndex() {
        String start = this.request.getParameter("start");
        try {
            return Integer.parseInt(start);
        } catch (Exception e) {
            return 0;
        }
    }


    private boolean validCallbackName(String name) {
        return name.matches("^[a-zA-Z_]+[\\w0-9_]*$");
    }

}
