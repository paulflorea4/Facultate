#!/bin/bash

L=$1
if [ -z "$L" ]; then
    L=5
fi
 
awk -F: '$1 == "exam" || $1 == "practice" {print $4}' /etc/group|sed "s/,/\n/g" | head -n 10 | while read U; do
    for P in `ps -u $U|tail -n +2|awk '{print $1}'`; do
        T=`ps -o etimes $P|tail -n +2`
        if [ $L -lt $T ]; then
            echo kill -9 $P
        fi
    done
done
