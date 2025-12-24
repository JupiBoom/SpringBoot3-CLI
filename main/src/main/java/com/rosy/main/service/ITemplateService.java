package com.rosy.main.service;

import java.util.Map;

/**
 * 模板服务接口
 *
 * @author rosy
 */
public interface ITemplateService {

    /**
     * 获取模板内容
     */
    String getTemplate(String templateId);

    /**
     * 替换模板变量
     */
    String replaceTemplateVariables(String template, Map<String, String> variables);
}