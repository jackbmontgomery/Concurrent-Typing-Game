# Assignment 2 MakeFile
# Jack Montgomery
# 27 August 2019

SRCDIR = src
BINDIR = bin
DOCDIR = doc

JAVAC = /usr/bin/javac
.SUFFIXES: .java .class

src_files=$(wildcard $(SRCDIR)/*.java)
out_files=$(src_files:$(SRCDIR)/%.java=$(BINDIR)/%.class)

compiler_flags=-d $(BINDIR) -sourcepath $(SRCDIR)

build: $(out_files)

$(BINDIR)/%.class: $(SRCDIR)/%.java
	$(JAVAC) $(compiler_flags) $^
	
CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

docs:
	javadoc -d $(DOCDIR) $(SRCDIR)/*.java

clean:
	rm $(BINDIR)/*
	rm -r $(DOCDIR)/*