################################################################################
# CONFIGURABLE PART
################################################################################

JARFILE = atomic.jar
SRCDIR = src
LIBDIR = lib
CLASSESDIR = java
BINFILES = atomic cosmotron $(JARFILE)
JAVAC_FLAGS = -g -w

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

# Build the main jar file.
$(JARFILE): classdist_java.stamp
	if ! test -d scratch ; then \
	  mkdir scratch ; \
	  cd scratch ; \
	  find ../lib/*.jar -exec jar -xf {} \; ; \
	  find . -iname manifest.mf -exec rm {} + ; fi
	if ! test -f atomic.jar ; then \
	  touch atomic.jar ; \
	  @JAR@ -uf atomic.jar -C ./scratch . ; fi
	@JAR@ -uf atomic.jar -C $(javadir) .
	@JAR@ -uf atomic.jar ./atomic_templates

# Supplemental cleaning rule
clean-local:
	rm -rf scratch
	rm -rf $(javadir)

# Phony targets have no dependencies.
.PHONY: distclean-local clean-local
