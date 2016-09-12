package model;

public class AnClass implements Comparable<AnClass> {
	private int week = 0;
	private int index = 0;
	private String name = "";
	private String place = "";
	private String id = "";// kch_kxh

	public AnClass(String id, String name, String place, String time) {
		this.setId(id);
		this.setName(name);
		this.setPlace(place);
		this.setTime(time);
	}

	public int getIndex() {
		return index;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	// ////////

	public void setTime(String time) {
		String[] times = time.split("-");
		this.week = Integer.parseInt(times[0]);
		this.index = Integer.parseInt(times[1]);
	}

	@Override
	public int compareTo(AnClass o) {
		if (this.week < o.week)
			return -1;
		else if (this.week == o.week)
			if (this.index < o.index)
				return -1;
			else if (this.index == o.index)
				return 0;
			else
				return 1;
		else
			return 1;
	}

	@Override
	public String toString() {
		String text = "ÐÇÆÚ" + week + "-µÚ" + index + "½Ú\n" + id + "\n" + name
				+ "\n" + place + "\n";
		return text;
	}

}