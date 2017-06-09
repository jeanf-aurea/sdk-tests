//=====================================================================================================================
// $HeadURL:  $
// Checked in by: $Author: $
// $Date: $
// $Revision: $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2015. Aurea Software, Inc. All Rights Reserved.
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

package bp.pathid;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

import com.actional.lg.interceptor.internal.simulator.Simulator;
import com.actional.lg.interceptor.sdk.ServerInteraction;

public class MultiStepsControlled extends Simulator
{
	public static void main(String[] args)
		throws Exception
	{
		new MultiStepsControlled().run();

		Thread.sleep(1000L);
	}

	private MultiStepsControlled()
	{
		super("bp");
	}

	private void run() throws Exception
	{
		final LineNumberReader console = new LineNumberReader(new InputStreamReader(System.in));
		int counter = 1;

		ServerInteraction parent = inbound("client", "machine" + counter, "app" + counter, "service" + counter, "op" + counter);

		parent.setElapsed(counter++);
		parent.end();

		System.out.println("Produced SI#1");

		while (true)
		{
			String line = console.readLine();

			if (line == null)
				return;

			line = line.trim();

			if ("exit".equals(line) || "quit".equals(line))
				return;

			final String opName = line.isEmpty() ? "op" + counter : line;

			parent = inbound(parent, "machine" + counter, "app" + counter, "service" + counter, opName);
			parent.setElapsed(counter);
			parent.end();

			System.out.println("Produced SI#" + counter);

			counter++;
		}
	}
}
