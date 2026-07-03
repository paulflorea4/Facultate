#!/bin/bash

D=x

PREV=""
while true; do
    ST=""
    for F in `find $D`; do
        if [ -f $F ]; then
            MCS=`ls -l $F|sha1sum`
            CCS=`cat $F | sha1sum`
        elif [ -d $F ]; then
            MCS=`ls -l -d $F|sha1sum`
            CCS=`ls -l $F | sha1sum`
        else
            continue
        fi
        ST="$ST $MCS $CCS"
    done
    ST=`echo $ST | sha1sum`

    if [ -n "$PREV" ] && [ "$PREV" != "$ST" ]; then
        echo "s-a schimbat ceva"
    fi
    PREV=$ST
done
