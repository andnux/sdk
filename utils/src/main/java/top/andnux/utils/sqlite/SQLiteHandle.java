package top.andnux.utils.sqlite;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SQLiteHandle {

    private String whereClause;
    private String[] whereArgs;

    public SQLiteHandle(Object o) {
        StringBuilder buffer = new StringBuilder();
        List<String> list = new ArrayList<>();
        try {
            Field[] fields = o.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object obj = field.get(o);
                if (obj != null) {
                    buffer.append(field.getName()).append("=? ");
                    list.add(obj.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        whereClause = buffer.toString();
        whereArgs = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            whereArgs[i] = list.get(i);
        }
    }

    public String getWhereClause() {
        return whereClause;
    }

    public String[] getWhereArgs() {
        return whereArgs;
    }
}