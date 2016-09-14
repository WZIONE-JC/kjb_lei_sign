package model;

/** 一节课的信息 */
public class AnClass implements Comparable<AnClass> {
	private int week = 0;// 周次
	private int index = 0;// 节次
	private String name = "";// 课名
	private String place = "";// 地点
	private String teacher = "";// 教师
	private String id = "";// ID

	// id=kch_kxh
	// 隐含time=(week - 1) * 5 + index

	public AnClass(String id, String name, String place, String time) {
		this.setId(id);
		this.setName(name);
		this.setPlace(place);
		this.setTime(time);
	}

	public AnClass(String id, String name, String place, int time,
			String teacher) {
		this.setId(id);
		this.setName(name);
		this.setPlace(place);
		this.setTime(time);
		this.setTeacher(teacher);
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

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	// MINE

	public int getTime() {
		return (week - 1) * 5 + index;
	}

	public void setTime(String time) {// 给原生课表使用
		String[] times = time.split("-");
		this.week = Integer.parseInt(times[0]);
		this.index = Integer.parseInt(times[1]);
	}

	public void setTime(int time) {// 给数据库数据使用
		this.week = time / 5 + 1;
		this.index = time % 5;
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
		String text = "星期" + week + "-第" + index + "节\n" + id + "\n" + name
				+ "\n" + place + "\n";
		return text;
	}

}