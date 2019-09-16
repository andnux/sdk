package top.andnux.sqlite;

public class Where {

    private QueryWhere mWhere;
    private String mColumnName;
    private StringBuilder sb;

    public Where(QueryWhere where, String columnName, StringBuilder sb) {
        mWhere = where;
        mColumnName = columnName;
        this.sb = sb;
    }

    public QueryWhere like(String text) {
        sb.append(" like %'")
                .append(text)
                .append("'%");
        return mWhere;
    }




    public QueryWhere glob(String text) {
        sb.append(" ")
                .append(mColumnName)
                .append(" glob '")
                .append(text)
                .append("' ");
        return mWhere;
    }



    public QueryWhere greater(String text) {
        sb.append(" ")
                .append(mColumnName)
                .append(" > '")
                .append(text)
                .append("' ");
        return mWhere;
    }

    public QueryWhere greaterEqual(String text) {
        sb.append(" ")
                .append(mColumnName)
                .append(" >= '")
                .append(text)
                .append("' ");
        return mWhere;
    }

    public QueryWhere less(String text) {
        sb.append(" ")
                .append(mColumnName)
                .append(" < '")
                .append(text)
                .append("' ");
        return mWhere;
    }

    public QueryWhere lessEqual(String text) {
        sb.append(" ")
                .append(mColumnName)
                .append(" <= '")
                .append(text)
                .append("' ");
        return mWhere;
    }

    public QueryWhere equal(String text) {
        sb.append(" ")
                .append(mColumnName)
                .append(" = '")
                .append(text)
                .append("' ");
        return mWhere;
    }
}
