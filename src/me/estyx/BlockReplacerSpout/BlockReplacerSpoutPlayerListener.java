package me.estyx.BlockReplacerSpout;

/* Internal */
import me.estyx.BlockReplacerSpout.BlockReplacerSpout.ReplacerType;

/* Bukkit */
import org.bukkit.block.Sign;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class BlockReplacerSpoutPlayerListener extends PlayerListener
{
    private final BlockReplacerSpout plugin;

    public BlockReplacerSpoutPlayerListener(BlockReplacerSpout instance)
    {
        plugin = instance;
    }

    /* Player interaction event */
    public void onPlayerInteract(PlayerInteractEvent event)
    {
    	/* Check if player interaction is allowed */
    	if(!plugin.enablePlayerClick)
    		return;
    	
    	/* Check if its a sign */
    	if(event.getClickedBlock() == null || !plugin.signHandler.isSign(event.getClickedBlock()))
    		return;
    	
    	/* Does the player have permissions to do this? */
    	if(!plugin.hasPermissions(event.getPlayer(), "blockreplacerspout.use"))
    		return;
    	
    	if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
    	{
    		Sign signObject = (Sign) event.getClickedBlock().getState();
    		ReplacerType result = plugin.signHandler.getReplaceSignType(signObject);
    		
    		switch(result)
    		{
	    		case TYPE_REGULAR_ON:
					plugin.signHandler.powerOff(event.getClickedBlock());
					break;
				
				case TYPE_REGULAR_OFF:
					plugin.signHandler.powerOn(event.getClickedBlock());
					break;
	    		
				case TYPE_PRESET_ON:
	    			if(plugin.enablePresets)
	    				plugin.signHandler.powerOffPreset(event.getClickedBlock());
	    			break;
	    		
				case TYPE_PRESET_OFF:
	    			if(plugin.enablePresets)
	    				plugin.signHandler.powerOnPreset(event.getClickedBlock());
	    			break;
    		}
    		
    	}
    }
}
