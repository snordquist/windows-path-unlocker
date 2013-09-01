package eu.sn7.unlocker.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListUtil<T> {
	public static <T> List<T> emptyList() {
		return new ArrayList<T>();
	}

	public static <T> List<T> create(T... elements) {
		return Arrays.asList(elements);
	}

	public static <T> ArrayList<T> createListOfSize(int numberOfElements) {
		return new ArrayList<T>(numberOfElements);
	}

}
