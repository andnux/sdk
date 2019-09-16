package top.andnux.sqlite;

import android.util.Log;

import androidx.annotation.NonNull;

/**
 * SELECT FROM WHERE GROUP BY HAVING ORDER BY
 */
public class QueryWhere {

    private StringBuilder sb;
    private String mColumnName;

    public QueryWhere(Class<?> clazz) {
        sb = new StringBuilder("select * from " + Support.getTableName(clazz) + " where 1=1 ");
    }

    public Where and(String columnName) {
        sb.append(" ").append("and").append(" ");
        return new Where(this, columnName, sb);
    }

    public Where or(String columnName) {
        sb.append(" ").append("or").append(" ");
        return new Where(this, columnName, sb);
    }


    public QueryWhere limit(String text) {
        sb.append(" ")
                .append(" limit '")
                .append(text)
                .append("' ");
        return this;
    }

    public QueryWhere orderBy(String columnName, OrderBy orderBy) {
        sb.append(" ")
                .append("order by ")
                .append(columnName)
                .append(" ")
                .append(orderBy.name())
                .append(" ");
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        Log.e("TAG", "QueryWhere = " + sb.toString());
        return sb.toString();
    }
}
