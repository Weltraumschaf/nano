package de.weltraumschaf.nano.container;

import com.beust.jcommander.Parameter;
import lombok.Getter;
import lombok.ToString;

/**
 * @since 1.0.0
 */
@ToString
public final class CliOptions {
    static final String USAGE = "[-d|--debug] [-h|--help] [-v|--version] ";
    static final String DESCRIPTION = "This command starts the nano container daemon.";
    static final String EXAMPLE = Constants.PROGRAM_NAME;

    @Getter
    @Parameter(names = {"-d", "--debug"}, description = "Enables debug output.")
    private boolean debug;
    @Getter
    @Parameter(names = {"-h", "--help"}, description = "Show this help.", help = true)
    private boolean help;
    @Getter
    @Parameter(names = {"-v", "--version"}, description = "Show version.", help = true)
    private boolean version;
}
