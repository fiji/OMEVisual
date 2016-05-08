# OMEVisual

`OMEVisual` is a Fiji plugin to easily visualize OME metadata.

More details can be found on the wiki : http://imagej.net/OMEVisual

Please note that :

- This goal of this plugin is **not to implement** all the OME features.
- This plugin makes several strong assumptions about the OME model in order to simplify the interface :
    - Each image contains only one `Pixels` element.
    - Each `TiffData` contains only one `Plane`.
    - `Plane` and `TiffData` informations are displayed at the same location without distinction.
- Any ideas to improve the visualization and/or add OME features is very welcome :
    - The best way is to submit a PR.
    - You can also open an issue and post an OME-TIFF file which contains additional OME features you would like to see added to the plugin.

## Authors

`FilamentDetector` has been created by [Hadrien Mary](mailto:hadrien.mary@gmail.com).

This work started in 2016 at the [Gary Brouhard laboratory](http://brouhardlab.mcgill.ca/) at the University of McGill.

## License

MIT. See [LICENSE.txt](LICENSE.txt)
