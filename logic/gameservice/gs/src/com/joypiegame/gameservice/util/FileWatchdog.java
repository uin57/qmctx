package com.joypiegame.gameservice.util;

import java.io.File;

public class FileWatchdog implements Runnable
{ 
	  protected String filename;
	  private Runnable doOnChange;
	  private Runnable doOnNotExist;
	  File file;
	  long lastModif = 0; 

	  public FileWatchdog()
	  {
		  
	  }
	  
	  public void setFile(String filename, Runnable doOnChange) 
	  {
		  this.filename = filename;
		  this.doOnChange = doOnChange;
		  file = new File(filename);
		  checkAndUpdate();
	  }
	  
	  public void setFile(String filename, Runnable doOnChange, Runnable doOnNotExist) 
	  {
		  this.filename = filename;
		  this.doOnChange = doOnChange;
		  this.doOnNotExist = doOnNotExist;
		  file = new File(filename);
		  checkAndUpdate();
	  }

	  protected void checkAndUpdate() 
	  {
		  //System.out.println("checkAndUpdate:"+file);
		  if (file == null || doOnChange == null)
			  return;
		  boolean fileExists;
		  try 
		  {
			  fileExists = file.exists();
		  } 
		  catch(SecurityException  e) 
		  {
			  return;
		  }
		  if(fileExists) 
		  {
			  long l = file.lastModified(); // this can also throw a SecurityException
			  if(l != lastModif) // however, if we reached this point this
			  {
				  lastModif = l;              // is very unlikely.
				  doOnChange.run();
			  }
		  }
		  else
		  {
			  if (doOnNotExist != null)
				  doOnNotExist.run();
		  }
	  }
  
	@Override
	public void run()
	{
		checkAndUpdate();
	}
	
}


