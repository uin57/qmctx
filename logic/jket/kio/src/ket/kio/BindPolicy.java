
package ket.kio;

// policy being used when listen or bind methods failed for local address is busy.
 
public enum BindPolicy
{
	eReuseListen,
	eReuseTimewait, // recommend for server applications with fixed port
	eLoopIncPort,	// recommend when port is not important
	eGiveup
}
