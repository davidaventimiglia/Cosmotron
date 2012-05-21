################################################################################
# CONFIGURABLE PART
################################################################################

JARFILE = cosmotron.jar
SRCDIR = src
LIBDIR = lib
CLASSESDIR = classes
BINFILES = cosmotron $(JARFILE)
JAVAC_FLAGS = -g -w
TEMPLATEDIR = cosmotron_templates

################################################################################
# NON-CONFIGURABLE PART
################################################################################

# Automake `primary' says to compile *.java files and put results in `classesdir'
javadir = $(top_builddir)/$(CLASSESDIR)
JAVAROOT = $(javadir)
dist_java_JAVA = $(shell find $(SRCDIR) -name '*.java')

# Debug flags, no warnings, for javac and gcj
AM_JAVACFLAGS = $(JAVAC_FLAGS)

# Automake variable with supplemental items to clean
CLEANFILES = *.class $(JARFILE)

# Automake variables needed by java and javac
JARS = $(shell find $(LIBDIR) -name '*.jar' -printf '%p:')
CLASSPATH = "$(shell $(CYGPATH_PW) $(JARS)$(top_builddir)/$(CLASSESDIR))"
CLASSPATH_ENV = export CLASSPATH=$(CLASSPATH);

# Automake `primary' says to put these `scripts' into the ../bin directory
dist_bin_SCRIPTS = $(BINFILES)

# Automake `primary' says to put these `architecture-independent data files'
# in something like /usr/local/share
dist_pkgdata_DATA = $(shell find $(TEMPLATEDIR) -name '*.stg') 

# We need this for substitution in sed commands
cosmotrondir := $(pkgdatadir)

# Build the main jar file.
$(JARFILE): classdist_java.stamp $(dist_data_DATA)
	if ! test -d scratch ; then \
	  mkdir scratch ; \
	  cd scratch ; \
	  find ../lib/*.jar -exec jar -xf {} \; ; \
	  find . -iname manifest.mf -exec rm {} + ; fi
	if ! test -f $(JARFILE) ; then \
	  touch $(JARFILE) ; \
	  @JAR@ -uf $(JARFILE) -C ./scratch . ; fi
	@JAR@ -uf $(JARFILE) -C $(javadir) .

cosmotron: cosmotron.in
	sed 's|PKGDATADIR|$(pkgdatadir)|' cosmotron.in > cosmotron

# Supplemental cleaning rule
clean-local:
	rm -rf scratch
	rm -rf $(javadir)

# Phony targets have no dependencies.
.PHONY: distclean-local clean-local all
