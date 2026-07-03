#!/bin/bash

for X in cat dog cow mouse bear; do
    echo Here is another animal that I know of $X
done

for F in *.c; do
    wc -l $F
done

for F in $@; do
    echo arg $F
done

for F; do
    echo arg $F
done

for F
do
    echo arg $F
done
