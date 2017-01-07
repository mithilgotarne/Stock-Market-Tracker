package com.example.mithildarshan.team_91.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 7/1/17.
 */

public class Company extends RealmObject {
    @PrimaryKey
    private String name;

    private RealmList<Record> records;

    private Record lastRecord;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<Record> getRecords() {
        return records;
    }

    public void setRecords(RealmList<Record> records) {
        this.records = records;
    }

    public Record getLastRecord() {
        return lastRecord;
    }

    public void setLastRecord(Record lastRecord) {
        this.lastRecord = lastRecord;
    }
}
