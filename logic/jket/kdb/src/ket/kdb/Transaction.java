
package ket.kdb;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface Transaction
{
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface AutoInit { }
	
	public enum ErrorCode
	{
		eOK, eUserCancel, eBadTable, eRejected
	}
	
	public boolean doTransaction();
	public void onCallback(ErrorCode errcode);
}
