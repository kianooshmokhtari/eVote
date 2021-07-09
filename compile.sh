# Compilation script for CSCI 4050 Term Project

CLASSESDIR=/opt/classes

javac -d classes -cp ${CLASSESDIR}/mysql-connector-java-5.1.37-bin.jar:${CLASSESDIR}/freemarker.jar:${CLASSESDIR}/jboss-servlet-api_3.1_spec-1.0.0.Final.jar src/edu/uga/cs/evote/EVException.java src/edu/uga/cs/evote/persistence/*.java src/edu/uga/cs/evote/persistence/impl/*.java src/edu/uga/cs/evote/entity/*.java src/edu/uga/cs/evote/entity/impl/*.java src/edu/uga/cs/evote/object/*.java src/edu/uga/cs/evote/object/impl/*.java src/edu/uga/cs/evote/test/object/*.java src/edu/uga/cs/evote/logic/*.java src/edu/uga/cs/evote/logic/impl/*.java src/edu/uga/cs/evote/session/*.java src/edu/uga/cs/evote/presentation/*.java
