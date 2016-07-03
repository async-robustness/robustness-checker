package com.vlille.checker.db;

import android.content.Context;

import com.vlille.checker.model.Station;

import rjava.util.ArrayList;
import java.util.List;

public class StationEntityManager /*extends EntityManager<Station>*/ {

    private static final String TAG = StationEntityManager.class.getSimpleName();
    List<Station> stations;

    private static StationEntityManager instance;

    public static StationEntityManager getInstance(Context c) {
        if(instance == null) {
            instance = new StationEntityManager(c);
        }

        return instance;
    }

    private StationEntityManager(Context ctx) {
        //super(Station.class, ctx);
        stations = new ArrayList<Station>();
        stations.add(new Station());
        stations.add(new Station());
        //stations.add(new Station());
        //stations.add(new Station());
        //stations.add(new Station());
        //stations.add(new Station());
    }

    public int count() {
        return stations.size();
    }

    public List<Station> findAll() {
        //return readAll(select().orderBy(Station.NAME, true));
        return stations;
    }

    public List<Station> findAllStarred() {
        //return readAll(select().where(Station.STARRED, Is.EQUAL, true).orderBy(Station.NAME, true));
        List<Station> starred = new ArrayList<Station>();
        for(Station s: stations) {
            if(s.isStarred()) {
                //System.out.println("starred");
                starred.add(s);
            }
        }
        return starred;
    }

    //@Override
    public boolean create(Station item) {
        /*createForeignKeys(item);
        ContentValues cv = toContentValues(item);

        long id = 0;
        try {
            id = getDB().insertOrThrow(getTableName(), null, cv);
        } catch (SQLException e) {
            Log.d(TAG, "Error during insert", e);
        }
        if (id > 0) {
            item.id = id;
            return true;
        } else {
            return false;
        }*/
        return false;
    }

    //@Override
    public boolean create(List items) {
        /*createForeignKeys(item);
        ContentValues cv = toContentValues(item);

        long id = 0;
        try {
            id = getDB().insertOrThrow(getTableName(), null, cv);
        } catch (SQLException e) {
            Log.d(TAG, "Error during insert", e);
        }
        if (id > 0) {
            item.id = id;
            return true;
        } else {
            return false;
        }*/
        return false;
    }

    public void update(Object o) {

    }

}
