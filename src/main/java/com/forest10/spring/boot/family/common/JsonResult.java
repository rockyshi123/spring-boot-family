package com.forest10.spring.boot.family.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 描述: JSON返回结果
 *
 * @author Forest10
 * @date 2018/04/01 16:32
 */
@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class JsonResult<T> implements Serializable {

	private static final long serialVersionUID = -8963262177114836103L;

	protected T data;

	protected int status;

	protected String msg;

	@JsonCreator
	protected JsonResult(@JsonProperty("status") int status, @JsonProperty("msg") String msg,
			@JsonProperty("data") T data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}


	public static <T> JsonResult<T> success(String message, T data) {
		return new JsonResult<>(0, message, data);
	}

	public static <T> JsonResult<T> success(T data) {
		return new JsonResult<>(0, "success", data);
	}

	public static <T> JsonResult<T> error(String message) {
		return new JsonResult<>(-1, message, null);
	}


}