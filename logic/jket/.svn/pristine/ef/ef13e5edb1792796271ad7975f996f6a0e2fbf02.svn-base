
package ket.kdb;

import java.nio.file.Path;
import org.apache.log4j.Logger;
//import java.util.concurrent.Executor;

public interface DB
{
	public void open(Path pathDBDir, Path pathCfgFile);
	public void close();
	
	public void execute(Transaction transaction);
	// TODO public void execute(Transaction transaction, Executor executorCallback);
	// set extenral logger
	public void setLogger(Logger logger);
}
