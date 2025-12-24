package com.rosy.main.service.impl;

import com.rosy.main.service.ITemplateService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 模板服务实现类
 *
 * @author rosy
 */
@Service
public class TemplateServiceImpl implements ITemplateService {

    // 简单的模板存储，实际项目中可以从数据库或配置文件中读取
    private static final Map<String, String> TEMPLATES = Map.of(
            "payment_success", "您的订单【{orderNo}】已支付成功，商品：{itemName}，感谢您的购买！",
            "shipment", "您的订单【{orderNo}】已发货，商品：{itemName}，请留意快递信息！",
            "receipt", "您的订单【{orderNo}】已签收，商品：{itemName}，祝您购物愉快！"
    );

    @Override
    public String getTemplate(String templateId) {
        return TEMPLATES.getOrDefault(templateId, "");
    }

    @Override
    public String replaceTemplateVariables(String template, Map<String, String> variables) {
        if (template == null || template.isEmpty()) {
            return template;
        }
        // 使用正则表达式替换模板变量
        Pattern pattern = Pattern.compile("\\{([^}]+)}");
        Matcher matcher = pattern.matcher(template);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = variables.getOrDefault(key, "");
            matcher.appendReplacement(result, value);
        }
        matcher.appendTail(result);
        return result.toString();
    }
}