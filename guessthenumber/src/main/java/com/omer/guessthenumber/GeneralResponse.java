package com.omer.guessthenumber;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneralResponse {
	private Object error;
	private Object result;

	public GeneralResponse() {
	}

	public GeneralResponse(Object result) {
		this.result = result;
	}

	public GeneralResponse(Exception e) {
		this.result = null;
		this.error = e;
		
		StackTraceElement[] stackTrace = e.getStackTrace();
		System.out.println("The exception occured in method " + stackTrace[0].getMethodName());
		System.out.println("The class name is " + stackTrace[0].getClassName());
		System.out.println("at line number: " + stackTrace[0].getLineNumber());
		System.out.println(e.toString() + " \n" + "error cause: " + e.getCause() + "\n" + "at: " + formatTime(System.currentTimeMillis()));
	}

	public Object getError() {
		return error;
	}

	public void setError(Object error) {
		this.error = error;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	private String formatTime(Long currentTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
		Date resultdate = new Date(currentTime);
		return (sdf.format(resultdate));
	}
}
