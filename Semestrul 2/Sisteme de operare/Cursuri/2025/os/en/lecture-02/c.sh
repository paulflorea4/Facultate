#!/bin/bash

echo command $0
echo first four args $1 $2 $3 $4
echo all args $@
echo all args $*
echo arg count $#

echo ================================
shift 5
echo first four args $1 $2 $3 $4
echo all args $@
echo all args $*
echo arg count $#
