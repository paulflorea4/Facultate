#!/bin/bash
echo Content-type: text/html
echo
echo "Am primit pe request antetele HTTP cu valorile:<br>"
set | sed 's/$/<br>/' | grep '^HTTP_' | sed 's/^HTTP_//'