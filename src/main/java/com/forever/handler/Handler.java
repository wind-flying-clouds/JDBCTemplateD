package com.forever.handler;

import java.sql.ResultSet;

/**
 * created by: Zhaoliting
 * description: 结果集处理器接口
 * created time: 2021/7/10 21:43
 */

public interface Handler {

	/**
	 * 处理结果集
	 *
	 * @param resultSet 需要处理的结果集
	 * @return 结果集处理后的返回结果
	 */
	Object handlerResultSet(ResultSet resultSet);

}
