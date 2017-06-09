//=====================================================================================================================
// $HeadURL:  $
// Checked in by: $Author: $
// $Date: $
// $Revision: $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2016. Aurea Software, Inc. All Rights Reserved.
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

package docker;

import com.actional.lg.interceptor.internal.simulator.Simulator;
import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;

public class SimpleClientServer
{
	public static void main(final String[] args) throws Exception
	{
		{
			final Simulator sim = new Simulator("host2docker");
			final ClientInteraction ci = sim.outbound(null, "192.168.2.190", "192.168.2.236", "app", "service", "op");
			ci.end();

			final ServerInteraction si = sim.inbound(ci, "192.168.2.190", "172.17.0.2", "app", "service", "op");
			si.end();
		}

		{
			final Simulator sim = new Simulator("docker2host");
			final ClientInteraction ci = sim.outbound(null, "172.17.0.2", "192.168.2.190", "app", "service", "op");
			ci.end();

			final ServerInteraction si = sim.inbound(ci, "192.168.2.236", "192.168.2.190", "app", "service", "op");
			si.end();
		}

		Thread.sleep(2000L);
	}
}
