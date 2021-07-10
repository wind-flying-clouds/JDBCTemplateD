package com.forever.jdbc;

import com.forever.handler.Handler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * created by: Zhaoliting
 * description: 自定义JDBCTemplate
 * created time: 2021/7/10 21:05
 */

public class JDBCTemplate {

	private DataSource dataSource;

	public JDBCTemplate() {
	}

	public JDBCTemplate(DataSource dataSource) {
		setDataSource(dataSource);
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 执行增删改的操作
	 *
	 * @param sql     sql语句
	 * @param objects 参数
	 * @return 数据库操作成功的条数
	 */
	public int update(String sql, Object... objects) {
		//验证数据源
		if (dataSource == null) {
			throw new NullPointerException("dataSource should not be null");
		}
		//2.定义jdbc操作的相关对象
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			//获取数据库链接
			connection = dataSource.getConnection();
			//获取预处理对象
			preparedStatement = connection.prepareStatement(sql);
			//获取参数的元信息
			ParameterMetaData metaData = preparedStatement.getParameterMetaData();
			//获取sql中参数的个数
			int parameterCount = metaData.getParameterCount();
			//判断sql语句中是否有参数
			if (parameterCount > 0) {
				if (objects == null) {
					throw new IllegalArgumentException("params should not be null");
				}
				//判断个数是否匹配
				if (parameterCount != objects.length) {
					throw new IllegalArgumentException("Incorrect parameter size: expected: " + parameterCount + ", but found " + objects.length);
				}
				//参数校验通过，给占位符赋值
				for (int i = 0; i < parameterCount; i++) {
					preparedStatement.setObject(i + 1, objects[i]);
				}
			}
			//执行sql语句
			return preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException throwables) {
					throwables.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException throwables) {
					throwables.printStackTrace();
				}
			}

		}
	}

	/**
	 * 执行查询操作
	 *
	 * @param sql     sql语句
	 * @param objects 参数
	 * @ResultSetHandler handler  处理结果集的很封装，此处只是提供一个接口，由使用者编写具体的实现
	 * @return 数据库操作成功的条数
	 */
	public Object query(String sql, Handler handler, Object... objects) {
		//验证数据源
		if (dataSource == null) {
			throw new NullPointerException("dataSource should not be null");
		}
		//2.定义jdbc操作的相关对象
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			//获取数据库链接
			connection = dataSource.getConnection();
			//获取预处理对象
			preparedStatement = connection.prepareStatement(sql);
			//获取参数的元信息
			ParameterMetaData metaData = preparedStatement.getParameterMetaData();
			//获取sql中参数的个数
			int parameterCount = metaData.getParameterCount();
			//判断sql语句中是否有参数
			if (parameterCount > 0) {
				if (objects == null) {
					throw new IllegalArgumentException("params should not be null");
				}
				//判断个数是否匹配
				if (parameterCount != objects.length) {
					throw new IllegalArgumentException("Incorrect parameter size: expected: " + parameterCount + ", but found " + objects.length);
				}
				//参数校验通过，给占位符赋值
				for (int i = 0; i < parameterCount; i++) {
					preparedStatement.setObject(i + 1, objects[i]);
				}
			}
			//执行sql语句
			resultSet = preparedStatement.executeQuery();
			return handler.handlerResultSet(resultSet);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException throwables) {
					throwables.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException throwables) {
					throwables.printStackTrace();
				}
			}

		}
	}


}
