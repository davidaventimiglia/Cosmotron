JAR_FILE = cosmotron.jar
SRC_DIR = src
LIB_DIR = lib
OBJ_DIR = classes
BIN_FILE = cosmotron
JAVAC_FLAGS = -g

$(shell mkdir -p $(OBJ_DIR))
$(shell cd src ; find . -type f ! -iname '*.java' -exec cp --parents -u {} ../$(OBJ_DIR) \;)

dist_bin_SCRIPTS = $(BIN_FILE) $(JAR_FILE)
dist_java_JAVA := $(shell find $(SRC_DIR) -name '*.java' )
javadir = $(top_builddir)/$(OBJ_DIR)
JAVAROOT = $(javadir)
AM_JAVACFLAGS = $(JAVAC_FLAGS)
CLEANFILES = *.class $(JAR_FILE) $(BIN_FILE)
LIBS := $(shell find $(LIB_DIR) -name '*.jar' -printf '%p:')
CLASSPATH := $(shell $(CYGPATH_PW) $(LIBS)$(top_builddir)/$(OBJ_DIR))
CLASSPATH_ENV = export CLASSPATH=$(CLASSPATH);
NEWEST_LIB := $(shell find lib -type f -iname '*.jar' -printf '%C@\n' | sort | tail -n 1)

$(BIN_FILE): cosmotron.in
	sed 's|PKGDATADIR|$(pkgdatadir)|' $< > $@
	chmod a+x $@

$(JAR_FILE): classjava.stamp $(LIB_DIR) $(OBJ_DIR)/$(NEWEST_LIB)
	touch $@
	@JAR@ -uf $@ -C $(OBJ_DIR) .

$(OBJ_DIR)/$(NEWEST_LIB):
	cd $(OBJ_DIR) ; find ../$(LIB_DIR)/*.jar -exec @JAR@ -xf {} \; ; find . -iname MANIFEST.MF -exec rm {} +
	touch $@

clean-local:
	rm -rf $(OBJ_DIR)

install-data-local:
	mkdir -p $(pkgdatadir)
	cp -rf $(srcdir)/templates $(pkgdatadir)

.PHONY: distclean-local clean-local all
