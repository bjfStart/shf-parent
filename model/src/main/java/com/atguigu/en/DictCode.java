package com.atguigu.en;

/**
 * @author feng
 * @create 2022-06-15 11:07
 */
public enum DictCode {
    HOUSETYPE("houseType"),
    FLOOR("floor"),
    BUILDSTRUCTURE("buildstructure"),
    DECORATION("decoration"),
    DIRECTION("direction"),
    HOUSEUSE("houseUse");

    private String message;

    DictCode(String message){
        this.message = message;
    }
    public String getMessage(){
        return message;
    }
}
