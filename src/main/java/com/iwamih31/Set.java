package com.iwamih31;

public class Set {
	public String name;
	public int value;

	public Set(String name, int value) {
		super();
		this.name = name;
		this.value = value;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static String[] get_Name_Set(Set[] set_list) {
		String[] name_Set = new String[set_list.length];
		for (int i = 0; i < name_Set.length; i++) {
			name_Set[i] = set_list[i].name;
		}
		return name_Set;
	}

	public static int[] get_Value_Set(Set[] set_list) {
		int[] value_Set = new int[set_list.length];
		for (int i = 0; i < value_Set.length; i++) {
			value_Set[i] = set_list[i].value;
		}
		return value_Set;
	}

}
