package net.weasel.Zap;


import org.bukkit.Server;

public class TimerTask
  implements Runnable
{
  public static Zap plugin;
  public static Server server;

  public TimerTask(Zap instance)
  {
    plugin = instance;
    server = plugin.getServer();
  }

  public void run()
  {
	  Zap.processBlock();
  }
}