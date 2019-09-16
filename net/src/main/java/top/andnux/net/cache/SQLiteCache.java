package top.andnux.net.cache;

import android.content.Context;

import java.util.List;

import top.andnux.net.utils.Utils;
import top.andnux.sqlite.QueryWhere;
import top.andnux.sqlite.SQLiteDao;
import top.andnux.sqlite.SQLiteManager;

public class SQLiteCache extends AbstractCache {

    private SQLiteDao<CacheEntity> dao;

    public SQLiteCache(Context context) {
        super(context);
        SQLiteManager.init(context.getDatabasePath("cache.db"));
        dao = SQLiteManager.getInstance().getDao(CacheEntity.class);
    }

    @Override
    public void put(String url, String value, long time) {
        CacheEntity entity = new CacheEntity();
        entity.setTime(System.currentTimeMillis());
        entity.setDuration(time);
        entity.setData(Utils.encode(value));
        entity.setUrl(Utils.md5(url));
        QueryWhere where = new QueryWhere(CacheEntity.class);
        where.and("url").equal(Utils.md5(url));
        dao.delete(where);
        dao.insert(entity);
    }

    @Override
    public String get(String url) {
        QueryWhere where = new QueryWhere(CacheEntity.class);
        where.and("url").equal(Utils.md5(url));
        List<CacheEntity> query = dao.query(where);
        if (query.size() > 0) {
            CacheEntity entity = query.get(0);
            if (entity.getDuration() < 0) {
                return Utils.decode(entity.getData());
            } else {
                long l = System.currentTimeMillis() - entity.getTime();
                if (l - entity.getDuration() > 0) {
                    dao.delete(where);
                } else {
                    return Utils.decode(entity.getData());
                }
            }
        }
        return null;
    }

    @Override
    public void remove(String url) {
        QueryWhere where = new QueryWhere(CacheEntity.class);
        where.and("url").equal(Utils.md5(url));
        dao.delete(where);
    }

    @Override
    public void clear() {
        QueryWhere where = new QueryWhere(CacheEntity.class);
        dao.delete(where);
    }
}
