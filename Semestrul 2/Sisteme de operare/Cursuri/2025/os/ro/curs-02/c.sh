#!/bin/bash

echo comanda $0
echo args $1 $2 $3 $4
echo toate argumentele $@
echo toate argumentele $*
echo toate argumentele $#
echo ===================================
shift 5
echo comanda $0
echo args $1 $2 $3 $4
echo toate argumentele $@
echo toate argumentele $*
echo toate argumentele $#

echo ===================================
for X in asdf wqer fdsg t yhsdvf ew; do
    echo $X
done

echo ===================================
for F in *.c; do
    wc -l $F
done

echo ===================================
for A in $@
do
    echo $A
done

echo ===================================
for A; do
    echo $A
done
