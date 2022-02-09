package abilities;

public abstract class Ability{
	private String name;
	private int cooldown;
	private int cooldown_end;
	
	public Ability(String name, int cooldown) {
		setCooldown_end(cooldown_end);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCooldown() {
		return cooldown;
	}
	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}
	public int getCooldown_end() {
		return cooldown_end;
	}
	public void setCooldown_end(int cooldown_end) {
		this.cooldown_end = cooldown_end;
	}
}
