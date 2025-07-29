package me.chrisswr1.multimodulemavenplugin;

import lombok.Getter;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import proguard.annotation.Keep;
import proguard.annotation.KeepName;

import java.nio.file.Path;

@Mojo(
	name = "module-root-properties",
	defaultPhase = LifecyclePhase.VALIDATE,
	threadSafe = true
)
@Keep
public class ModuleRootPropertiesMojo extends AbstractMojo {
	@Parameter(
		defaultValue = "${session}",
		readonly = true
	)
	@Getter
	@KeepName
	private @Nullable MavenSession session;

	@Override
	public void execute() throws MojoExecutionException {
		final @Nullable MavenSession session = this.getSession();
		if (session == null) {
			throw new MojoExecutionException(
				"Couldn't determine Maven session!"
			);
		}
		final @Nullable MavenProject topProject = session.getTopLevelProject();
		if (topProject == null) {
			throw new MojoExecutionException(
				"Couldn't determine top level project!"
			);
		}

		final @Nullable String rootArtifactId = topProject.getArtifactId();
		final @NotNull Path topDir = topProject.getBasedir().toPath();
		final @NotNull String rootPath = topDir.toString();
		final @NotNull Path currentDir =
			session.getCurrentProject().getBasedir().toPath();
		@NotNull String relativePath = currentDir.relativize(topDir).toString();
		if (relativePath.isEmpty()) {
			relativePath = ".";
		}
		if (!(relativePath.endsWith("/"))) {
			relativePath = relativePath + '/';
		}

		this.getLog().info(
			"Setting project.module-root.artifactId = " +
			rootArtifactId
		);
		session.getUserProperties().setProperty(
			"project.module-root.artifactId",
			rootArtifactId
		);

		this.getLog().info(
			"Setting project.module-root.basedir = " +
			rootPath
		);
		session.getUserProperties().setProperty(
			"project.module-root.basedir",
			rootPath
		);

		this.getLog().info(
			"Setting project.module-root.relativedir = " +
			relativePath
		);
		session.getUserProperties().setProperty(
			"project.module-root.relativedir",
			relativePath
		);
	}
}
