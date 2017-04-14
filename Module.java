package robotGameNew;


public class Module {
	private int durability;
	private double weight;
	private double force;
	private double intelligence;
	private double skill;
	private String moduleName;
	private int degree;
	private int priceOfModule;
	private boolean isInventory;

	public boolean isInventory() {
		return isInventory;
	}

	public void setInventory(boolean isInventory) {
		this.isInventory = isInventory;
	}

	public int getPriceOfModule() {
		return priceOfModule;
	}

	public void setPriceOfModule(int pricesOfModule) {
		this.priceOfModule = pricesOfModule;
	}

	public int getDurability() {
		return durability;
	}

	public void setDurability(int durability) {
		this.durability = durability;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getForce() {
		return force;
	}

	public void setForce(double force) {
		this.force = force;
	}

	public double getIntelligence() {
		return intelligence;
	}

	public void setIntelligence(double intelligence) {
		this.intelligence = intelligence;
	}

	public double getSkill() {
		return skill;
	}

	public void setSkill(double skill) {
		this.skill = skill;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}
	public Module(String mdl, int inputDegree, int inputDrb,boolean isInventory) {
		this.durability = inputDrb;
		this.moduleName = mdl;
		this.degree = inputDegree;
		this.isInventory=isInventory;
		switch (mdl) {
		case "tr":
			this.priceOfModule =150*inputDegree;
				weight = (100 + inputDegree * 10);
				force = (100 + inputDegree * 80);
			
			break;
		case "hd":
			this.priceOfModule = 100*inputDegree;
			weight = (20 + inputDegree * 1);
			intelligence = (100 + inputDegree * 160);
			break;
		case "lg":
			this.priceOfModule = 50*inputDegree;
				weight = (80 + inputDegree * 1);
				force = (100 + inputDegree * 80);
			break;
		case "ar":
			this.priceOfModule = 40*inputDegree;
				weight = (40 + inputDegree * 2);
				skill = (100 + inputDegree * 200);
			break;
		}
	}

}
