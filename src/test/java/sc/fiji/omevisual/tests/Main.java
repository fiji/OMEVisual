
package sc.fiji.omevisual.tests;

import io.scif.config.SCIFIOConfig;
import io.scif.services.DatasetIOService;

import java.io.IOException;

import net.imagej.Dataset;
import net.imagej.ImageJ;

import sc.fiji.omevisual.OMEVisual;

public class Main {

	public static void main(final String... args) throws Exception {
		// Launch ImageJ as usual.
		final ImageJ ij = net.imagej.Main.launch(args);

		// Load image and rois test data
		Main.loadTestData(ij);

		// Launch the command.
		ij.command().run(OMEVisual.class, true);
	}

	public static void loadTestData(ImageJ ij) throws IOException {

		// Open image
		String fpath;
		fpath = Main.class.getResource(
			"/sc/fiji/omevisual/tests/testdata/multi-channel-4D-series.ome.tif")
			.getPath();

		DatasetIOService io = ij.context().getService(DatasetIOService.class);
		Dataset ds = io.open(fpath, new SCIFIOConfig().checkerSetOpen(true));
		ij.display().createDisplay(ds);

		ij.log().info("Load filaments.tif data.");

	}
}
