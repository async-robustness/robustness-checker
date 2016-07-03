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

import android.text.Spanned;

import com.irccloud.android.data.ServersDataSource;

public class ColorFormatter {
	private static final String[] COLOR_MAP = {
		"FFFFFF", //white
		"000000", //black
		"000080", //navy
		"008000", //green
		"FF0000", //red
		"800000", //maroon
		"800080", //purple
		"FFA500", //orange
		"FFFF00", //yellow
		"00FF00", //lime
		"008080", //teal
		"00FFFF", //cyan
		"0000FF", //blue
		"FF00FF", //magenta
		"808080", //grey
		"C0C0C0", //silver
	};

	public static Spanned html_to_spanned(String msg) {
		return html_to_spanned(msg, false, null);
	}
	
	public static Spanned html_to_spanned(String msg, boolean linkify, final ServersDataSource.Server server) {
		return null;
	}
	
	public static String irc_to_html(String msg) {
		int pos=0;
		boolean bold=false, underline=false, italics=false;
		String fg="", bg="";
		StringBuilder builder = new StringBuilder(msg);
		
		while(pos < builder.length()) {
			if(builder.charAt(pos) == 2) { //Bold
				String html = "";
				if(bold)
					html += "</b>";
				else
					html += "<b>";
				bold = !bold;
				builder.deleteCharAt(pos);
				builder.insert(pos, html);
			} else if(builder.charAt(pos) == 22 || builder.charAt(pos) == 29) { //Italics
				String html = "";
				if(italics)
					html += "</i>";
				else
					html += "<i>";
				italics = !italics;
				builder.deleteCharAt(pos);
				builder.insert(pos, html);
			} else if(builder.charAt(pos) == 31) { //Underline
				String html = "";
				if(underline)
					html += "</u>";
				else
					html += "<u>";
				underline = !underline;
				builder.deleteCharAt(pos);
				builder.insert(pos, html);
			} else if(builder.charAt(pos) == 15) { //Formatting clear
				String html = "";
				if(fg.length() > 0) {
					html += "</font>";
					fg = "";
				}
				if(bg.length() > 0) {
					html += "</_bg" + bg + ">";
					bg = "";
				}
				if(bold) {
					html += "</b>";
					bold = false;
				}
				if(underline) {
					html += "</u>";
					underline = false;
				}
				if(italics) {
					html += "</i>";
					italics = false;
				}
				builder.deleteCharAt(pos);
				if(html.length() > 0)
					builder.insert(pos, html);
			} else if(builder.charAt(pos) == 3 || builder.charAt(pos) == 4) { //Color
				boolean rgb = (builder.charAt(pos) == 4);
				int count = 0;
				String new_fg="", new_bg="";
				builder.deleteCharAt(pos);
				if(pos < builder.length()) {
					while(pos+count < builder.length() && (
							(builder.charAt(pos+count) >= '0' && builder.charAt(pos+count) <= '9') ||
							rgb && ((builder.charAt(pos+count) >= 'a' && builder.charAt(pos+count) <= 'f') ||
							(builder.charAt(pos+count) >= 'A' && builder.charAt(pos+count) <= 'F')))) {
						if((++count == 2 && !rgb) || count == 6)
							break;
					}
					if(count > 0) {
						if(count < 3 && !rgb) {
							try {
								int col = Integer.parseInt(builder.substring(pos, pos + count));
								if(col > 15) {
									count--;
									col /= 10;
								}
								new_fg = COLOR_MAP[col];
							} catch (NumberFormatException e) {
		    					new_fg = builder.substring(pos, pos + count);
							}
						} else
	    					new_fg = builder.substring(pos, pos + count);
						builder.delete(pos, pos + count);
					}
					if(pos < builder.length() && builder.charAt(pos) == ',') {
						builder.deleteCharAt(pos);
						if(new_fg.length() == 0)
							new_fg = "clear";
						new_bg = "clear";
						count = 0;
						while(pos+count < builder.length() && (
								(builder.charAt(pos+count) >= '0' && builder.charAt(pos+count) <= '9') ||
								rgb && ((builder.charAt(pos+count) >= 'a' && builder.charAt(pos+count) <= 'f') ||
								(builder.charAt(pos+count) >= 'A' && builder.charAt(pos+count) <= 'F')))) {
                            if((++count == 2 && !rgb) || count == 6)
								break;
						}
						if(count > 0) {
							if(count < 3 && !rgb) {
								try {
									int col = Integer.parseInt(builder.substring(pos, pos + count));
									if(col > 15) {
										count--;
										col /= 10;
									}
									new_bg = COLOR_MAP[col];
								} catch (NumberFormatException e) {
			    					new_bg = builder.substring(pos, pos + count);
								}
							} else
		    					new_bg = builder.substring(pos, pos + count);
							builder.delete(pos, pos + count);
						}
					}
					String html = "";
					if(new_fg.length() == 0 && new_bg.length() == 0) {
						new_fg = "clear";
						new_bg = "clear";
					}
					if(new_fg.length() > 0 && !new_fg.equals(fg) && fg.length() > 0) {
						html += "</font>";
					}
					if(new_bg.length() > 0 && !new_bg.equals(bg) && bg.length() > 0) {
						html += "</_bg" + bg + ">";
					}
					if(new_bg.length() > 0) {
						if(!new_bg.equals(bg)) {
							if(new_bg.equals("clear")) {
								bg = "";
							} else {
								bg = "";
								if(new_bg.length() == 6) {
									bg = new_bg;
								} else if(new_bg.length() == 3) {
									bg += new_bg.charAt(0);
									bg += new_bg.charAt(0);
									bg += new_bg.charAt(1);
									bg += new_bg.charAt(1);
									bg += new_bg.charAt(2);
									bg += new_bg.charAt(2);
								} else {
									bg = "#ffffff";
								}
	    						html += "<_bg" + bg + ">";
							}
						}
					}
					if(new_fg.length() > 0) {
						if(!new_fg.equals(fg)) {
							if(new_fg.equals("clear")) {
								fg = "";
							} else {
								fg = "";
								if(new_fg.length() == 6) {
									fg = new_fg;
								} else if(new_fg.length() == 3) {
									fg += new_fg.charAt(0);
									fg += new_fg.charAt(0);
									fg += new_fg.charAt(1);
									fg += new_fg.charAt(1);
									fg += new_fg.charAt(2);
									fg += new_fg.charAt(2);
								} else {
									fg = "#000000";
								}
	    						html += "<font color=\"#" + fg + "\">";
							}
						}
					}
					builder.insert(pos, html);
				}
			}
			pos++;
		}
		if(fg.length() > 0) {
			builder.append("</font>");
		}
		if(bg.length() > 0) {
			builder.append("</_bg" + bg + ">");
		}
		if(bold)
			builder.append("</b>");
		if(underline)
			builder.append("</u>");
		if(italics)
			builder.append("</i>");

		return builder.toString();
	}
}
