package net.weasel.Zap;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Zap extends JavaPlugin
{
  public static ArrayList<Location> blocks = null;
  public static Location tempBlock = null;
  public static Integer tTask = 0;
  public static BukkitScheduler timer = null;
  public static Integer typeId = 0;
  public static Integer dataId = 0;
  public static Player initiator = null;
  public static boolean isResetting = false;
  public static Integer targetType = 0;
  public static boolean zapSurfaceOnly = false;
  public static Integer cycles = 0;
  
  public void onDisable()
  {
    System.out.println("[" + getDescription().getName() + "] " + 
      getDescription().getName() + " v" + getDescription().getVersion() + " disabled.");
  }

  public void onEnable()
  {
	  blocks = new ArrayList<Location>();
	  timer = getServer().getScheduler();
	  tTask = timer.scheduleSyncRepeatingTask(this, new TimerTask(this), 1, 1 );
  }

  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    String pCommand = command.getName().toLowerCase();

    if ((sender instanceof Player))
    {
    	if( pCommand.equals( "zrl" ) )
    	{
    		cycles = 0;
  		    blocks = null;
  		    blocks = new ArrayList<Location>();
  		    return true;
    	}
    	
    	if( pCommand.equals( "zstat" ) )
    	{
    		sender.sendMessage( "Size: " + blocks.size() );
    		return true;
    	}
    	
	    if (pCommand.equals("zap") || pCommand.equals( "zapsurface" ) )
	    {
	        if (args.length == 1 || args.length == 2 )
	        {
	          int tArg = Integer.parseInt(args[0]);
	          Player player = (Player)sender;
	          Block targetBlock = player.getTargetBlock(null, 60);
	          
	          targetType = targetBlock.getTypeId();
	          initiator = player;
	          typeId = Integer.valueOf(tArg);
	          
	          if( args.length == 2 )
	        	  dataId = Integer.valueOf( args[1] );
	          else
	        	  dataId = 0;
	
	          if (targetBlock.getTypeId() != 0) addBlock(targetBlock.getLocation());
	
	          if( pCommand.equals( "zapsurface" ) ) 
	        	  zapSurfaceOnly = true;
	          else
	        	  zapSurfaceOnly = false;
	          
	          return true;
	        }
	
	        return false;
	    }
    }

    return true;
  }

  public static void addBlock( Location blockLoc )
  {
	  if( blocks.contains( blockLoc ) ) return;
	  
	  blocks.add( blockLoc );
	  blocks.trimToSize();
  }
  
  public static void delBlock(Location blockLoc)
  {
    blocks.remove( blockLoc );

    if (blocks.size() < 1)
    {
      initiator.sendMessage("Done.");
    }
  }
  
  public static void processBlock()
  {
	  try
	  {
		  if( blocks.size() > 0 )
		  {
			  Location targetLoc = (Location)Zap.blocks.get(0);
			  World world = Zap.initiator.getWorld();
			      
			  Zap.getAdjacentBlocks(world.getBlockAt(targetLoc));

			  world.getBlockAt(targetLoc).setTypeId(Zap.typeId.intValue());
			  world.getBlockAt(targetLoc).setData((byte)Zap.dataId.intValue());
			      
			  Zap.delBlock(targetLoc);
		  }
	  }
	  catch( Exception e )
	  {
		  // Do nothing..
	  }
  }
  
  public static boolean checkForAdjacentAir(Block whichBlock)
  {
	  if( zapSurfaceOnly == false ) return true;
	  
	  boolean retVal = false;
	  
	  if( whichBlock.getRelative(BlockFace.UP).getTypeId() == 0 ) retVal = true;
	  if( whichBlock.getRelative(BlockFace.NORTH).getTypeId() == 0 ) retVal = true;
	  if( whichBlock.getRelative(BlockFace.EAST).getTypeId() == 0 ) retVal = true;
	  if( whichBlock.getRelative(BlockFace.SOUTH).getTypeId() == 0 ) retVal = true;
	  if( whichBlock.getRelative(BlockFace.WEST).getTypeId() == 0 ) retVal = true;
	  if( whichBlock.getRelative(BlockFace.DOWN).getTypeId() == 0 ) retVal = true;

	  return retVal;
  }

  public static void getAdjacentBlocks(Block whichBlock)
  {
    Block targetBlock = null;

    targetBlock = whichBlock.getRelative(BlockFace.UP);
    if( targetBlock != null )
    {
	    if ((targetBlock.getTypeId() == targetType) && checkForAdjacentAir(targetBlock) == true )
	    	addBlock(targetBlock.getLocation());
    }

    targetBlock = whichBlock.getRelative(BlockFace.DOWN);
    if( targetBlock != null )
    {
	    if ((targetBlock.getTypeId() == targetType) && checkForAdjacentAir(targetBlock) == true )
	    	addBlock(targetBlock.getLocation());
    }

    targetBlock = whichBlock.getRelative(BlockFace.NORTH);
    if( targetBlock != null )
    {
	    if ((targetBlock.getTypeId() == targetType) && checkForAdjacentAir(targetBlock) == true )
	    	addBlock(targetBlock.getLocation());
    }

    targetBlock = whichBlock.getRelative(BlockFace.EAST);
    if( targetBlock != null )
    {
	    if ((targetBlock.getTypeId() == targetType) && checkForAdjacentAir(targetBlock) == true )
	    	addBlock(targetBlock.getLocation());
    }

    targetBlock = whichBlock.getRelative(BlockFace.SOUTH);
    if( targetBlock != null )
    {
	    if ((targetBlock.getTypeId() == targetType) && checkForAdjacentAir(targetBlock) == true )
	    	addBlock(targetBlock.getLocation());
    }

    targetBlock = whichBlock.getRelative(BlockFace.WEST);
    if( targetBlock != null )
    {
	    if ((targetBlock.getTypeId() == targetType) && checkForAdjacentAir(targetBlock) == true )
	    	addBlock(targetBlock.getLocation());
    }
  }
}