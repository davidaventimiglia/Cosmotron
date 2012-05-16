################################################################################
# Process this file with automake to produce Makefile.in
################################################################################

# Automake `primary' says to compile *.java files and put results in `classesdir'
javadir = $(top_builddir)/java
JAVAROOT = $(javadir)
dist_java_JAVA = $(shell find . -name '*.java')

# Automake `primary' says to put these `scripts' into the ../bin directory
dist_bin_SCRIPTS = atomic cosmotron atomic.jar

# Debug flags, no warnings, for javac and gcj
AM_JAVACFLAGS = -g -Wno-all
AM_GCJFLAGS = -g -nowarn

# Automake variable that says also to clean out class files
CLEANFILES = *.class atomic.jar

# Automake variables needed by java and javac
JARS = $(shell find lib -name '*.jar' -printf '%p:')
CLASSPATH = "$(shell $(CYGPATH_PW) $(JARS)$(top_builddir)/java)"
CLASSPATH_ENV = export CLASSPATH=$(CLASSPATH);

# Default target
all: atomic cosmotron

# Generate the atomic script.
atomic: atomic.in atomic.jar
	sed 's|JARDIR|/usr/local/bin|' atomic.in > atomic

# Generate the cosmotron script.
cosmotron: cosmotron.in atomic.jar
	sed 's|JARDIR|/usr/local/bin|' cosmotron.in > cosmotron

# Build the main jar file.
atomic.jar: classdist_java.stamp
	if ! test -d scratch ; \
	then \
		mkdir scratch ; \
		cd scratch ; \
		find ../lib/*.jar -exec jar -xf {} \; ; \
		find . -iname manifest.mf -exec rm {} + ; \
	fi
	touch atomic.jar
	@JAR@ -uf atomic.jar -C ./scratch .
	@JAR@ -uf atomic.jar -C $(javadir) .
	@JAR@ -uf atomic.jar ./atomic_templates

# Supplemental cleaning rule
clean-local:
	rm -rf scratch
	rm -rf $(javadir)

# Supplemental dist cleaning rule
distclean-local:
	rm -rf Test

# Perform a simple sanity self-test.
check:
	@JAVA@ -cp atomic.jar Cosmotron

# Phony targets have no dependencies.
.PHONY: distclean-local clean-local check
