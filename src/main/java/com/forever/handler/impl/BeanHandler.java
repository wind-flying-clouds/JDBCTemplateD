package com.forever.handler.impl;

import com.forever.handler.Handler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * created by: Zhaoliting
 * description: 处理器接口内置实现
 * created time: 2021/7/10 21:47
 */

public class BeanHandler<T> implements Handler {

	//定义封装到那个实体的字节码
	private final Class<T> tClass;

	/**
	 * 创建BeanHandler对象的时候，需要提供封装到的实体类的类字节码
	 *
	 * @param tClass 字节码对象
	 */
	public BeanHandler(Class<T> tClass) {
		this.tClass = tClass;
	}

	/**
	 * 处理结果集的具体实现
	 *
	 * @param resultSet 需要处理的结果集
	 * @return
	 */
	@Override
	public Object handlerResultSet(ResultSet resultSet) {
		//定义返回值
		T bean = null;
		//判断是否有结果集
		try {
			if (resultSet.next()) {
				//实例化返回对象
				bean = tClass.newInstance();
				//获取结果集元信息
				ResultSetMetaData metaData = resultSet.getMetaData();
				//获取结果集的列数
				int columnCount = metaData.getColumnCount();
				//遍历列数
				for (int i = 0; i < columnCount; i++) {
					//获取列的标题
					String columnName = metaData.getColumnName(i + 1);
					//获取对应列的数据内容
					Object object = resultSet.getObject(columnName);
					//返回值对象的属性填充，借助java的内省机制，使用属性填充器填充属性
					PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, tClass);
					Method method = propertyDescriptor.getWriteMethod();
					method.invoke(bean, object);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return bean;
	}
}
