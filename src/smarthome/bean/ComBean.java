package smarthome.bean;

import java.text.SimpleDateFormat;

/**
 * @author benjaminwan
 *´®¿ÚÊý¾Ý
 */
public class ComBean {
		public String bRec=null;
		public String sRecTime="";
		public String sComPort="";
		public ComBean(String sPort,String sb){
			sComPort=sPort;
			/*bRec=new byte[size];
			for (int i = 0; i < size; i++)
			{
				bRec[i]=buffer[i];
			}*/
			bRec=sb;
			SimpleDateFormat sDateFormat = new SimpleDateFormat("hh:mm:ss");       
			sRecTime = sDateFormat.format(new java.util.Date()); 
		}
}