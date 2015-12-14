package application.accounting.Interface;

import gnu.getopt.*;


// Parses arguments from an array provided at object-initialisation
public class ArgParser {

    private String[] args = null;
    private boolean showHelp = false;
    private boolean showVersion = false;
    private boolean interactive = true;
    private String inputFilename = null;
    private String outputFilename = null;
    private String logFilename = null;
    private String nonOptions = null;
    private String interest = null;
    private String eArg = null;
    private boolean eArgPresent = false;
    private String mArg = null;
    private boolean mArgPresent = false;

    public ArgParser(String[] args) {

        this.args = args;
        parseArgs();
        
    } // end of special constructor "ArgParser(String[])"


    /**
      Parses commandline arguments and stores relevant infomation in local variables.\n
      Offered Information:\n
	-h | --help\n
	-v | --version\n
	-i | --input-file <filename>\n
	-o | --output-file <filename>\n
	-l | --log-file <filename>\n
	-r | --rate-of-interest <interest>\n
	
	
    
    
    */
    private void parseArgs() {
	int c;
	String arg = "";
	StringBuffer sb = new StringBuffer();
	LongOpt[] longopts = 
	{
	    new LongOpt("input-file", LongOpt.REQUIRED_ARGUMENT, sb, 'i'),
	    new LongOpt("output-file", LongOpt.REQUIRED_ARGUMENT, sb, 'o'),
	    new LongOpt("log-file", LongOpt.REQUIRED_ARGUMENT, sb, 'l'),
	    new LongOpt("rate-of-interest", LongOpt.REQUIRED_ARGUMENT, sb, 'r'),
	    new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h'),
	    new LongOpt("version", LongOpt.NO_ARGUMENT, null, 'v'),
	};
	
	Getopt g = new Getopt("Buchhaltung", this.args, "i:o:l:r:hcpe:m:", longopts);
	
	while((c = g.getopt()) != -1) {
		switch(c) {
			case 'h':
			  this.showHelp = true;
			  break;
			  
			case 'v':
			  this.showVersion = true;
			  break;
			  
			case 'i':
			  arg = g.getOptarg();
			  this.inputFilename = arg;
			  break;
			  
			case 'o':
			  arg = g.getOptarg();
			  this.outputFilename = arg;
			  break;
			  
			case 'l':
			  arg = g.getOptarg();
			  this.logFilename = arg;
			  break;
			  
			case 'r':
			  arg = g.getOptarg();
			  this.interest = arg;
			  break;
			  
			case 'p':
			  this.interactive = false;
			  break;
			  
			case 'e':
			  if (!eArgPresent) {
				this.eArg = g.getOptarg();
				this.eArgPresent = true;
			  } else {
				//Just do noting, later occurences are ignored
			  }
			  break;
			  
			case 'm':
			  if (!eArgPresent) {
				this.mArg = g.getOptarg();
				this.mArgPresent = true;
			  } else {
				//Just do noting, later occurences are ignored
			  }
			  break;
			  
			  
			default:
			  this.nonOptions = this.nonOptions + ", " + c;
			  break;
		}
	}
        

    } // end of method "parseArgs()"


    /**
	returns some Information about all parsed arguments, including disallowed arguments


    */
    @Override
    public String toString() {

        StringBuffer sb = new StringBuffer();

        for ( int i = 0; i < args.length; i++ ) {

            if ( args[i].equals("-h") || args[i].equals("--h") ||
                 args[i].equals("-v") || args[i].equals("--v") ) {

                sb.append(args[i]).append("\n");

            } // end of if ( args[i].equals("-h")  ...)
            else if ( args[i].equals("-i") || args[i].equals("--input-file") ||
                args[i].equals("-o") || args[i].equals("--output-file") ) {

                //System.out.println("i: " + i + ", args.length: " + args.length);
                if ( i + 1 < args.length ) {
                    sb.append(args[i] + " " + args[++i]).append("\n");
                } // end of if ( i + 1 < args.length )
                else {
                    throw new IllegalArgumentException("missing filename");
                } // end of if ( i + 1 < args.length ) else

            } // end of else if ( args[i].equals("-i") ... )
            else if ( args[i].equals("-l") || args[i].equals("--log-file") ) {

                if ( i + 1 < args.length ) {
		    System.out.println(args[i] + " " + args[i+1]);
                    if ( args[i + 1].startsWith("-") ) {
                        sb.append(args[i]).append("\n");
                    } // end of if ( args[ i + 1].startsWith("-") )
                    else {
                        sb.append(args[i] + " " + args[++i]).append("\n");
                    } // end of if ( args[ i + 1].startsWith("-") ) else

                } // end of if ( i + 1 < args.length )

            } // end of else if ( args[i].equals("-i") ... )
            else if (args[i].equals("-r") || args[i].equals("--rate-of-interest")) {
		if ( i + 1 < args.length ) {

                    if ( args[i + 1].startsWith("-") ) {
                        sb.append(args[i]).append("\n");
                    } // end of if ( args[ i + 1].startsWith("-") )
                    else {
                        sb.append(args[i] + " " + args[++i]).append("\n");
                    } // end of if ( args[ i + 1].startsWith("-") ) else

                } // end of if ( i + 1 < args.length )
            }
            else {

                sb.append("non-option argument: " +
                          args[i]).append("\n");

            } // end of if ( args[i].equals("-h") || args[i].equals("--h") ) else

        } // end of for (int i = 0; i  < args.length; i++)

        return sb.toString();
/*
        sb = new StringBuffer();
        for (int i = 0; i < args.length; i++) {
		sb.append(args[i] + " ");
        }
       
        return sb.toString();
*/       
    } // end of method "toString()"


    public boolean getShowHelp() {
        return showHelp;
    } // end of method "getShowHelp()"


    public boolean getShowVersion() {
        return showHelp;
    } // end of method "getShowVersion()"
    
    public boolean isInteractive() {
	return this.interactive;
    }


    public String getInputFilename() {
        return inputFilename;
    } // end of method "getInputFilename()"


    public String getOutputFilename() {
        return outputFilename;
    } // end of method "getOutputFilename()"


    public String getLogFilename() {
        return logFilename;
    } // end of method "getLogFilename()"


    public String getNonOptions() {
        return nonOptions;
    } // end of method "getNonOptions()"
    
    public String getInterest() {
	return this.interest;
    }
    
    public String getMArg() {
	return this.mArg;
    }
    
    public String getEArg() {
	return this.eArg;
    }


    public static final void main(final String[] args) {
        ArgParser argParser = new ArgParser(args);
        System.out.println(argParser);
    } // end of method "main(String[] args)"

} // end of class "ArgParser"
