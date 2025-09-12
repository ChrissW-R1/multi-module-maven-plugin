module me.chrisswr1.multimodulemavenplugin {
	requires static org.jetbrains.annotations;
	requires static com.github.spotbugs.annotations;
	requires static lombok;
	requires static proguard.annotations;

	requires org.slf4j;
	requires maven.core;
	requires maven.plugin.api;
	requires maven.model;
	requires maven.plugin.annotations;
	requires plexus.interpolation;
}
