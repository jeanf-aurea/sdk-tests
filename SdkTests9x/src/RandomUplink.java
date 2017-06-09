//=====================================================================================================================
// $HeadURL:  $
// Checked in by: $Author: $
// $Date: $
// $Revision: $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2014. Aurea Software, Inc. All Rights Reserved.
//
// You are hereby placed on notice that the software, its related technology and services may be covered by one or
// more United States ("US") and non-US patents. A listing that associates patented and patent-pending products
// included in the software, software updates, their related technology and services with one or more patent numbers
// is available for you and the general public's access at www.aurea.com/legal/ (the "Patent Notice") without charge.
// The association of products-to-patent numbers at the Patent Notice may not be an exclusive listing of associations,
// and other unlisted patents or pending patents may also be associated with the products. Likewise, the patents or
// pending patents may also be associated with unlisted products. You agree to regularly review the products-to-patent
// number(s) association at the Patent Notice to check for updates.
//=====================================================================================================================

import com.actional.lg.interceptor.internal.IBaseEventSource;
import com.actional.lg.interceptor.internal.Uplink;
import com.actional.lg.interceptor.internal.config.UplinkCfg;
import com.actional.lg.interceptor.sdk.IAnalyzer;

public class RandomUplink implements Uplink, IAnalyzer
{
	/** <!-- ================================================================================================== -->
	 * (non-Javadoc)
	 * @see com.actional.lg.interceptor.sdk.IAnalyzer#getStabilizerValue(java.lang.String)
	 *
	 * @lastrev fixXXXXX - New method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public Object getStabilizerValue(String stabilizerName) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	/** <!-- ================================================================================================== -->
	 * (non-Javadoc)
	 * @see com.actional.lg.interceptor.internal.Uplink#getConfig()
	 *
	 * @lastrev fixXXXXX - New method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public UplinkCfg getConfig()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/** <!-- ================================================================================================== -->
	 * (non-Javadoc)
	 * @see com.actional.lg.interceptor.internal.Uplink#isAgentAlive()
	 *
	 * @lastrev fixXXXXX - New method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public boolean isAgentAlive()
	{
		// TODO Auto-generated method stub
		return false;
	}

	/** <!-- ================================================================================================== -->
	 * (non-Javadoc)
	 * @see com.actional.lg.interceptor.internal.Uplink#notifyAgent(com.actional.lg.interceptor.internal.IBaseEventSource)
	 *
	 * @lastrev fixXXXXX - New method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void notifyAgent(IBaseEventSource intr)
	{
		// TODO Auto-generated method stub

	}

	/** <!-- ================================================================================================== -->
	 * (non-Javadoc)
	 * @see com.actional.lg.interceptor.internal.Uplink#copyUplinkData(java.lang.Object)
	 *
	 * @lastrev fixXXXXX - New method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public Object copyUplinkData(Object uplinkData)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/** <!-- ================================================================================================== -->
	 * (non-Javadoc)
	 * @see com.actional.lg.interceptor.internal.Uplink#shutdown()
	 *
	 * @lastrev fixXXXXX - New method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void shutdown()
	{
		// TODO Auto-generated method stub

	}
}
