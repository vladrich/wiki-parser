Wiki-Parser
==========

 1) Prerequisites
 
 2.1) Installation (Linux)
 2.2) Installation (Windows)



1) Prerequisites

   Linux:
   Maven >= 1.0.2	http://maven.apache.org
   export PATH=/usr/local/maven/bin:$PATH


   Windows:
   Ant >= 1.6.5		http://ant.apache.org

    
2.1) Installation (Linux)

   (OPTIONAL: maven wikiparser:clean)
   maven wikiparser:jar

   Run Tests: java -jar build/lib/wyona-wiki-parser-rXXXXX.jar <file>
              (e.g. java -jar build/lib/wyona-wiki-parser-rXXXXX.jar test)

   Generate XML: java -classpath build/lib/wyona-wiki-parser-rXXXXX.jar org.wyona.wiki.Wiki2XML test/hello-world.txt
   
2.2) Installation (Windows)
   
   maven wikiparser:compile
   Put new classes directory into Classpath
   java org.wyona.wiki.Wiki2XML <file>
