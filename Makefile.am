## Process this file with automake to produce Makefile.in
# AM_JAVACFLAGS=-Xlint:unchecked -g
AM_JAVACFLAGS = -g -Wno-all
AM_GCJFLAGS = -g -nowarn
JAVAROOT=$(top_builddir)/classes
CLEANFILES = *.class
javadir = $(top_builddir)/src
classesdir = $(top_builddir)/classes
depdir = $(top_builddir)/deps
jardir = $(prefix)/java
classes_JAVA = $(shell find . -name '*.java')
JARS = $(shell find lib -name '*.jar' -printf '%p:')
AUTOTESTS = $(shell find src -name '*Test.java')
CLASSPATH = "$(shell $(CYGPATH_PW) $(JARS)$(top_builddir)/classes)"
CLASSPATH_ENV = export CLASSPATH=$(CLASSPATH);
JARFILE = atomic.jar

.PHONY: clean-local-check

all-local: $(JARFILE)

install-data-local: $(JARFILE)
	@MKDIR_P@ $(jardir)
	cp $(JARFILE) $(jardir)

uninstall-local:
	rm -f $(jardir)/$(JARFILE)

unpack-libs:
	mkdir -p scratch
	cd scratch ; find ../lib/*.jar -exec jar -xf {} \;

$(JARFILE): $(classes_JAVA) unpack-libs
	@JAR@ -cf $(JARFILE) -C $(classesdir) .
	@JAR@ -uf $(JARFILE) -C ./scratch .

clean-local: clean-local-check

clean-local-check:
	rm -f $(JARFILE)
	cd $(classesdir)
	find . -name *.class -execdir rm {} +

check:
	@JAVA@ -cp $(CLASSPATH) Atomic

run-atomic: all
	@JAVA@ -cp $(JARFILE) Atomic "org.apache.derby.jdbc.ClientDriver" "jdbc:derby://localhost:1527/Test;create=true" 

run-cosmotron: all
	@JAVA@ -cp $(JARFILE) Cosmotron
