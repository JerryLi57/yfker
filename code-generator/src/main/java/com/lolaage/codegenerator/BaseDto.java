package com.lolaage.codegenerator;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * @description:
 * @author: lijiayu
 * @date: 2020-03-06 17:41
 **/
public class BaseDto implements Serializable {

	private static final long serialVersionUID = 4537093877314249859L;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
	
	

}
