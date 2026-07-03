#!/bin/bash

D=/home/examiner/rares

find $D -name "*.c" | while read F; do
    grep -v "^[ {}]*$" $F | wc -l
done | awk '{s += $1} END{print s}'
