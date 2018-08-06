package marineboy308.mod.util.handlers;

public class EnergyHandler {
	
	public static int indices(int value, int by) {
		int newvalue = 1;
		for (int i = 0; i < by; i++) {
			newvalue = newvalue * value;
		}
		return newvalue;
	}
	
	public static String simplifyEnergyForDisplay(int energy) {
		if (energy < indices(10,3)) {
			return Integer.toString(energy) + "RE";
		} else if (energy < indices(10,4)) {
			return Double.toString((double)energy / (double)indices(10,3)) + "KRE";
		} else if (energy < indices(10,5)) {
			return Double.toString((double)energy / (double)indices(10,3)) + "KRE";
		} else if (energy < indices(10,6)) {
			return Double.toString((double)energy / (double)indices(10,3)) + "KRE";
		} else if (energy < indices(10,7)) {
			return Double.toString((double)energy / (double)indices(10,6)) + "MRE";
		} else if (energy < indices(10,8)) {
			return Double.toString((double)energy / (double)indices(10,6)) + "MRE";
		} else if (energy < indices(10,9)) {
			return Double.toString((double)energy / (double)indices(10,6)) + "MRE";
		} else if (energy < Integer.MAX_VALUE) {
			return Double.toString((double)energy / (double)indices(10,9)) + "BRE";
		} else {
			return "InfRE";
		}
	}

	public static String getEnergyForDisplay(int energy, int maxEnergy) {
		if (energy == 0 && maxEnergy == 0) return "Empty";
		if (energy > maxEnergy) return "Error";
		return simplifyEnergyForDisplay(energy) + " / " + simplifyEnergyForDisplay(maxEnergy);
	}
}
