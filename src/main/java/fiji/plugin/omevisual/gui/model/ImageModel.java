/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fiji.plugin.omevisual.gui.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import loci.formats.ome.OMEXMLMetadata;
import ome.xml.model.primitives.PositiveInteger;

/**
 *
 * @author Hadrien Mary
 */
public class ImageModel extends GenericModel<TiffDataModel> {

    private final int imageID;
    private final String id;
    private final String name;
    private final String pixelID;
    private final float physicalSizeX;
    private final float physicalSizeY;
    private final float physicalSizeZ;
    private final float timeIncrement;
    private final PositiveInteger sizeC;
    private final PositiveInteger sizeT;
    private final PositiveInteger sizeX;
    private final PositiveInteger sizeY;
    private final PositiveInteger sizeZ;

    private final List<Map<String, String>> channels;

    public ImageModel(int imageID, OMEXMLMetadata md) {
        this.imageID = imageID;
        this.id = md.getImageID(imageID);
        this.name = md.getImageName(imageID);

        this.pixelID = md.getPixelsID(imageID);

        this.physicalSizeX = md.getPixelsPhysicalSizeX(imageID).value().floatValue();
        this.physicalSizeY = md.getPixelsPhysicalSizeY(imageID).value().floatValue();
        this.physicalSizeZ = md.getPixelsPhysicalSizeZ(imageID).value().floatValue();
        this.timeIncrement = md.getPixelsTimeIncrement(imageID).value().floatValue();
        this.sizeC = md.getPixelsSizeC(imageID);
        this.sizeT = md.getPixelsSizeT(imageID);
        this.sizeX = md.getPixelsSizeX(imageID);
        this.sizeY = md.getPixelsSizeY(imageID);
        this.sizeZ = md.getPixelsSizeZ(imageID);

        channels = new ArrayList<>();
        for (int i = 0; i < md.getChannelCount(imageID); i++) {
            Map<String, String> channel = new HashMap<>();
            channel.put("ID", md.getChannelID(imageID, i));
            channel.put("Name", md.getChannelName(imageID, i));
            channels.add(channel);
        }

    }

    @Override
    public String toString() {
        return "Image : " + name;
    }

    @Override
    public Iterable<List<String>> getInformationsRow() {
        List<List<String>> rows = new ArrayList<>();

        rows.add(Arrays.asList("Name", this.name));
        rows.add(Arrays.asList("ID", this.id));
        rows.add(Arrays.asList("Pixel ID", this.pixelID));

        rows.add(Arrays.asList("Physical Size X", this.physicalSizeX + " µm"));
        rows.add(Arrays.asList("Physical Size Y", this.physicalSizeY + " µm"));
        rows.add(Arrays.asList("Physical Size Z", this.physicalSizeZ + " µm"));
        rows.add(Arrays.asList("Time Increment", this.timeIncrement + " s"));
        rows.add(Arrays.asList("Size X", this.sizeX.toString()));
        rows.add(Arrays.asList("Size Y", this.sizeY.toString()));
        rows.add(Arrays.asList("Size Z", this.sizeZ.toString()));
        rows.add(Arrays.asList("Size Channel", this.sizeC.toString()));
        rows.add(Arrays.asList("Size Time", this.sizeT.toString()));

        for (int i=0; i < channels.size(); i++) {
            Map<String, String> channel = channels.get(i);
            for (Map.Entry<String, String> entry : channel.entrySet()) {
                rows.add(Arrays.asList("Channel " + Integer.toString(i) + " - " + entry.getKey(),
                        entry.getValue()));
            }
        }

        return rows;
    }
}
