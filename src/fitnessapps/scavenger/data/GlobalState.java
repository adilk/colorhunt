package fitnessapps.scavenger.data;

import java.util.List;

public class GlobalState {
	
	private final static long DEFAULT_DURATION = 310000;
	private final static int DEFAULT_TASKS_TO_COMPLETE = 6;
	private final static int DEFAULT_TASK_NUMBER = 1;
	private final static int DEFAULT_TASK_INDEX = 0;
	private final static List<ColorEnum> DEFAULT_LIST_OF_COLORS = null;
	
	public static long taskDurationMili = DEFAULT_DURATION;
	public static int tasksToComplete = DEFAULT_TASKS_TO_COMPLETE;
	public static int currentTask = DEFAULT_TASK_NUMBER;
	public static int currentIdx = DEFAULT_TASK_INDEX;
	public static List<ColorEnum> randColorList = DEFAULT_LIST_OF_COLORS;
}

