package me.estyx.BlockReplacerSpout;

/* Bukkit */
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.configuration.file.FileConfiguration;

/* Permissions */
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

/* Internal */
import me.estyx.BlockReplacerSpout.BlockReplacerSpoutBlockListener;
import me.estyx.BlockReplacerSpout.BlockReplacerSpoutPlayerListener;
import me.estyx.BlockReplacerSpout.BlockReplacerSpoutPresets;
import me.estyx.BlockReplacerSpout.BlockReplacerSpoutSignHandler;

public class BlockReplacerSpout extends JavaPlugin
{
	public boolean debug = false;
	public boolean enablePresets;
	public boolean enablePlayerClick;
	public String  signIdentifier;
	public String  presetIdentifier;
	
	private static PermissionHandler Permissions;
	public FileConfiguration pluginConfiguration;
	
	public final BlockReplacerSpoutPlayerListener playerListener = new BlockReplacerSpoutPlayerListener(this);
	public final BlockReplacerSpoutBlockListener  blockListener  = new BlockReplacerSpoutBlockListener(this);
	public final BlockReplacerSpoutPresets        blockPresets   = new BlockReplacerSpoutPresets(this);
	public final BlockReplacerSpoutSignHandler    signHandler    = new BlockReplacerSpoutSignHandler(this);
	
	public enum ReplacerType { TYPE_REGULAR_ON, TYPE_REGULAR_OFF, TYPE_PRESET_ON, TYPE_PRESET_OFF, TYPE_FAILED }
	
	/* On plugin enable */
	public void onEnable()
	{
		if(this.debug)
			System.out.println("BlockReplacerSpout enabled");
		
		this.getConfig().addDefault("config.signIdentifier", "[brs]");
		this.getConfig().addDefault("config.enablePresets", false);
		this.getConfig().addDefault("config.presetIdentifier", "[brspreset]");
		this.getConfig().addDefault("config.enablePlayerClick", false);
		this.getConfig().options().copyDefaults(true);
		
		signIdentifier    = this.getConfig().getString ("config.signIdentifier");
		enablePresets     = this.getConfig().getBoolean("config.enablePresets");
		presetIdentifier  = this.getConfig().getString ("config.presetIdentifier");
		enablePlayerClick = this.getConfig().getBoolean("config.enablePlayerClick");
		this.saveConfig();
		
		if(this.debug)
			System.out.println("[BlockReplacerSpout] Identifier:" + signIdentifier + " PresetIdentifier:" + presetIdentifier + " EnablePresets:" + enablePresets + " EnablePlayerClick:" + enablePlayerClick);
		
		/* Register our events */
		PluginManager pm = getServer().getPluginManager();
		
		/* Register event for changes in sign contents */
		pm.registerEvent(Event.Type.SIGN_CHANGE, blockListener, Priority.Normal, this);
		
		/* Register event for changes in redstone power */
		pm.registerEvent(Event.Type.REDSTONE_CHANGE, blockListener, Priority.High, this);
		
		/* Enables player interaction with BlockReplacerSpout signs by right-clicking if set to true */
		if(enablePlayerClick)
			pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
		
		/* Set up permissions.. */
		setupPermissions();
		
		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
	}

	/* On plugin disable */
	public void onDisable()
	{
		if(this.debug)
			System.out.println("BlockReplacerSpout disabled");
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
	}

	/* Permissions Methods */
	private void setupPermissions()
	{
		Plugin permissions = this.getServer().getPluginManager().getPlugin("Permissions");
		if(Permissions == null)
				if(permissions != null)
						Permissions = ((Permissions)permissions).getHandler();
	}

	/* Check permissions */
	public static boolean hasPermissions(Player player, String node)
	{
	if (Permissions != null)
			return Permissions.has(player, node);
		else
			return player.hasPermission(node);
	}
}
