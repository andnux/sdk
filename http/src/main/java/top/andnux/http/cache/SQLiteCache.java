package top.andnux.http.cache;

import java.util.List;

import top.andnux.http.utils.Utils;
import top.andnux.utils.sqlite.SQLiteDao;
import top.andnux.utils.sqlite.SQLiteDaoFactory;

public class SQLiteCache implements Cache {

    private SQLiteDao<CacheEntity> dao = SQLiteDaoFactory.getDao(CacheEntity.class);

    @Override
    public void put(String url, String value, long time) {
        CacheEntity entity = new CacheEntity();
        entity.setTime(System.currentTimeMillis());
        entity.setDuration(time);
        entity.setData(Utils.encode(value));
        entity.setUrl(Utils.md5(url));
        CacheEntity where = new CacheEntity();
        where.setUrl(Utils.md5(url));
        dao.delete(where);
        dao.insert(entity);
    }

    @Override
    public String get(String url) {
        CacheEntity where = new CacheEntity();
        where.setUrl(Utils.md5(url));
        List<CacheEntity> query = dao.query(where);
        if (query.size() > 0) {
            CacheEntity entity = query.get(0);
            if (entity.getDuration() < 0){
                return Utils.decode(entity.getData());
            }else {
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
        CacheEntity where = new CacheEntity();
        where.setUrl(Utils.md5(url));
        dao.delete(where);
    }

    @Override
    public void clear() {
        CacheEntity where = new CacheEntity();
        dao.delete(where);
    }
}
