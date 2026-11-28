package com.bea.beacommoncore.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import javax.print.DocFlavor;
import java.text.SimpleDateFormat;
/**
 * JSON⼯具类
 */
@Slf4j
public class JsonUtil {
    /**
     * 对象转换器
     */
    private static ObjectMapper OBJECT_MAPPER;
    static {
        OBJECT_MAPPER = JsonMapper.builder().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                        .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                        .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
                        .configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false)
                        .configure(MapperFeature.USE_ANNOTATIONS, false)
                        .addModule(new JavaTimeModule())
                        .defaultDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) //todo 处理魔值
                        .serializationInclusion(JsonInclude.Include.NON_NULL)
                        .build();
    }


    /**
     * 对象转Json格式字符串
     * @param obj 对象
     * @return Json格式字符串
     * @param <T> 对象类型
     */
    public static <T> String obj2String(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj :
                    OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Parse Object to String error : {}", e.getMessage());
            return null;
        }
    }
    /**
     * 对象转Json格式字符串(格式化的Json字符串)
     * @param obj 对象
     * @return 美化的Json格式字符串
     * @param <T> 对象类型
     */
    public static <T> String obj2StringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj :
                    OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Parse Object to String error : {}", e.getMessage());
            return null;
        }
    }

    /**
     * 字符串转换为⾃定义对象
     * @param str 要转换的字符串
     * @param clazz ⾃定义对象的class对象
     * @return ⾃定义对象
     * @param <T> 对象类型
     */
    public static <T> T string2Obj(String str, Class<T> clazz) {
        //todo 提取工具类
        if (str == null || str.length() == 0 || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T)str : OBJECT_MAPPER.readValue(str, clazz);
        } catch (JsonProcessingException e) {
            log.warn("Parse String to Object error : {}", e.getMessage());
            return null;
        }
    }
}
