BUILD
=====

To build the current branch:

 - `sbt publishLocal`
 or
 - `make package-docker-image`

 This will create a docker image named `nexus.prod.makeorg.tech/make-backoffice:<commit_hash>`