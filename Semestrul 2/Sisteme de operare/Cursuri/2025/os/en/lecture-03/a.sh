#!/bin/bash

ps -ef | tail -n +2 | while read U P X; do
    if groups $U | grep -v -E -q "\<exam\>|\<practice\>|\<examiner\>"; then
        continue
    fi
    D=`ps -o etime $P |tail -n +2|sed -E "s/^ +//"|sed -E "s/-/:/"`
    if [ -z "$D" ]; then
        continue
    fi

    S=0
    if echo $D | grep -E -q "^[0-9]+(:[0-9]+){3}$"; then
        S=`echo $D | awk -F: '{print ($1*24*60*60 + $2*60*60 + $3*60 + $4)}'`
    elif echo $D | grep -E -q "^[0-9]+(:[0-9]+){2}$"; then
        S=`echo $D | awk -F: '{print ($1*60*60 + $2*60 + $3)}'`
    elif echo $D | grep -E -q "^[0-9]+:[0-9]+$"; then
        S=`echo $D | awk -F: '{print ($1*60 + $2)}'`
    else
        echo "Cannot parse duration \"$D\"" >&2
    fi

    if [ $S -gt $1 ]; then
        echo kill $P
    fi
done
