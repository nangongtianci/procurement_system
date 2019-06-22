package com.msb.common.utils.base;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import java.lang.reflect.Field;
import java.util.Objects;

public class Qp2EwUtils  {
    private Qp2EwUtils(){}
    private static final String FIELD_STRING = "java.lang.String";

    public static <T> EntityWrapper<T> qp2ew(Object obj){
        Class clz = obj.getClass();
        Field[] fields = clz.getDeclaredFields(); // 获取所有属性,不包含继承过来的属性
        Field field;
        EntityWrapper<T> ew = new EntityWrapper<>();
        for(int i = 0;i<fields.length;i++){
            try {
                System.out.println("------------------------------------------");
                field = fields[i];
                field.setAccessible(true);

                String name = field.getName();
                Object value = field.get(obj);
                Class type = field.getType();

                if(!Objects.isNull(obj)){
                    switch(type.getName()){
                        case FIELD_STRING:
                            ew.where(field.getName()+"={"+i+"}",value);
                            break;
                        default:
                            break;
                    }
                }

                //System.out.println(name+" field declare class:----"+field.getDeclaringClass().getName());
                System.out.println("------------------------------------------");
                System.out.println(" ");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

