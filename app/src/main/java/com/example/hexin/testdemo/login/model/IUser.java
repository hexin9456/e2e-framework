package com.example.hexin.testdemo.login.model;

/**
 * Created by estel on 2015/10/12.
 */
public interface IUser {
	String getName();

	String getPasswd();

	int checkUserValidity(String name, String passwd);
}
