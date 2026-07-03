#!/bin/bash

D=$1

N=0
for F in $(find $D -type f); do
    for G in `find $D -type f`; do
        if [ "$F" != "$G" ]; then
            if cmp -s $F $G; then
                N=`expr $N + 1`
                echo $F $G
            fi
            if [ $N -eq 2 ]; then
                break
            fi
        fi
    done
done

N=0
find $D -type f | while read F; do
    find $D -type f | while read G; do
        if test "$F" != "$G"; then
            if cmp -s "$F" "$G"; then
                N=`expr $N + 1`
                echo "$F" "$G"
            fi
            if [ $N -eq 2 ]; then
                break
            fi
        fi
    done
done

N=0
find $D -type f | while read F; do
    find $D -type f | while read G; do
        if test "$F" != "$G" && cmp -s "$F" "$G"; then
            N=`expr $N + 1`
            echo "$F" "$G"
        fi
        if [ $N -eq 2 ]; then
            break
        fi
    done
done

find $D -type f > files.txt
N=0
while read F; do
    while read G; do
        if test "$F" != "$G" && cmp -s "$F" "$G"; then
            N=`expr $N + 1`
            echo "$F" "$G"
        fi
        if [ $N -eq 2 ]; then
            break
        fi
    done < files.txt
done < files.txt




