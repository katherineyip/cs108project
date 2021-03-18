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
			if (trigger.contains("on drop")) {
				trigger += " " + actionArr[2];
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
	
	static public String combineScriptStrings(String s1, String s2) {
		String result = "";
		if (s2 == null || s2 == "") {
			return s1;
		}
		
		String[] clauses1 = s1.split(";");
		String[] clauses2 = s2.split(";");
		String[] parseClause2 = clauses2[0].trim().split(" ", 0);
		String trigger2 = parseClause2[0] + " " + parseClause2[1];
		int start2 = 2;
		if (trigger2.equalsIgnoreCase("on drop")) {
			trigger2 += " " + parseClause2[2];
			start2++;
		}
		
		if (s1 == null || s1 == "") {
			result = clauses2[0];
			String res2 = "";
			for (int i = 1; i < clauses2.length; i++) {
				res2 += clauses2[i] + ";";
			}
			return combineScriptStrings(result, res2);
		}
		
		for (int i = 0; i < clauses1.length; i++) {
			String[] parseClause1 = clauses1[i].trim().split(" ", 0); 
			String trigger1 = parseClause1[0] + " " + parseClause1[1];
			if (trigger1.equalsIgnoreCase("on drop")) {
				trigger1 += " " + parseClause1[2];
			}
			if (trigger2.equals(trigger1)) {
				String[] resulting = new String[parseClause1.length + parseClause2.length - start2];
				for (int j = 0; j < parseClause1.length; j++) {
					resulting[j] = parseClause1[j];
				}
				for (int k = start2; k< parseClause2.length; k++) {
					resulting[parseClause1.length - start2 + k] = parseClause2[k];
				}
				String newRes = "";
				int l = 0;
				while (l < resulting.length) {
					newRes += resulting[l] + " ";
					l++;
				}
				clauses1[i] = newRes;
				for (int m = 0; m < clauses1.length; m++) {
					result += clauses1[m];
					if (m != clauses1.length - 1) {
						result += ";";
					}
				}
				break;
			}
			if (i == clauses1.length - 1) {
				result = s1 + ";" + clauses2[0];
			}
		}
		
		String res2 = "";
		for (int i = 1; i < clauses2.length; i++) {
			res2 += clauses2[i] + ";";
		}
		
		
		return combineScriptStrings(result, res2);
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
