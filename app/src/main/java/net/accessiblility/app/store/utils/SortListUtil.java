package net.accessiblility.app.store.utils;

import java.text.Collator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

public class SortListUtil implements Comparator<String> {
	HashMap<String, String> nameAndPinyin = new HashMap<String, String>();

	@Override
	public int compare(String ls, String rs) {
		Collator ca = Collator.getInstance(Locale.CHINA);
		int flags = 0;
		String one = null;
		if (!nameAndPinyin.containsKey(ls)) {
			one = Pinyin4j.getStringPinYin(ls).toUpperCase(Locale.CHINA);
			nameAndPinyin.put(ls, one);
		} else {
			one = nameAndPinyin.get(ls);
		}

		String two = null;
		if (!nameAndPinyin.containsKey(rs)) {
			two = Pinyin4j.getStringPinYin(rs).toUpperCase(Locale.CHINA);
			nameAndPinyin.put(rs, two);
		} else {
			two = nameAndPinyin.get(rs);
		}

		if (ca.compare(one, two) < 0) {
			flags = -1;
		} else if (ca.compare(one, two) > 0) {
			flags = 1;
		} else {
			flags = 0;
		}

		return flags;
	}
	
	public HashMap<String, String> getPinyinMap() {
		return nameAndPinyin;
	}
}
