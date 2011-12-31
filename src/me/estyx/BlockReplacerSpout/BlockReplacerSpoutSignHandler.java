package me.estyx.BlockReplacerSpout;

/* Internal */
import me.estyx.BlockReplacerSpout.BlockReplacerSpout.ReplacerType;

/* Bukkit */
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.Location;

/* Spout functionality */
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.material.CustomBlock;

public class BlockReplacerSpoutSignHandler
{
	private final BlockReplacerSpout plugin;
	
    public BlockReplacerSpoutSignHandler(BlockReplacerSpout instance)
    {
        plugin = instance;
    }
    
    public void powerOnPreset(Block block)
    {
    	String preset;
    	int    length;
		int    position;
    }
    
    public void powerOffPreset(Block block)
    {
    	String preset;
    	int    length;
		int    position;
    }
    
    public void powerOn(Block block)
    {
    	Sign signObject = (Sign) block.getState();
    	
    	int length;
		int position;
		int blockid;
		
		try
		{
						
			/* Line 1: <y-position>[;<length>]*/
			if(signObject.getLine(1).contains(";"))
			{
				String[] str;
				str = signObject.getLine(1).split(";");
				position = Integer.parseInt(str[0]);
				length   = Integer.parseInt(str[1]);
			} else
			{
				position = Integer.parseInt(signObject.getLine(1));
				length   = 1;
			}
			if(length < 1)
				length = 1;
			if(length > 32)
				length = 32;
			
			/* Line 2: <customid-on:customid-off> */
			if(signObject.getLine(2).contains(":"))
			{
				String[] str;
				str = signObject.getLine(2).split(":");
				blockid     = Integer.parseInt(str[0]);
			} else
			{
				blockid     = Integer.parseInt(signObject.getLine(2));
			}
		} catch (NumberFormatException e)
		{
			System.err.println("Caught NumberFormatException: " + e.getMessage());
			return;
		}
		
		Location location = block.getLocation();
		World w = location.getWorld();
		location.setY(location.getY() + position);
		
		if(position >= 0)
		{
			for(int i=position; i<length+position; i++)
			{
				SpoutBlock sb = (SpoutBlock) w.getBlockAt(location);
				sb.setCustomBlock((CustomBlock) org.getspout.spoutapi.material.MaterialData.getCustomBlock(blockid));
				sb.getState().update(true);
				location.setY(location.getY() + 1);
			}
		} else
		{
			for(int i=position; i>position-length; i--)
			{
				SpoutBlock sb = (SpoutBlock) w.getBlockAt(location);
				sb.setCustomBlock((CustomBlock) org.getspout.spoutapi.material.MaterialData.getCustomBlock(blockid));
				sb.getState().update(true);
				location.setY(location.getY() - 1);
			}
		}
		
		signObject.setLine(0, ChatColor.GREEN + plugin.signIdentifier);
		signObject.update(true);
    	return;
    }
    
    public void powerOff(Block block)
    {
    	Sign signObject = (Sign) block.getState();
    	
    	int length;
		int position;
		int blockid;
		
		try
		{
			/* Line 1: <y-position>[;<length>]*/
			if(signObject.getLine(1).contains(";"))
			{
				String[] str;
				str = signObject.getLine(1).split(";");
				position = Integer.parseInt(str[0]);
				length   = Integer.parseInt(str[1]);
			} else
			{
				position = Integer.parseInt(signObject.getLine(1));
				length   = 1;
			}
			if(length < 1)
				length = 1;
			if(length > 32)
				length = 32;
			
			/* Line 2: <custom-on:customid-off> */
			if(signObject.getLine(2).contains(":"))
			{
				String[] str;
				str = signObject.getLine(2).split(":");
				blockid     = Integer.parseInt(str[1]);
			} else
			{
				blockid     = Integer.parseInt(signObject.getLine(2));
			}
		} catch (NumberFormatException e)
		{
			System.err.println("Caught NumberFormatException: " + e.getMessage());
			return;
		}
		
		Location location = block.getLocation();
		World w = location.getWorld();
		location.setY(location.getY() + position);
		
		if(position >= 0)
		{
			for(int i=position; i<length+position; i++)
			{
				SpoutBlock sb = (SpoutBlock) w.getBlockAt(location);
				sb.setCustomBlock((CustomBlock) org.getspout.spoutapi.material.MaterialData.getCustomBlock(blockid));
				sb.getState().update(true);
				location.setY(location.getY() + 1);
			}
		} else
		{
			for(int i=position; i>position-length; i--)
			{
				SpoutBlock sb = (SpoutBlock) w.getBlockAt(location);
				sb.setCustomBlock((CustomBlock) org.getspout.spoutapi.material.MaterialData.getCustomBlock(blockid));
				sb.getState().update(true);
				location.setY(location.getY() - 1);
			}
		}
		
		signObject.setLine(0, ChatColor.RED + plugin.signIdentifier);
		signObject.update(true);
    	return;
    }
    
    public boolean isSign(Block b)
    {
    	if (b.getType() == Material.SIGN || b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN)
    		return true;
    	else
    		return false;
    }
    
    public ReplacerType getReplaceSignType(Sign signObject)
    {
    	if(signObject.getLine(0).equals(ChatColor.GREEN + plugin.signIdentifier))
    		return ReplacerType.TYPE_REGULAR_ON;
    	if(signObject.getLine(0).equals(ChatColor.RED + plugin.signIdentifier))
    		return ReplacerType.TYPE_REGULAR_OFF;
    	
    	if(signObject.getLine(0).equals(ChatColor.GREEN + plugin.presetIdentifier))
    		return ReplacerType.TYPE_PRESET_ON;
    	if(signObject.getLine(0).equals(ChatColor.RED + plugin.presetIdentifier))
    		return ReplacerType.TYPE_PRESET_OFF;
    	
    	return ReplacerType.TYPE_FAILED;
    }
}

