/*
 * Copyright (c) 2013 IRCCloud, Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.irccloud.android;

import java.util.ArrayList;

public class IRCCloudJSONObject {
	Object o;
	int cid = -1;
	int bid = -1;
	long eid = -1;
	String type = null;
	
	public IRCCloudJSONObject() {
		o = new Object();
	}
	
	public IRCCloudJSONObject(String message) {
	}
	
	public IRCCloudJSONObject(Object object) {
	}

	public int cid() {
		return cid;
	}
	
	public int bid() {
		return bid;
	}

	public long eid() {
		return eid;
	}
	
	public String type() {
		if(type == null) {
			type = "undefined";
		}
		return type;
	}

	public boolean highlight() {
		return false;
	}
	
	public boolean has(String name) {
		return false;
	}
	
	public boolean getBoolean(String name) {
		return false;
	}
	
	public int getInt(String name) {
        return 0;
	}
	
	public long getLong(String name) {
        return 0;
	}
	
	public String getString(String name) {
		return "";
	}
	
	public Object getJsonObject(String name) {
		return this;
	}
	
	public ArrayList<Object> getJsonArray(String name) {
		return new ArrayList<Object>();
	}
	
	public Object getObject() {
		return o;
	}
	
	public String toString() {
		return o.toString();
	}
}
