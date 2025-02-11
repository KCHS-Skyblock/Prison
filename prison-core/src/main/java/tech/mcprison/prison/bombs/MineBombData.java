package tech.mcprison.prison.bombs;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.block.PrisonBlock;

public class MineBombData {

	public static final String MINE_BOMB_DEFAULT_ITEM_NAME = "&c-= &7{name}&c =-";
	
	private String name;
	
	private String description;
	
	
//	/**
//	 * <p>The 'bombItemId' is first line of the bomb's item lore, and
//	 * it really needs to be unique and not match any other bomb's id.
//	 * </p>
//	 */
//	private String loreBombItemId;
	private List<String> lore;
	

	/**
	 * <p>The String value that is shown over the placed item when the bomb is 
	 * placed.  Null or empty will show none.  The placeholder '{name}' will show
	 * the bomb's name.
	 * </p>
	 */
	private String nameTag;
	
	
	/**
	 * <p>The itemName is the displayname that is used for the items.
	 * This can be customized without impacting the bomb's name, the bomb's nameTag, 
	 * etc...
	 * </p>
	 * 
	 * <p>If no value is provided, then the name will be used.
	 * </p>
	 * 
	 */
	private String itemName;
	
	
	/**
	 * <p>The String name of an XMaterial item to use as the "bomb".
	 * </p>
	 */
	private String itemType;
	
	/*
	 * <p>A transient reference to the actual XMaterial item that is used
	 * for the bomb.  This is converted from the String itemType.
	 * </p>
	 */
	private transient PrisonBlock item;
	
	/**
	 * <p>The radius identifies how large the blast should be as 
	 * expressed in a radius.  Generally this applies to a sphere or 
	 * a circle or disk, but can also apply to other shapes such as
	 * cubes.
	 * </p>
	 * 
	 * <p>The radius is always based upon a center block, plus the radius.
	 * Therefore a radius of 1 will result in a sphere with a diameter of
	 * three (1 block for the center, and then 1 additional block on each
	 * side).  The calculated diameter can never be even, and the actual 
	 * blast size will appear to be larger than this specified radius due
	 * to the starting inner block.
	 * </p>
	 * 
	 */
	private int radius = 1;
	
	
	/**
	 * <p>Some shapes may require an inner radius, such as a hollow sphere or
	 * a torus.  This is ignored for all other shapes.
	 * </p>
	 */
	private int radiusInner = 0;
	
	
	/**
	 * <p>Some shapes may require a height, such as a cylinder or a cone.
	 * Even if the shape is laying on it's side, the height will still 
	 * apply in other planes and not just up and down.  
	 * This is ignored for all other shapes.
	 * </p>
	 */
	private int height = 0;
	
	
	/**
	 * <p>This adjusts the placement of the bomb along the y axis. By 
	 * default, the y adjustment is -1 to drop the bomb deeper in to the 
	 * mine to pickup more blocks.  The larger a bomb is in radius, then 
	 * the deeper you may want to make this value to maximize the number 
	 * of blocks that are included for the player.
	 * </p>
	 * 
	 * <p>You can easily move the bomb to be sub-surface where it does not break
	 * any top layered blocks.  That may not be so clear to the player that
	 * the bomb went off.  It's also a risk that the bomb could be moved outside
	 * of the mine too.
	 * </p>
	 */
	private int placementAdjustmentY = -1;
	
	/**
	 * <p>The chance of complete removal.  So if the explosion includes
	 * 100 blocks, but the chance is only 50%, each block will be given
	 * a 50% chance to be included from the explosion block list.
	 * </p>
	 */
	private double removalChance = 100.0d; // 100.0 % chance
	
	
	private String explosionShape;
	
	
	private String toolInHandName;
	
	private int toolInHandFortuneLevel = 0;
	
	private int toolInHandDurabilityLevel = 0;

	private int toolInHandDigSpeedLevel = 0;
	
	
	private int fuseDelayTicks = 5 * 20; // 5 seconds
	
	private int cooldownTicks = 5 * 20; // 5 seconds

	
	/**
	 * <p>This is the number of ticks after the explosion occurs to when
	 * the armor stand is removed.  This is preferred since bukkit is 
	 * managing the removal of the armor stand, which will help prevent it
	 * from not being removed, which will create a zombie.
	 * </p>
	 * 
	 * <p>This value will be added to fuseDelayTicks to setup the removal
	 * time that will be used when placing the armor stand.
	 * </p>
	 */
	private int itemRemovalDelayTicks = 5; // 0.25 seconds
	
	
	/**
	 * <p>On spigot versions that support it, the bomb, when placed, will glow.
	 * Was introduced with Minecraft 1.9.  Applies only to Entities, of which
	 * dropped items may be considered an entity?
	 * </p>
	 */
	private boolean glowing = false;
	
	
	/**
	 * <p>Identifies if the item is effected by gravity.
	 * Gravity was introduced with Minecraft 1.9.
	 * </p>
	 */
	private boolean gravity = true;
	
	
	/**
	 * <p>This autosell will force the results of an explosion to autosell even when autosell
	 * is disabled on the server's auto features.  This is almost a requirement for large 
	 * explosions, especially if an unsafe fortune enchantment is being used.
	 * </p>
	 */
	private boolean autosell = false;
	
	
	/**
	 * <p>Internal just to indicated if a mine bomb is activated or not.
	 * This has not purpose if used in a save file.
	 * </p>
	 */
	private transient boolean activated = false;
	
	

	private TreeSet<MineBombEffectsData> soundEffects;
	
	private TreeSet<MineBombEffectsData> visualEffects;
	
	private Block placedBombBlock;
	
	
	public MineBombData() {
		super();
		
		
		this.soundEffects = new TreeSet<>( new MineBombEffectsData() );
		this.visualEffects = new TreeSet<>( new MineBombEffectsData() );
	}
	
	
	public MineBombData( String name, String itemType, String explosionShape, 
				int radius, String... lores ) {
		this();
		
		this.name = name;
		this.nameTag = "{name}";

		this.itemType = itemType;
		this.itemName = MINE_BOMB_DEFAULT_ITEM_NAME;
		
		
		this.explosionShape = explosionShape;
		this.radius = radius;
		
//		this.loreBombItemId = "PrisonMineBomb: " + name;
		
		this.lore = new ArrayList<>();
		
		if ( lores != null ) {
			for ( String l : lores ) {
				this.lore.add( l );
			}
		}
		
		this.toolInHandName = null;
		this.toolInHandFortuneLevel = 0;
		this.toolInHandDurabilityLevel = 0;
		this.toolInHandDigSpeedLevel = 0;
		
		this.removalChance = 100.0d;
		
		this.fuseDelayTicks = 5 * 20;
		
		this.cooldownTicks = 30 * 20;
		
		this.itemRemovalDelayTicks = 5;
		
		this.glowing = false;
		this.gravity = true;
		
	}
	
	
	public MineBombData clone() {
		MineBombData cloned = new MineBombData( getName(), getItemType(), getExplosionShape(),
				getRadius() );
		
//		cloned.setLoreBombItemId( getLoreBombItemId() );
		
		cloned.setDescription( getDescription() );
		
		cloned.setNameTag( getNameTag() );
		
		cloned.setItemName( getItemName() );
		
		cloned.setToolInHandName( getToolInHandName() );
		cloned.setToolInHandFortuneLevel( getToolInHandFortuneLevel() );
		cloned.setToolInHandDurabilityLevel( getToolInHandDurabilityLevel() );
		cloned.setToolInHandDigSpeedLevel( getToolInHandDigSpeedLevel() );
		
		cloned.setRadiusInner( getRadiusInner() );
		cloned.setHeight( getHeight() );
		
		cloned.setPlacementAdjustmentY( getPlacementAdjustmentY() );
		
		cloned.setRemovalChance( getRemovalChance() );
		cloned.setFuseDelayTicks( getFuseDelayTicks() );
		cloned.setCooldownTicks( getCooldownTicks() );
		
		cloned.setGlowing( isGlowing() );
		cloned.setGravity( isGravity() );
		
		cloned.setItemRemovalDelayTicks( getItemRemovalDelayTicks() );
		
		cloned.setAutosell( isAutosell() );
		cloned.setActivated( isActivated() );
		
		
		for ( String l : getLore() ) {
			cloned.getLore().add( l );
		}
		
		
		for ( MineBombEffectsData soundEffect : getSoundEffects() ) 
		{
			cloned.getSoundEffects().add( soundEffect.clone() );
		}
		
		
		for ( MineBombEffectsData visualEffect : getVisualEffects() ) 
		{
			cloned.getVisualEffects().add( visualEffect.clone() );
		}
		
		
		return cloned;
	}

	public String getName() {
		return name;
	}
	public void setName( String name ) {
		this.name = name;
	}

	public String getNameTag() {
		return nameTag;
	}
	public void setNameTag( String nameTag ) {
		this.nameTag = nameTag;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription( String description ) {
		this.description = description;
	}

//	public String getLoreBombItemId() {
//		return loreBombItemId;
//	}
//	public void setLoreBombItemId( String loreBombItemId ) {
//		this.loreBombItemId = loreBombItemId;
//	}

	public List<String> getLore() {
		return lore;
	}
	public void setLore( List<String> lore ) {
		this.lore = lore;
	}

	public String getItemType() {
		return itemType;
	}
	public void setItemType( String itemType ) {
		this.itemType = itemType;
	}

	public String getItemName() {
		if ( itemName == null ) {
			itemName = MINE_BOMB_DEFAULT_ITEM_NAME;
		}
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public PrisonBlock getItem() {
		return item;
	}
	public void setItem( PrisonBlock item ) {
		this.item = item;
	}

	public int getRadius() {
		return radius;
	}
	public void setRadius( int radius ) {
		this.radius = radius;
	}

	public int getRadiusInner() {
		return radiusInner;
	}
	public void setRadiusInner( int radiusInner ) {
		this.radiusInner = radiusInner;
	}

	public int getHeight() {
		return height;
	}
	public void setHeight( int height ) {
		this.height = height;
	}

	public int getPlacementAdjustmentY() {
		return placementAdjustmentY;
	}
	public void setPlacementAdjustmentY( int placementAdjustmentY ){
		this.placementAdjustmentY = placementAdjustmentY;
	}

	public double getRemovalChance() {
		return removalChance;
	}
	public void setRemovalChance( double removalChance ) {
		this.removalChance = removalChance;
	}

	public String getExplosionShape() {
		return explosionShape;
	}
	public void setExplosionShape( String explosionShape ) {
		this.explosionShape = explosionShape;
	}

	public String getToolInHandName() {
		return toolInHandName;
	}
	public void setToolInHandName( String toolInHandName ) {
		this.toolInHandName = toolInHandName;
	}

	public int getToolInHandFortuneLevel() {
		return toolInHandFortuneLevel;
	}
	public void setToolInHandFortuneLevel( int toolInHandFortuneLevel ) {
		this.toolInHandFortuneLevel = toolInHandFortuneLevel;
	}

	public int getToolInHandDurabilityLevel() {
		return toolInHandDurabilityLevel;
	}
	public void setToolInHandDurabilityLevel( int toolInHandDurabilityLevel ) {
		this.toolInHandDurabilityLevel = toolInHandDurabilityLevel;
	}

	public int getToolInHandDigSpeedLevel() {
		return toolInHandDigSpeedLevel;
	}
	public void setToolInHandDigSpeedLevel( int toolInHandDigSpeedLevel ) {
		this.toolInHandDigSpeedLevel = toolInHandDigSpeedLevel;
	}

	public int getFuseDelayTicks() {
		return fuseDelayTicks;
	}
	public void setFuseDelayTicks( int fuseDelayTicks ) {
		this.fuseDelayTicks = fuseDelayTicks;
	}

	public int getCooldownTicks() {
		return cooldownTicks;
	}
	public void setCooldownTicks( int cooldownTicks ) {
		this.cooldownTicks = cooldownTicks;
	}

	public boolean isGlowing() {
		return glowing;
	}
	public void setGlowing( boolean glowing ) {
		this.glowing = glowing;
	}

	public boolean isGravity() {
		return gravity;
	}
	public void setGravity( boolean gravity ) {
		this.gravity = gravity;
	}

	public int getItemRemovalDelayTicks() {
		return itemRemovalDelayTicks;
	}
	public void setItemRemovalDelayTicks( int itemRemovalDelayTicks ) {
		this.itemRemovalDelayTicks = itemRemovalDelayTicks;
	}

	public boolean isAutosell() {
		return autosell;
	}
	public void setAutosell( boolean autosell ) {
		this.autosell = autosell;
	}

	public boolean isActivated() {
		return activated;
	}
	public void setActivated( boolean activated ) {
		this.activated = activated;
	}

	public TreeSet<MineBombEffectsData> getSoundEffects() {
		return soundEffects;
	}
	public void setSoundEffects( TreeSet<MineBombEffectsData> soundEffects ) {
		this.soundEffects = soundEffects;
	}

	public TreeSet<MineBombEffectsData> getVisualEffects() {
		return visualEffects;
	}
	public void setVisualEffects( TreeSet<MineBombEffectsData> visualEffects ) {
		this.visualEffects = visualEffects;
	}

	public Block getPlacedBombBlock() {
		return placedBombBlock;
	}
	public void setPlacedBombBlock( Block placedBombBlock ) {
		this.placedBombBlock = placedBombBlock;
	}

	
}
