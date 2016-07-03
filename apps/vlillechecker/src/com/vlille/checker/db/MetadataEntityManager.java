package com.vlille.checker.db;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.vlille.checker.model.Metadata;

public class MetadataEntityManager /*extends EntityManager<Metadata>*/ {

    private static final String TAG = MetadataEntityManager.class.getSimpleName();

    public MetadataEntityManager(Context ctx) {
        //super(Metadata.class, ctx);
    }

    public Metadata find() {
        //return readFirst(select());
        return null;
    }

    public void changeLastUpdateToNow() {
        Metadata metadata = find();
        if (metadata != null) {
            Log.d(TAG, "Change last update millis");

            metadata.setLastUpdate(System.currentTimeMillis());
            update(metadata);
        }
    }

    /**
     * Overrides the update item the remove the clause whereID.
     * In the legacy code, Metadata doesn't have ID.
     */
    //@Override
    public boolean update(Metadata item) {
        /*ContentValues cv = toContentValues(item);
        cv.remove("_id");

        return update().setValues(cv).execute() > 0;*/
        return false;
    }
}
