#!/bin/bash

for A in $@; do
    if test -f $A; then
        echo $A e fisier
    elif test -d $A; then
        echo $A e director
    elif echo $A | grep -E -q "^[0-9]+$"; then
        echo $A e numar
        if test $A -gt 5; then
            echo $A e numar mai mare decat 5
        fi
    else
        echo N-am idee ce e $A
    fi
done
