package com.commons.hibernate

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.apache.commons.lang.ObjectUtils
import org.hibernate.HibernateException
import org.hibernate.engine.spi.SessionImplementor
import org.hibernate.usertype.UserType

import java.lang.reflect.Type
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types

/**
 * Created by lorispy on 15/8/19.
 */
class JsonListType implements UserType {


    public static int SQLTYPE = 11900001;

    private final Type userType = List.class;

    private final Gson gson = new GsonBuilder().create();

    @Override
    public int[] sqlTypes() {
        int[] sqlTypes = new int[1]
        sqlTypes[0] = SQLTYPE
        return sqlTypes;
    }


    @Override
    public Class<?> returnedClass() {
        return userType.getClass();
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return ObjectUtils.equals(x, y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x == null ? 0 : x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        String jsonString = rs.getString(names[0]);
        if(!jsonString){
            return null
        }
        return gson.fromJson(jsonString, userType);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (!value) {
            st.setNull(index, Types.OTHER);
            return
        }
        st.setObject(index, gson.toJson(value, userType), Types.OTHER);

    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        if (value == null) {
            return null;
        }

        List l = (List) value;
        return new ArrayList(l);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return gson.toJson(value, userType);
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return gson.fromJson((String) cached, userType);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}
