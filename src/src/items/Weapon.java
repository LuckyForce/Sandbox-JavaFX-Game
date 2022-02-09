package items;

public abstract class Weapon extends Item{
	private int damage;
	private int durability;
	private int cooldown;
	private int cooldown_end;
	
	public Weapon(String name, int damage, int durability,  int cooldown_end) {
		super(name);
		setDamage(damage);
		setDurability(durability);
		setCooldown_end(cooldown_end);
	}

	public int getDurability() {
		return durability;
	}

	public void setDurability(int durability) {
		this.durability = durability;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
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
