package fiji.plugin.omevisual.tests;

import fiji.plugin.omevisual.OMEVisual;

import java.io.IOException;

import net.imagej.Dataset;
import net.imagej.ImageJ;

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
        fpath = Main.class.getResource("/fiji/plugin/omevisual/tests/testdata/multi-channel-4D-series.ome.tif").getPath();
        Dataset ds = ij.dataset().open(fpath);
        ij.display().createDisplay(ds);

        ij.log().info("Load filaments.tif data.");

    }
}
