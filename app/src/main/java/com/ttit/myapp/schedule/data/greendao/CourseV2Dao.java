package com.ttit.myapp.schedule.data.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.ttit.myapp.schedule.data.beanv2.CourseV2;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;
public class CourseV2Dao extends AbstractDao<CourseV2, Long> {

    public static final String TABLENAME = "COURSE_V2";

    public static class Properties {
        public final static Property CouId = new Property(0, Long.class, "couId", true, "_id");
        public final static Property CouName = new Property(1, String.class, "couName", false, "COU_NAME");
        public final static Property CouLocation = new Property(2, String.class, "couLocation", false, "COU_LOCATION");
        public final static Property CouTeacher = new Property(3, String.class, "couTeacher", false, "COU_TEACHER");
        public final static Property CouWeek = new Property(4, Integer.class, "couWeek", false, "COU_WEEK");
        public final static Property CouStartNode = new Property(5, Integer.class, "couStartNode", false, "COU_START_NODE");
        public final static Property CouNodeCount = new Property(6, Integer.class, "couNodeCount", false, "COU_NODE_COUNT");
        public final static Property CouAllWeek = new Property(7, String.class, "couAllWeek", false, "COU_ALL_WEEK");
        public final static Property CouColor = new Property(8, Integer.class, "couColor", false, "COU_COLOR");
        public final static Property CouCgId = new Property(9, Long.class, "couCgId", false, "COU_CG_ID");
        public final static Property CouOnlyId = new Property(10, String.class, "couOnlyId", false, "COU_ONLY_ID");
        public final static Property CouDeleted = new Property(11, Boolean.class, "couDeleted", false, "COU_DELETED");
    }

    private Query<CourseV2> courseGroup_CgItemsQuery;

    public CourseV2Dao(DaoConfig config) {
        super(config);
    }
    
    public CourseV2Dao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COURSE_V2\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: couId
                "\"COU_NAME\" TEXT," + // 1: couName
                "\"COU_LOCATION\" TEXT," + // 2: couLocation
                "\"COU_TEACHER\" TEXT," + // 3: couTeacher
                "\"COU_WEEK\" INTEGER," + // 4: couWeek
                "\"COU_START_NODE\" INTEGER," + // 5: couStartNode
                "\"COU_NODE_COUNT\" INTEGER," + // 6: couNodeCount
                "\"COU_ALL_WEEK\" TEXT," + // 7: couAllWeek
                "\"COU_COLOR\" INTEGER," + // 8: couColor
                "\"COU_CG_ID\" INTEGER," + // 9: couCgId
                "\"COU_ONLY_ID\" TEXT," + // 10: couOnlyId
                "\"COU_DELETED\" INTEGER);"); // 11: couDeleted
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COURSE_V2\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CourseV2 entity) {
        stmt.clearBindings();
 
        Long couId = entity.getCouId();
        if (couId != null) {
            stmt.bindLong(1, couId);
        }
 
        String couName = entity.getCouName();
        if (couName != null) {
            stmt.bindString(2, couName);
        }
 
        String couLocation = entity.getCouLocation();
        if (couLocation != null) {
            stmt.bindString(3, couLocation);
        }
 
        String couTeacher = entity.getCouTeacher();
        if (couTeacher != null) {
            stmt.bindString(4, couTeacher);
        }
 
        Integer couWeek = entity.getCouWeek();
        if (couWeek != null) {
            stmt.bindLong(5, couWeek);
        }
 
        Integer couStartNode = entity.getCouStartNode();
        if (couStartNode != null) {
            stmt.bindLong(6, couStartNode);
        }
 
        Integer couNodeCount = entity.getCouNodeCount();
        if (couNodeCount != null) {
            stmt.bindLong(7, couNodeCount);
        }
 
        String couAllWeek = entity.getCouAllWeek();
        if (couAllWeek != null) {
            stmt.bindString(8, couAllWeek);
        }
 
        Integer couColor = entity.getCouColor();
        if (couColor != null) {
            stmt.bindLong(9, couColor);
        }
 
        Long couCgId = entity.getCouCgId();
        if (couCgId != null) {
            stmt.bindLong(10, couCgId);
        }
 
        String couOnlyId = entity.getCouOnlyId();
        if (couOnlyId != null) {
            stmt.bindString(11, couOnlyId);
        }
 
        Boolean couDeleted = entity.getCouDeleted();
        if (couDeleted != null) {
            stmt.bindLong(12, couDeleted ? 1L: 0L);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CourseV2 entity) {
        stmt.clearBindings();
 
        Long couId = entity.getCouId();
        if (couId != null) {
            stmt.bindLong(1, couId);
        }
 
        String couName = entity.getCouName();
        if (couName != null) {
            stmt.bindString(2, couName);
        }
 
        String couLocation = entity.getCouLocation();
        if (couLocation != null) {
            stmt.bindString(3, couLocation);
        }
 
        String couTeacher = entity.getCouTeacher();
        if (couTeacher != null) {
            stmt.bindString(4, couTeacher);
        }
 
        Integer couWeek = entity.getCouWeek();
        if (couWeek != null) {
            stmt.bindLong(5, couWeek);
        }
 
        Integer couStartNode = entity.getCouStartNode();
        if (couStartNode != null) {
            stmt.bindLong(6, couStartNode);
        }
 
        Integer couNodeCount = entity.getCouNodeCount();
        if (couNodeCount != null) {
            stmt.bindLong(7, couNodeCount);
        }
 
        String couAllWeek = entity.getCouAllWeek();
        if (couAllWeek != null) {
            stmt.bindString(8, couAllWeek);
        }
 
        Integer couColor = entity.getCouColor();
        if (couColor != null) {
            stmt.bindLong(9, couColor);
        }
 
        Long couCgId = entity.getCouCgId();
        if (couCgId != null) {
            stmt.bindLong(10, couCgId);
        }
 
        String couOnlyId = entity.getCouOnlyId();
        if (couOnlyId != null) {
            stmt.bindString(11, couOnlyId);
        }
 
        Boolean couDeleted = entity.getCouDeleted();
        if (couDeleted != null) {
            stmt.bindLong(12, couDeleted ? 1L: 0L);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public CourseV2 readEntity(Cursor cursor, int offset) {
        CourseV2 entity = new CourseV2( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // couId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // couName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // couLocation
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // couTeacher
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // couWeek
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // couStartNode
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // couNodeCount
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // couAllWeek
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8), // couColor
            cursor.isNull(offset + 9) ? null : cursor.getLong(offset + 9), // couCgId
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // couOnlyId
            cursor.isNull(offset + 11) ? null : cursor.getShort(offset + 11) != 0 // couDeleted
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CourseV2 entity, int offset) {
        entity.setCouId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCouName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCouLocation(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCouTeacher(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCouWeek(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setCouStartNode(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setCouNodeCount(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setCouAllWeek(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setCouColor(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
        entity.setCouCgId(cursor.isNull(offset + 9) ? null : cursor.getLong(offset + 9));
        entity.setCouOnlyId(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setCouDeleted(cursor.isNull(offset + 11) ? null : cursor.getShort(offset + 11) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(CourseV2 entity, long rowId) {
        entity.setCouId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(CourseV2 entity) {
        if(entity != null) {
            return entity.getCouId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CourseV2 entity) {
        return entity.getCouId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "cgItems" to-many relationship of CourseGroup. */
    public List<CourseV2> _queryCourseGroup_CgItems(Long couCgId) {
        synchronized (this) {
            if (courseGroup_CgItemsQuery == null) {
                QueryBuilder<CourseV2> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.CouCgId.eq(null));
                courseGroup_CgItemsQuery = queryBuilder.build();
            }
        }
        Query<CourseV2> query = courseGroup_CgItemsQuery.forCurrentThread();
        query.setParameter(0, couCgId);
        return query.list();
    }

}
