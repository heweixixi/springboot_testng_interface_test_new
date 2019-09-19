package com.example.springboot_quartz.utils;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.alibaba.fastjson.serializer.SerialContext;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by ${user} on 2019/8/1
 */
public class LevelPropertyPreFilter implements PropertyPreFilter {


    private final Class<?> clazz;
    private final Set<String> includes = new HashSet<String>();
    private final Set<String> excludes = new HashSet<String>();
    private int maxLevel = 0;

    public LevelPropertyPreFilter(String... properties) {
        this(null, properties);
    }

    public LevelPropertyPreFilter(Class<?> clazz, String... properties) {
        super();
        this.clazz = clazz;
        for (String item : properties) {
            if (item != null) {
                this.includes.add(item);
            }
        }
    }

    public LevelPropertyPreFilter addExcludes(String... filters) {
        for (int i = 0; i < filters.length; i++) {
            this.getExcludes().add(filters[i]);
        }
        return this;
    }

    public LevelPropertyPreFilter addIncludes(String... filters) {
        for (int i = 0; i < filters.length; i++) {
            this.getIncludes().add(filters[i]);
        }
        return this;
    }

    public boolean apply(JSONSerializer serializer, Object source, String name) {
        if (source == null) {
            return true;
        }

        if (clazz != null && !clazz.isInstance(source)) {
            return true;
        }

        // 过滤带层级属性（store.book.price）
        SerialContext serialContext = serializer.getContext();
        String levelName = serialContext.toString();
        levelName = levelName + "." + name;
        if (levelName.startsWith("$.")){
            levelName = levelName.replace("$.", "");
        }else if (levelName.startsWith("$[")){
            Matcher matcher = Pattern.compile("\\$\\[\\d+\\].").matcher(levelName);
            levelName = matcher.replaceAll("");
//            levelName = levelName.replace("\\$\\[\\d+\\].","");
        }
        levelName = levelName.replaceAll("\\[\\d+\\]", "");
        if (this.excludes.contains(levelName)) {
            return false;
        }

        if (maxLevel > 0) {
            int level = 0;
            SerialContext context = serializer.getContext();
            while (context != null) {
                level++;
                if (level > maxLevel) {
                    return false;
                }
                context = context.parent;
            }
        }

        if (includes.size() == 0 || includes.contains(levelName)) {
            return true;
        }

        return false;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Set<String> getIncludes() {
        return includes;
    }

    public Set<String> getExcludes() {
        return excludes;
    }

    public static void main(String[] args) {

        String levelName = "$[0].name";
        levelName = levelName.replace("\\$\\[\\d+\\].","");
        Pattern compile = Pattern.compile("\\$\\[\\d+\\].");
        Matcher matcher = compile.matcher(levelName);
        String s = matcher.replaceAll("");



        System.out.println(s);
    }
}
