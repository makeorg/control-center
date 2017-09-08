.PHONY: clean release reload-all start

help:
	@echo "Please use 'make <rule>' where <rule> is one of"
	@echo "   clean                          to clean"
	@echo "   release                        to release the application"
	@echo "   reload-all                     to clean and start webpack dev server"
	@echo "   start                          to start webpack dev server"

clean:
	sbt clean

release:
	sbt release

reload-all: clean start

start:
	sbt ~fastOptJS::startWebpackDevServer
