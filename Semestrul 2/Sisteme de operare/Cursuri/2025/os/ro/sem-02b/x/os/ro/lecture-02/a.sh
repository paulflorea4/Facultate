#!/bin/bash

echo $0 $1 $2 $9
echo $*
echo $@
echo $#

shift 5
echo $*
echo $5

for X in xzcv asdf wqer; do
    echo $X
done

for X; do
    echo $X
done

for X in *; do
    wc -l $X
done

for X in *.c; do
    E=`echo $X|sed "s/\..*//"`
    gcc -Wall -g -o $E $X
done

for X in *.c
do
    E=`echo $X|sed "s/\..*//"`
    gcc -Wall -g -o $E $X
done




