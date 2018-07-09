# Make.org Control Center
# Copyright (C) 2018 Make.org
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.

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
	sbt fastOptJS::startWebpackDevServer ~fastOptJS
