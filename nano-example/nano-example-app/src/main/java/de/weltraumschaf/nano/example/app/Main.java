package de.weltraumschaf.nano.example.app;

import de.weltraumschaf.commons.application.InvokableAdapter;
import de.weltraumschaf.commons.application.Version;
import de.weltraumschaf.commons.jcommander.JCommanderImproved;
import de.weltraumschaf.nano.container.Container;

/**
 * Main application class.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
public final class Main extends InvokableAdapter {
    private static final String BASE_PACKAGE = "/de/weltraumschaf/nano/example/app";

    private final JCommanderImproved<CliOptions> cliArgs = new JCommanderImproved<>(Constants.PROGRAM_NAME, CliOptions.class);
    private final Container container = new Container();

    private Main(final String[] args) {
        super(args);
    }

    /**
     * Invoked by JVM.
     *
     * @param args not {@code null}
     */
    public static void main(final String[] args) {
        InvokableAdapter.main(new Main(args));
    }

    @Override
    public void execute() throws Exception {
        final CliOptions opts = cliArgs.gatherOptions(getArgs());
        this.debug = opts.isDebug();

        if (opts.isHelp()) {
            getIoStreams().print(cliArgs.helpMessage(CliOptions.USAGE, CliOptions.DESCRIPTION, CliOptions.EXAMPLE));
            exit(ExitCodeImpl.OK);
        }

        if (opts.isVersion()) {
            final Version version = new Version(BASE_PACKAGE + "/version.properties");
            version.load();
            getIoStreams().println(version.getVersion());
            exit(ExitCodeImpl.OK);
        }

        registerShutdownHook(container::stop);
        container.start();
    }
}
