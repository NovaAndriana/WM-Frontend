package com.warehousemart.wm.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

public class Menu implements Serializable {

    public int id;
    public String name;
    public String image;
    public String created_at;
    public String updated_at;
    public boolean selected;
}
