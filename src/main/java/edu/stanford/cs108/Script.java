package edu.stanford.cs108;

import java.util.HashMap;
import java.util.Map;

public class Script {

	//private static Map<String, actionPairs[]> triggerMap;

	/* class to allow for pairs of the action and the target in the script */
	static class actionPairs {
		public String action;
		public String target;
	}

	/* sets a Map of String : actionPair[] in which the actionPair is an action and its target, and the key
	 * of the map is the trigger for the actions
	 * ex. "on click play sound throw bottle; on drop carrot say hello" will generate
	 * <["on click" : [play, sound], [throw, bottle]], ["on drop" : [drop, carrot], [say, hello]]>
	 * */





	public static void setPageScript(Page page) {
		String script = page.pageScript;
		page.scriptMap = new HashMap<String, actionPairs[]>();
		page.scriptMap = setScript(script);
		//showMap(page.scriptMap);
	}

	public static void setShapeScript(Shape shape) {
		String script = shape.shapeScript;
		shape.scriptMap = new HashMap<String, actionPairs[]>();
		shape.scriptMap = setScript(script); //assigns the map to the shape
		//showMap(shape.scriptMap);
	}


	public static Map<String, actionPairs[]> setScript(String script) {

		Map<String, actionPairs[]> triggerMap = new HashMap<String, actionPairs[]>();

		if (script == null || script == "") {
			return triggerMap;
		}

		String[] clauses = script.split(";");

		for (int i = 0; i < clauses.length; i++) {

			String[] actionArr = clauses[i].trim().split(" ", 0);
			String trigger = actionArr[0] + " " + actionArr[1];

			int size = (actionArr.length / 2) - 1;
			if (trigger.equalsIgnoreCase("on drop")) { //takes into account that on drop has its own target
				size++;
			}

			actionPairs[] actionTarget = new actionPairs[size];

			int start = 2;
			if (trigger.equalsIgnoreCase("on drop")) {
				actionPairs dropTarget = new actionPairs(); // adds [drop, target] so that we know the target shape to drop
				dropTarget.action = "drop";
				dropTarget.target = actionArr[2];
				actionTarget[0] = dropTarget;
				start++;
			}

			while (start < actionArr.length - 1) {

				actionPairs action = new actionPairs(); //adds each pair of action and target to the array
				action.action = actionArr[start];
				action.target = actionArr[start + 1];
				actionTarget[(start - 1) / 2] = action;
				start += 2;
			}

			triggerMap.put(trigger, actionTarget); //adds the array to the map with its trigger

		}

		return triggerMap;

	}

	//for debugging
	/*private static void showMap(Map<String, actionPairs[]> scriptMap) { 
		for (String trig : scriptMap.keySet()) {
			System.out.println(trig + ":" + " ");
			for (Script.actionPairs tar : scriptMap.get(trig)) {
				System.out.println("     " + tar.action + ", " + tar.target + ";");

			}
		}
	}*/
}
