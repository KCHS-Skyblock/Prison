package tech.mcprison.prison.spigot.autofeatures;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;

import me.badbones69.crazyenchantments.api.events.BlastUseEvent;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.autofeatures.AutoManager.AutoManagerBlockBreakEventListener;
import tech.mcprison.prison.spigot.autofeatures.events.PrisonEventLManager;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener.BlockBreakPriority;
import tech.mcprison.prison.spigot.game.SpigotHandlerList;

public class AutoManagerCrazyEnchants
	extends AutoManagerFeatures
	implements PrisonEventLManager
{
	
	public AutoManagerCrazyEnchants() {
		super();
	}
	
	@Override
	public void registerEvents( SpigotPrison spigotPrison ) {
		
		new AutoManagerBlastUseEventListener().initialize();
		
	}

	
	
	public class AutoManagerBlastUseEventListener
		extends AutoManager
		implements Listener {
		
		@EventHandler(priority=EventPriority.LOW) 
		public void onCrazyEnchantsBlockExplode(BlastUseEvent e) {
			super.onCrazyEnchantsBlockExplode( e );
		}
		
		public void initialize() {
	    	boolean isEventEnabled = isBoolean( AutoFeatures.isProcessCrazyEnchantsBlockExplodeEvents );
	    	
	    	if ( !isEventEnabled ) {
	    		return;
	    	}
			
			// Check to see if the class BlastUseEvent even exists:
			try {
				Output.get().logInfo( "AutoManager: checking if loaded: CrazyEnchants" );
				
				Class.forName( "me.badbones69.crazyenchantments.api.events.BlastUseEvent", false, 
								this.getClass().getClassLoader() );
				
				Output.get().logInfo( "AutoManager: Trying to register CrazyEnchants" );

				
				String eP = getMessage( AutoFeatures.CrazyEnchantsBlastUseEventPriority );
				BlockBreakPriority eventPriority = BlockBreakPriority.fromString( eP );

				if ( eventPriority != BlockBreakPriority.DISABLED ) {
					
					EventPriority ePriority = EventPriority.valueOf( eventPriority.name().toUpperCase() );           
					
					PluginManager pm = Bukkit.getServer().getPluginManager();

					pm.registerEvent(BlastUseEvent.class, this, ePriority,
							new EventExecutor() {
								public void execute(Listener l, Event e) { 
									((AutoManagerBlastUseEventListener)l)
													.onCrazyEnchantsBlockExplode((BlastUseEvent)e);
								}
							},
							SpigotPrison.getInstance());
					
				}
				
				// The following is paper code:
//				var executor = EventExecutor
//						.create( AutoManagerBlastUseEventListener.class
//								.getDeclaredMethod( "onCrazyEnchantsBlockExplode", BlastUseEvent.class ),
//								BlastUseEvent.class );
//				
//				Bukkit.getServer().getPluginManager()
//					.register( BlastUseEvent.class, this, EventPriority.LOW, executor, SpigotPrison.getInstance() );
			}
			catch ( ClassNotFoundException e ) {
				// CrazyEnchants is not loaded... so ignore.
				Output.get().logInfo( "AutoManager: CrazyEnchants is not loaded" );
			}
			catch ( Exception e ) {
				Output.get().logInfo( "AutoManager: CrazyEnchants failed to load. [%s]", e.getMessage() );
			}
		}
	}
   
	
    @Override
    public void unregisterListeners() {
    	
    	AutoManagerBlastUseEventListener listener = null;
    	for ( RegisteredListener lstnr : BlastUseEvent.getHandlerList().getRegisteredListeners() )
		{
			if ( lstnr.getListener() instanceof AutoManagerBlockBreakEventListener ) {
				listener = (AutoManagerBlastUseEventListener) lstnr.getListener();
				break;
			}
		}

    	if ( listener != null ) {
    		
			HandlerList.unregisterAll( listener );
    	}
    	
    }
	
	@Override
	public void dumpEventListeners() {
    	boolean isEventEnabled = isBoolean( AutoFeatures.isProcessCrazyEnchantsBlockExplodeEvents );
    	
    	if ( !isEventEnabled ) {
    		return;
    	}
		
		// Check to see if the class BlastUseEvent even exists:
		try {
			
			Class.forName( "me.badbones69.crazyenchantments.api.events.BlastUseEvent", false, 
							this.getClass().getClassLoader() );
			

			ChatDisplay eventDisplay = Prison.get().getPlatform().dumpEventListenersChatDisplay( 
					"BlastUseEvent", 
					new SpigotHandlerList( BlastUseEvent.getHandlerList()) );

			if ( eventDisplay != null ) {
				Output.get().logInfo( "" );
				eventDisplay.toLog( LogLevel.DEBUG );
			}
		}
		catch ( ClassNotFoundException e ) {
			// CrazyEnchants is not loaded... so ignore.
		}
		catch ( Exception e ) {
			Output.get().logInfo( "AutoManager: CrazyEnchants failed to load. [%s]", e.getMessage() );
		}
	}
    
}
