package com.forever.handler.impl;

import com.forever.handler.Handler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * created by: Zhaoliting
 * description: 返回集合结果
 * created time: 2021/7/10 22:08
 */

public class BeanListHandler<T> implements Handler {

	private final Class<T> tClass;

	public BeanListHandler(Class<T> tClass) {
		this.tClass = tClass;
	}

	@Override
	public Object handlerResultSet(ResultSet resultSet) {
		if (resultSet == null) {
			throw new RuntimeException("");
		}
		List<Object> list = new ArrayList<>();
		T bean;
		try {
			while (resultSet.next()) {
				bean = tClass.newInstance();
				ResultSetMetaData metaData = resultSet.getMetaData();
				int columnCount = metaData.getColumnCount();
				for (int i = 0; i < columnCount; i++) {
					String columnName = metaData.getColumnName(i + 1);
					Object object = resultSet.getObject(columnName);
					PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, tClass);
					Method writeMethod = propertyDescriptor.getWriteMethod();
					writeMethod.invoke(bean, object);
				}
				list.add(bean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return list;
	}
}
