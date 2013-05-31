#!/bin/bash

mvn install:install-file \
	-Dfile=/c/dev/github/square/dagger/core/target/square-dagger-1.0-SNAPSHOT.jar \
	-Dsources=/c/dev/github/square/dagger/core/target/square-dagger-1.0-SNAPSHOT-sources.jar \
	-DpomFile=/c/dev/github/square/dagger/core/pom.xml \
	-DgroupId=com.squareup \
	-DartifactId=dagger \
	-Dversion=1.0-SNAPSHOT \
	-DgeneratePom=false \
	-DcreateChecksum=true \
	-Dpackaging=jar \
	-DlocalRepositoryPath=.

mvn install:install-file \
	-Dfile=/c/dev/github/square/dagger/compiler/target/square-dagger-compiler-1.0-SNAPSHOT.jar \
	-Dsources=/c/dev/github/square/dagger/compiler/target/square-dagger-compiler-1.0-SNAPSHOT-sources.jar \
	-DpomFile=/c/dev/github/square/dagger/compiler/pom.xml \
	-DgroupId=com.squareup \
	-DartifactId=dagger-compiler \
	-Dversion=1.0-SNAPSHOT \
	-DgeneratePom=false \
	-DcreateChecksum=true \
	-Dpackaging=jar \
	-DlocalRepositoryPath=.

mvn install:install-file \
	-DpomFile=/c/dev/github/square/dagger/pom.xml \
	-DgroupId=com.squareup \
	-DartifactId=dagger-parent \
	-Dversion=1.0-SNAPSHOT \
	-DgeneratePom=false \
	-DcreateChecksum=false \
	-DlocalRepositoryPath=.
