#!/bin/bash

java -jar `find ./target -regex ".*housie-.*\.jar" ! -name '*javadoc' ! -path '*archive*tmp'`
